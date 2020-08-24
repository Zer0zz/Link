package com.self.link.verifycode;

import com.leconssoft.common.BaseMvp.view.BaseView;

/**
 * description：
 * author：Administrator on 2020/5/23 20:53
 */
public interface VerifyVodeIView extends BaseView {

    /**
     * 获取验证码
     * @return 验证码
     */
    String getInputCode();

    /**
     * 验证成功后下一步
     */
    void doNext();


}
