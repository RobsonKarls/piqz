package com.may.amy.piqz.view.adapter;

import android.databinding.ObservableArrayList;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.may.amy.piqz.databinding.PostImageBinding;
import com.may.amy.piqz.databinding.PostTextBinding;
import com.may.amy.piqz.model.NewsItem;
import com.may.amy.piqz.model.holder.ImagePostVH;
import com.may.amy.piqz.model.holder.TextPostVH;

/**
 * Created by kuhnertj on 15.04.2016.
 */
public class PostAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private ObservableArrayList<NewsItem> mPosts;

    private static final int TYPE_IMAGE = 0;
    private static final int TYPE_POST = 1;

    public PostAdapter(ObservableArrayList<NewsItem> posts) {
        mPosts = posts;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(final ViewGroup parent, final int viewType) {
        switch (viewType) {
            case TYPE_IMAGE:
                return new ImagePostVH(PostImageBinding
                        .inflate(LayoutInflater.from(parent.getContext()), parent, false));
            case TYPE_POST:
            default:
                return new TextPostVH(PostTextBinding
                        .inflate(LayoutInflater.from(parent.getContext()), parent, false));

        }
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        switch (holder.getItemViewType()) {
            case TYPE_IMAGE:
                ((ImagePostVH) holder).displayPost(mPosts.get(position));
                break;
            case TYPE_POST:
                break;
        }
    }

    @Override
    public int getItemCount() {
        return mPosts.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (mPosts.get(position).getDomain().equals("i.imgur.com")) return TYPE_IMAGE;
        if (mPosts.get(position).getDomain().contains("reddit.com"))return TYPE_POST;

        return super.getItemViewType(position);
    }
}
