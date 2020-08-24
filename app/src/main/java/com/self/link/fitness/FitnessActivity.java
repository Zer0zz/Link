package com.self.link.fitness;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.leconssoft.common.BaseMvp.BaseMvpHeadAct;
import com.leconssoft.common.BaseMvp.factory.CreateMvpPresenter;
import com.self.link.R;
import com.self.link.fitness.coach.CoachFragment;
import com.self.link.fitness.course.CourseFragment;
import com.self.link.identify.IdentifyActivity;
import com.self.link.main.adapter.GlideImageLoader;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.Transformer;
import com.youth.banner.listener.OnBannerListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

@CreateMvpPresenter(FitnessPersenter.class)
public class FitnessActivity extends BaseMvpHeadAct<FitnessIVeiw, FitnessPersenter> implements FitnessIVeiw {

    public static final String FITNESS_ID = "fitnessId";
    public static final String FITNESS_NAME = "fitnessName";

    @BindView(R.id.banner)
    Banner banner;
    @BindView(R.id.banner_layout)
    RelativeLayout banner_layout;

    @BindView(R.id.mtab_layout)
    TabLayout tabLayout;

    @BindView(R.id.view_pager)
    ViewPager viewPager;

    @BindView(R.id.ll_personal_profile)
    LinearLayout ll_personal_profile;
    @BindView(R.id.identify_btn)
    Button identify_btn;

    @BindView(R.id.renzheng_info_tv)
    TextView renzheng_info_tv;

    private String fitnessId;
    private String fitnessName;
    List<Fragment> fragments = new ArrayList<>();

    int status = 0;//0:没有认证 认证状态：1.审核中 2.已同意 3.已拒绝 ,

    @Override
    public int layoutId() {
        return R.layout.activity_fitness;
    }

    @Override
    protected void initUIData() {
        Intent intent = getIntent();
        setIvLeftSrc(R.mipmap.back_icon);
        fitnessId = intent.getStringExtra(FITNESS_ID);
        fitnessName = intent.getStringExtra(FITNESS_NAME);
        String stat = intent.getStringExtra("status");
        if (!TextUtils.isEmpty(stat)) {
            status = Integer.parseInt(stat);
        }
        initNarmol();

    }

    private void intiMyCenter() {
        setTvLeftMsg(fitnessName + "健身房");
        initListener();
        initViewPager(1);
        ll_personal_profile.setVisibility(View.GONE);
        banner_layout.setVisibility(View.GONE);

    }

    private void initNarmol() {
        setTvLeftMsg(fitnessName + "健身房");
        initListener();
        initViewPager(0);
        initBanner();
        if (status == 0) {//没有认证
            ll_personal_profile.setVisibility(View.VISIBLE);
        } else if (status == 1) {//审核中
            identify_btn.setVisibility(View.GONE);
            renzheng_info_tv.setText("认证中...");
        } else if (status == 2) {//已同意
            identify_btn.setVisibility(View.GONE);
            renzheng_info_tv.setText("认证通过");
        } else if (status == 3) {//被拒绝
            identify_btn.setVisibility(View.GONE);
            renzheng_info_tv.setText("认证被拒");
        }
        getMvpPresenter().getFitnessBanner(fitnessId);

    }

    void initBanner() {
        banner.setImageLoader(new GlideImageLoader());
        //设置banner动画效果
        banner.setBannerAnimation(Transformer.DepthPage);
        //设置自动轮播，默认为true
        banner.isAutoPlay(true);
        //设置轮播时间
        banner.setDelayTime(3000);
        //设置指示器位置（当banner模式中有指示器时）
        banner.setIndicatorGravity(BannerConfig.CENTER);
        banner.setOnBannerListener(new OnBannerListener() {
            @Override
            public void OnBannerClick(int position) {
            }
        });
    }

    @OnClick({R.id.identify_btn})
    public void onclick(View view) {
        switch (view.getId()) {
            case R.id.identify_btn:
                Intent intent = new Intent(this, IdentifyActivity.class);
                intent.putExtra("fitnessId", fitnessId);
                startActivity(intent);
                break;
        }
    }

    @Override
    public void setBannerData(List<Object> imgUrls) {

        banner.post(new Runnable() {
            @Override
            public void run() {
                banner.setImages(imgUrls);
                banner.start();
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        banner.startAutoPlay();
    }

    @Override
    public void onStop() {
        super.onStop();
        banner.stopAutoPlay();
    }

    private void initListener() {
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        tabLayout.addOnTabSelectedListener(new TabLayout.BaseOnTabSelectedListener() {

            @Override
            public void onTabSelected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    private void initViewPager(int from) {
        Bundle args = new Bundle();
        args.putString(FITNESS_ID, fitnessId);
        args.putString(FITNESS_NAME, fitnessName);
        fragments.add(CourseFragment.newInstance(args));
        fragments.add(CoachFragment.newInstance(args));

        viewPager.setAdapter(new FragmentStatePagerAdapter(getSupportFragmentManager()) {
            @NonNull
            @Override
            public Fragment getItem(int position) {
                return fragments.get(position);
            }

            @Override
            public int getCount() {
                return fragments.size();
            }
        });
        tabLayout.setupWithViewPager(viewPager);

        tabLayout.getTabAt(0).setText("课程");
        tabLayout.getTabAt(1).setText("教练");

    }

    @Override
    public void showLodingDialog() {
        showProgress();
    }

    @Override
    public void hidLodingDialog() {
        hindProgress();
    }

    @Override
    public void showErrMsg(String msg) {
        Toast.makeText(mActivity, msg, Toast.LENGTH_SHORT).show();
    }
}
