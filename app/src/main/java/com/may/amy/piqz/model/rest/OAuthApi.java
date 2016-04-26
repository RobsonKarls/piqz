package com.may.amy.piqz.model.rest;

import android.content.Context;
import android.content.SharedPreferences;

import com.loopj.android.http.Base64;
import com.loopj.android.http.RequestParams;
import com.may.amy.piqz.model.AuthResponseBody;

import java.io.IOException;
import java.util.UUID;

import okhttp3.Authenticator;
import okhttp3.Credentials;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.Route;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by kuhnertj on 15.04.2016.
 */
public class OAuthApi {
    private static String CLIENT_ID = "FwkV6F1VZWc0uw";
    private static final String BASE_URL = "https://www.reddit.com";
    private static String REDIRECT_URI = "http://amylinn.github.io/piqz";
    private static String GRANT_TYPE = "https://oauth.reddit.com/grants/installed_client";


    private SharedPreferences pref;
    private String token;
    private Context context;

    private final AuthHelper mAuthHelper;

    public OAuthApi(Context cnt) {
        context = cnt;
        pref = cnt.getSharedPreferences("AppPref", Context.MODE_PRIVATE);


        Retrofit retrofit = new Retrofit.Builder()
                .client(getClient())
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(BASE_URL)
                .build();

        mAuthHelper = retrofit.create(AuthHelper.class);
    }

    private OkHttpClient getClient() {
        return new OkHttpClient.Builder()
              /*  .authenticator(new Authenticator() {
                    @Override
                    public Request authenticate(Route route, Response response) throws IOException {
                        //String credential = Credentials.basic(CLIENT_ID, "");
                        String credentials = CLIENT_ID + ":" + "";
                        String cred = "Basic " + Base64.encodeToString(credentials.getBytes(), Base64.NO_WRAP);
                        Request.Builder builder = response.request().newBuilder().addHeader("Authorization", cred)
                                .addHeader("Accept", "application/json");
                        return builder.build();
                    }
                })*/
                .addInterceptor(new Interceptor() {
                    @Override
                    public Response intercept(Chain chain) throws IOException {
                        Request original = chain.request();
                        String credentials = CLIENT_ID + ":" + "";
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
/*        RequestParams requestParams = new RequestParams();
        requestParams.put("grant_type", "https://oauth.reddit.com/grants/installed_client");
       // requestParams.put("redirect_uri", REDIRECT_URI);
        requestParams.put("device_id", getDeviceId());

        Call<AuthResponseBody> responseCall = mAuthHelper.auth(Credentials.basic(CLIENT_ID, ""), requestParams);*/
        String credentials = CLIENT_ID + ":" + "";
        //String cred = Credentials.basic(CLIENT_ID, "");
        String cred = "Basic " + Base64.encodeToString(credentials.getBytes(), Base64.NO_WRAP);
        Call<AuthResponseBody> responseCall =
                mAuthHelper.auth(GRANT_TYPE, getDeviceId(), REDIRECT_URI);
        responseCall.enqueue(handler);
    }

    private String getDeviceId() {
        if (pref.getString("device_id", null) == null) {
            String uuid = UUID.randomUUID().toString();
            pref.edit().putString("device_id", uuid).apply();
        }
        return pref.getString("device_id", null);
    }

}