package com.may.amy.piqz.util;

/**
 * Kac = Keys and Constants
 */

public class KaC {

    //Keys for AppPreferences
    public static final String KEY_AFTER = "after";
    public static final String KEY_DEVICE_ID = "device_id";
    public static final String KEY_BEFORE = "before";
    public static final String KEY_TOKEN = "token";
    public static final String KEY_TOKEN_TYPE = "token_type";
    public static final String KEY_EXPIRES_IN = "expires_in";
    public static final String KEY_SCOPE = "scope";
    public static final String KEY_EXPIRES_AT = "expires_at";


    //Post types
    public static final int TYPE_GENERIC = 0; //every unsuspected post type
    public static final int TYPE_IMAGE = 1; //simple image, url contains .jpg or .png
    public static final int TYPE_GIF = 2; //simple gif type, url contains .gif
    public static final int TYPE_SELF = 3; //Text type, like a joke
    public static final int TYPE_YOUTUBE = 4; //link to a youtube video, show preview, onclick opens yt
    public static final int TYPE_GALLERY = 5; //gallery
}
