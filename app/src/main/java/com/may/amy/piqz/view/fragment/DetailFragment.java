package com.may.amy.piqz.view.fragment;


import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.may.amy.piqz.R;
import com.may.amy.piqz.databinding.DetailFragmentBinding;
import com.may.amy.piqz.model.NewsItem;
import com.may.amy.piqz.view.activity.MainActivity;
import com.may.amy.piqz.viewmodel.PostItemViewModel;

/**
 * A simple {@link Fragment} subclass.
 */
public class DetailFragment extends Fragment {
    private PostItemViewModel mViewModel;
    private DetailFragmentBinding binding;

    public DetailFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_detail, container, false);
        // Inflate the layout for this fragment
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (mViewModel == null){
            ((MainActivity) getActivity()).removeFragment(this);
        }
        binding.setViewModel(mViewModel);

    }

    public void setNewsItem(PostItemViewModel viewModel){
        this.mViewModel = viewModel;
    }

}
