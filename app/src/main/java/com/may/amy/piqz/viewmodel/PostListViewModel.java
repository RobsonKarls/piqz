package com.may.amy.piqz.viewmodel;

import android.content.Context;
import android.content.Intent;
import android.databinding.BindingAdapter;
import android.databinding.ObservableArrayList;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableInt;
import android.databinding.ObservableList;
import android.graphics.drawable.AnimatedVectorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.graphics.drawable.AnimatedVectorDrawableCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.GlideBitmapDrawable;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.request.target.ViewTarget;
import com.may.amy.piqz.R;
import com.may.amy.piqz.manager.NewsManager;
import com.may.amy.piqz.model.NewsItem;
import com.may.amy.piqz.model.RResponse;
import com.may.amy.piqz.model.DataReceivedInterface;
import com.may.amy.piqz.view.activity.MainActivity;
import com.may.amy.piqz.view.adapter.PostAdapter;
import com.may.amy.piqz.view.fragment.DetailFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * ViewModel for a single post.
 */
public class PostListViewModel implements DataReceivedInterface {
    private final static String TAG = PostItemViewModel.class.getSimpleName();

    private final NewsManager mNewsManager;
    private final String token;
    private String after;
    private NotifyFragmentInterface notifyFragmentInterface;


    private final ObservableList<NewsItem> mPosts = new ObservableArrayList<>();
    private final ObservableInt mEmptyViewVisibility = new ObservableInt(View.VISIBLE);
    private final ObservableBoolean mSwipeRefreshLayoutRefreshing = new ObservableBoolean();


    @BindingAdapter({"imageUrl"})
    public static void loadImage(final ImageView imageView, final String imageUrl) {
        AnimatedVectorDrawableCompat avd = AnimatedVectorDrawableCompat.create(imageView.getContext(), R.drawable.avd_points_bounce);
        imageView.setImageDrawable(avd);
        if (imageView.getDrawable() instanceof AnimatedVectorDrawableCompat) {
            ((AnimatedVectorDrawableCompat) imageView.getDrawable()).start();
        }
        if (!imageUrl.endsWith("gif")) {
            ViewTarget<ImageView, GlideDrawable> viewTarget = new ViewTarget<ImageView, GlideDrawable>(imageView) {
                @Override
                public void onResourceReady(GlideDrawable resource, GlideAnimation<? super GlideDrawable> glideAnimation) {
                    this.view.setImageDrawable(resource.getCurrent());
                    if (resource.getCurrent() instanceof AnimatedVectorDrawableCompat) {
                        ((AnimatedVectorDrawableCompat) resource.getCurrent()).start();
                    }
                }
            };
            Glide.with(imageView.getContext())
                    .load(imageUrl)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .crossFade()
                    .placeholder(avd)
                    //  .error(R.drawable.ic_sync_problem_black_48dp)
                    .into(viewTarget);

        } else {
          //  Glide.with(imageView.getContext())
          //          .load(imageUrl).asGif()
          //          .diskCacheStrategy(DiskCacheStrategy.RESULT)
          //          .placeholder(R.drawable.ic_sync_black_48dp)
          //          .error(R.drawable.ic_sync_problem_black_48dp)
          //          .into(imageView);
        }
    }

    @BindingAdapter({"items"})
    public static void loadItems(final RecyclerView recyclerView, final List<NewsItem> posts) {
        if (recyclerView.getAdapter() == null) {
            recyclerView.setAdapter(new PostAdapter((ObservableArrayList<NewsItem>) posts));
        } else {
            recyclerView.getAdapter().notifyDataSetChanged();
        }
    }

    @BindingAdapter({"refreshing"})
    public static void setRefreshing(final SwipeRefreshLayout swipeRefreshLayout, final boolean refreshing) {
        swipeRefreshLayout.setRefreshing(refreshing);
    }

    @BindingAdapter({"htmlText"})
    public static void convertAndSetText(TextView textView, NewsItem post) {
        try {
            textView.setText(Html.fromHtml(post.getHtmlSelftext()).toString());
        } catch (Exception e) {
            Log.d("BindingAdapterHtmlText", "html Text was null");
            textView.setVisibility(View.GONE);
        }
    }

    @BindingAdapter({"showDetails"})
    public static void showPostDetails(final LinearLayout layout, final PostItemViewModel itemViewModel) {
        Log.d(TAG, "Context: " + layout.getContext().getClass().getSimpleName());
        layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Context context = layout.getContext();
                MainActivity activity = ((MainActivity) context);
                if (itemViewModel.getPost().get().getPostType() == 1) { //image
                    DetailFragment frag = new DetailFragment();
                    frag.setNewsItem(itemViewModel);
                    activity.addFragment(frag);
                } else {
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(itemViewModel.getPost().get().getUrl()));
                    context.startActivity(browserIntent);
                }

            }
        });
    }

    @BindingAdapter({"visible"})
    public static void showDivider(View view, PostItemViewModel viewModel) {
        if (viewModel.getPost().get().getHtmlSelftext() == null || viewModel.getPost().get().getHtmlSelftext().isEmpty()) {
            view.setVisibility(View.GONE);
        } else {
            view.setVisibility(View.VISIBLE);
        }
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

    /*
        *  CONSTRUCTORS:
        * */
    public PostListViewModel(boolean auth, NotifyFragmentInterface notifyFragmentInterface) {
        this(auth, "", notifyFragmentInterface);
    }

    public PostListViewModel(boolean auth, String token, NotifyFragmentInterface notifyFragmentInterface) {
        mNewsManager = new NewsManager(this, auth, token);
        this.token = token;
        this.notifyFragmentInterface = notifyFragmentInterface;

    }

    private void loadNewsFeed(final boolean refreshTop) {
        AsyncTask.THREAD_POOL_EXECUTOR.execute(new Runnable() {
            @Override
            public void run() {
                String limit = "10";
                mNewsManager.getMulti(token, refreshTop ? "" : after, limit);

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

        
        if (posts.isEmpty()) {
            // mPosts.clear();
            // mEmptyViewVisibility.set(View.VISIBLE);
            String error = rResponse.getError() != null ? rResponse.getError() : "Pull to refresh";
            notifyFragmentInterface.updated(error);
            if (mPosts.isEmpty()){
                mEmptyViewVisibility.set(View.VISIBLE);
            }
        } else {
            mEmptyViewVisibility.set(View.GONE);
            mPosts.addAll(posts);
            notifyFragmentInterface.updated();
        }
    }

    public interface NotifyFragmentInterface {
        void updated();

        void updated(String error);
    }
}
