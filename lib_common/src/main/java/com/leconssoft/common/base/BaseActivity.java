package com.leconssoft.common.base;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import android.view.KeyEvent;
import android.view.Window;

import com.leconssoft.common.R;
import com.leconssoft.common.baseUtils.Utils;
import com.leconssoft.common.baseUtils.ZTLUtils;
import com.leconssoft.common.baseUtils.csustomdialog.MainProgressDialog;
import com.leconssoft.common.baseUtils.permssion.EasyPermission;
import com.leconssoft.common.baseUtils.permssion.PermissionCallBackM;

import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;
import butterknife.ButterKnife;


/**
 * @author yucheng
 * @date 2018/9/29 10:30
 * @Description
 */
public abstract class BaseActivity extends FragmentActivity implements EasyPermission.PermissionCallback {
    public Activity mActivity;
    private int mRequestCode;
    private String[] mPermissions;
    public PermissionCallBackM mPermissionCallBack;
    public MainProgressDialog mProgressDialog;

    public void showProgress() {//显示对话框
        if (null != mProgressDialog) {
            mProgressDialog.show();
        } else {

            mProgressDialog = new MainProgressDialog(this, R.style.MainProgressDialogStyle);
            mProgressDialog.show();
        }

    }

    public void hindProgress() {         //隐藏进度条对话框
        if (null != mProgressDialog) {
            mProgressDialog.dismiss();
        }
    }

    protected final String TAG = getClass().getSimpleName();


    @Override
    protected void onCreate(@Nullable Bundle bundle) {
        super.onCreate(bundle);
        ViewManager.getInstance().addActivity(this);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        mActivity = this;
        if (statusBar()) {
            new ZTLUtils(this).initSystemBar(this,getTranslucentStatusColor());
//                    setTranslucentStatus(getTranslucentStatusColor());
        }
        ViewManager.getInstance().addActivity(this);
        setContentView(layoutId());
        ButterKnife.bind(this);
        initUIData(bundle);
        initData(bundle);
    }


    /**
     * 是否显示状态栏
     * @return
     */
    public boolean statusBar(){
        return true;
    }
    /**
     * 设置状态栏底色
     * @return
     */
    public int getTranslucentStatusColor() {

        return R.color.transparent;
    }

    protected abstract int layoutId();

    protected abstract void initUIData(Bundle bundle);

    protected void initData(Bundle bundle) {
    }

    ;

    protected String getLogTag() {
        // return this class name
        return getClass().getName();
    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onPause() {
        super.onPause();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        ViewManager.getInstance().finishActivity(this);
    }


    @Override
    protected void onRestart() {
        // TODO Auto-generated method stub
        super.onRestart();
    }

    @Override
    protected void onStart() {
        // TODO Auto-generated method stub
        super.onStart();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // TODO Auto-generated method stub
        return super.onKeyDown(keyCode, event);
    }

    // rationale: 申请授权理由
    public void requestPermission(int requestCode, String[] permissions, String rationale, PermissionCallBackM permissionCallback) {
        this.mRequestCode = requestCode;
        this.mPermissionCallBack = permissionCallback;
        this.mPermissions = permissions;

        EasyPermission.with(this).addRequestCode(requestCode).permissions(permissions).rationale(rationale).request();
    }

    @SuppressLint({"NewApi", "Override"})
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermission.onRequestPermissionsResult(this, requestCode, permissions, grantResults);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        /*
         * 从Settings界面跳转回来，标准代码，就这么写
         */
        if (requestCode == EasyPermission.SETTINGS_REQ_CODE) {
            if (EasyPermission.hasPermissions(this, mPermissions)) {
                // 已授权，处理业务逻辑
                onEasyPermissionGranted(mRequestCode, mPermissions);
            } else {
                onEasyPermissionDenied(mRequestCode, mPermissions);
            }
        }
    }

    @Override
    public void onEasyPermissionGranted(int requestCode, String... perms) {
        if (mPermissionCallBack != null) {
            mPermissionCallBack.onPermissionGrantedM(requestCode, perms);
        }
    }

    @Override
    public void onEasyPermissionDenied(final int requestCode, final String... perms) {
        // rationale: Never Ask Again后的提示信息
        if (EasyPermission.checkDeniedPermissionsNeverAskAgain(this, "无法使用,请设置权限" + "设置里授权", android.R.string.ok, android.R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (mPermissionCallBack != null) {
                    mPermissionCallBack.onPermissionDeniedM(requestCode, perms);
                }
            }
        }, perms)) {
            return;
        }

        if (mPermissionCallBack != null) {
            mPermissionCallBack.onPermissionDeniedM(requestCode, perms);
        }
    }


    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }


    /**
     * 添加fragment
     *
     * @param fragment
     * @param frameId
     */
    protected void addFragment(BaseFragment fragment, @IdRes int frameId) {
        Utils.checkNotNull(fragment);
        getSupportFragmentManager().beginTransaction().add(frameId, fragment, fragment.getClass().getSimpleName()).addToBackStack(fragment.getClass().getSimpleName()).commitAllowingStateLoss();

    }


    /**
     * 替换fragment
     *
     * @param fragment
     * @param frameId
     */
    protected void replaceFragment(BaseFragment fragment, @IdRes int frameId) {
        Utils.checkNotNull(fragment);
        getSupportFragmentManager().beginTransaction().replace(frameId, fragment, fragment.getClass().getSimpleName()).addToBackStack(fragment.getClass().getSimpleName()).commitAllowingStateLoss();

    }


    /**
     * 隐藏fragment
     *
     * @param fragment
     */
    protected void hideFragment(BaseFragment fragment) {
        Utils.checkNotNull(fragment);
        getSupportFragmentManager().beginTransaction().hide(fragment).commitAllowingStateLoss();

    }


    /**
     * 显示fragment
     *
     * @param fragment
     */
    protected void showFragment(BaseFragment fragment) {
        Utils.checkNotNull(fragment);
        getSupportFragmentManager().beginTransaction().show(fragment).commitAllowingStateLoss();

    }


    /**
     * 移除fragment
     *
     * @param fragment
     */
    protected void removeFragment(BaseFragment fragment) {
        Utils.checkNotNull(fragment);
        getSupportFragmentManager().beginTransaction().remove(fragment).commitAllowingStateLoss();

    }


    /**
     * 弹出栈顶部的Fragment
     */
    protected void popFragment() {
        if (getSupportFragmentManager().getBackStackEntryCount() > 1) {
            getSupportFragmentManager().popBackStack();
        } else {
            finish();
        }
    }

}