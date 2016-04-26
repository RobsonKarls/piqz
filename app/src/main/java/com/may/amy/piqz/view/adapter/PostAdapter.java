package com.may.amy.piqz.view.adapter;

import android.databinding.ObservableArrayList;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.may.amy.piqz.databinding.PostItemBinding;
import com.may.amy.piqz.model.NewsItem;
import com.may.amy.piqz.viewmodel.PostItemViewModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kuhnertj on 15.04.2016.
 */
public class PostAdapter extends RecyclerView.Adapter<PostAdapter.PostItemViewHolder> {
    private ObservableArrayList<NewsItem> mPosts;

    public PostAdapter(ObservableArrayList<NewsItem> posts) {
        mPosts = posts;
    }


    @Override
    public PostItemViewHolder onCreateViewHolder(final ViewGroup parent, final int viewType) {
        final PostItemBinding binding = PostItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new PostItemViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(final PostItemViewHolder holder, final int position) {
        holder.displayBeer(mPosts.get(position));
    }

    @Override
    public int getItemCount() {
        return mPosts.size();
    }

    public static class PostItemViewHolder extends RecyclerView.ViewHolder {

        private final PostItemBinding mBinding;

        public PostItemViewHolder(final PostItemBinding binding) {
            super(binding.getRoot());
            mBinding = binding;
        }

        public void displayBeer(final NewsItem post) {
            if (mBinding.getViewModel() == null) {
                mBinding.setViewModel(new PostItemViewModel());
            }
            mBinding.getViewModel().getPost().set(post);
        }
    }
}
