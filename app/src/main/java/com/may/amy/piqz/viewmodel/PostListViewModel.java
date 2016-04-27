package com.may.amy.piqz.viewmodel;

import android.databinding.BindingAdapter;
import android.databinding.ObservableArrayList;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableInt;
import android.databinding.ObservableList;
import android.os.AsyncTask;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.may.amy.piqz.R;
import com.may.amy.piqz.manager.NewsManager;
import com.may.amy.piqz.model.NewsItem;
import com.may.amy.piqz.model.RResponse;
import com.may.amy.piqz.model.DataReceivedInterface;
import com.may.amy.piqz.view.adapter.PostAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * ViewModel for a single post.
 */
public class PostListViewModel implements DataReceivedInterface {
    private final NewsManager mNewsManager;
    private final String token;
    private String after;


    private final ObservableList<NewsItem> mPosts = new ObservableArrayList<>();
    private final ObservableInt mEmptyViewVisibility = new ObservableInt(View.GONE);
    private final ObservableBoolean mSwipeRefreshLayoutRefreshing = new ObservableBoolean();

    @BindingAdapter({"imageUrl"})
    public static void loadImage(final ImageView imageView, final String imageUrl) {
        if (!imageUrl.endsWith("gif")) {
            Glide.with(imageView.getContext()).load(imageUrl)
                    .diskCacheStrategy(DiskCacheStrategy.RESULT)
                    //TODO: Remove for release
                    .listener(new RequestListener<String, GlideDrawable>() {
                        @Override
                        public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                            if (e != null) e.printStackTrace();
                            else Log.e("GLIDE", "Exception was null");
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                            return false;
                        }
                    })
                    .placeholder(R.drawable.ic_sync_black_48dp)
                    .error(R.drawable.ic_sync_problem_black_48dp)
                    .into(imageView);

        } else {
            //TODO: Make this as Bitmap with watermark/overlay "play"
            Glide.with(imageView.getContext())
                    .load(imageUrl).asGif()
                    .diskCacheStrategy(DiskCacheStrategy.RESULT)
                    .placeholder(R.drawable.ic_sync_black_48dp)
                    .error(R.drawable.ic_sync_problem_black_48dp)
                    .into(imageView);
        }
    }

    @BindingAdapter({"items"})
    public static void loadItems(final RecyclerView recyclerView, final List<NewsItem> posts) {
        if (recyclerView.getAdapter() == null){
            recyclerView.setAdapter(new PostAdapter((ObservableArrayList<NewsItem>) posts));
        }else{
             recyclerView.getAdapter().notifyDataSetChanged();
        }
    }

    @BindingAdapter({"refreshing"})
    public static void setRefreshing(final SwipeRefreshLayout swipeRefreshLayout, final boolean refreshing) {
        swipeRefreshLayout.setRefreshing(refreshing);
    }

    @BindingAdapter({"htmlText"})
    public static void convertAndSetText(TextView textView, String htmlString){
        try {
            textView.setText(Html.fromHtml(htmlString).toString());
        } catch (Exception e) {
            e.printStackTrace();
            textView.setText("");
        }
    }

    /*
    *  CONSTRUCTORS:
    * */

    public PostListViewModel() {
        this(false, "");
    }

    public PostListViewModel(boolean auth) {
        this(auth, "");
    }
    public PostListViewModel(String token) {
        this(true, token);
    }

    public PostListViewModel(boolean auth, String token) {
        mNewsManager = new NewsManager(this, auth, token);
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

    public void onRefresh(boolean refreshTop) {
        loadNewsFeed(refreshTop);
    }

    private void loadNewsFeed(final boolean refreshTop) {
        AsyncTask.THREAD_POOL_EXECUTOR.execute(new Runnable() {
            @Override
            public void run() {
                    mNewsManager.getMulti();
                   /* if (refreshTop) {
                        mNewsManager.getNews(token, "funny", "", "10");
                    } else {
                        mNewsManager.getNews(token, "funny", after, "10");
                    }*/
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
