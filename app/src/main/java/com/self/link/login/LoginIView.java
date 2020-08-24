package com.self.link.login;

import com.leconssoft.common.BaseMvp.view.BaseView;

/**
 * description：
 * author：Administrator on 2020/5/23 19:36
 */
public interface LoginIView extends BaseView {
    /**
     * 获取用户输入的手机号
     *
     * @return
     */
    String getPhoneNo();

    void loginSuccess();

    void resetGetCodeTv();
}
