package com.may.amy.piqz.viewmodel;

import android.databinding.BindingAdapter;
import android.databinding.ObservableArrayList;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableInt;
import android.databinding.ObservableList;
import android.os.AsyncTask;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.may.amy.piqz.model.DataResponse;
import com.may.amy.piqz.model.NewsManager;
import com.may.amy.piqz.model.NewsItem;
import com.may.amy.piqz.view.adapter.PostAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kuhnertj on 15.04.2016.
 */
public class PostListViewModel {
    private final NewsManager mNewsManager;
    private final String token;
    private String after;

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
    }

    public PostListViewModel(NewsManager mNewsManager) {
        this.mNewsManager = mNewsManager;
        this.token = "";
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
                DataResponse dataResponse = mNewsManager.getNews(token, after, "10");
                List<NewsItem> posts = dataResponse.getChildren();
                after = dataResponse.getAfter();

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
            }
        });
    }
}
