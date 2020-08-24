package com.leconssoft.common.BaseMvp.view;

/**
 * 作者：yucheng 2018/9/18 0018
 * 类描述：
 */
public interface BaseView  {
     void showLodingDialog();


    void hidLodingDialog();


    void showErrMsg(String msg);
}
