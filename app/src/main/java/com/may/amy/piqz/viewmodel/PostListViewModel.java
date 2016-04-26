package com.may.amy.piqz.viewmodel;

import android.databinding.BindingAdapter;
import android.databinding.ObservableArrayList;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableInt;
import android.databinding.ObservableList;
import android.os.AsyncTask;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.may.amy.piqz.model.ChildrenResponse;
import com.may.amy.piqz.model.DataResponse;
import com.may.amy.piqz.model.NewsManager;
import com.may.amy.piqz.model.NewsItem;
import com.may.amy.piqz.model.NewsResponse;
import com.may.amy.piqz.model.RResponse;
import com.may.amy.piqz.view.adapter.PostAdapter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by kuhnertj on 15.04.2016.
 */
public class PostListViewModel {
    private final NewsManager mNewsManager;
    private final String token;
    private Callback<NewsResponse> callback;
    private String after;
    private RResponse rResponse;

    private final ObservableList<NewsItem> mPosts = new ObservableArrayList<>();
    private final ObservableInt mEmptyViewVisibility = new ObservableInt(View.GONE);
    private final ObservableBoolean mSwipeRefreshLayoutRefreshing = new ObservableBoolean();

    @BindingAdapter({"imageUrl"})
    public static void loadImage(final ImageView imageView, final String imageUrl) {
        if (!imageUrl.contains("gif")) {
            String url = imageUrl;
            if (!imageUrl.contains(".jpg") || !imageUrl.contains(".png") || !imageUrl.contains("i.reddituploads.com")) {
                url = imageUrl + ".jpg";
            }
            Glide.with(imageView.getContext()).load(url)
                    .placeholder(android.R.drawable.ic_menu_help)
                    .error(android.R.drawable.ic_menu_close_clear_cancel)
                    .into(imageView);

        } else {
            Glide.with(imageView.getContext())
                    .load(imageUrl).asGif()
                    .placeholder(android.R.drawable.ic_menu_help)
                    .error(android.R.drawable.ic_menu_close_clear_cancel).into(imageView);
        }
    }

    @BindingAdapter({"items"})
    public static void loadItems(final RecyclerView recyclerView, final List<NewsItem> posts) {
        recyclerView.setAdapter(new PostAdapter(posts));
    }

    @BindingAdapter({"refreshing"})
    public static void setRefreshing(final SwipeRefreshLayout swipeRefreshLayout, final boolean refreshing) {
        swipeRefreshLayout.setRefreshing(refreshing);
    }

    public PostListViewModel(final NewsManager newsManager, String token) {
        mNewsManager = newsManager;
        this.token = token;
        rResponse = new RResponse();

        //TODO: Das hat nichts im viewmodel zu suchen..
        callback = new Callback<NewsResponse>() {
            @Override
            public void onResponse(Call<NewsResponse> call, Response<NewsResponse> response) {
                if (response.isSuccessful()) {
                    ArrayList<NewsItem> news = new ArrayList<>();
                    for (ChildrenResponse childrenResponse : response.body().getData().getChildren()) {
                        NewsItem item = childrenResponse.getData();
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

                    rResponse.setChildren(news);
                    rResponse.setAfter(response.body().getData().getAfter());
                    rResponse.setBefore(response.body().getData().getBefore());

                    List<NewsItem> posts = new ArrayList<>();

                    if (rResponse.getChildren() != null) {
                        posts = rResponse.getChildren();
                    }

                    after = rResponse.getAfter();

                    mSwipeRefreshLayoutRefreshing.set(false);
                    mSwipeRefreshLayoutRefreshing.notifyChange();

                    if (posts == null) {
                        posts = new ArrayList<>();
                    }
                    if (posts.isEmpty()) {
                        mPosts.clear();
                        mEmptyViewVisibility.set(View.VISIBLE);
                    } else {
                        mEmptyViewVisibility.set(View.GONE);
                        mPosts.addAll(posts);
                    }


                } else {
                    Log.e(NewsManager.class.getSimpleName(), "Response not successful.");
                }
            }

            @Override
            public void onFailure(Call<NewsResponse> call, Throwable t) {
                Log.e(NewsManager.class.getSimpleName(), "Response failed: " + t.getMessage());
            }
        };


    }

    public PostListViewModel(NewsManager mNewsManager) {
        this(mNewsManager, "");
    }

    public ObservableBoolean getSwipeRefreshLayoutRefreshing() {
        return mSwipeRefreshLayoutRefreshing;
    }

    public ObservableInt getEmptyViewVisibility() {
        return mEmptyViewVisibility;
    }

    public ObservableList<NewsItem> getPosts() {
        return mPosts;
    }

    public void onFirstRefresh() {
        mSwipeRefreshLayoutRefreshing.set(true);
        loadNewsFeed();
    }

    public void onRefresh() {
        loadNewsFeed();
    }

    private void loadNewsFeed() {
        AsyncTask.THREAD_POOL_EXECUTOR.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    mNewsManager.getNews(token, after, "10", callback);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
