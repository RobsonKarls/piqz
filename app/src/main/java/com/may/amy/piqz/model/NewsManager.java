package com.may.amy.piqz.model;

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
    private DataReceivedInterface dataReceivedInterface;

    public NewsManager(DataReceivedInterface dataReceivedInterface) {
        api = new RestApi(true, "");
        this.dataReceivedInterface = dataReceivedInterface;
    }


    public NewsManager(DataReceivedInterface dataReceivedInterface, boolean auth, String token) {
        api = new RestApi(auth, token);
        this.dataReceivedInterface = dataReceivedInterface;
    }

    public void getNews(String token, String subreddit, final String after, String limit) throws IOException {
        Call<NewsResponse> callResponse;
        if (token.isEmpty()) {
            callResponse = api.getNews(subreddit, after, limit);
        } else {
            callResponse = api.getNews(token, subreddit, after, limit);
        }

        Callback<NewsResponse> callback = new Callback<NewsResponse>() {
            @Override
            public void onResponse(Call<NewsResponse> call, Response<NewsResponse> response) {
                if (response.isSuccessful()) {
                    ArrayList<NewsItem> news = new ArrayList<>();
                    for (ChildrenResponse childrenResponse : response.body().getData().getChildren()) {
                        NewsItem item = childrenResponse.getData();
                        if (isItemCorrect(item)) {
                            news.add(new NewsItem(item.getAuthor(), item.getTitle(),
                                    item.getNumComments(), item.getCreated(), item.getThumbnail(), item.getUrl()));
                            Log.d(NewsManager.class.getSimpleName(), "Title: " + item.getTitle());
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
                    dataReceivedInterface.updateData(rResponse);

                } else {
                    Log.e(NewsManager.class.getSimpleName(), "Response not successful:\n" +
                            (response.errorBody() != null ? response.errorBody().toString() : "Errorbody is null")+
                            "\nError code: " + response.code());

                }
            }

            @Override
            public void onFailure(Call<NewsResponse> call, Throwable t) {
                Log.e(NewsManager.class.getSimpleName(), "Response failed: " + t.getMessage());
            }
        };

        callResponse.enqueue(callback);
    }

    private boolean isItemCorrect(NewsItem item) {
        return item.getAuthor() != null && !item.getAuthor().equals("funny_mod") &&
                (item.getUrl().contains("jpg") || item.getUrl().contains("png")
                        || item.getUrl().contains("gif"));
    }
}
