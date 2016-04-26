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
    public static final String KEY_DEVICE_ID= "before";



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
