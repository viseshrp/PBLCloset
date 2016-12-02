package com.dbfall16.pblcloset.utils;

import android.content.Context;

import com.dbfall16.pblcloset.models.User;

/**
 * Created by viseshprasad on 11/4/16.
 */

public class UserSessionUtils {

    public static void saveUserLoginData(Context mContext, User response) {
        PreferencesUtils.setString(mContext, AppConstants.USER_ID, response.getUserId());
        PreferencesUtils.setString(mContext, AppConstants.EMAIL, response.getEmail());
        PreferencesUtils.setString(mContext, AppConstants.USER_FIRST_NAME, response.getFirstName());
        PreferencesUtils.setString(mContext, AppConstants.USER_LAST_NAME, response.getSecondName());
        PreferencesUtils.setBoolean(mContext, AppConstants.IS_LOGGED_IN, true);
    }
}