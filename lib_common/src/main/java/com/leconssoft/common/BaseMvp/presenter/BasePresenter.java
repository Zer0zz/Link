package com.leconssoft.common.BaseMvp.presenter;

import android.os.Bundle;
import android.util.Log;

import com.leconssoft.common.BaseMvp.view.BaseView;

/**
 * 作者：yucheng 2018/9/17 0017
 * 类描述： 所有mvp中Presenter中抽象类
 * 所有Presenter的基类，并不强制实现这些方法，有需要在重写
 */
public abstract class BasePresenter<V extends BaseView> {
    /**
     * V层view
     */
    private V mView;

    /**
     * Presenter被创建后调用
     *
     * @param savedState 被意外销毁后重建后的Bundle
     */
    public  void onCreatePersenter(Bundle savedState) {
        Log.e("perfect-mvp", "P onCreatePersenter = ");
    }

    /**
     * 绑定View
     */
    public void onAttachMvpView(V mvpView) {
        mView = mvpView;
        Log.e("perfect-mvp", "P onResume");
    }

    /**
     * 解除绑定View
     */
    public void onDetachMvpView() {
        mView = null;
        Log.e("perfect-mvp", "P onDetachMvpView = ");
    }

    /**
     * Presenter被销毁时调用
     */
    public void onDestroyPersenter() {
        Log.e("perfect-mvp", "P onDestroy = ");
    }

    /**
     * 在Presenter意外销毁的时候被调用，它的调用时机和Activity、Fragment、View中
     * 的onSaveInstanceState
     * 时机相同
     *
     * @param outState
     */
    public void onSaveInstanceState(Bundle outState) {
        Log.e("perfect-mvp", "P onSaveInstanceState = ");
    }

    /**
     * 获取V层接口View
     *
     * @return 返回当前MvpView
     */
    public V getMvpView() {
        return mView;
    }



}
