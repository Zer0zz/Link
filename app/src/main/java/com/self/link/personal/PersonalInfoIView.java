package com.self.link.personal;

import com.leconssoft.common.BaseMvp.view.BaseView;
import com.self.link.base.UserInfo;

/**
 * description：
 * author：Administrator on 2020/5/24 16:15
 */
public interface PersonalInfoIView extends BaseView {

    void updateNickname();

    void updateBindPhoneNo();

    void loginout();

    UserInfo getUserInfo();

}
