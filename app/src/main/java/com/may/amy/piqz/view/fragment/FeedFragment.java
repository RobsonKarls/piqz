package com.may.amy.piqz.view.fragment;


import android.content.Context;
import android.content.SharedPreferences;
import android.databinding.DataBindingUtil;
import android.databinding.ObservableArrayList;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.may.amy.piqz.R;
import com.may.amy.piqz.databinding.FeedFragmentBinding;
import com.may.amy.piqz.model.InfinteScrollListener;
import com.may.amy.piqz.model.NewsItem;
import com.may.amy.piqz.model.NewsManager;
import com.may.amy.piqz.util.AppUtil;
import com.may.amy.piqz.view.adapter.PostAdapter;
import com.may.amy.piqz.viewmodel.PostListViewModel;

import java.util.ArrayList;
import java.util.Collections;

/**
 * A simple {@link Fragment} subclass.
 */
public class FeedFragment extends Fragment
        implements SwipeRefreshLayout.OnRefreshListener, InfinteScrollListener.LoadInterface {
    private FeedFragmentBinding binding;
    private PostListViewModel mViewModel;

    public FeedFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_feed, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        SharedPreferences pref = getActivity().getSharedPreferences("AppPref", Context.MODE_PRIVATE);
        String token = "bearer " + pref.getString("token", "");
        mViewModel = new PostListViewModel(true, token);
        binding.setViewModel(mViewModel);

        binding.swipeLayout.setColorSchemeResources(R.color.colorAccent);
        binding.swipeLayout.setOnRefreshListener(this);

        binding.recyclerView.setHasFixedSize(true);
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        binding.recyclerView.clearOnScrollListeners();
        binding.recyclerView.addOnScrollListener(new InfinteScrollListener((LinearLayoutManager) binding.recyclerView.getLayoutManager(), this));

        loadItems();
    }

    @Override
    public void onRefresh() {
        AppUtil.getInstance().getAppPreferences().edit().remove(AppUtil.KEY_AFTER)
                .remove(AppUtil.KEY_BEFORE).commit();
        mViewModel.onRefresh(true);
    }

    @Override
    public void loadItems() {
        mViewModel.onRefresh(false);
    }

    private void refreshAfter(){

    }
}
