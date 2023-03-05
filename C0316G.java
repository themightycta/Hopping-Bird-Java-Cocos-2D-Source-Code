package com.brilliant.kids.game.hopping.bird;

import android.media.MediaPlayer;
import org.cocos2d.types.CGPoint;

/* renamed from: com.brilliant.kids.game.hopping.bird.G */
public class C0316G {
    public static MediaPlayer bgSound = null;
    public static float display_h = 0.0f;
    public static float display_w = 0.0f;
    public static float fastVx = (normalVx * 2.0f);
    public static float gravity = ((((normalVx * 432.0f) * normalVx) / 180.0f) / 180.0f);
    public static int gsPause = 0;
    public static int gsReady = 1;
    public static int gsRun = 2;
    public static float height = 0.0f;
    public static float jumpVy = (((gravity * 180.0f) + 0.1f) / normalVx);
    public static boolean music = false;
    public static float normalVx = 7.0f;
    public static float scale;
    public static boolean sound;
    public static MediaPlayer soundClick;
    public static MediaPlayer soundCollect;
    public static MediaPlayer soundCollide;
    public static MediaPlayer soundDirection;
    public static MediaPlayer soundGame;
    public static MediaPlayer soundJump;
    public static MediaPlayer soundLongJump;
    public static MediaPlayer soundMenu;
    public static MediaPlayer soundSpeedDown;
    public static MediaPlayer soundSpeedUp;
    public static float width;

    public static CGPoint displayCenter() {
        return CGPoint.ccp(width / 2.0f, height / 2.0f);
    }
}
