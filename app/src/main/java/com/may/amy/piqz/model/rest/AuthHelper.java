package com.may.amy.piqz.model.rest;

import com.loopj.android.http.RequestParams;
import com.may.amy.piqz.model.AuthResponseBody;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Header;
import retrofit2.http.POST;

/**
 * Created by Amy on 16.04.2016.
 */
public interface AuthHelper {
    @POST("/api/v1/access_token")
    Call<AuthResponseBody> auth(@Body RequestParams body);

    @POST("/api/v1/access_token")
    Call<AuthResponseBody> auth(@Header("Authorization") String credentials, @Body RequestParams body);

    @FormUrlEncoded
    @POST("/api/v1/access_token")
    Call<AuthResponseBody> auth(@Header("Authorization")String credentials, @Field("grant_type") String grantType,
                                @Field("device_id") String deviceId, @Field("redirect_uri") String redirectUri);

    @FormUrlEncoded
    @POST("/api/v1/access_token")
    Call<AuthResponseBody> auth(@Field("grant_type") String grantType,
                                @Field("device_id") String deviceId);
}


