package com.self.link.main.me;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.android.material.appbar.AppBarLayout;
import com.leconssoft.common.BaseMvp.BaseMvpFragment;
import com.leconssoft.common.BaseMvp.factory.CreateMvpPresenter;
import com.leconssoft.common.baseUtils.ZTLUtils;
import com.self.link.R;
import com.self.link.base.Constant;
import com.self.link.base.UserInfo;
import com.self.link.daka.DakaActivity;
import com.self.link.main.me.adapter.MeAdapter;
import com.self.link.main.me.dto.FitnessDto;
import com.self.link.mycenter.MyFitnessActivity;
import com.self.link.notification.NotificationActivity;
import com.self.link.personal.PersonalInfoActivity;
import com.self.link.utils.RecyclerViewSpacesItemDecoration;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

import static com.self.link.fitness.FitnessActivity.FITNESS_ID;
import static com.self.link.fitness.FitnessActivity.FITNESS_NAME;

/**
 * <p>类说明</p>
 * 我的模块
 *
 * @author zhangchenglin
 * @date 2018/9/28 10:02
 * @Description
 */
@CreateMvpPresenter(MePresenter.class)
public class MeFrag extends BaseMvpFragment<MeIView, MePresenter> implements MeIView,
        BaseQuickAdapter.RequestLoadMoreListener, SwipeRefreshLayout.OnRefreshListener {

    @BindView(R.id.me_toolbat)
    Toolbar toolbar;

    @BindView(R.id.me_appbarlayout)
    AppBarLayout me_appbarlayout;

    @BindView(R.id.nick_name)
    TextView nick_name;

    @BindView(R.id.sign_tv)
    TextView sign_tv;

    @BindView(R.id.head_iv)
    ImageView iv_head;
    @BindView(R.id.me_fitness_rv)
    RecyclerView mRecyclerView;

    @BindView(R.id.swipeLayout)
    SwipeRefreshLayout swipeLayout;
    UserInfo mUserInfo;
    MeAdapter mAdapter;
    private List<FitnessDto> mData; //todo
    private int mCurrentPage = 1;


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
    protected int getLayoutId() {
        return R.layout.frag_me_view;
    }

    @OnClick({R.id.daka_btn, R.id.head_iv})
    public void onClick(View view) {
        int viewId = view.getId();
        switch (viewId) {
            case R.id.daka_btn:
                daka();
                break;
            case R.id.head_iv:
                userSetting();
                break;
        }
    }


    @Override
    protected void initUI(Bundle bundle) {
        EventBus.getDefault().register(this);
        ZTLUtils.setStatusBarColor(getActivity(), R.color.transparent);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                int i = item.getItemId();
                if (i == R.id.setting_item) {
                    userSetting();
                    return true;
                } else if (i == R.id.msg_item) {
//                    userMsg();
                    showErrMsg("功能正在开发中");
                    return true;
                }

                return false;
            }
        });

        me_appbarlayout.addOnOffsetChangedListener(new AppBarLayout.BaseOnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int i) {


            }
        });
        initAdapter();
        initRecyclerView();

    }


    @Override
    public void onResume() {
        super.onResume();
        getMvpPresenter().getUserInfo(1, 5);

    }

    private void initAdapter() {
        swipeLayout.setEnabled(false);//禁用刷新
        swipeLayout.setOnRefreshListener(this);
        mAdapter = new MeAdapter(R.layout.me_fitness_item_layout);

        /** --------------------------- 动画效果 --------------------------- **/
        //开启动画效果
        mAdapter.openLoadAnimation();
        //设置动画效果
        /**
         * 渐显 ALPHAIN
         * 缩放 SCALEIN
         * 从下到上 SLIDEIN_BOTTOM
         * 从左到右 SLIDEIN_LEFT
         * 从右到左 SLIDEIN_RIGHT
         */
        mAdapter.openLoadAnimation(BaseQuickAdapter.ALPHAIN);
//        mAdapter.setNewData(mData);
        //设置自定义加载更多布局
//        mAdapter.setLoadMoreView(customView);
        /** --------------------------- 点击事件 --------------------------- **/

        //设置Item点击事件
        mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
//                ToastUitl.showShort("onItemClick：" + position);
                FitnessDto model = mAdapter.getData().get(position);
                Intent intent = new Intent(mActivity, MyFitnessActivity.class);
                intent.putExtra(FITNESS_ID, model.id);
                intent.putExtra(FITNESS_NAME, model.name);
                intent.putExtra("status", model.status);
                intent.putExtra("from", 1);
                startActivity(intent);
            }
        });
        //设置Item长按事件
        mAdapter.setOnItemLongClickListener(new BaseQuickAdapter.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(BaseQuickAdapter adapter, View view, int position) {
//                ToastUitl.showShort("onItemLongClick：" + position);
                return false;
            }
        });
        //设置Item中子控件点击事件
        mAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                //判断子控件
//                if (view.getId() == R.id.tv_name) {
//                   ToastUitl.showShort("onItemChildClick：" + position);
//                }
            }
        });

        /** --------------------------- 加载更多 --------------------------- **/

        //设置加载更多
        mAdapter.setOnLoadMoreListener(this, mRecyclerView);
        //默认第一次加载会进入回调，如果不需要可以配置
        mAdapter.disableLoadMoreIfNotFullPage();

        //当列表滑动到倒数第N个Item的时候(默认是1)回调onLoadMoreRequested方法
        mAdapter.setPreLoadNumber(mCurrentPage);


        /** --------------------------- 添加头部尾部 --------------------------- **/
//        View item_head = LayoutInflater.from(getContext()).inflate(R.layout.item_head, (ViewGroup) mRecyclerView.getParent(), false);
//        mAdapter.addHeaderView(item_head);
//        View item_footer = LayoutInflater.from(getContext()).inflate(R.layout.item_footer, (ViewGroup) mRecyclerView.getParent(), false);
//        mAdapter.addFooterView(item_footer);

    }

    private void initRecyclerView() {
        //设置布局方式
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        //解决数据加载不完的问题
        mRecyclerView.setNestedScrollingEnabled(false);
        //当知道Adapter内Item的改变不会影响RecyclerView宽高的时候，可以设置为true让RecyclerView避免重新计算大小
        mRecyclerView.setHasFixedSize(true);
        //解决数据加载完成后, 没有停留在顶部的问题
        mRecyclerView.setFocusable(false);
        //添加分割线
        mRecyclerView.addItemDecoration(new RecyclerViewSpacesItemDecoration(10));
//        mRecyclerView.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));
        //设置适配器
        mRecyclerView.setAdapter(mAdapter);

        // -----------------------点击长按事件的使用方式-----------------------//
//        //另一种Item点击事件
//        mRecyclerView.addOnItemTouchListener(new OnItemClickListener() {
//            @Override
//            public void onSimpleItemClick(BaseQuickAdapter adapter, View view, int position) {
//                ToastUitl.showShort("mRecyclerView.onItemClick：" + position);
//            }
//        });
//        //另一种Item长按事件
//        mRecyclerView.addOnItemTouchListener(new OnItemLongClickListener() {
//            @Override
//            public void onSimpleItemLongClick(BaseQuickAdapter adapter, View view, int position) {
//                ToastUitl.showShort("mRecyclerView.onItemLongClick：" + position);
//            }
//        });
//        //另一种设置Item中子控件点击事件
//        mRecyclerView.addOnItemTouchListener(new OnItemChildClickListener() {
//            @Override
//            public void onSimpleItemChildClick(BaseQuickAdapter adapter, View view, int position) {
//                ToastUitl.showShort("mRecyclerView.onItemChildClick：" + position);
//            }
//        });
//        //另一种设置Item中子控件长按事件
//        mRecyclerView.addOnItemTouchListener(new OnItemChildLongClickListener() {
//            @Override
//            public void onSimpleItemChildLongClick(BaseQuickAdapter adapter, View view, int position) {
//                ToastUitl.showShort("mRecyclerView.onItemChildLongClick：" + position);
//            }
//        });

//        //if you wish to implement various forms of click(如果你希望实施各种形式的点击)
//        mRecyclerView.addOnItemTouchListener(new SimpleClickListener() {
//            @Override
//            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
//                ToastUitl.showShort("mRecyclerView.onItemClick：" + position);
//            }
//
//            @Override
//            public void onItemLongClick(BaseQuickAdapter adapter, View view, int position) {
//                ToastUitl.showShort("mRecyclerView.onItemLongClick：" + position);
//            }
//
//            @Override
//            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
//                ToastUitl.showShort("mRecyclerView.onItemChildClick：" + position);
//            }
//
//            @Override
//            public void onItemChildLongClick(BaseQuickAdapter adapter, View view, int position) {
//                ToastUitl.showShort("mRecyclerView.onItemChildLongClick：" + position);
//            }
//        });
        // -----------------------End点击长按事件的使用方式----------------------- //

    }


    @Override
    public void userMsg() {
        Intent intent = new Intent(getActivity(), NotificationActivity.class);
        startActivity(intent);
    }

    @Override
    public void userSetting() {
        Intent intent = new Intent(getActivity(), PersonalInfoActivity.class);
        startActivity(intent);
    }

    @Override
    public void daka() {
        Intent intent = new Intent(getActivity(), DakaActivity.class);
        startActivity(intent);
    }

    @Subscribe(threadMode = ThreadMode.MAIN, priority = 1, sticky = true)
    @Override
    public void setUserInfo(UserInfo userInfo) {
        mUserInfo = userInfo;
        nick_name.setText(userInfo.name);
        if (!TextUtils.isEmpty(userInfo.headPortrait)) {
            Glide.with(mActivity).load(Constant.RequestUrl.imageBaseUrl+userInfo.headPortrait).into(iv_head);
        }


    }

    @Override
    public MeAdapter getAdapter() {
        return mAdapter;
    }

    @Override
    public void setRefreshing(boolean refreshing) {
        swipeLayout.setRefreshing(refreshing);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onLoadMoreRequested() {
        mCurrentPage += 1;
        if (!getMvpPresenter().hasNextPage()) {
            mAdapter.loadMoreEnd();
            return;
        }
        getMvpPresenter().getUserInfo(mCurrentPage, MePresenter.pageSize);
        // 1秒钟后刷新页面
//        mRecyclerView.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                // 模拟网络请求获取数据
//                List<String> data = new ArrayList<>();
//                mCurrentPage += 1;
//                for (int i = 0; i < 10; i++) {
//                    data.add(i + "");
//                }
//                if (mCurrentPage <= 5) {
////                    mAdapter.addData(data);
//                    //数据加载完成
//                    mAdapter.loadMoreComplete();
//                } else {
//                    //数据加载完毕
//                    mAdapter.loadMoreEnd();
//                }
//            }
//        }, 1000);
    }

    @Override
    public void onRefresh() {
        //刷新的时候禁止加载更多
        mAdapter.setEnableLoadMore(false);
        mCurrentPage = 1;
        getMvpPresenter().getUserInfo(mCurrentPage, MePresenter.pageSize);
        // 1秒钟后刷新页面
//        mRecyclerView.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                // 模拟网络请求获取数据
//                List<String> data = new ArrayList<>();
//                mCurrentPage = 1;
//                for (int i = 0; i < 10; i++) {
//                    data.add(i + "");
//                }
//                if (mCurrentPage <= 5) {
////                    mAdapter.addData(data);
//                    //数据加载完成
//                    mAdapter.loadMoreComplete();
//                } else {
//                    //数据加载完毕
//                    mAdapter.loadMoreEnd();
//                }
//            }
//        }, 1000);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}
