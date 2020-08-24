package com.self.link.mycenter;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.alibaba.fastjson.JSONObject;
import com.google.android.material.tabs.TabLayout;
import com.leconssoft.common.BaseMvp.model.BaseModel;
import com.leconssoft.common.NetService.OnHttpCallBack;
import com.leconssoft.common.base.BaseResponse;
import com.leconssoft.common.base.ViewManager;
import com.leconssoft.common.baseUtils.ZTLUtils;
import com.leconssoft.common.baseUtils.csustomdialog.MainProgressDialog;
import com.self.link.R;
import com.self.link.base.Constant;
import com.self.link.fitness.body.Attachment;
import com.self.link.fitness.coach.CoachFragment;
import com.self.link.fitness.course.CourseFragment;
import com.self.link.identify.IdentifyActivity;
import com.self.link.main.adapter.GlideImageLoader;
import com.self.link.mycenter.personal.PersonalFragment;
import com.self.link.mycenter.tuan.MyTuankeFragment;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.Transformer;
import com.youth.banner.listener.OnBannerListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.leconssoft.common.baseUtils.ZTLUtils.FlymeSetStatusBarLightMode;
import static com.leconssoft.common.baseUtils.ZTLUtils.MIUISetStatusBarLightMode;
import static com.self.link.fitness.FitnessActivity.FITNESS_ID;
import static com.self.link.fitness.FitnessActivity.FITNESS_NAME;

public class MyFitnessActivity extends AppCompatActivity {
    @BindView(R.id.banner)
    Banner banner;

    @BindView(R.id.ivLeft)
    ImageView ivLeft;

    @BindView(R.id.tvLeft)
    TextView tvLeft;

    @BindView(R.id.tab_collect)
    TabLayout tabLayout;
    @BindView(R.id.vp_collect)
    ViewPager viewPager;

    @BindView(R.id.identify_btn)
    Button identify_btn;
    @BindView(R.id.ll_personal_profile)
    LinearLayout ll_personal_profile;


    @BindView(R.id.renzheng_info_tv)
    TextView renzheng_info_tv;

    private String fitnessId;
    private String fitnessName;

    public MainProgressDialog mProgressDialog;
    List<Fragment> fragments = new ArrayList<>();
    int status = 0;

    int from;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_center);
        StatusBarLightMode(this);
        ButterKnife.bind(this);
        ViewManager.getInstance().addActivity(this);
        Intent intent = getIntent();
        fitnessId = intent.getStringExtra(FITNESS_ID);
        fitnessName = intent.getStringExtra(FITNESS_NAME);
        status = intent.getIntExtra("status",1);
        from = intent.getIntExtra("from", 1);

        initUi();
        initBanner();
        initViewPager();
        initListener();
        getFitnessBanner(fitnessId);

    }


    @OnClick({R.id.identify_btn, R.id.ivLeft})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.identify_btn:
                Intent intent = new Intent(this, IdentifyActivity.class);
                intent.putExtra("fitnessId", fitnessId);
                startActivity(intent);
                break;
            case R.id.ivLeft:
                onBackPressed();
                break;
        }
    }

    /**
     * 状态栏亮色模式，设置状态栏黑色文字、图标，
     * 适配4.4以上版本MIUIV、Flyme和6.0以上版本其他Android
     *
     * @param activity
     * @return 1:MIUUI 2:Flyme 3:android6.0
     */
    public static int StatusBarLightMode(Activity activity) {
        int result = 0;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            if (MIUISetStatusBarLightMode(activity, true)) {
                result = 1;
            } else if (FlymeSetStatusBarLightMode(activity.getWindow(), true)) {
                result = 2;
            } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                activity.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
                result = 3;
            }
        }
        return result;
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

    private void initViewPager() {

        Bundle args = new Bundle();
        args.putString(FITNESS_ID, fitnessId);
        args.putString(FITNESS_NAME, fitnessName);
        if (from == 1) {
            fragments.add(PersonalFragment.newInstance(args));
            fragments.add(MyTuankeFragment.newInstance(args));
        } else {
            fragments.add(CourseFragment.newInstance(args));
            fragments.add(CoachFragment.newInstance(args));
        }


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
        if (from == 1) {
            tabLayout.getTabAt(0).setText("私教");
            tabLayout.getTabAt(1).setText("团课");
        } else {
            tabLayout.getTabAt(0).setText("课程");
            tabLayout.getTabAt(1).setText("教练");
        }
    }

    private void initUi() {

        if (from == 1) {
            tvLeft.setText("我的健身房(" + fitnessName + ")");
        } else {
            tvLeft.setText(fitnessName);
        }

//            ll_personal_profile.setVisibility(View.GONE);

//            tvLeft.setText(fitnessName + "健身房");
            if (status == 1) {//没有认证 1.未认证 2.已过期 3.已认证 4.审核中
                ll_personal_profile.setVisibility(View.VISIBLE);
                identify_btn.setVisibility(View.VISIBLE);
            } else if (status == 2) {//已过期
                identify_btn.setVisibility(View.VISIBLE);
                renzheng_info_tv.setText("认证已过期");

            }else if (status == 3) {//已认证
                identify_btn.setVisibility(View.GONE);
                renzheng_info_tv.setText("认证通过");
            } else if (status == 4) {//4.审核中
                identify_btn.setVisibility(View.GONE);
                renzheng_info_tv.setText("认证审核中");
            }
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

    public void setBannerData(List<Object> imgUrls) {

        banner.post(new Runnable() {
            @Override
            public void run() {
                banner.setImages(imgUrls);
                banner.start();
            }
        });
    }

    public void showProgress() {//显示对话框
        if (null != mProgressDialog) {
            mProgressDialog.show();
        } else {
            mProgressDialog = new MainProgressDialog(this, com.leconssoft.common.R.style.MainProgressDialogStyle);
            mProgressDialog.show();
        }

    }

    public void hindProgress() {         //隐藏进度条对话框
        if (null != mProgressDialog) {
            mProgressDialog.dismiss();
        }
    }

    public void getFitnessBanner(String id) {

        Map map = new HashMap();
        map.put("id", id);
        new BaseModel().netReqModleNew.getBandParmHttp(Constant.RequestUrl.fitnessSlideshow, Constant.RequsetCode.FITNESS_SLIDESHOW_ID, map,
                new OnHttpCallBack<String>() {
                    @Override
                    public void onSuccessful(int id, String res) {
                        if (id == Constant.RequsetCode.FITNESS_SLIDESHOW_ID) {
                            BaseResponse baseResponse = JSONObject.parseObject(res, BaseResponse.class);
                            if (baseResponse.isSuccess()) {
                                List<Object> paths = new ArrayList<>();
                                List<Attachment> bannerPics = JSONObject.parseArray(baseResponse.getRe(), Attachment.class);

                                for (int i = 0; i < bannerPics.size(); i++) {
                                    paths.add(Constant.RequestUrl.imageBaseUrl + bannerPics.get(i).path);
                                }
                                setBannerData(paths);
                            } else {
//                                showErrMsg(baseResponse.getMsg());
                            }
                        }
                    }

                    @Override
                    public void onFaild(int id, String res, String err) {
//                        showErrMsg(err);
                    }
                });
    }

}