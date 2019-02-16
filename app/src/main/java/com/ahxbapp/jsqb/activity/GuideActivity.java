package com.ahxbapp.jsqb.activity;

import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.ahxbapp.jsqb.R;
import com.ahxbapp.jsqb.utils.GlideImageLoader;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.Transformer;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.List;

@EActivity(R.layout.activity_guide)
public class GuideActivity extends AppCompatActivity {

    @ViewById
    Banner mBanner;
    @ViewById
    TextView tvNext;

    @AfterViews
    void initView() {
        initBanner();
    }

    @Click
    void tvNext() {
        LoanMainActivity_.intent(this).start();
    }

    private List<Integer> bannerUrls;

    /**
     *
     * */
    private void initBanner() {
        bannerUrls = new ArrayList<>();
        bannerUrls.add(R.mipmap.img_kefu_bg);
        bannerUrls.add(R.mipmap.img_letter_bg);
        bannerUrls.add(R.mipmap.img_kefu_bg);
        bannerUrls.add(R.mipmap.img_letter_bg);
        mBanner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR);
        mBanner.setImageLoader(new GlideImageLoader());
        mBanner.setBannerAnimation(Transformer.Default);
        mBanner.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                if (position==bannerUrls.size()){
                    tvNext.setVisibility(View.VISIBLE);
                }else {
                    tvNext.setVisibility(View.GONE);
                }
            }

            @Override
            public void onPageSelected(int position) {
                if (position==bannerUrls.size()){
                    tvNext.setVisibility(View.VISIBLE);
                }else {
                    tvNext.setVisibility(View.GONE);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        mBanner.isAutoPlay(false);
        mBanner.setIndicatorGravity(BannerConfig.CENTER);
        mBanner.setImages(bannerUrls);
        mBanner.start();
    }
}
