package com.may.amy.piqz.view.activity;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import com.may.amy.piqz.R;
import com.may.amy.piqz.model.AuthResponseBody;
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
        setContentView(R.layout.activity_main);

        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        ((Toolbar) findViewById(R.id.toolbar)).setTitle(getString(R.string.app_name));
        mFeedFragment = new FeedFragment();
        AppUtil.getInstance().getOAuthApi().refreshTokenIfExpired();
        replaceFragment();


    }

    private void replaceFragment() {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container_main, mFeedFragment).commit();

    }
}
