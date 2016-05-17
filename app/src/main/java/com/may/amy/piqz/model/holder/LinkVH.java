package com.may.amy.piqz.model.holder;

import com.may.amy.piqz.databinding.PostLinkBinding;
import com.may.amy.piqz.model.NewsItem;
import com.may.amy.piqz.viewmodel.PostItemViewModel;

/**
 * ViewHolder for Links
 */
public class LinkVH extends BaseVH{
    PostLinkBinding mBinding;

    public LinkVH(PostLinkBinding mBinding) {
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
