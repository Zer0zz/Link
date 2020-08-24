package com.leconssoft.common.base;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

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
 * <p>类说明</p>
 * 简单的带标题 的基类 布局里带一个筛选框 这里不做操作
 * 如有需要可以在重写一个 针对筛选框的基类
 *
 * @author yucheng
 * @date 2018-9-25
 * @Description
 */
public abstract class BaseHeadActivity extends FragmentActivity implements EasyPermission.PermissionCallback {
    LinearLayout llContainer;
    public TextView tvTitle = null;//标题
    ImageView ivLeft = null;//左上角图片
    TextView tvLeft = null;//左上文字

    public ImageView ivRight = null;//右上图片
    TextView tvRight = null;//右上文字
    public LinearLayout headSearch_layout;
    public EditText contact_search;

    protected boolean setStatusBar = true;
    public Activity mActivity;
    private int mRequestCode;
    private String[] mPermissions;
    private PermissionCallBackM mPermissionCallBack;
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
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.act_head_baseview);
        if (setStatusBar) {
            new ZTLUtils(this).initSystemBar(this, R.color.transparent);
        }
        mActivity = this;
        ViewManager.getInstance().addActivity(this);
        llContainer = findViewById(R.id.llcontainer);
        View subView = LayoutInflater.from(this).inflate(layoutId(), null);
        llContainer.addView(subView);
        ButterKnife.bind(this, subView);
        tvTitle = findViewById(R.id.tvTitle);
        ivLeft = findViewById(R.id.ivLeft);
        tvLeft = findViewById(R.id.tvLeft);
        tvRight = findViewById(R.id.tvRight);
        ivRight = findViewById(R.id.ivRight);
        headSearch_layout = findViewById(R.id.headSearch_layout);
        contact_search = findViewById(R.id.contact_search);
        ivLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setIvLeftOnclick();
                onBackPressed();
            }
        });
        tvLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        tvRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setOnClickTvRight();
            }
        });
        ivRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setOnClickIvRight();
            }
        });
        initUIData();
    }

    protected void setIvLeftOnclick() {
    }


    public abstract int layoutId();

    protected abstract void initUIData();


    protected String getLogTag() {
        return getClass().getName();
    }

    public void hideIvLeft(int code) {
        ivLeft.setVisibility(code);
    }

    /**
     * @param title 添加标题
     */
    public void setTvTitle(String title) {
        tvTitle.setText(title);
    }

    /**
     * 点击右侧图片
     */
    public void setOnClickIvRight() {

    }

    public void hideIvRight(int code) {
        ivRight.setVisibility(code);
    }

    public void setIvLeftSrc(int drawable) {
        ivLeft.setImageResource(drawable);
    }

    public void setIvRightSrc(int drawable) {
        ivRight.setImageResource(drawable);
    }

    /**
     * 点击右侧文字
     */
    public void setOnClickTvRight() {

    }

    public void hideTvRight(int code) {
        tvRight.setVisibility(code);
    }

    public void setTvRightMsg(String msg) {
        tvRight.setText(msg);
        tvRight.setTextColor(getResources().getColor(R.color.black));
    }

    public void setTvLeftMsg(String msg) {
        tvLeft.setText(msg);
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
            //获取完权限需要继续处理之前的逻辑时
            //重写onPermissionGrantedM方法，写上操作处理逻辑
            mPermissionCallBack.onPermissionGrantedM(requestCode, perms);
        }
    }

    @Override
    public void onEasyPermissionDenied(final int requestCode, final String... perms) {
        // rationale: Never Ask Again后的提示信息
        if (EasyPermission.checkDeniedPermissionsNeverAskAgain(this, "授权,不授权无法使用," + "设置里授权", android.R.string.ok, android.R.string.cancel, new DialogInterface.OnClickListener() {
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
