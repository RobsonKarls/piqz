package com.may.amy.piqz.viewmodel;

import android.databinding.BindingAdapter;
import android.databinding.ObservableField;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.util.FloatMath;
import android.util.Log;
import android.util.StringBuilderPrinter;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;

import com.may.amy.piqz.model.NewsItem;


public class PostItemViewModel {
    private static final String TAG = PostItemViewModel.class.getSimpleName();

    private final ObservableField<NewsItem> mPost = new ObservableField<>();

    public ObservableField<NewsItem> getPost() {
        return mPost;
    }

    public PostItemViewModel() {

    }

    @BindingAdapter({"backClick"})
    public static void onBackClick(View iv, PostItemViewModel vm) {
        Log.d(TAG, "Context: " + iv.getContext().getClass().getSimpleName());
    }

}
