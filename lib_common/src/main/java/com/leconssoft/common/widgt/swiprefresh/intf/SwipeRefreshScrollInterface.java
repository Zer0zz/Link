package com.leconssoft.common.widgt.swiprefresh.intf;

/**
 *swipeRefresh控件子布局滑动判断
 * Created by GYH on 2017/11/3.
 */

public interface SwipeRefreshScrollInterface {

    /**
     * 子控件能否下拉  一般在下拉刷新时判断
     * @return
     */
    boolean canChildScrollUp(float x, float y);

    /**
     * 子控件能否上拉  一般在上拉加载时判断
     * @return
     */
    boolean canChildScrollDown(float x, float y);
}
