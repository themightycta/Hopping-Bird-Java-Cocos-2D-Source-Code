package com.brilliant.kids.game.hopping.bird;

import org.cocos2d.actions.base.CCAction;
import org.cocos2d.actions.base.CCRepeatForever;
import org.cocos2d.actions.instant.CCCallFunc;
import org.cocos2d.actions.interval.CCAnimate;
import org.cocos2d.actions.interval.CCFadeOut;
import org.cocos2d.actions.interval.CCMoveTo;
import org.cocos2d.actions.interval.CCScaleTo;
import org.cocos2d.actions.interval.CCSequence;
import org.cocos2d.layers.CCTMXLayer;
import org.cocos2d.layers.CCTMXTiledMap;
import org.cocos2d.nodes.CCAnimation;
import org.cocos2d.nodes.CCSprite;
import org.cocos2d.types.CGPoint;
import org.cocos2d.types.CGSize;

public class LevelMap extends CCTMXTiledMap {
    private static final int[] tileKinds = {0, 1, 2, 3, 4, 4, 4, 4, 5, 5, 5, 6, 6, 6, 6, 6, 6, 0, 7, 8, 0, 9, 10, 11, 12, 0, 7, 8, 0, 9, 10, 11, 12};
    private static final int tkBird = 1;
    private static final int tkBlock = 2;
    private static final int tkFastL = 12;
    private static final int tkFastR = 10;
    private static final int tkJump = 5;
    private static final int tkLeft = 7;
    private static final int tkNone = 0;
    private static final int tkNormalL = 11;
    private static final int tkNormalR = 9;
    private static final int tkObstacle = 4;
    private static final int tkOutX = 14;
    private static final int tkOutY = 15;
    private static final int tkRight = 8;
    private static final int tkSplinter = 6;
    private static final int tkTarget = 13;
    private static final int trCherry = 3;
    private CCAction _aniFly;
    private CCAction _aniRun;
    private CCSprite _bird;
    private int _birdDir;
    private float _birdGravity;
    private float _birdVx;
    private float _birdVy;
    private GameLayer _game;
    private boolean _isJumping;
    private CCTMXLayer _layer = layerNamed("Level");
    private CGSize _mapSize = this._layer.layerSize;
    private CGSize _tileSize = getTileSize();

    public LevelMap(GameLayer gameLayer, String str) {
        super(str);
        this._game = gameLayer;
        createBird();
        int i = 0;
        while (true) {
            float f = (float) i;
            if (f < this._mapSize.height) {
                int i2 = 0;
                while (true) {
                    float f2 = (float) i2;
                    if (f2 >= this._mapSize.width) {
                        break;
                    }
                    int i3 = tileKinds[this._layer.tileGIDAt(CGPoint.ccp(f2, f)) >> 24];
                    int i4 = 1;
                    if (i3 == 3) {
                        CCSprite tileAt = this._layer.tileAt(CGPoint.ccp(f2, f));
                        tileAt.setAnchorPoint(0.5f, 0.5f);
                        tileAt.setPosition(CGPoint.ccp(tileAt.getPosition().f57x + (this._tileSize.width * 0.5f), tileAt.getPosition().f58y + (this._tileSize.height * 0.5f)));
                        tileAt.runAction(CCRepeatForever.action(CCSequence.actions(CCScaleTo.action(0.5f, 1.2f), CCScaleTo.action(0.5f, 1.0f))));
                    } else if (!(i3 == 6 || i3 == 5 || i3 != 1)) {
                        this._layer.removeTileAt(CGPoint.ccp(f2, f));
                        Object propertyNamed = propertyNamed("StartDirection");
                        if (propertyNamed != null && propertyNamed.equals("1")) {
                            i4 = -1;
                        }
                        setBirdDir(i4);
                        this._bird.setPosition(((f2 + 0.5f) - (((float) this._birdDir) * 1.3f)) * this._tileSize.width, ((this._mapSize.height - f) - 0.5f) * this._tileSize.height);
                    }
                    i2++;
                }
                i++;
            } else {
                upadtePosition();
                return;
            }
        }
    }

    public void createBird() {
        CCAnimation animation = CCAnimation.animation("run", 0.02f);
        for (int i = 0; i < 16; i++) {
            animation.addFrame(String.format("game/bird/run%d.png", new Object[]{Integer.valueOf(i)}));
        }
        this._aniRun = CCRepeatForever.action(CCAnimate.action(animation));
        CCAnimation animation2 = CCAnimation.animation("fly", 0.02f);
        for (int i2 = 0; i2 < 4; i2++) {
            animation2.addFrame(String.format("game/bird/fly%d.png", new Object[]{Integer.valueOf(i2)}));
        }
        this._aniFly = CCRepeatForever.action(CCAnimate.action(animation2));
        this._bird = CCSprite.sprite("game/bird/run0.png");
        this._bird.setAnchorPoint(0.5f, 0.46f);
        addChild(this._bird, 2);
        this._bird.runAction(this._aniRun);
        this._isJumping = false;
        this._birdVx = C0316G.normalVx;
        this._birdGravity = C0316G.gravity;
        this._birdVy = 0.0f;
    }

    public CGPoint getTilePos(CGPoint cGPoint) {
        return CGPoint.make((float) ((int) (cGPoint.f57x / this._tileSize.width)), (float) ((int) ((this._mapSize.height - ((float) ((int) (cGPoint.f58y / this._tileSize.height)))) - 1.0f)));
    }

    public int getTileKind(CGPoint cGPoint) {
        if (cGPoint.f57x < 0.0f || cGPoint.f57x >= this._mapSize.width) {
            return (cGPoint.f57x < -1.0f || cGPoint.f57x > this._mapSize.width) ? 13 : 14;
        }
        if (cGPoint.f58y < 0.0f) {
            return 0;
        }
        if (cGPoint.f58y >= this._mapSize.height) {
            return 15;
        }
        return tileKinds[this._layer.tileGIDAt(cGPoint) >> 24];
    }

    public void setBirdDir(int i) {
        this._birdDir = i;
        this._bird.setScaleX((float) this._birdDir);
    }

    public void birdJump() {
        if (this._birdVy == 0.0f) {
            if ((this._layer.tileGIDAt(getTilePos(CGPoint.ccp(this._bird.getPosition().f57x, this._bird.getPosition().f58y + (this._tileSize.height * 0.5f)))) >> 24) != 2) {
                this._birdVy = C0316G.jumpVy;
                if (!this._isJumping) {
                    this._isJumping = true;
                    this._bird.stopAction(this._aniRun);
                    this._bird.runAction(this._aniFly);
                }
            }
        }
    }

    public void update(float f) {
        int tileKind;
        this._birdVy -= this._birdGravity;
        CGPoint ccp = CGPoint.ccp(this._bird.getPosition().f57x + (((float) this._birdDir) * this._birdVx), this._bird.getPosition().f58y + this._birdVy);
        CGPoint tilePos = getTilePos(CGPoint.ccp(ccp.f57x, ccp.f58y - (this._tileSize.height * 0.5f)));
        int tileKind2 = getTileKind(tilePos);
        if (tileKind2 == 2 || tileKind2 == 14 || tileKind2 == 5) {
            ccp.f58y = ((this._mapSize.height - tilePos.f58y) + 0.5f) * this._tileSize.height;
            this._bird.setPosition(ccp);
            if (tileKind2 == 5) {
                if (C0316G.sound) {
                    C0316G.soundLongJump.start();
                }
                this._birdVy = C0316G.jumpVy * 2.0f;
                this._birdGravity = C0316G.gravity * 2.0f;
                if (!this._isJumping) {
                    this._isJumping = true;
                    this._bird.stopAction(this._aniRun);
                    this._bird.runAction(this._aniFly);
                }
            } else {
                if (this._isJumping) {
                    this._isJumping = false;
                    this._bird.stopAction(this._aniFly);
                    this._bird.runAction(this._aniRun);
                    this._birdGravity = C0316G.gravity;
                }
                this._birdVy = 0.0f;
            }
            CGPoint tilePos2 = getTilePos(ccp);
            if (getTileKind(tilePos2) == 3) {
                gotCherry(tilePos2);
            }
        } else {
            this._bird.setPosition(ccp);
            if (tileKind2 == 6 || tileKind2 == 4 || tileKind2 == 15) {
                gameOver();
                return;
            } else if (tileKind2 == 13) {
                this._game.gameCompleted();
                return;
            } else if (tileKind2 == 3) {
                gotCherry(tilePos);
            }
        }
        int tileKind3 = getTileKind(getTilePos(CGPoint.ccp(ccp.f57x + (((float) this._birdDir) * this._tileSize.width * 0.5f), ccp.f58y)));
        if (tileKind3 == 2 || tileKind3 == 4) {
            gameOver();
            return;
        }
        CGPoint tilePos3 = getTilePos(ccp);
        while (true) {
            tileKind = getTileKind(tilePos3);
            if (tileKind != 0) {
                break;
            }
            tilePos3.f58y += 1.0f;
        }
        switch (tileKind) {
            case 7:
                if (this._birdDir != -1) {
                    if (C0316G.sound) {
                        C0316G.soundDirection.start();
                    }
                    setBirdDir(-1);
                    break;
                }
                break;
            case 8:
                if (this._birdDir != 1) {
                    if (C0316G.sound) {
                        C0316G.soundDirection.start();
                    }
                    setBirdDir(1);
                    break;
                }
                break;
            case 9:
                if (this._birdDir == 1) {
                    if (C0316G.sound) {
                        C0316G.soundSpeedDown.start();
                    }
                    this._birdVx = C0316G.normalVx;
                    break;
                }
                break;
            case 10:
                if (this._birdDir == 1) {
                    if (C0316G.sound) {
                        C0316G.soundSpeedUp.start();
                    }
                    this._birdVx = C0316G.fastVx;
                    break;
                }
                break;
            case 11:
                if (this._birdDir == -1) {
                    if (C0316G.sound) {
                        C0316G.soundSpeedDown.start();
                    }
                    this._birdVx = C0316G.normalVx;
                    break;
                }
                break;
            case 12:
                if (this._birdDir == -1) {
                    if (C0316G.sound) {
                        C0316G.soundSpeedUp.start();
                    }
                    this._birdVx = C0316G.fastVx;
                    break;
                }
                break;
        }
        upadtePosition();
    }

    public void upadtePosition() {
        float f = (C0316G.width * (0.5f - (((float) this._birdDir) * 0.25f))) - this._bird.getPosition().f57x;
        if (f > 0.0f) {
            f = 0.0f;
        } else if (this.contentSize_.width + f < C0316G.width) {
            f = C0316G.width - this.contentSize_.width;
        }
        setPosition(f, ((C0316G.height * 0.5f) - (this._tileSize.height * 0.5f)) - this._bird.getPosition().f58y);
    }

    public void gotCherry(CGPoint cGPoint) {
        if (C0316G.sound) {
            C0316G.soundCollect.start();
        }
        CGPoint position = this._layer.tileAt(cGPoint).getPosition();
        CCSprite cCSprite = new CCSprite("game/cherry.png");
        cCSprite.setPosition(this._game.convertToNodeSpace(convertToWorldSpace(position.f57x, position.f58y)));
        cCSprite.runAction(CCFadeOut.action(0.99f));
        cCSprite.runAction(CCSequence.actions(CCMoveTo.action(1.0f, CGPoint.ccp(60.0f, C0316G.height - 50.0f)), CCCallFunc.action(cCSprite, "removeSelf")));
        this._game.addChild(cCSprite);
        this._layer.removeTileAt(cGPoint);
        this._game.setScore(this._game.getScore() + 1);
    }

    public void gameOver() {
        if (C0316G.sound) {
            C0316G.soundCollide.start();
        }
        this._game.state = C0316G.gsPause;
        this._bird.stopAllActions();
        this._game.gameOver();
    }
}
