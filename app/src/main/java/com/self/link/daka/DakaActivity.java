package com.self.link.daka;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import butterknife.BindView;
import butterknife.OnClick;

import android.os.Bundle;
import android.view.View;

import com.google.android.material.tabs.TabLayout;
import com.leconssoft.common.BaseMvp.BaseMvpHeadAct;
import com.leconssoft.common.baseUtils.ZTLUtils;
import com.self.link.R;
import com.self.link.daka.sijiao.SijiaoFragment;
import com.self.link.daka.tuanke.TuankeFragment;
import com.self.link.fitness.course.CourseFragment;

import java.util.ArrayList;
import java.util.List;

public class DakaActivity extends BaseMvpHeadAct<DakaIView, DakaPersenter> implements DakaIView {

    @BindView(R.id.view_pager)
    ViewPager viewPager;
    @BindView(R.id.mtab_layout)
    TabLayout tabLayout;
    List<Fragment> fragments = new ArrayList<>();

    @Override
    public int layoutId() {
        return R.layout.activity_daka;
    }

    @Override
    protected void initUIData() {
        setTvLeftMsg("打卡中心");
        setIvLeftSrc(R.mipmap.back_icon);
        initViewPager();
        initListener();

    }

    private void initViewPager() {

        fragments.add(SijiaoFragment.newInstance());
        fragments.add(new TuankeFragment());

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

        tabLayout.getTabAt(0).setText("私教打卡");
        tabLayout.getTabAt(1).setText("团课打卡");
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
    }
}
