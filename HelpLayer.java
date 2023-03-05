package com.brilliant.kids.game.hopping.bird;

import android.view.KeyEvent;
import android.view.MotionEvent;
import org.cocos2d.layers.CCLayer;
import org.cocos2d.layers.CCScene;
import org.cocos2d.menus.CCMenu;
import org.cocos2d.menus.CCMenuItemImage;
import org.cocos2d.nodes.CCDirector;
import org.cocos2d.nodes.CCSprite;
import org.cocos2d.transitions.CCFadeTransition;

public class HelpLayer extends CCLayer {
    public HelpLayer() {
        setScale(C0316G.scale);
        setAnchorPoint(0.0f, 0.0f);
        CCSprite cCSprite = new CCSprite("menu/menu_bg.png");
        cCSprite.setPosition(C0316G.displayCenter());
        addChild(cCSprite);
        CCSprite cCSprite2 = new CCSprite("menu/help.png");
        cCSprite2.setScale(Math.min(C0316G.display_w / 1280.0f, C0316G.display_h / 800.0f) / C0316G.scale);
        cCSprite2.setPosition(C0316G.displayCenter());
        addChild(cCSprite2);
        CCMenuItemImage item = CCMenuItemImage.item("menu/back1.png", "menu/back2.png", this, "onBack");
        item.setPosition(C0316G.width * 0.09f, C0316G.height * 0.06f);
        CCMenu menu = CCMenu.menu(item);
        menu.setPosition(0.0f, 0.0f);
        addChild(menu);
        setIsTouchEnabled(true);
        setIsKeyEnabled(true);
    }

    public void onBack(Object obj) {
        if (C0316G.sound) {
            C0316G.soundClick.start();
        }
        CCScene node = CCScene.node();
        node.addChild(new MenuLayer(false));
        CCDirector.sharedDirector().replaceScene(CCFadeTransition.transition(0.7f, node));
    }

    public boolean ccTouchesEnded(MotionEvent motionEvent) {
        onBack((Object) null);
        return true;
    }

    public boolean ccKeyDown(int i, KeyEvent keyEvent) {
        onBack((Object) null);
        return true;
    }
}
