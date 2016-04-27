package com.may.amy.piqz.util;

import android.app.Application;
import android.content.SharedPreferences;

/**
 * Created by kuhnertj on 26.04.2016.
 */
public class AppUtil extends Application {
    private static final String KEY_PREF = "AppUtil";
    public static final String KEY_AFTER = "after";
    public static final String KEY_BEFORE= "before";
    public static final String KEY_DEVICE_ID= "device_id";
    public static final String KEY_TOKEN = "token";
    public static final String KEY_TOKEN_TYPE = "token_type";
    public static final String KEY_EXPIRES_IN = "expires_in";
    public static final String KEY_SCOPE= "scope";



    private static AppUtil sInstance;
    private SharedPreferences mSharedPreferences;


    @Override
    public void onCreate() {
        super.onCreate();
        sInstance = this;
        initializeInstance();

    }

    public static AppUtil getInstance() {
        return sInstance;
    }

    private void initializeInstance() {
        mSharedPreferences = getSharedPreferences(KEY_PREF, MODE_PRIVATE);
    }

    public SharedPreferences getAppPreferences() {
        return mSharedPreferences;
    }
}
