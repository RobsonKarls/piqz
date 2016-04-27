package com.may.amy.piqz.model;

import android.util.Log;

import com.may.amy.piqz.model.rest.OAuthApi;
import com.may.amy.piqz.model.rest.RestApi;
import com.may.amy.piqz.util.AppUtil;

import java.io.IOException;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by kuhnertj on 15.04.2016.
 */
public class NewsManager {

    private static final String TAG = NewsManager.class.getSimpleName();
    private RestApi api;
    private RestApi apiNoAuth;
    private DataReceivedInterface dataReceivedInterface;
    private Call<NewsResponse> callResponse;

    public NewsManager(DataReceivedInterface dataReceivedInterface) {
        api = new RestApi(true, "");
        this.dataReceivedInterface = dataReceivedInterface;
    }


    public NewsManager(DataReceivedInterface dataReceivedInterface, boolean auth, String token) {
        api = new RestApi(auth, token);
        apiNoAuth = new RestApi();

        this.dataReceivedInterface = dataReceivedInterface;
    }

    public void getNews(String token, String subreddit, String after, final String limit) {
        if (after == null || after.isEmpty())
            after = AppUtil.getInstance().getAppPreferences().getString(AppUtil.KEY_AFTER, "");

        token = AppUtil.getInstance().getAppPreferences().getString(AppUtil.KEY_TOKEN_TYPE, "")
                + " " + AppUtil.getInstance().getAppPreferences().getString(AppUtil.KEY_TOKEN, "");

        if (token.isEmpty()) {
            callResponse = apiNoAuth.getNews(subreddit, after, limit);

        } else {
            callResponse = api.getNews(token, subreddit, after, limit);
        }
        callResponse.enqueue(callback);
    }

    private void getNews() {
        getNews("", "", "", "10");
    }

    Callback<NewsResponse> callback = new Callback<NewsResponse>() {
        @Override
        public void onResponse(Call<NewsResponse> call, Response<NewsResponse> response) {
            if (response.isSuccessful()) {
                ArrayList<NewsItem> news = new ArrayList<>();
                for (ChildrenResponse childrenResponse : response.body().getData().getChildren()) {
                    NewsItem item = childrenResponse.getData();
                    if (isItemCorrect(item)) {
                        news.add(item);
                    }
                    if (item.getAuthor() == null) {
                        Log.e(NewsManager.class.getSimpleName(), "Response items: getAuthor() returns is null");
                        break;
                    }
                }
                RResponse rResponse = new RResponse();
                rResponse.setChildren(news);
                rResponse.setAfter(response.body().getData().getAfter());
                rResponse.setBefore(response.body().getData().getBefore());
                AppUtil.getInstance().getAppPreferences().edit()
                        .putString(AppUtil.KEY_AFTER, response.body().getData().getAfter())
                        .putString(AppUtil.KEY_BEFORE, response.body().getData().getBefore()).commit();
                dataReceivedInterface.updateData(rResponse);

            } else {
                Log.e(TAG, "Response not successful:\n" +
                        (response.errorBody() != null ? response.errorBody().toString() : "Errorbody is null") +
                        "\nError code: " + response.code());

                onError(call, response);
            }
        }

        @Override
        public void onFailure(Call<NewsResponse> call, Throwable t) {
            Log.e(NewsManager.class.getSimpleName(), "Response failed: " + t.getMessage());
        }
    };

    private void onError(Call<NewsResponse> call, Response<NewsResponse> response) {
        switch (response.code()) {
            case 403:
                AppUtil.getInstance().getOAuthApi().refreshTokenIfExpired();
                getNews();
                break;
        }

    }

    private boolean isItemCorrect(NewsItem item) {
        if (item.getAuthor() != null && !item.getAuthor().equals("funny_mode")) {
            if (item.getUrl().endsWith("imgur.com") && !item.getUrl().endsWith("gif"))
                item.setUrl(item.getUrl() + ".jpg");
            if (item.getUrl().endsWith("gifv"))
                item.setUrl(item.getUrl().replace("gifv", "gif"));

            if (item.getUrl().endsWith("jpg")
                    || item.getUrl().endsWith("png") ||
                    item.getUrl().endsWith("gif"))
                return true;
        }
        return false;
    }


    public void getMulti() {
        Call<NewsResponse> call = api.getMultis();
        call.enqueue(callback);
    }
}
