package com.leconssoft.common.BaseMvp.factory;

import android.util.Log;

import com.leconssoft.common.BaseMvp.presenter.BasePresenter;
import com.leconssoft.common.BaseMvp.view.BaseView;

/**
 * 作者：yucheng 2018/9/18 0018
 * 类描述：Presenter工厂实现类
 */
public class PresenterMvpFactoryImpl<V extends BaseView, P extends BasePresenter<V>>
        implements PresenterMvpFactory<V,P> {
    /**
     * 需要创建的Presenter的类型
     */
    private final Class<P> mPresenterClass;


    private PresenterMvpFactoryImpl(Class<P> presenterClass) {
        this.mPresenterClass = presenterClass;
    }

    @Override
    public P createMvpPresenter() {
        try {
            return mPresenterClass.newInstance();
        } catch (Exception e) {
            Log.e("-=========",e.toString());
            throw new RuntimeException("Presenter创建失败!，检查是否声明了@CreatePresenter(xx.class)注解");
        }
    }

    /**
     * 根据注解创建Presenter的工厂实现类
     * @param viewClazz 需要创建Presenter的V层实现类
     * @param <V> 当前View实现的接口类型
     * @param <P> 当前要创建的Presenter类型
     * @return 工厂类
     */
    public static <V extends BaseView, P extends BasePresenter<V>> PresenterMvpFactoryImpl<V,P> createFactory(Class<?> viewClazz){
        CreateMvpPresenter annotation = viewClazz.getAnnotation(CreateMvpPresenter.class);
        Class<P> aClass = null;
        if(annotation != null){
            aClass = (Class<P>) annotation.value();
        }
        return aClass == null ? null : new PresenterMvpFactoryImpl<V, P>(aClass);
    }
}
