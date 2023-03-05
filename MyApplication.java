package com.brilliant.kids.game.hopping.bird;

import android.app.Application;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;

public class MyApplication extends Application {
    private InterstitialAd interstitial;

    public void onCreate() {
        super.onCreate();
        this.interstitial = new InterstitialAd(this);
        this.interstitial.setAdUnitId(getString(C0319R.string.interId));
        this.interstitial.loadAd(new AdRequest.Builder().build());
        this.interstitial.setAdListener(new AdListener() {
            public void onAdClosed() {
                super.onAdClosed();
                MyApplication.this.reLoadInterstitial();
            }
        });
    }

    public void showInterstitial() {
        if (this.interstitial.isLoaded()) {
            this.interstitial.show();
        }
    }

    /* access modifiers changed from: private */
    public void reLoadInterstitial() {
        this.interstitial.loadAd(new AdRequest.Builder().build());
    }
}
