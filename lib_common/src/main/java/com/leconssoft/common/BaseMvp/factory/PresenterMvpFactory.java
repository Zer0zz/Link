package com.leconssoft.common.BaseMvp.factory;

import com.leconssoft.common.BaseMvp.presenter.BasePresenter;
import com.leconssoft.common.BaseMvp.view.BaseView;

/**
 * 作者：yucheng 2018/9/18 0018
 * 类描述：我们既然要采用工厂模式创建Presenter，
 *         那么我们首先来创建一个工厂接口
 */
public interface PresenterMvpFactory<V extends BaseView,P extends BasePresenter<V>> {
    /**
     * 创建Presenter的接口方法
     * @return 需要创建的Presenter
     */
    P createMvpPresenter();
}
