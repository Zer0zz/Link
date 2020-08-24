package com.leconssoft.common.pageMvp;

import android.content.Context;
import android.view.View;

import com.leconssoft.common.BaseMvp.BaseMvpHeadAct;
import com.leconssoft.common.R;
import com.leconssoft.common.baseUtils.UIHelper;

import cn.droidlover.xrecyclerview.XRecyclerContentLayout;
import cn.droidlover.xrecyclerview.XRecyclerView;

/**
 * <p>类说明</p>
 * 抽象列表Activity
 *
 * @author yucheng
 * @date 2018/9/27 11:44
 * @Description
 */
public abstract class BasePageAct<T, M extends BasePageModel, P extends PagePersenter<T, M>> extends BaseMvpHeadAct<PageView, P> implements PageView {
    XRecyclerContentLayout contentLayout;

    boolean isCanRefres = true;
    int startPageNo = 1;
    public abstract M getMvpModel();

    @Override
    public void initUIData() {
        getMvpPresenter().setMvpModel(getMvpModel());
        init();
        contentLayout = findViewById(R.id.contentLayout);
        setLayoutManager(contentLayout.getRecyclerView());
        contentLayout.getRecyclerView().setAdapter(getPageAdapter());
        //使用默认加载更多布局
//        contentLayout.getRecyclerView().useDefLoadMoreView();
        if (isCanRefres) {
            contentLayout.loadingView(View.inflate(getFragMentContext(), R.layout.dlg_main_progress, null));
            contentLayout.getRecyclerView().setOnRefreshAndLoadMoreListener(new XRecyclerView.OnRefreshAndLoadMoreListener() {
                @Override
                public void onRefresh() {
                    getMvpPresenter().getFragmentMpdel().loadData(startPageNo, getUrl());
                }

                @Override
                public void onLoadMore(int page) {
                    getMvpPresenter().getFragmentMpdel().loadData(page, getUrl());
                }
            });
        } else {
            contentLayout.getRecyclerView().stateCallback(null);
        }
        getMvpPresenter().getFragmentMpdel().loadData(startPageNo, getUrl());
    }

    protected abstract void init();

    @Override
    public XRecyclerContentLayout getContentLayout() {
        return contentLayout;
    }

    /**
     * 重新刷新列表
     */
    public void onReshList() {
        getMvpPresenter().getFragmentMpdel().loadData(startPageNo, getUrl());
    }

    /**
     * 关闭刷新控制
     *
     * @param isRefres
     */
    public void setRefres(boolean isRefres) {
        isCanRefres = isRefres;
    }

    @Override
    public Context getFragMentContext() {
        return this.mActivity;
    }


    public abstract String getUrl();

    @Override
    public void showLodingDialog() {
        this.showProgress();
    }

    @Override
    public void hidLodingDialog() {
        this.hindProgress();
    }

    @Override
    public void showErrMsg(String msg) {
        UIHelper.ToastMessage(mActivity, msg);
    }

    public abstract void setLayoutManager(XRecyclerView recyclerView);

    @Override
    public int startPageNo() {
        return startPageNo;
    }

    @Override
    public void setTitleMsg(String titleMsg) {

    }

    @Override
    public void setHidTvRight(boolean flag) {

    }

    @Override
    public void finshView() {
        this.finish();
    }
}
