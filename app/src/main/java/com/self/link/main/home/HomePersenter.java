package com.self.link.main.home;

import android.location.Location;

import com.leconssoft.common.BaseMvp.presenter.BasePresenter;

/**
 * description：
 * author：Administrator on 2020/5/21 10:06
 */
public class HomePersenter extends BasePresenter<HomeView> {

    HomeModle homeModle = new HomeModle(this);

    void loadBanerData(){
        homeModle.loadBanerData();
    }

    @Override
    public void onDetachMvpView() {
        super.onDetachMvpView();

    }

    //获取健身房信息
    void loadFitnessData(Location location,int pageNum,int pageSize,String vagueQuery){
        homeModle.loadFitnessData(location,pageNum,pageSize,vagueQuery);
    }
    public boolean hasNextPage() {
        return homeModle.hasNextPage();
    }
}
