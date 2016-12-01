package com.dbfall16.pblcloset.ui.activity;

import android.Manifest;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import com.dbfall16.pblcloset.R;
import com.dbfall16.pblcloset.ui.fragments.ParentSlide;
import com.github.paolorotolo.appintro.AppIntro;
import com.github.paolorotolo.appintro.AppIntro2;

/**
 * Created by viseshprasad on 12/1/16.
 */

public class PBLAppIntro extends AppIntro {

    @Override
    public void init(Bundle savedInstanceState) {

        //// TODO: 12/1/16 : replace slide images

        addSlide(ParentSlide.newInstance(R.layout.app_intro_slide_1));
        addSlide(ParentSlide.newInstance(R.layout.app_intro_slide_2));
        addSlide(ParentSlide.newInstance(R.layout.app_intro_slide_3));
        addSlide(ParentSlide.newInstance(R.layout.app_intro_slide_4));

        showStatusBar(false);
    }

    private void loadMainActivity() {
        Intent intent = new Intent(this, UserChooserActivity.class);
        startActivity(intent);
    }

    @Override
    public void onSkipPressed() {
        loadMainActivity();
    }

    @Override
    public void onNextPressed() {
    }

    @Override
    public void onDonePressed() {
        loadMainActivity();
    }

    @Override
    public void onSlideChanged() {
    }

}
