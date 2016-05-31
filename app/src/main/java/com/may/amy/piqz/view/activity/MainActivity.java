package com.may.amy.piqz.view.activity;

import android.content.SharedPreferences;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import com.may.amy.piqz.R;
import com.may.amy.piqz.databinding.ActivityMainBinding;
import com.may.amy.piqz.model.AuthResponseBody;
import com.may.amy.piqz.model.DataReceivedInterface;
import com.may.amy.piqz.model.rest.OAuthApi;
import com.may.amy.piqz.util.AppUtil;
import com.may.amy.piqz.view.fragment.FeedFragment;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getSimpleName();

    private FeedFragment mFeedFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityMainBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        mFeedFragment = new FeedFragment();
        AppUtil.getInstance().getOAuthApi().refreshTokenIfExpired();

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container_main, mFeedFragment).commit();

    }

    public void addFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction()
                .add(R.id.container_main, fragment)
                .hide(mFeedFragment)
                .addToBackStack(null).commit();

    }
    public void removeFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction()
                .remove(fragment)
                .show(mFeedFragment)
                .addToBackStack(null).commit();

    }
}
