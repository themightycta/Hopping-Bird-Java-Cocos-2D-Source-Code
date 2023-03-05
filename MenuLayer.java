package com.brilliant.kids.game.hopping.bird;

import android.content.SharedPreferences;
import android.os.Process;
import android.view.KeyEvent;
import org.cocos2d.layers.CCLayer;
import org.cocos2d.layers.CCScene;
import org.cocos2d.menus.CCMenu;
import org.cocos2d.menus.CCMenuItemImage;
import org.cocos2d.menus.CCMenuItemToggle;
import org.cocos2d.nodes.CCDirector;
import org.cocos2d.nodes.CCSprite;
import org.cocos2d.transitions.CCFadeTransition;

public class MenuLayer extends CCLayer {
    public MenuLayer(boolean z) {
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
        CCSprite cCSprite2 = new CCSprite("menu/bird.png");
        cCSprite2.setAnchorPoint(0.0f, 0.0f);
        cCSprite2.setPosition(0.0f, 0.0f);
        addChild(cCSprite2);
        CCSprite cCSprite3 = new CCSprite("menu/leaves.png");
        cCSprite3.setAnchorPoint(0.0f, 1.0f);
        cCSprite3.setPosition(0.0f, C0316G.height);
        addChild(cCSprite3);
        CCSprite cCSprite4 = new CCSprite("menu/title.png");
        cCSprite4.setPosition(C0316G.width * 0.47f, C0316G.height * 0.73f);
        addChild(cCSprite4);
        CCMenuItemImage item = CCMenuItemImage.item("menu/play1.png", "menu/play2.png", this, "onPlay");
        item.setAnchorPoint(1.0f, 0.0f);
        item.setPosition(C0316G.width, 0.0f);
        CCMenuItemToggle item2 = CCMenuItemToggle.item(this, "onSound", CCMenuItemImage.item("menu/sound_off.png", "menu/sound_off.png"), CCMenuItemImage.item("menu/sound_on.png", "menu/sound_on.png"));
        item2.setSelectedIndex(C0316G.sound ? 1 : 0);
        item2.setPosition(C0316G.width * 0.4f, 70.0f);
        CCMenuItemToggle item3 = CCMenuItemToggle.item(this, "onMusic", CCMenuItemImage.item("menu/music_off.png", "menu/music_off.png"), CCMenuItemImage.item("menu/music_on.png", "menu/music_on.png"));
        item3.setSelectedIndex(C0316G.music ? 1 : 0);
        item3.setPosition(C0316G.width * 0.5f, 70.0f);
        CCMenuItemImage item4 = CCMenuItemImage.item("menu/help1.png", "menu/help2.png", this, "onHelp");
        item4.setPosition(C0316G.width * 0.6f, 70.0f);
        CCMenu menu = CCMenu.menu(item, item2, item3, item4);
        menu.setPosition(0.0f, 0.0f);
        addChild(menu);
        setIsKeyEnabled(true);
    }

    public void onPlay(Object obj) {
        if (C0316G.sound) {
            C0316G.soundClick.start();
        }
        CCScene node = CCScene.node();
        node.addChild(new SelectLayer(false));
        CCDirector.sharedDirector().replaceScene(CCFadeTransition.transition(0.7f, node));
    }

    public void onSound(Object obj) {
        if (C0316G.sound) {
            C0316G.soundClick.start();
        }
        C0316G.sound = !C0316G.sound;
        SharedPreferences.Editor edit = CCDirector.sharedDirector().getActivity().getSharedPreferences("GameInfo", 0).edit();
        edit.putBoolean("sound", C0316G.sound);
        edit.commit();
    }

    public void onMusic(Object obj) {
        if (C0316G.sound) {
            C0316G.soundClick.start();
        }
        C0316G.music = !C0316G.music;
        SharedPreferences.Editor edit = CCDirector.sharedDirector().getActivity().getSharedPreferences("GameInfo", 0).edit();
        edit.putBoolean("music", C0316G.music);
        edit.commit();
        if (C0316G.music) {
            C0316G.bgSound.start();
        } else {
            C0316G.bgSound.pause();
        }
    }

    public void onHelp(Object obj) {
        if (C0316G.sound) {
            C0316G.soundClick.start();
        }
        CCScene node = CCScene.node();
        node.addChild(new HelpLayer());
        CCDirector.sharedDirector().replaceScene(CCFadeTransition.transition(0.7f, node));
    }

    public boolean ccKeyDown(int i, KeyEvent keyEvent) {
        Process.killProcess(Process.myPid());
        Runtime.getRuntime().gc();
        System.gc();
        return true;
    }
}
