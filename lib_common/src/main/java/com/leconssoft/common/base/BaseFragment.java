package com.leconssoft.common.base;

import android.content.Context;
import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import butterknife.ButterKnife;


/**
 *
 * @author yucheng
 * @date  2018/9/28 10:13
 * @Description
 */

public abstract class BaseFragment extends Fragment {

    protected BaseActivity mActivity;
    /**
     * 布局View
     */
    protected View view;
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.mActivity = (BaseActivity) context;
    }


    @Override
    public View onCreateView(LayoutInflater inflater,  ViewGroup container, Bundle savedInstanceState) {
         view = inflater.inflate(getLayoutId(), container, false);
        ButterKnife.bind(this,view);
        initUI(savedInstanceState);
        initData(savedInstanceState);
        return view;
    }


    /**
     *  frag布局
     * @return
     */
    protected abstract int getLayoutId();

    /**
     * 初始化界面元
     */
    protected abstract void initUI(Bundle bundle);

    /**
     * 初始化界面元
     */
    public void initData(Bundle bundle) {
    };
    /**
     * <给Fragment封装findViewById方法><br>
     * <封装Fragment的View变量用于findView><br>
     *
     * @param resId
     *            View资源的ID
     * @return View子资源的ID
     * @see [{@link BaseFragment}、{@link BaseFragment#findViewById(int)}、
     *      {@link View#findViewById(int)}]
     */
    protected View findViewById(int resId) {
        // View 为Fragment 的 View资源
        return view.findViewById(resId);

    }

    /**
     *
     * <判断当前Fragment是否被关闭> <判断Fragment是否正在关闭>
     *
     * @return 当前状态
     * @see [{@link Fragment#isRemoving()}]
     */
    protected boolean isFinish() {
        return isRemoving();
    }

    /**
     * 取当前Fragment的TAG标签
     *
     * @return
     */
    public String getFragTag() {
        return getClass().getSimpleName();
    }

    /**
     * 获取传参
     *
     * @return
     */
    protected Bundle getIntent() {

        return this.bundle;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    private Bundle bundle;

    public void setArgument(Bundle bundle) {
        this.bundle = bundle;
    }

}
