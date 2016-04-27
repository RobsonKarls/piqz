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

    public RestApi() {
        this(false, "");
    }

    public RestApi(boolean auth, String token) {
        Retrofit retrofit;
        if (auth) {
            retrofit = new Retrofit.Builder()
                    .baseUrl("https://oauth.reddit.com")
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(getClient(token))
                    .build();
        } else {
            retrofit = new Retrofit.Builder()
                    .baseUrl("http://www.reddit.com")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        mRestHelper = retrofit.create(RestHelper.class);
    }

    private OkHttpClient getClient(final String token) {
        OkHttpClient.Builder builder = new OkHttpClient.Builder()
                .addInterceptor(new Interceptor() {
                    @Override
                    public Response intercept(Chain chain) throws IOException {
                        Request request = chain.request().newBuilder()
                                .addHeader("Authorization", token)
                                .build();

                        return chain.proceed(request);
                    }
                });

        return builder.build();
    }

    public Call<NewsResponse> getNews(String token, String subreddit, String after, String limit) {
        return mRestHelper.listHot(subreddit, after, limit);
    }

    public Call<NewsResponse> getNews(String subreddit, String after, String limit) {
        return getNews("", subreddit, after, limit);
    }

    public Call<NewsResponse> getNews() {
        return getNews("funny", "","");
    }

    public Call<NewsResponse> getMultis(){
        return mRestHelper.getMultis();
    }
}
