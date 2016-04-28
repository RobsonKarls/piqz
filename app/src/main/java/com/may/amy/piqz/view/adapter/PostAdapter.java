package com.may.amy.piqz.view.adapter;

import android.databinding.ObservableArrayList;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.may.amy.piqz.databinding.PostImageBinding;
import com.may.amy.piqz.databinding.PostTextBinding;
import com.may.amy.piqz.model.NewsItem;
import com.may.amy.piqz.model.holder.BaseVH;
import com.may.amy.piqz.model.holder.ImagePostVH;
import com.may.amy.piqz.model.holder.TextPostVH;
import com.may.amy.piqz.util.KaC;

/**
 * Created by kuhnertj on 15.04.2016.
 */
public class PostAdapter extends RecyclerView.Adapter<BaseVH> {
    private ObservableArrayList<NewsItem> mPosts;


    public PostAdapter(ObservableArrayList<NewsItem> posts) {
        mPosts = posts;
    }


    @Override
    public BaseVH onCreateViewHolder(final ViewGroup parent, final int viewType) {
        switch (viewType) {
            case KaC.TYPE_IMAGE:
                return new ImagePostVH(PostImageBinding
                        .inflate(LayoutInflater.from(parent.getContext()), parent, false));
            case KaC.TYPE_SELF:
                return new TextPostVH(PostTextBinding
                        .inflate(LayoutInflater.from(parent.getContext()), parent, false));

            case KaC.TYPE_GENERIC:
            default:
                return new TextPostVH(PostTextBinding
                        .inflate(LayoutInflater.from(parent.getContext()), parent, false));

        }
    }

    @Override
    public void onBindViewHolder(final BaseVH holder, final int position) {
        holder.displayPost(mPosts.get(position));
    }

    @Override
    public int getItemCount() {
        return mPosts.size();
    }

    @Override
    public int getItemViewType(int position) {
        return mPosts.get(position).getPostType();

    }
}
