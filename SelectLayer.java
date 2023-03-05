package com.brilliant.kids.game.hopping.bird;

import android.view.KeyEvent;
import org.cocos2d.layers.CCLayer;
import org.cocos2d.layers.CCScene;
import org.cocos2d.menus.CCMenu;
import org.cocos2d.menus.CCMenuItemImage;
import org.cocos2d.nodes.CCDirector;
import org.cocos2d.nodes.CCLabelAtlas;
import org.cocos2d.nodes.CCNode;
import org.cocos2d.nodes.CCSprite;
import org.cocos2d.transitions.CCFadeTransition;

public class SelectLayer extends CCLayer {
    int currentLevel;
    CCMenuItemImage decrease;
    CCMenuItemImage increase;
    CCLabelAtlas levelLabel;
    int levelMode;
    int maxLevel;
    CCNode packPage;
    CCNode selectPage;

    public SelectLayer(boolean z) {
        if (z) {
            if (C0316G.bgSound.isPlaying()) {
                C0316G.bgSound.pause();
            }
            C0316G.bgSound = C0316G.soundMenu;
            if (C0316G.music) {
                C0316G.bgSound.start();
            }
        }
        setScale(C0316G.scale);
        setAnchorPoint(0.0f, 0.0f);
        CCSprite cCSprite = new CCSprite("menu/menu_bg.png");
        cCSprite.setPosition(C0316G.displayCenter());
        addChild(cCSprite);
        createPackPage();
        createSelectPage();
        addChild(this.packPage);
        setIsKeyEnabled(true);
    }

    public void createPackPage() {
        this.packPage = CCNode.node();
        CCSprite cCSprite = new CCSprite("menu/level_pack.png");
        cCSprite.setAnchorPoint(0.5f, 1.0f);
        cCSprite.setPosition(C0316G.width * 0.5f, C0316G.height * 0.9f);
        this.packPage.addChild(cCSprite);
        CCMenuItemImage item = CCMenuItemImage.item("menu/beginning1.png", "menu/beginning2.png", this, "onBeginning");
        item.setPosition(C0316G.width * 0.5f, C0316G.height * 0.56f);
        CCMenuItemImage item2 = CCMenuItemImage.item("menu/evolution1.png", "menu/evolution2.png", this, "onEvolution");
        item2.setPosition(C0316G.width * 0.5f, C0316G.height * 0.36f);
        CCMenuItemImage item3 = CCMenuItemImage.item("menu/experience1.png", "menu/experience2.png", this, "onExperience");
        item3.setPosition(C0316G.width * 0.5f, C0316G.height * 0.16f);
        CCMenuItemImage item4 = CCMenuItemImage.item("menu/back1.png", "menu/back2.png", this, "onBack1");
        item4.setPosition(C0316G.width * 0.09f, C0316G.height * 0.06f);
        CCMenu menu = CCMenu.menu(item, item2, item3, item4);
        menu.setPosition(0.0f, 0.0f);
        this.packPage.addChild(menu);
    }

    public void onBeginning(Object obj) {
        showSelectPage(0);
    }

    public void onEvolution(Object obj) {
        showSelectPage(1);
    }

    public void onExperience(Object obj) {
        showSelectPage(2);
    }

    public void onBack1(Object obj) {
        if (C0316G.sound) {
            C0316G.soundClick.start();
        }
        CCScene node = CCScene.node();
        node.addChild(new MenuLayer(false));
        CCDirector.sharedDirector().replaceScene(CCFadeTransition.transition(0.7f, node));
    }

    public void showSelectPage(int i) {
        if (C0316G.sound) {
            C0316G.soundClick.start();
        }
        this.levelMode = i;
        this.maxLevel = CCDirector.sharedDirector().getActivity().getSharedPreferences("GameInfo", 0).getInt(String.format("GameLevel%d", new Object[]{Integer.valueOf(this.levelMode)}), 1);
        if (this.maxLevel > 1) {
            removeChild(this.packPage, false);
            addChild(this.selectPage);
            setLevel(this.maxLevel);
            return;
        }
        this.currentLevel = 1;
        onStart((Object) null);
    }

    public void createSelectPage() {
        this.selectPage = CCNode.node();
        CCSprite cCSprite = new CCSprite("menu/level_select.png");
        cCSprite.setAnchorPoint(0.5f, 1.0f);
        cCSprite.setPosition(C0316G.width * 0.5f, C0316G.height * 0.9f);
        this.selectPage.addChild(cCSprite);
        this.levelLabel = CCLabelAtlas.label("0", "font.png", 34, 43, '0');
        this.levelLabel.setAnchorPoint(0.5f, 0.5f);
        this.levelLabel.setPosition(C0316G.width * 0.5f, C0316G.height * 0.5f);
        this.selectPage.addChild(this.levelLabel);
        this.decrease = CCMenuItemImage.item("menu/arrow1.png", "menu/arrow2.png", this, "onDecrease");
        this.decrease.setPosition((C0316G.width * 0.5f) - 150.0f, C0316G.height * 0.5f);
        this.increase = CCMenuItemImage.item("menu/arrow1.png", "menu/arrow2.png", this, "onIncrease");
        this.increase.setScaleX(-1.0f);
        this.increase.setPosition((C0316G.width * 0.5f) + 150.0f, C0316G.height * 0.5f);
        CCMenuItemImage item = CCMenuItemImage.item("menu/start1.png", "menu/start2.png", this, "onStart");
        item.setPosition(C0316G.width * 0.5f, C0316G.height * 0.3f);
        CCMenuItemImage item2 = CCMenuItemImage.item("menu/back1.png", "menu/back2.png", this, "onBack2");
        item2.setPosition(C0316G.width * 0.09f, C0316G.height * 0.06f);
        CCMenu menu = CCMenu.menu(this.decrease, this.increase, item, item2);
        menu.setPosition(0.0f, 0.0f);
        this.selectPage.addChild(menu);
    }

    public void setLevel(int i) {
        this.currentLevel = i;
        int i2 = 128;
        this.decrease.setOpacity(this.currentLevel > 1 ? 255 : 128);
        this.decrease.setIsEnabled(this.currentLevel > 1);
        CCMenuItemImage cCMenuItemImage = this.increase;
        if (this.currentLevel < this.maxLevel) {
            i2 = 255;
        }
        cCMenuItemImage.setOpacity(i2);
        this.increase.setIsEnabled(this.currentLevel < this.maxLevel);
        this.levelLabel.setString(String.format("%02d", new Object[]{Integer.valueOf(this.currentLevel)}));
    }

    public void onDecrease(Object obj) {
        if (C0316G.sound) {
            C0316G.soundClick.start();
        }
        if (this.currentLevel > 1) {
            setLevel(this.currentLevel - 1);
        }
    }

    public void onIncrease(Object obj) {
        if (C0316G.sound) {
            C0316G.soundClick.start();
        }
        if (this.currentLevel < this.maxLevel) {
            setLevel(this.currentLevel + 1);
        }
    }

    public void onStart(Object obj) {
        if (C0316G.sound) {
            C0316G.soundClick.start();
        }
        CCScene node = CCScene.node();
        node.addChild(new GameLayer(this.levelMode, this.currentLevel, true));
        CCDirector.sharedDirector().replaceScene(CCFadeTransition.transition(0.7f, node));
    }

    public void onBack2(Object obj) {
        if (C0316G.sound) {
            C0316G.soundClick.start();
        }
        addChild(this.packPage);
        removeChild(this.packPage, false);
    }

    public boolean ccKeyDown(int i, KeyEvent keyEvent) {
        if (this.selectPage.getParent() == null) {
            onBack1((Object) null);
            return true;
        }
        onBack2((Object) null);
        return true;
    }
}
