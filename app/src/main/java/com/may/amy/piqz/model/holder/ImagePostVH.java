package com.may.amy.piqz.model.holder;

import android.support.v7.widget.RecyclerView;

import com.may.amy.piqz.databinding.PostImageBinding;
import com.may.amy.piqz.model.NewsItem;
import com.may.amy.piqz.viewmodel.PostItemViewModel;

/**
 * Created by kuhnertj on 27.04.2016.
 */
public class ImagePostVH extends RecyclerView.ViewHolder {

    private final PostImageBinding mBinding;

    public ImagePostVH(final PostImageBinding binding) {
        super(binding.getRoot());
        mBinding = binding;
    }

    public void displayPost(final NewsItem post) {
        if (mBinding.getViewModel() == null) {
            mBinding.setViewModel(new PostItemViewModel());
        }
        mBinding.getViewModel().getPost().set(post);
    }
}
