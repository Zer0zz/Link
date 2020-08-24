package com.self.link.notification;

import android.content.Intent;
import android.view.View;

import com.leconssoft.common.BaseMvp.factory.CreateMvpPresenter;
import com.leconssoft.common.baseUtils.Common;
import com.leconssoft.common.pageMvp.BasePageAct;
import com.leconssoft.common.recyclerView.SimpleRecAdapter;
import com.self.link.R;

import java.util.List;

import cn.droidlover.xrecyclerview.XRecyclerView;

@CreateMvpPresenter(NotificationPersenter.class)
public class NotificationActivity extends BasePageAct<List<NotificationResult>, NotificationModel, NotificationPersenter> {

    NotificationAdapter adapter;
//    BasePageFragment


    @Override
    public int layoutId() {
        return R.layout.activity_notification;
    }

    @Override
    public NotificationModel getMvpModel() {
        return new NotificationModel(getMvpPresenter());
    }


    @Override
    protected void init() {
        tvTitle.setVisibility(View.GONE);
        setTvLeftMsg("消息通知");
        setIvLeftSrc(R.mipmap.back_icon);

    }

    @Override
    public SimpleRecAdapter getPageAdapter() {
        if (adapter == null) {
            adapter = new NotificationAdapter(mActivity);
        }
        return adapter;
    }

    @Override
    public String getUrl() {
        return "";
    }

    @Override
    public void showLodingDialog() {

    }

    @Override
    public void hidLodingDialog() {

    }

    @Override
    public void showErrMsg(String msg) {

    }

    @Override
    public void setLayoutManager(XRecyclerView recyclerView) {
        recyclerView.verticalLayoutManager(mActivity);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        onReshList();

    }

}
