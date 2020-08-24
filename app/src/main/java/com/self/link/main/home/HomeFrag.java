package com.self.link.main.home;

import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.location.Location;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.Toast;

import androidx.core.view.ViewCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.material.tabs.TabLayout;
import com.leconssoft.common.BaseMvp.BaseMvpFragment;
import com.leconssoft.common.BaseMvp.factory.CreateMvpPresenter;
import com.leconssoft.common.recyclerView.SimpleRecAdapter;
import com.leconssoft.common.widgt.RefreshAllLayout;
import com.leconssoft.common.widgt.swiprefresh.intf.SwipeRefreshScrollInterface;
import com.self.link.R;
import com.self.link.main.adapter.FitnessAdapter;
import com.self.link.main.adapter.GlideImageLoader;
import com.self.link.main.me.dto.FitnessDto;
import com.self.link.mycenter.MyFitnessActivity;
import com.self.link.utils.RecyclerViewSpacesItemDecoration;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.Transformer;
import com.youth.banner.listener.OnBannerListener;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import butterknife.BindView;
import cn.droidlover.xrecyclerview.RecyclerItemCallback;

import static com.self.link.fitness.FitnessActivity.FITNESS_ID;
import static com.self.link.fitness.FitnessActivity.FITNESS_NAME;


/**
 * @author yucheng
 * @date 2017/12/20
 * @Description
 */
@CreateMvpPresenter(HomePersenter.class)
public class HomeFrag extends BaseMvpFragment<HomeView, HomePersenter> implements HomeView,
        RefreshAllLayout.OnRefreshListener, RefreshAllLayout.OnLoadListener, SwipeRefreshScrollInterface {


    @BindView(R.id.banner)
    Banner banner;

    @BindView(R.id.tab_layout)
    TabLayout tabLayout;

    @BindView(R.id.contentLayout)
    RecyclerView contentLayout;
    @BindView(R.id.contact_search)
    EditText contact_search;

    @BindView(R.id.refreshAllLayout)
    RefreshAllLayout refreshAllLayout;
    //位置信息
    Location mLocation = null;
    FitnessAdapter adapter;

    int pageNo = 1;
    int pageSize = 10;

    @Override
    protected int getLayoutId() {
        return R.layout.frag_frist;
    }

    @Override
    protected void initUI(Bundle bundle) {
        EventBus.getDefault().register(this);
        initBanner();
        initTabLayout();
        initSearchBar();
    }

    private void initSearchBar() {
        contact_search.setOnEditorActionListener((v, actionId, event) -> {
            switch (actionId) {
                case EditorInfo.IME_ACTION_SEARCH:
                    pageNo = 1;
                    String searchmsg = contact_search.getText().toString();
                    getMvpPresenter().loadFitnessData(mLocation, pageNo, pageSize, searchmsg);
                    break;
                default:
                    break;
            }
            return true;
        });

        contact_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    @Override
    public void initData(Bundle bundle) {
        super.initData(bundle);
        contentLayout.setLayoutManager(new LinearLayoutManager(mActivity));
        contentLayout.setAdapter(getPageAdapter());

        refreshAllLayout.setRefreshing(false);
        refreshAllLayout.setCanRefresh(false);
//        refreshAllLayout.setCanLoadMore(true);
        refreshAllLayout.setOnRefreshListener(this);
        refreshAllLayout.setLoadListener(this);
        refreshAllLayout.setScrollAble(this);

        contentLayout.addItemDecoration(new RecyclerViewSpacesItemDecoration(10));
        getMvpPresenter().loadBanerData();
    }

    @Override
    public void onResume() {
        super.onResume();

        refreshAllLayout.setCanLoadMore(true);
        contact_search.clearFocus();
        String searchmsg = contact_search.getText().toString();
        getMvpPresenter().loadFitnessData(mLocation, pageNo, pageSize, searchmsg);
    }

    private void initTabLayout() {
        tabLayout.addOnTabSelectedListener(new TabLayout.BaseOnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                tab.getText();
                if (tab.getText().toString().trim().equals("健身房")) {
//                    new RegisterModel().getCode("", null);

                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                //被取消
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                //重复选中
                if (tab.getText().toString().trim().equals("健身房")) {
                    onRefresh();
                }
            }
        });
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


    @Subscribe(threadMode = ThreadMode.MAIN, priority = 1, sticky = false)
    public void setBannerData(List<Object> imgUrls) {
        banner.setImages(imgUrls);
        banner.start();
    }

    @Subscribe(threadMode = ThreadMode.MAIN, priority = 1, sticky = true)
    public void getLocation(Location location) {
        mLocation = location;
    }

    @Override
    public void onStart() {
        super.onStart();
        banner.startAutoPlay();
        onRefresh();
    }

    @Override
    public void onStop() {
        super.onStop();
        banner.stopAutoPlay();
    }

    @Override
    public void onRefresh() {
        pageNo = 1;

    }

    @Override
    public void onLoad() {

        if (!getMvpPresenter().hasNextPage()) {//没有更多
            refreshAllLayout.setCanLoadMore(false);
//            showErrMsg("没有更多");
            return;
        }
        pageNo++;
        String searchmsg = contact_search.getText().toString();
        getMvpPresenter().loadFitnessData(mLocation, pageNo, pageSize, searchmsg);
    }

    /**
     * 子控件能否下拉  一般在下拉刷新时判断
     *
     * @return
     */
    @Override
    public boolean canChildScrollUp(float x, float y) {
        int[] loca = new int[2];
        banner.getLocationOnScreen(loca);
        Rect rect = new Rect(loca[0], loca[1], loca[0] + banner.getWidth(), loca[1] + banner.getHeight());
        if (rect.contains((int) x, (int) y))
            return false;
        else {
            Log.d("topoffset", ViewCompat.canScrollVertically(contentLayout, -1) + "");
            return ViewCompat.canScrollVertically(contentLayout, -1);
        }
    }

    @Override
    public boolean canChildScrollDown(float x, float y) {
        return ViewCompat.canScrollVertically(contentLayout, 1);
    }

    @Override
    public SimpleRecAdapter getPageAdapter() {
        if (adapter == null) {
            adapter = new FitnessAdapter(mActivity);
            adapter.setRecItemClick(new RecyclerItemCallback<FitnessDto, FitnessAdapter.ViewHolder>() {
                @Override
                public void onItemClick(int position, FitnessDto model, int tag, FitnessAdapter.ViewHolder holder) {
                    super.onItemClick(position, model, tag, holder);
                    Intent intent = new Intent(mActivity, MyFitnessActivity.class);
                    intent.putExtra(FITNESS_ID, model.id);
                    intent.putExtra(FITNESS_NAME, model.name);
                    intent.putExtra("status", model.status);
                    intent.putExtra("from",0);
                    startActivity(intent);
                }
            });
        }
        return adapter;
    }

    @Override
    public RefreshAllLayout getSwipeRefreshLayout() {
        return refreshAllLayout;
    }

    @Override
    public RecyclerView getContentLayout() {
        return contentLayout;
    }

    @Override
    public Context getFragMentContext() {
        return mActivity;
    }

    @Override
    public int pageNo() {
        return pageNo;
    }

    @Override
    public void setPageNo(int pageNo) {
        this.pageNo = pageNo;
    }

    @Override
    public void finshView() {

    }


    @Override
    public void showLodingDialog() {
        mActivity.showProgress();
    }

    @Override
    public void hidLodingDialog() {
        mActivity.hindProgress();
    }

    @Override
    public void showErrMsg(String msg) {
        Toast.makeText(mActivity, msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);

    }
}
