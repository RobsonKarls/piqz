package com.may.amy.piqz.viewmodel;

import android.content.Context;
import android.databinding.BindingAdapter;
import android.databinding.ObservableField;
import android.util.Log;
import android.view.View;

import com.may.amy.piqz.model.NewsItem;

import butterknife.Bind;
import retrofit2.http.POST;


public class PostItemViewModel {
    private static final String TAG = PostItemViewModel.class.getSimpleName();

    private final ObservableField<NewsItem> mPost = new ObservableField<>();
    public ObservableField<NewsItem> getPost() {
        return mPost;
    }

    private ShowDetailsInterface showDetailsInterface;

    public PostItemViewModel(ShowDetailsInterface showDetailsInterface) {
        this.showDetailsInterface = showDetailsInterface;
    }

    public PostItemViewModel() {
    }



    public interface ShowDetailsInterface{
        void showDetails(View view, NewsItem item);
    }

}
