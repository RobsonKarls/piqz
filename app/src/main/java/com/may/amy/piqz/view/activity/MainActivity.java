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

        OAuthApi mAuthApi = new OAuthApi();
        mFeedFragment = new FeedFragment();
        boolean auth = true;
        if (auth){
            mAuthApi.auth(new Callback<AuthResponseBody>() {
                @Override
                public void onResponse(Call<AuthResponseBody> call, Response<AuthResponseBody> response) {
                    Log.d(TAG, "onResponse - Call: " + call.toString() + "\nResponse: " + response.raw().toString());
                    response.code();
                    if (response.isSuccessful()) {
                        if (response.body().getAccessToken() == null && response.errorBody() != null){
                            Log.d(TAG,response.errorBody().toString());
                            return;
                        }
                        replaceFragment();
                        Log.d(TAG, "access token: " + response.body().getAccessToken());
                        SharedPreferences.Editor editor = AppUtil.getInstance().getAppPreferences().edit();
                        editor.putString(AppUtil.KEY_TOKEN, response.body().getAccessToken());
                        editor.putString(AppUtil.KEY_TOKEN_TYPE, response.body().getTokenType());
                        editor.putString(AppUtil.KEY_EXPIRES_IN, response.body().getExpiresIn());
                        editor.putString(AppUtil.KEY_SCOPE, response.body().getScope());
                        editor.apply();
                    }
                }

                @Override
                public void onFailure(Call<AuthResponseBody> call, Throwable t) {
                    Log.e(TAG, t.toString());
                }
            });
        }else{
            replaceFragment();
        }


    }

    private void replaceFragment() {
        if (getSupportFragmentManager().getFragments() == null || !getSupportFragmentManager().getFragments().contains(mFeedFragment)) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container_main, mFeedFragment).commitAllowingStateLoss();
        }

    }
}
