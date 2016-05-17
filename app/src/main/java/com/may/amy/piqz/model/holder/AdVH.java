package com.may.amy.piqz.model.holder;

import com.google.android.gms.ads.AdRequest;
import com.may.amy.piqz.databinding.BannerAdBinding;
import com.may.amy.piqz.model.NewsItem;
import com.may.amy.piqz.util.PrivateConstants;

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
        AdRequest adRequest = new AdRequest.Builder()
               // .addTestDevice(PrivateConstants.TEST_DEVICE_S4)
                .build();
        binding.adView.loadAd(adRequest);
    }
}
