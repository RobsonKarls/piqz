package com.may.amy.piqz.model.holder;

import android.graphics.Matrix;
import android.graphics.PointF;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;

import com.may.amy.piqz.databinding.PostImageBinding;
import com.may.amy.piqz.model.NewsItem;
import com.may.amy.piqz.viewmodel.PostItemViewModel;

/**
 * Created by kuhnertj on 27.04.2016.
 */
public class ImagePostVH extends BaseVH{

    private final PostImageBinding mBinding;

    public ImagePostVH(final PostImageBinding binding) {
        super(binding);
        mBinding = binding;
    }

    @Override
    public void displayPost(final NewsItem post) {
        if (mBinding.getViewModel() == null) {
            mBinding.setViewModel(new PostItemViewModel());
        }
        mBinding.getViewModel().getPost().set(post);
    }
}
