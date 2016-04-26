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
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.may.amy.piqz.model.ChildrenResponse;
import com.may.amy.piqz.model.DataResponse;
import com.may.amy.piqz.model.NewsManager;
import com.may.amy.piqz.model.NewsItem;
import com.may.amy.piqz.model.NewsResponse;
import com.may.amy.piqz.model.RResponse;
import com.may.amy.piqz.model.rest.DataReceivedInterface;
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
public class PostListViewModel implements DataReceivedInterface {
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
                    .listener(new RequestListener<String, GlideDrawable>() {
                        @Override
                        public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                            e.printStackTrace();
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                            return false;
                        }
                    })
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
        this(mNewsManager, "");
    }
    public PostListViewModel(String token) {
        mNewsManager = new NewsManager(this);
        this.token = token;
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
                    mNewsManager.getNews(token, "funny", after, "10");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public void updateData(RResponse rResponse) {
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


    }
}
