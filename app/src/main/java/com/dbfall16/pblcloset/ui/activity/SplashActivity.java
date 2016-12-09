package com.dbfall16.pblcloset.ui.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.daimajia.androidanimations.library.Techniques;
import com.dbfall16.pblcloset.PBLApp;
import com.dbfall16.pblcloset.R;
import com.dbfall16.pblcloset.utils.AppConstants;
import com.dbfall16.pblcloset.utils.PreferencesUtils;
import com.viksaa.sssplash.lib.activity.AwesomeSplash;
import com.viksaa.sssplash.lib.cnst.Flags;
import com.viksaa.sssplash.lib.model.ConfigSplash;

public class SplashActivity extends AwesomeSplash {
    @Override
    public void initSplash(ConfigSplash configSplash) {
        //Customize Circular Reveal
        configSplash.setBackgroundColor(R.color.colorPrimary); //any colorView you want form colors.xml
        configSplash.setAnimCircularRevealDuration(2000); //int ms
        configSplash.setRevealFlagX(Flags.REVEAL_RIGHT);  //or Flags.REVEAL_LEFT
        configSplash.setRevealFlagY(Flags.REVEAL_BOTTOM); //or Flags.REVEAL_TOP

        //Choose LOGO OR PATH; if you don't provide String value for path it's logo by default

        //Customize Logo
        configSplash.setLogoSplash(R.drawable.pblfinal); //or any other drawable
        configSplash.setAnimLogoSplashDuration(2000); //int ms
        configSplash.setAnimLogoSplashTechnique(Techniques.Bounce); //choose one form Techniques (ref: https://github.com/daimajia/AndroidViewAnimations)


        //Customize Path
        //configSplash.setPathSplash(Constants.DROID_LOGO); //set path String
        configSplash.setOriginalHeight(400); //in relation to your svg (path) resource
        configSplash.setOriginalWidth(400); //in relation to your svg (path) resource
        configSplash.setAnimPathStrokeDrawingDuration(3000);
        configSplash.setPathSplashStrokeSize(3); //I advise value be <5
        configSplash.setPathSplashStrokeColor(R.color.colorAccent); //any colorView you want form colors.xml
        configSplash.setAnimPathFillingDuration(3000);
        configSplash.setPathSplashFillColor(R.color.materialRed); //path object filling colorView


        //TODO : Alternative if animationsFinished doesnt do the login check.
/*
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
            }
        }, SPLASH_TIME);
*/

        //Customize Title
        configSplash.setTitleSplash(PBLApp.get().getString(R.string.app_name));
        configSplash.setTitleTextColor(R.color.textColorPrimary);
        configSplash.setTitleTextSize(30f); //float value
        configSplash.setAnimTitleDuration(3000);
        configSplash.setAnimTitleTechnique(Techniques.FadeInDown);
        //configSplash.setTitleFont("fonts/myfont.ttf"); //provide string to your font located in assets/fonts/

    }

    @Override
    public void animationsFinished() {
        //// TODO: 11/21/16 check default values later.
        Intent intent = null;
        if (PreferencesUtils.getBoolean(SplashActivity.this, AppConstants.IS_LOGGED_IN, false)) {
            if (PreferencesUtils.getString(SplashActivity.this, AppConstants.USER_TYPE, "").equals(AppConstants.USER_TYPE_BUYER))
                intent = new Intent(SplashActivity.this, MainActivity.class);
            else
                intent = new Intent(SplashActivity.this, DonorActivity.class);
        } else {
            intent = new Intent(SplashActivity.this, PBLAppIntro.class);
        }
        startActivity(intent);
        finish();
    }

}
