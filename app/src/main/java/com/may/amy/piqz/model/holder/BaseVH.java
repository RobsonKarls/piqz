package com.may.amy.piqz.model.holder;

import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.databinding.tool.Binding;
import android.databinding.tool.DataBindingBuilder;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.may.amy.piqz.BR;
import com.may.amy.piqz.databinding.PostImageBinding;
import com.may.amy.piqz.model.NewsItem;
import com.may.amy.piqz.viewmodel.PostItemViewModel;

/**
 * Created by kuhnertj on 28.04.2016.
 */
public class BaseVH extends RecyclerView.ViewHolder {
    private ViewDataBinding mBinding;

    public BaseVH(ViewDataBinding mBinding) {
        super(mBinding.getRoot());
        this.mBinding = mBinding;

    }

    public void displayPost(final NewsItem post) {
        PostItemViewModel vm = new PostItemViewModel();
        vm.getPost().set(post);
        mBinding.setVariable(BR.viewModel, vm);
    }
}
