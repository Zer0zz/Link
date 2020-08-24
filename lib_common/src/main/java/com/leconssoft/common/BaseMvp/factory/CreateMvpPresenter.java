package com.leconssoft.common.BaseMvp.factory;


import com.leconssoft.common.BaseMvp.presenter.BasePresenter;

import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * 作者：yucheng 2018/9/18 0018
 * 类描述： 标注创建Presenter的注解
 * 然后我们需要创建一个默认使用注解创建的工厂，那么首先要创建一个注解
 */
@Inherited
@Retention(RetentionPolicy.RUNTIME)
public @interface CreateMvpPresenter {
    Class<? extends BasePresenter> value();
}
