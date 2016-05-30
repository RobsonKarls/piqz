package com.may.amy.piqz.manager;

import android.util.Log;

import com.may.amy.piqz.model.ChildrenResponse;
import com.may.amy.piqz.model.DataReceivedInterface;
import com.may.amy.piqz.model.NewsItem;
import com.may.amy.piqz.model.NewsResponse;
import com.may.amy.piqz.model.RResponse;
import com.may.amy.piqz.model.rest.RestApi;
import com.may.amy.piqz.util.AppUtil;
import com.may.amy.piqz.util.KaC;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * a manager class which sends a request to the api and checks the response
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

    public void getMulti(String token, String after, String limit) {
        if (token.equals("666")) {
            AppUtil.getInstance().getOAuthApi().refreshTokenIfExpired();
            token = "bearer " + AppUtil.getInstance().getAppPreferences().getString(KaC.KEY_TOKEN, "");
        }
        api = new RestApi(token);
        Call<NewsResponse> call = api.getMulti(after, limit);
        call.enqueue(callback);
    }

    public void getNews(String token, String subreddit, String after, final String limit) {
        if (after == null || after.isEmpty())
            after = AppUtil.getInstance().getAppPreferences().getString(KaC.KEY_AFTER, "");

        if (token.isEmpty())
            token = AppUtil.getInstance().getAppPreferences().getString(KaC.KEY_TOKEN_TYPE, "")
                    + " " + AppUtil.getInstance().getAppPreferences().getString(KaC.KEY_TOKEN, "");

        if (token.isEmpty()) {
            callResponse = apiNoAuth.getNews(subreddit, after, limit);

        } else {
            api = new RestApi(true, token);
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
                    //Add ad every ten items - TODO: // FIXME: 30.05.2016 
                    if (news.size() % 10 == 0 && news.size() != 0) {
                        NewsItem adItem = new NewsItem();
                        adItem.setPostType(KaC.TYPE_AD);
                        adItem.setTitle("Advertising");
                        news.add(adItem);
                    }
                    if (item.getAuthor() != null && !item.isNsfw()) {
                        item = formatItem(item);
                        Log.d(TAG, "Title: " + item.getTitle() +
                                "\nURL: " + item.getUrl() +
                                "\nPost type: " + item.getPostType() +
                                "\nPost Hint: " + (item.getPostHint() != null ? item.getPostHint() : "null"));
                        news.add(item);
                    }
                    if (item.getAuthor() == null) {
                        Log.e(TAG, "Response items: getAuthor() returns is null");
                        break;
                    }
                }
                RResponse rResponse = new RResponse();
                rResponse.setChildren(news);
                rResponse.setAfter(response.body().getData().getAfter());
                rResponse.setBefore(response.body().getData().getBefore());
                AppUtil.getInstance().getAppPreferences().edit()
                        .putString(KaC.KEY_AFTER, response.body().getData().getAfter())
                        .putString(KaC.KEY_BEFORE, response.body().getData().getBefore()).commit();

                dataReceivedInterface.updateData(rResponse);

            } else {
                Log.e(TAG, "Response not successful:\n" +
                        "Error code: " + response.code());
                onError(call, response);
            }
        }

        @Override
        public void onFailure(Call<NewsResponse> call, Throwable t) {
            Log.e(NewsManager.class.getSimpleName(), "Response failed: " + t.getMessage());
            RResponse errorResponse = new RResponse();
            errorResponse.setError(t.getMessage());
            dataReceivedInterface.updateData(errorResponse);
        }
    };

    private void onError(Call<NewsResponse> call, Response<NewsResponse> response) {
        switch (response.code()) {
            case 401:
            case 403:
                AppUtil.getInstance().getOAuthApi().refreshTokenIfExpired();
                if (AppUtil.getInstance().getRefreshTryCount() < 3) {
                    AppUtil.getInstance().updateRefreshTryCount();
                    getMulti("666", AppUtil.getInstance().getAppPreferences().getString(KaC.KEY_AFTER, ""), "10");
                } else {
                    RResponse errorResponse = new RResponse();
                    AppUtil.getInstance().setRefreshTryCount(0);
                    errorResponse.setError(String.valueOf(response.code()));
                    dataReceivedInterface.updateData(errorResponse);
                }

                break;
        }

    }

    /*
    * Format Url so Glide can handle images correctly. :)
    * */

    private NewsItem formatItem(NewsItem item) {
        item.setPostType(KaC.TYPE_GENERIC);
        // image is formatted
        if (item.getPostHint() != null && item.getPostHint().equals("image") && !item.getUrl().contains("/a/")) {
            item.setPostType(KaC.TYPE_IMAGE);
        }
        //This is a gallery. It needs extra handling, TODO: maybe a ViewHandler with Horizontal scrolling?
        else if (item.getPostHint() != null && (item.getPostHint().equals("link") || item.getPostHint().equals("rich:video"))
                || (item.getUrl().contains("imgur.com/gallery") || item.getUrl().contains("imgur.com/a/"))) {
            item.setPostType(KaC.TYPE_GALLERY);
        }
        //if link is an imgur link
        else if (item.getPostHint() != null && item.getPostHint().equals("link")
                && item.getUrl().contains("imgur") && !item.getUrl().contains(".gif")) {
            item.setUrl(item.getUrl() + ".png");
            item.setPostType(KaC.TYPE_IMAGE);
        }
        //Glide cannot handle .gifv data, change suffix:
        else if (item.getPostHint() != null && (item.getPostHint().equals("link")
                || item.getPostHint().equals("rich:video")) && item.getUrl().endsWith(".gifv")) {
            item.setUrl(item.getUrl().replace(".gifv", ".gif"));
            item.setPostType(KaC.TYPE_GIF);
        }
        //link to youtube
        else if (item.getPostHint() != null && item.getPostHint().equals("rich::video")
                && (item.getUrl().contains("youtu.be") || item.getUrl().contains("youtube.com"))) {
            item.setPostType(KaC.TYPE_YOUTUBE);
        }
        //link is self post
        else if (item.getDomain().contains("self")) {
            item.setPostType(KaC.TYPE_SELF);
        } else if (item.getPostHint() != null && item.getPostHint().equals("link")) {
            item.setPostType(KaC.TYPE_LINK);
        }

        //handle reddituploads url
        if (item.getUrl().contains("i.reddituploads")) {
            String url = item.getUrl();

        }
        return item;
    }


}
