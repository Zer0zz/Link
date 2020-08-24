package com.self.link.login;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.leconssoft.common.BaseMvp.BaseMvpAct;
import com.leconssoft.common.BaseMvp.factory.CreateMvpPresenter;
import com.leconssoft.common.baseUtils.ZTLUtils;
import com.self.link.R;
import com.self.link.main.MainActivity;
import com.self.link.register.RegisterActivity;
import com.self.link.utils.CommUtils;
import com.self.link.utils.MyTimer;
import com.self.link.verifycode.VerifyCodeActivity;

import butterknife.BindView;
import butterknife.OnClick;

import static com.self.link.utils.CommUtils.setClickBackEffect;
import static com.self.link.utils.CommUtils.setRequestFocusWarming;

@CreateMvpPresenter(LoginPersenter.class)
public class LoginActivity extends BaseMvpAct<LoginIView, LoginPersenter> implements LoginIView {

    @BindView(R.id.phone_no_et)
    EditText phone_no_et;
    @BindView(R.id.get_code_ed)
    EditText get_code_ed;

    @BindView(R.id.get_code_tv)
    TextView get_code_tv;
    @BindView(R.id.denglu)
    Button denglu;

    @BindView(R.id.zhuce)
    TextView zhuce;
    private MyTimer timer;

    private int getVerifyKeyTimelips = 60;
    private Handler mHandler = new Handler();

    @Override
    protected int layoutId() {
        return R.layout.activity_login;
    }

    @Override
    protected void initUIData(Bundle bundle) {
        ZTLUtils.StatusBarLightMode(this);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        setClickBackEffect(denglu);
    }


    @Override
    public String getPhoneNo() {
        return phone_no_et.getText().toString().trim();
    }

    @Override
    public void loginSuccess() {
        finish();
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
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
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    @OnClick({R.id.get_code_tv, R.id.denglu, R.id.zhuce})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.get_code_tv:
                String phoneNo = phone_no_et.getText().toString().trim();
                if (TextUtils.isEmpty(phoneNo)) {
                    setRequestFocusWarming(phone_no_et, "请输入手机号码");
//                    showErrMsg("请输入手机号码");
                    return;
                } else {    //不为空
                    if (!CommUtils.isPhoneNum(phoneNo)) {
//                        showErrMsg("请输入正确的手机号码");
                        setRequestFocusWarming(phone_no_et, "请输入正确的手机号码");
                        return;
                    }
                }
                setReGetKeyTime();
                getMvpPresenter().setCode2Phone();
                break;
            case R.id.denglu:
                getMvpPresenter().securityCodeLogin(phone_no_et.getText().toString().trim(), get_code_ed.getText().toString().trim());
                break;
            case R.id.zhuce:
                zhuce();
                break;

        }

    }

    private void zhuce() {
//        finish();
        Intent intent = new Intent(this, RegisterActivity.class);
        startActivity(intent);
    }


    //重置获取验证码的按钮
    @Override
    public void resetGetCodeTv(){
        timer.shutdown();
        get_code_tv.setClickable(true);
        get_code_tv.setText("获取验证码");
        get_code_tv.setTextColor(getResources().getColor(R.color.black));
    }
    private void setReGetKeyTime() {
        get_code_tv.setClickable(false);
        get_code_tv.setText("重新获取（" + getVerifyKeyTimelips + "）");
        timer = new MyTimer(getVerifyKeyTimelips);
        final int[] time = {getVerifyKeyTimelips};
        new Thread(new Runnable() {
            @Override
            public void run() {
                get_code_tv.setTextColor(getResources().getColor(R.color.colorPrimary));
                while (true) {
                    if (timer==null)
                        break;
                    long tempTime = timer.getOutTime();
                    if (time[0] != tempTime) {
                        time[0] = (int) tempTime;
                        mHandler.post(new Runnable() {
                            @SuppressLint("SetTextI18n")
                            @Override
                            public void run() {
                                if (timer!=null){
                                    get_code_tv.setText("重新获取（" + timer.getOutTime() + "）");
                                }

                            }
                        });
                    }
                    if (timer!=null&&timer.getOutTime() <= 0) {
                        mHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                get_code_tv.setClickable(true);
                                get_code_tv.setText("获取验证码");
                                get_code_tv.setTextColor(getResources().getColor(R.color.black));
//                                inputcode_time.setTextColor(Color.parseColor("#000000"));
                            }
                        });
                        break;
                    }
                }

            }
        }).start();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (timer != null) {
            timer.shutdown();
//            timer = null;
        }
    }

}
