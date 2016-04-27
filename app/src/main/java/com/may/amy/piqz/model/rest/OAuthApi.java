package com.may.amy.piqz.model.rest;

import android.content.Context;

import com.loopj.android.http.Base64;
import com.may.amy.piqz.model.AuthResponseBody;
import com.may.amy.piqz.util.AppConstants;
import com.may.amy.piqz.util.AppUtil;

import java.io.IOException;
import java.util.UUID;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by kuhnertj on 15.04.2016.
 */
public class OAuthApi {
    private static final String BASE_URL = "https://ssl.reddit.com";
    private static final String GRANT_TYPE = "https://oauth.reddit.com/grants/installed_client";

    private final AuthHelper mAuthHelper;

    public OAuthApi() {
        Retrofit retrofit = new Retrofit.Builder()
                .client(getClient())
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(BASE_URL)
                .build();

        mAuthHelper = retrofit.create(AuthHelper.class);
    }

    private OkHttpClient getClient() {
        return new OkHttpClient.Builder()
                .addInterceptor(new Interceptor() {
                    @Override
                    public Response intercept(Chain chain) throws IOException {
                        Request original = chain.request();
                        String credentials = AppConstants.CLIENT_ID + ":" + "";
                        final String basic =
                                "Basic " + Base64.encodeToString(credentials.getBytes(), Base64.NO_WRAP);

                        Request.Builder requestBuilder = original.newBuilder()
                                .header("Authorization", basic)
                                .header("Accept", "application/json")
                                .method(original.method(), original.body());

                        Request request = requestBuilder.build();
                        return chain.proceed(request);
                    }
                })
                .build();
    }

    public void auth(Callback<AuthResponseBody> handler) {
        Call<AuthResponseBody> responseCall =
                mAuthHelper.auth(GRANT_TYPE, getDeviceId());
        responseCall.enqueue(handler);
    }

    private String getDeviceId() {
        if (AppUtil.getInstance().getAppPreferences().getString(AppUtil.KEY_DEVICE_ID, null) == null) {
            String uuid = UUID.randomUUID().toString();
            AppUtil.getInstance().getAppPreferences().edit().putString(AppUtil.KEY_DEVICE_ID, uuid).apply();
        }
        return AppUtil.getInstance().getAppPreferences().getString(AppUtil.KEY_DEVICE_ID, null);
    }

}