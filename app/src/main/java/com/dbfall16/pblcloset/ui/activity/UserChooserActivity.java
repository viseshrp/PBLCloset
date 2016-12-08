package com.dbfall16.pblcloset.ui.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.dbfall16.pblcloset.R;
import com.dbfall16.pblcloset.utils.AppConstants;
import com.dbfall16.pblcloset.utils.PreferencesUtils;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class UserChooserActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_chooser);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.donor_login_button)
    void onDonorClicked() {
        Log.e("Str","afaffaf");
        PreferencesUtils.setString(UserChooserActivity.this, AppConstants.USER_TYPE, AppConstants.USER_TYPE_DONOR);
        startActivity(new Intent(UserChooserActivity.this, LoginActivity.class));
        finish();
    }

    @OnClick(R.id.buyer_login_button)
    void onBuyerClicked() {
        PreferencesUtils.setString(UserChooserActivity.this, AppConstants.USER_TYPE, AppConstants.USER_TYPE_BUYER);
        startActivity(new Intent(UserChooserActivity.this, LoginActivity.class));
        finish();
    }

    //// TODO: 12/1/16
    @OnClick(R.id.action_login_skip)
    void onSkipClicked() {
        PreferencesUtils.setString(UserChooserActivity.this, AppConstants.USER_TYPE, AppConstants.USER_TYPE_UNREG);
        startActivity(new Intent(UserChooserActivity.this, MainActivity.class));
        finish();
    }

}
