package com.may.amy.piqz.model.holder;

import android.content.Context;
import android.content.res.Resources;
import android.support.v4.content.ContextCompat;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.may.amy.piqz.R;
import com.may.amy.piqz.databinding.BannerAdBinding;
import com.may.amy.piqz.model.NewsItem;
import com.may.amy.piqz.util.PrivateConstants;

import java.util.Random;

/**
 * ViewHolder for Ads
 */
public class AdVH extends BaseVH {

  private BannerAdBinding binding;
    public AdVH(BannerAdBinding mBinding) {
        super(mBinding);
        this.binding = mBinding;
    }

    @Override
    public void displayPost(NewsItem post) {
        super.displayPost(post);
    }

    public void displayAd(){
     //   binding.adView.setAdUnitId(getRandomAdId());
     //   binding.adView.setAdSize(AdSize.BANNER);
        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice(PrivateConstants.TEST_DEVICE_S4)
                .build();
        binding.adView.loadAd(adRequest);
    }

    private String getRandomAdId(){
        String id = "";
        try {
            String[] idArray = binding.getRoot().getContext().getResources().getStringArray(R.array.ad_banner_array);
            id = idArray[new Random().nextInt(5)];
        } catch (Exception e) {
            e.printStackTrace();
        }
        return id;
    }
}
