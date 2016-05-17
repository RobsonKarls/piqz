package com.may.amy.piqz.manager;

import android.content.Context;
import android.util.Log;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.formats.NativeAd;

/**
 * Created by kuhnertj on 17.05.2016.
 */
public class AdManager {
    private Context mContext;
    private NativeAd mNativeAd;
    private AdLoader adLoader;

    public AdManager(Context mContext) {
        this.mContext = mContext;
    }

    public AdLoader createAdLoader(){
        if (adLoader == null){
            adLoader = new AdLoader.Builder(mContext, "")
            .withAdListener(new AdListener() {
                @Override
                public void onAdFailedToLoad(int errorCode) {
                    super.onAdFailedToLoad(errorCode);
                    Log.e("AdLoader", "AdFailedToLoad. Error = " + errorCode);
                }
            })
                    .build();
        }

        return adLoader;
    }


}
