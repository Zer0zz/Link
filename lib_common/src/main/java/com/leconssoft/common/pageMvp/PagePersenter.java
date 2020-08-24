package com.leconssoft.common.pageMvp;

import android.os.Bundle;

import com.leconssoft.common.BaseMvp.presenter.BasePresenter;
import com.leconssoft.common.baseUtils.baseModle.BaseResult;
import com.leconssoft.common.baseUtils.Common;

import java.util.List;

public class PagePersenter<T,M extends BasePageModel> extends BasePresenter<PageView> {

    M fragmentMpdel;


    public void setMvpModel(M mvpModel) {
        fragmentMpdel = mvpModel;
    }

    public M getFragmentMpdel() {
        return fragmentMpdel;
    }

    @Override
    public void onDestroyPersenter() {
        super.onDestroyPersenter();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    public void reqSuccessful(int id, T baseResponse) {
        switch (id) {
            case Common.REQ_LIST:
                if (getFragmentMpdel().reqPageNo() == getMvpView().startPageNo()) {

                    getMvpView().getPageAdapter().setData((List) baseResponse);
                } else {
                    getMvpView().getPageAdapter().addData((List) baseResponse);
                }
                if (((List) baseResponse).size() == Common.PAGE_SIZE) {
                    getMvpView().getContentLayout().getRecyclerView().setPage(getFragmentMpdel().reqPageNo(), fragmentMpdel.reqPageNo() + 1);
                } else {
                    getMvpView().getContentLayout().getRecyclerView().setPage(getFragmentMpdel().reqPageNo(), fragmentMpdel.reqPageNo());
                }

                if (getMvpView().getPageAdapter().getItemCount() < 1) {
                    getMvpView().getContentLayout().notifyEmpty();
                    return;
                }
                break;
        }
    }

    public void reqErro(int id, Object baseResponse, String err) {
        switch (id) {
            case Common.REQ_LIST:
                getMvpView().getContentLayout().notifyEmpty();
                getMvpView().getContentLayout().refreshState(false);
                getMvpView().showErrMsg(err);
                break;
        }
    }

    public void reqDelect(BaseResult result) {
        if (result.getResult() == 0) {
            getMvpView().showErrMsg(result.resultRemark);
        } else {
            getFragmentMpdel().loadData(getMvpView().startPageNo(), getMvpView().getUrl());
        }
    }

    @Override
    public PageView getMvpView() {
        return super.getMvpView();
    }

}
