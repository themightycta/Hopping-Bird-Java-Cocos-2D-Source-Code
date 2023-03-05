package com.brilliant.kids.game.hopping.bird;

import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.p003v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.View;
import android.widget.RelativeLayout;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import org.cocos2d.layers.CCScene;
import org.cocos2d.nodes.CCDirector;
import org.cocos2d.opengl.CCGLSurfaceView;

public class MainActivity extends AppCompatActivity {
    public AdView adView = null;
    private MyApplication globV;
    private CCGLSurfaceView mGLSurfaceView;

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        this.mGLSurfaceView = new CCGLSurfaceView(this);
        RelativeLayout relativeLayout = new RelativeLayout(this);
        relativeLayout.setLayoutParams(new RelativeLayout.LayoutParams(-1, -1));
        this.adView = new AdView(this);
        this.adView.setAdSize(AdSize.BANNER);
        this.adView.setAdUnitId(getString(C0319R.string.adUnitId));
        this.globV = (MyApplication) getApplicationContext();
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(-2, -2);
        layoutParams.addRule(10, -1);
        layoutParams.addRule(14, -1);
        this.adView.setLayoutParams(layoutParams);
        relativeLayout.addView(this.mGLSurfaceView);
        relativeLayout.addView(this.adView);
        setContentView((View) relativeLayout);
        this.adView.loadAd(new AdRequest.Builder().build());
        CCDirector sharedDirector = CCDirector.sharedDirector();
        sharedDirector.attachInView(this.mGLSurfaceView);
        sharedDirector.setAnimationInterval(0.0d);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        C0316G.display_w = (float) displayMetrics.widthPixels;
        C0316G.display_h = (float) displayMetrics.heightPixels;
        C0316G.scale = Math.max(C0316G.display_w / 1280.0f, C0316G.display_h / 800.0f);
        C0316G.width = C0316G.display_w / C0316G.scale;
        C0316G.height = C0316G.display_h / C0316G.scale;
        SharedPreferences sharedPreferences = CCDirector.sharedDirector().getActivity().getSharedPreferences("GameInfo", 0);
        C0316G.music = sharedPreferences.getBoolean("music", true);
        C0316G.sound = sharedPreferences.getBoolean("sound", true);
        C0316G.soundMenu = MediaPlayer.create(this, C0319R.raw.menu);
        C0316G.soundMenu.setLooping(true);
        C0316G.soundGame = MediaPlayer.create(this, C0319R.raw.game);
        C0316G.soundGame.setLooping(true);
        C0316G.soundCollide = MediaPlayer.create(this, C0319R.raw.collide);
        C0316G.soundJump = MediaPlayer.create(this, C0319R.raw.jump);
        C0316G.soundLongJump = MediaPlayer.create(this, C0319R.raw.long_jump);
        C0316G.soundSpeedDown = MediaPlayer.create(this, C0319R.raw.speed_down);
        C0316G.soundSpeedUp = MediaPlayer.create(this, C0319R.raw.speed_up);
        C0316G.soundDirection = MediaPlayer.create(this, C0319R.raw.direction_sign);
        C0316G.soundClick = MediaPlayer.create(this, C0319R.raw.menu_click);
        C0316G.soundCollect = MediaPlayer.create(this, C0319R.raw.collect);
        C0316G.bgSound = C0316G.soundMenu;
        CCScene node = CCScene.node();
        node.addChild(new MenuLayer(true));
        sharedDirector.runWithScene(node);
    }

    public void onPause() {
        if (this.adView != null) {
            this.adView.pause();
        }
        super.onPause();
        C0316G.bgSound.pause();
        CCDirector.sharedDirector().onPause();
    }

    public void onResume() {
        super.onResume();
        if (this.adView != null) {
            this.adView.resume();
        }
        if (C0316G.music) {
            C0316G.bgSound.start();
        }
        CCDirector.sharedDirector().onResume();
    }

    public void onDestroy() {
        if (this.adView != null) {
            this.adView.destroy();
        }
        super.onDestroy();
        C0316G.bgSound.pause();
        CCDirector.sharedDirector().end();
    }

    public boolean onKeyDown(int i, KeyEvent keyEvent) {
        if (i != 4) {
            return super.onKeyDown(i, keyEvent);
        }
        CCDirector.sharedDirector().onKeyDown(keyEvent);
        return true;
    }
}
