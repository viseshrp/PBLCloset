package com.dbfall16.pblcloset.ui.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.dbfall16.pblcloset.R;
import com.dbfall16.pblcloset.utils.AppConstants;

import butterknife.OnClick;

public class UserChooserActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_chooser);
    }

    @OnClick(R.id.donor_login_button)
    void onDonorClicked() {
        startActivity(new Intent(UserChooserActivity.this, LoginActivity.class).putExtra(AppConstants.USER_TYPE, AppConstants.USER_TYPE_DONOR));
        finish();
    }

    @OnClick(R.id.buyer_login_button)
    void onBuyerClicked() {
        startActivity(new Intent(UserChooserActivity.this, LoginActivity.class).putExtra(AppConstants.USER_TYPE, AppConstants.USER_TYPE_BUYER));
        finish();
    }

    //// TODO: 12/1/16
    @OnClick(R.id.action_login_skip)
    void onSkipClicked() {
        //startActivity(new Intent(UserChooserActivity.this, MainActivity.class).putExtra(AppConstants.USER_TYPE, "unreg"));
    }

}
