package com.may.amy.piqz.model;

import android.databinding.DataBindingUtil;
import android.util.Log;

import com.may.amy.piqz.model.rest.RestApi;

import java.io.IOException;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by kuhnertj on 15.04.2016.
 */
public class NewsManager {
    private RestApi api;
    private Callback<NewsResponse> callback;
    private Response<NewsResponse> response;

    private DataResponse dataResponse;

    public NewsManager() {
        api = new RestApi(false);
    }

    public void getNews(String token, String after, String limit, Callback<NewsResponse> callback) throws IOException {
      //  Call<NewsResponse> callResponse = api.getNews(after, limit, callback);
        Call<NewsResponse> callResponse = api.getNews();
        dataResponse = new DataResponse();
        callResponse.enqueue(callback);

    }
}
