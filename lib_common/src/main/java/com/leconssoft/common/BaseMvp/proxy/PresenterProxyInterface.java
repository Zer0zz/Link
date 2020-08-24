package com.leconssoft.common.BaseMvp.proxy;

import com.leconssoft.common.BaseMvp.factory.PresenterMvpFactory;
import com.leconssoft.common.BaseMvp.presenter.BasePresenter;
import com.leconssoft.common.BaseMvp.view.BaseView;

/**
 * 作者：yucheng 2018/9/18 0018
 * 类描述：代理接口
 * 我们说了不能写死这个工厂，那么我们需要使用者可以自定义，
 * 那么我们还需要给使用者提供一个设置的方法，
 * 我们定义一个接口提供设置工厂、获取工厂、获取Presenter的方法，
 * 然后让V层来实现这个接口，这样V层的子类就可以通过相应的方法使用了
 */
public interface PresenterProxyInterface<V extends BaseView,P extends BasePresenter<V>> {
    /**
     * 设置创建Presenter的工厂
     * @param presenterFactory PresenterFactory类型
     */
    void setPresenterFactory(PresenterMvpFactory<V, P> presenterFactory);

    /**
     * 获取Presenter的工厂类
     * @return 返回PresenterMvpFactory类型
     */
    PresenterMvpFactory<V,P> getPresenterFactory();

    /**
     * 获取创建的Presenter
     * @return 指定类型的Presenter
     */
    P getMvpPresenter();
}
