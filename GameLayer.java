package com.brilliant.kids.game.hopping.bird;

import android.app.Activity;
import android.content.SharedPreferences;
import android.view.KeyEvent;
import android.view.MotionEvent;
import org.cocos2d.actions.ease.CCEaseElasticIn;
import org.cocos2d.actions.ease.CCEaseElasticOut;
import org.cocos2d.actions.instant.CCCallFunc;
import org.cocos2d.actions.interval.CCDelayTime;
import org.cocos2d.actions.interval.CCFadeIn;
import org.cocos2d.actions.interval.CCFadeOut;
import org.cocos2d.actions.interval.CCMoveTo;
import org.cocos2d.actions.interval.CCSequence;
import org.cocos2d.layers.CCColorLayer;
import org.cocos2d.layers.CCLayer;
import org.cocos2d.layers.CCScene;
import org.cocos2d.menus.CCMenu;
import org.cocos2d.menus.CCMenuItemImage;
import org.cocos2d.nodes.CCDirector;
import org.cocos2d.nodes.CCLabelAtlas;
import org.cocos2d.nodes.CCSprite;
import org.cocos2d.nodes.CCTextureCache;
import org.cocos2d.transitions.CCFadeTransition;
import org.cocos2d.types.CGPoint;
import org.cocos2d.types.ccColor4B;

public class GameLayer extends CCLayer {
    private static final int[] maxLevel = {60, 25, 20};
    private int _level;
    private int _levelMode;
    private LevelMap _map;
    private CCColorLayer _mask;
    private CCSprite _msg;
    private int _score;
    private CCLabelAtlas _scoreLabel;
    private boolean _touched;
    public int state;

    public GameLayer(int i, int i2, boolean z) {
        if (z) {
            if (C0316G.bgSound.isPlaying()) {
                C0316G.bgSound.pause();
            }
            C0316G.bgSound = C0316G.soundGame;
            if (C0316G.music) {
                C0316G.bgSound.start();
            }
        }
        setScale(C0316G.scale);
        setAnchorPoint(0.0f, 0.0f);
        this._levelMode = i;
        this._level = i2;
        this._score = CCDirector.sharedDirector().getActivity().getSharedPreferences("GameInfo", 0).getInt(String.format("GameScore%d_%d", new Object[]{Integer.valueOf(this._levelMode), Integer.valueOf(this._level - 1)}), 0);
        CCSprite cCSprite = new CCSprite("game/game_bg.png");
        cCSprite.setPosition(C0316G.displayCenter());
        addChild(cCSprite);
        this._map = new LevelMap(this, String.format("levels/level%d_%d.tmx", new Object[]{Integer.valueOf(this._levelMode), Integer.valueOf(this._level)}));
        addChild(this._map);
        CCSprite cCSprite2 = new CCSprite("game/score.png");
        cCSprite2.setAnchorPoint(1.0f, 0.5f);
        cCSprite2.setPosition(80.0f, C0316G.height - 50.0f);
        addChild(cCSprite2);
        this._scoreLabel = CCLabelAtlas.label(String.format("%d", new Object[]{Integer.valueOf(this._score)}), "font.png", 34, 43, '0');
        this._scoreLabel.setPosition(100.0f, C0316G.height - 76.0f);
        addChild(this._scoreLabel);
        CCSprite cCSprite3 = new CCSprite("game/level.png");
        cCSprite3.setAnchorPoint(4.0f, 0.5f);
        cCSprite3.setPosition((C0316G.width * 0.5f) - 10.0f, C0316G.height - 50.0f);
        addChild(cCSprite3);
        CCLabelAtlas label = CCLabelAtlas.label(String.format("%d", new Object[]{Integer.valueOf(this._level)}), "font.png", 34, 43, '0');
        label.setPosition((C0316G.width * 0.23f) + 10.0f, C0316G.height - 76.0f);
        addChild(label);
        CCMenuItemImage item = CCMenuItemImage.item("menu/back1.png", "menu/back2.png", this, "onPause");
        item.setScaleX(-1.0f);
        item.setPosition(C0316G.width - 80.0f, C0316G.height - 50.0f);
        CCMenu menu = CCMenu.menu(item);
        menu.setPosition(0.0f, 0.0f);
        addChild(menu);
        this._mask = CCColorLayer.node(ccColor4B.ccc4(0, 0, 0, 255), C0316G.width, C0316G.height);
        addChild(this._mask);
        this._msg = new CCSprite("game/get_ready_logo.png");
        this._msg.setPosition((-C0316G.width) * 0.5f, C0316G.height * 0.5f);
        addChild(this._msg, 3);
        this.state = C0316G.gsPause;
        this._mask.runAction(CCFadeOut.action(0.6f));
        this._msg.runAction(CCSequence.actions(CCEaseElasticOut.action(CCMoveTo.action(0.6f, C0316G.displayCenter()), 0.5f), CCCallFunc.action(this, "gameReady")));
        setIsTouchEnabled(true);
        setIsKeyEnabled(true);
        scheduleUpdate();
    }

    public int getScore() {
        return this._score;
    }

    public void gameReady() {
        this.state = C0316G.gsReady;
    }

    public void gameRun() {
        this.state = C0316G.gsRun;
    }

    public void setScore(int i) {
        this._score = i;
        this._scoreLabel.setString(String.format("%d", new Object[]{Integer.valueOf(this._score)}));
    }

    public void gameOver() {
        this._mask.runAction(CCFadeIn.action(0.6f));
        this._msg.setTexture(CCTextureCache.sharedTextureCache().addImage("game/failed_logo.png"));
        this._msg.runAction(CCSequence.actions(CCEaseElasticOut.action(CCMoveTo.action(0.6f, C0316G.displayCenter()), 0.5f), CCDelayTime.action(0.5f), CCEaseElasticIn.action(CCMoveTo.action(0.6f, CGPoint.ccp((-C0316G.width) * 0.5f, C0316G.height * 0.5f)), 0.5f), CCCallFunc.action(this, "restart")));
        final Activity activity = CCDirector.sharedDirector().getActivity();
        activity.runOnUiThread(new Runnable() {
            public void run() {
                ((MyApplication) activity.getApplicationContext()).showInterstitial();
            }
        });
    }

    public void restart() {
        CCScene node = CCScene.node();
        node.addChild(new GameLayer(this._levelMode, this._level, false));
        CCDirector.sharedDirector().replaceScene(node);
    }

    public void gameCompleted() {
        this.state = C0316G.gsPause;
        this._mask.runAction(CCFadeIn.action(0.6f));
        this._msg.setTexture(CCTextureCache.sharedTextureCache().addImage("game/level_completed_logo.png"));
        this._msg.runAction(CCSequence.actions(CCEaseElasticOut.action(CCMoveTo.action(0.6f, C0316G.displayCenter()), 0.5f), CCDelayTime.action(0.5f), CCEaseElasticIn.action(CCMoveTo.action(0.6f, CGPoint.ccp((-C0316G.width) * 0.5f, C0316G.height * 0.5f)), 0.5f), CCCallFunc.action(this, "nextLevel")));
    }

    public void nextLevel() {
        SharedPreferences sharedPreferences = CCDirector.sharedDirector().getActivity().getSharedPreferences("GameInfo", 0);
        SharedPreferences.Editor edit = sharedPreferences.edit();
        edit.putInt(String.format("GameScore%d_%d", new Object[]{Integer.valueOf(this._levelMode), Integer.valueOf(this._level)}), this._score);
        this._level++;
        if (this._level > sharedPreferences.getInt(String.format("GameLevel%d", new Object[]{Integer.valueOf(this._levelMode)}), 1)) {
            edit.putInt(String.format("GameLevel%d", new Object[]{Integer.valueOf(this._levelMode)}), this._level);
        }
        edit.commit();
        if (this._level > maxLevel[this._levelMode]) {
            CCScene node = CCScene.node();
            node.addChild(new SelectLayer(true));
            CCDirector.sharedDirector().replaceScene(CCFadeTransition.transition(0.7f, node));
            return;
        }
        CCScene node2 = CCScene.node();
        node2.addChild(new GameLayer(this._levelMode, this._level, false));
        CCDirector.sharedDirector().replaceScene(node2);
    }

    public void update(float f) {
        if (this.state == C0316G.gsRun) {
            if (this._touched) {
                this._map.birdJump();
            }
            this._map.update(f);
        }
    }

    public boolean ccTouchesBegan(MotionEvent motionEvent) {
        this._touched = true;
        if (this.state == C0316G.gsReady) {
            this.state = C0316G.gsPause;
            this._msg.runAction(CCSequence.actions(CCEaseElasticIn.action(CCMoveTo.action(0.6f, CGPoint.ccp(C0316G.width * 1.5f, C0316G.height * 0.5f)), 0.5f), CCCallFunc.action(this, "gameRun")));
        }
        return true;
    }

    public boolean ccTouchesEnded(MotionEvent motionEvent) {
        this._touched = false;
        return true;
    }

    public boolean ccKeyDown(int i, KeyEvent keyEvent) {
        onPause((Object) null);
        return true;
    }

    public void onPause(Object obj) {
        if (C0316G.sound) {
            C0316G.soundClick.start();
        }
        CCScene node = CCScene.node();
        node.addChild(new MenuLayer(true));
        CCDirector.sharedDirector().replaceScene(CCFadeTransition.transition(0.7f, node));
    }
}
