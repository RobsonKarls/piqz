package com.may.amy.piqz.model.holder;

import android.support.v7.widget.RecyclerView;

import com.may.amy.piqz.databinding.PostTextBinding;
import com.may.amy.piqz.model.NewsItem;
import com.may.amy.piqz.viewmodel.PostItemViewModel;

/**
 * Created by kuhnertj on 27.04.2016.
 */
public class TextPostVH extends BaseVH {

    private final PostTextBinding mBinding;

    public TextPostVH(final PostTextBinding binding) {
        super(binding);
        mBinding = binding;
    }

    public void displayPost(final NewsItem post) {
        if (mBinding.getViewModel() == null) {
            mBinding.setViewModel(new PostItemViewModel());
        }
        mBinding.getViewModel().getPost().set(post);
    }
}
