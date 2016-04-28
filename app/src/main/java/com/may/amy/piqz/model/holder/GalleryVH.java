package com.may.amy.piqz.model.holder;

import android.databinding.ViewDataBinding;

import com.may.amy.piqz.databinding.PostGalleryBinding;
import com.may.amy.piqz.model.NewsItem;
import com.may.amy.piqz.viewmodel.PostItemViewModel;

/**
 * Created by kuhnertj on 28.04.2016.
 */
public class GalleryVH extends BaseVH{
    PostGalleryBinding mBinding;

    public GalleryVH(PostGalleryBinding mBinding) {
        super(mBinding);
        this.mBinding = mBinding;
    }

    @Override
    public void displayPost(NewsItem post) {
        if (mBinding.getViewModel() == null) {
            mBinding.setViewModel(new PostItemViewModel());
        }
        mBinding.getViewModel().getPost().set(post);
    }
}
