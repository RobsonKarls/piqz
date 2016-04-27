package com.may.amy.piqz.viewmodel;

import android.content.Context;
import android.databinding.ObservableField;
import android.view.View;

import com.may.amy.piqz.model.NewsItem;


public class PostItemViewModel {

    private final ObservableField<NewsItem> mPost = new ObservableField<>();

    public ObservableField<NewsItem> getPost() {
        return mPost;
    }

    public void showPostDetails(final View view) {
        final Context context = view.getContext();
        //context.startActivity(BeerDetailsActivity.getIntent(context, mPost.get().getId()));
    }


}
