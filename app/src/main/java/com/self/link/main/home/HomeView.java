package com.self.link.main.home;

import android.content.Context;

import com.leconssoft.common.BaseMvp.view.BaseView;
import com.leconssoft.common.recyclerView.SimpleRecAdapter;
import com.leconssoft.common.widgt.RefreshAllLayout;

import androidx.recyclerview.widget.RecyclerView;

/**
 * description：
 * author：Administrator on 2020/5/21 10:06
 */
public interface HomeView extends BaseView {

    /**
     * 获取适配器
     */
    SimpleRecAdapter getPageAdapter();

    RefreshAllLayout getSwipeRefreshLayout();

    /**
     * 获取列控件
     *
     * @return
     */
    RecyclerView getContentLayout();


    /**
     * 获取Contenxt对象
     *
     * @return
     */
    Context getFragMentContext();


    int pageNo();


    void setPageNo(int pageNo);

    void finshView();
}
