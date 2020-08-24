package com.self.link.main.me;

import androidx.recyclerview.widget.RecyclerView;

import com.leconssoft.common.BaseMvp.view.BaseView;
import com.self.link.base.UserInfo;
import com.self.link.main.me.adapter.MeAdapter;

/**
 * <p>类说明</p>
 *
 * @author yucheng
 * @date 2018/9/28 10:04
 * @Description
 */
public interface MeIView extends BaseView {

    /**
     *
     */
    void userMsg();

    void userSetting();

    void daka();

    void setUserInfo(UserInfo userInfo);

    MeAdapter getAdapter();

    void setRefreshing(boolean refreshing);
}
