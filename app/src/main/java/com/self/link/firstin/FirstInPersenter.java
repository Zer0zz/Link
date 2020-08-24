package com.self.link.firstin;

import com.leconssoft.common.BaseMvp.presenter.BasePresenter;

/**
 * description：
 * author：Administrator on 2020/5/22 14:37
 */
public class FirstInPersenter extends BasePresenter<FirstInIView> {

    FirstInModel firstInModel;

    public FirstInPersenter() {
        firstInModel = new FirstInModel();
    }



}
