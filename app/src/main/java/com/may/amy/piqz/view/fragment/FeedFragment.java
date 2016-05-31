package com.may.amy.piqz.view.fragment;


import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.graphics.drawable.AnimatedVectorDrawableCompat;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.may.amy.piqz.R;
import com.may.amy.piqz.databinding.FeedFragmentBinding;
import com.may.amy.piqz.util.AppUtil;
import com.may.amy.piqz.util.InfinteScrollListener;
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

        ((AppCompatActivity) getActivity()).setSupportActionBar(binding.toolbar);
        binding.toolbar.setTitle(R.string.app_name);
        ImageView loadImage = new ImageView(getActivity());
        loadImage.setImageDrawable(AnimatedVectorDrawableCompat.create(getActivity(), R.drawable.avd_load));

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
        Snackbar.make(binding.getRoot(), R.string.snackbar_more, Snackbar.LENGTH_INDEFINITE)
                .show();
    }

    @Override
    public void updated() {
        Snackbar.make(binding.getRoot(), R.string.snackbar_ready, Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public void updated(String error) {
        binding.tvEmpty.setText(R.string.tv_empty_pull_refresh);
        Snackbar.make(binding.getRoot(), getActivity().getString(R.string.snackbar_error) + error,
                Snackbar.LENGTH_SHORT).show();
    }
}
