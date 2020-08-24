package com.self.link.verifycode;

import android.text.TextUtils;

import com.leconssoft.common.BaseMvp.presenter.BasePresenter;

/**
 * description：
 * author：Administrator on 2020/5/23 21:02
 */
public class VerifyCodePersenter extends BasePresenter<VerifyVodeIView> {


    final VerifyCodeModel model;

    public VerifyCodePersenter() {
        model = new VerifyCodeModel();
    }


    public void setCode2Phone(String standerCode) {
        String inputCode = getMvpView().getInputCode();
        if(TextUtils.isEmpty(inputCode)){
            getMvpView().showErrMsg("请正确输入验证码！");
            return;
        }
        if (!TextUtils.equals(standerCode,inputCode)) {
            getMvpView().showErrMsg("验证码错误！");
            return;
        }
        verifyCode();

    }


    private void verifyCode() {
        getMvpView().doNext();
    }

}
