package com.may.amy.piqz.view.fragment;


import android.content.SharedPreferences;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.may.amy.piqz.R;
import com.may.amy.piqz.databinding.FeedFragmentBinding;
import com.may.amy.piqz.util.InfinteScrollListener;
import com.may.amy.piqz.util.AppUtil;
import com.may.amy.piqz.util.KaC;
import com.may.amy.piqz.viewmodel.PostListViewModel;

/**
 * A simple {@link Fragment} subclass.
 */
public class FeedFragment extends Fragment
        implements SwipeRefreshLayout.OnRefreshListener, InfinteScrollListener.LoadInterface, PostListViewModel.NotifyFragmentInterface {
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
        mViewModel = new PostListViewModel(true, this);
        binding.setViewModel(mViewModel);

        binding.swipeLayout.setColorSchemeResources(R.color.colorAccent);
        binding.swipeLayout.setOnRefreshListener(this);

        binding.recyclerView.setHasFixedSize(true);
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        binding.recyclerView.clearOnScrollListeners();
        binding.recyclerView.addOnScrollListener(new InfinteScrollListener((LinearLayoutManager) binding.recyclerView.getLayoutManager(), this));
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        loadItems();
    }

    @Override
    public void onRefresh() {
        AppUtil.getInstance().getAppPreferences().edit().remove(KaC.KEY_AFTER)
                .remove(KaC.KEY_BEFORE).commit();
        mViewModel.onRefresh(true);
    }

    @Override
    public void loadItems() {
        mViewModel.onRefresh(false);
        Snackbar.make(binding.getRoot(), "Loading more...", Snackbar.LENGTH_INDEFINITE)
                .show();
    }

    @Override
    public void updated() {
        Snackbar.make(binding.getRoot(), "Loaded", Snackbar.LENGTH_SHORT).show();
    }
}
