package com.self.link.main;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.leconssoft.common.BaseMvp.BaseMvpAct;
import com.leconssoft.common.BaseMvp.factory.CreateMvpPresenter;
import com.leconssoft.common.base.BaseFragment;
import com.leconssoft.common.base.ViewManager;
import com.leconssoft.common.baseUtils.UIHelper;
import com.leconssoft.common.baseUtils.ZTLUtils;
import com.leconssoft.common.widgt.NoScrollViewPager;
import com.self.link.R;
import com.self.link.main.home.HomeFrag;
import com.self.link.main.me.MeFrag;
import com.self.link.service.LinkService;
import com.self.link.utils.PermissionsUtils;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.ViewPager;

import butterknife.BindView;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;

@CreateMvpPresenter(MainPersenter.class)
public class MainActivity extends BaseMvpAct<MainIView, MainPersenter> implements MainIView {

    @BindView(R.id.container_pager)
    NoScrollViewPager mPager;

    @BindView(R.id.navigation)
    BottomNavigationView navigation;

    private FragmentAdapter mAdapter;

    private List<BaseFragment> mFragments;
    private MenuItem menuItem;
    private long mExitTime = 0;

    HomeFrag homeFrag;
    MeFrag meFrag;

    final int requestCode = 1111;
    String[] permissions = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION, READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE};

    @Override
    protected int layoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected void initUIData(Bundle bundle) {
        setColorTitle(R.color.transparent);
        homeFrag = new HomeFrag();
        meFrag = new MeFrag();
        ViewManager.getInstance().addFragment(0, homeFrag);
        ViewManager.getInstance().addFragment(1, meFrag);
        mFragments = ViewManager.getInstance().getAllFragment();
        mAdapter = new FragmentAdapter(getSupportFragmentManager(), mFragments);
        mPager.setPagerEnabled(false);
        mPager.setAdapter(mAdapter);
        navigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int i = item.getItemId();
                if (i == R.id.navigation_fri) {
                    mPager.setCurrentItem(0);
                    return true;
                } else if (i == R.id.navigation_sed) {
                    mPager.setCurrentItem(1);
                    return true;
                }

                return false;
            }
        });
        BottomNavigationViewHelper.disableShiftMode(navigation);

        mPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (menuItem != null) {
                    menuItem.setChecked(false);
                } else {
                    navigation.getMenu().getItem(0).setChecked(false);
                }
                menuItem = navigation.getMenu().getItem(position);
                menuItem.setChecked(true);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        getPermission();

    }


    protected void getPermission() {
        //申请权限
        PermissionsUtils.IPermissionsResult permissionsResult = new PermissionsUtils.IPermissionsResult() {
            @Override
            public void passPermissons() {
                //权限通过执行的方法
                //权限通过验证
                mActivity.startService(new Intent(mActivity, LinkService.class));
            }

            @Override
            public void forbitPermissons() {
                //这是没有通过权限的时候提示的内容，自定义即可
                showErrMsg("您没有允许部分权限，可能会导致部分功能不能正常使用，如需正常使用  请允许权限");
                finish();
            }
        };
        PermissionsUtils.getInstance().chekPermissions(this, permissions, permissionsResult);

//        int permissionCheck = getPermi();
//        if (permissionCheck != 0) {
//            ActivityCompat.requestPermissions(this, permissions, requestCode);
//        } else {
//            mActivity.startService(new Intent(mActivity, LinkService.class));
//        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            //两秒之内按返回键就会退出
            if ((System.currentTimeMillis() - mExitTime) > 2000) {
                UIHelper.ToastMessage(this, "再按一次退出程序");
                mExitTime = System.currentTimeMillis();
            } else {
                ViewManager.getInstance().exitApp(this);
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    /**
     * 设置沉浸式状态栏颜色
     *
     * @param color
     */
    public void setColorTitle(int color) {
        ZTLUtils.StatusBarLightMode(this);
        ZTLUtils.setStatusBarColor(MainActivity.this, color);
    }

    @Override
    public void showLodingDialog() {
        showProgress();
    }

    @Override
    public void hidLodingDialog() {
        hindProgress();
    }

    @Override
    public void showErrMsg(String msg) {
        Toast.makeText(mActivity, msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (mPager.getCurrentItem() == 0 && homeFrag != null) {
            homeFrag.onActivityResult(requestCode, resultCode, data);
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (mPager.getCurrentItem() == 0 && homeFrag != null) {
            homeFrag.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
        PermissionsUtils.getInstance().onRequestPermissionsResult(this, requestCode, permissions, grantResults);

    }
}
