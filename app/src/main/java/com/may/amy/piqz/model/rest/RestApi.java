package com.may.amy.piqz.model.rest;

import com.may.amy.piqz.model.NewsResponse;

import java.io.IOException;

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
public class RestApi {

    private final RestHelper mRestHelper;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    private String token = "";


    public RestApi() {
        this(false);
    }

    public RestApi(boolean auth) {
        final Retrofit retrofit;
        if (auth) {
            retrofit = new Retrofit.Builder()
                    .baseUrl("https://oauth.reddit.com")
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(getClient())
                    .build();
        } else {
            retrofit = new Retrofit.Builder()
                    .baseUrl("http://www.reddit.com")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        mRestHelper = retrofit.create(RestHelper.class);
    }

    private OkHttpClient getClient() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder()
                .addInterceptor(new Interceptor() {
                    @Override
                    public Response intercept(Chain chain) throws IOException {
                        Request request = chain.request().newBuilder()
                                .addHeader("Accept", "application/json")
                                .addHeader("Authorization", token)
                                .build();

                        return chain.proceed(request);
                    }
                });

        return builder.build();

    }

    public void getNews(String token, String after, String limit, Callback<NewsResponse> handler) {
        Call<NewsResponse> call = mRestHelper.listHot(token, "application/json", after, limit);
        call.enqueue(handler);
    }

    public Call<NewsResponse> getNews(String after, String limit, Callback<NewsResponse> handler) {
        return mRestHelper.listHot(after, limit);
    }
    public Call<NewsResponse> getNews() {
        return mRestHelper.listHot();
    }
}
