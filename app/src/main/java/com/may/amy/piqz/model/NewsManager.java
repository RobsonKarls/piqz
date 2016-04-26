package com.may.amy.piqz.model;

import android.databinding.DataBindingUtil;
import android.util.Log;

import com.may.amy.piqz.model.rest.RestApi;

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

    public NewsManager() {
        api = new RestApi();
    }

    public DataResponse getNews(String token, String after, String limit) {
        DataResponse dataResponse = new DataResponse();
        if (after == null) after = "";

        callback = new Callback<NewsResponse>() {
            @Override
            public void onResponse(Call<NewsResponse> call, Response<NewsResponse> response) {
                if (response.isSuccessful()) {
                    DataResponse dataResponse = new DataResponse();
                    ArrayList<NewsItem> news = new ArrayList<>();
                    for (NewsItem item : response.body().data.getChildren()) {
                        if (item.getAuthor() != null && !item.getAuthor().equals("funny_mod")) {
                            news.add(new NewsItem(item.getAuthor(), item.getTitle(),
                                    item.getNumComments(), item.getCreated(), item.getThumbnail(), item.getUrl()));
                            Log.d(NewsManager.class.getSimpleName(), "Title: " + item.getTitle());
                        }
                        if (item.getAuthor() == null) {
                            Log.e(NewsManager.class.getSimpleName(), "Response items: getAuthor() returns is null");
                            break;
                        }
                    }
                    dataResponse.setChildren(news);
                    dataResponse.setAfter(response.body().data.getAfter());
                } else {
                    Log.e("NewsManager.java", "Response was not successful");
                }
            }

            @Override
            public void onFailure(Call<NewsResponse> call, Throwable t) {
                Log.e("NewsManager.java", t.getMessage());
            }
        };
       // api.getNews(token, after, limit, callback);
         api.getNews(after, limit, callback);

        return dataResponse;
    }

}
   /*// callback = api.getNews(after, limit);
        try {
            response = callback.execute();
            if (response.isSuccessful()) {
                DataResponse dataResponse = new DataResponse();
                ArrayList<NewsItem> news = new ArrayList<>();
                for (NewsItem item : response.body().data.getChildren()) {
                    if (item.getAuthor() != null &&!item.getAuthor().equals("funny_mod")) {
                        news.add(new NewsItem(item.getAuthor(), item.getTitle(),
                                item.getNumComments(), item.getCreated(), item.getThumbnail(), item.getUrl()));
                        Log.d(NewsManager.class.getSimpleName(), "Title: " + item.getTitle());
                    }
                    if (item.getAuthor() == null){
                        Log.e(NewsManager.class.getSimpleName(), "Response items: getAuthor() returns is null" );
                        break;
                    }
                }
                dataResponse.setChildren(news);
                dataResponse.setAfter(response.body().data.getAfter());
                return dataResponse;
            } else {
                Log.e("NewsManager.java", "Response was not successful");
                return new DataResponse();
            }
        } catch (IOException e) {
            e.printStackTrace();
            return new DataResponse();
        }*/