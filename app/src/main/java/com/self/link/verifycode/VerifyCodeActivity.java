package com.self.link.verifycode;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.leconssoft.common.BaseMvp.BaseMvpAct;
import com.leconssoft.common.BaseMvp.factory.CreateMvpPresenter;
import com.leconssoft.common.NetService.OnHttpCallBack;
import com.leconssoft.common.base.BaseResponse;
import com.leconssoft.common.baseUtils.ZTLUtils;
import com.self.link.R;
import com.self.link.login.LoginActivity;
import com.self.link.login.LoginModel;
import com.self.link.main.MainActivity;
import com.self.link.utils.MyTimer;
import com.wynsbin.vciv.VerificationCodeInputView;

import butterknife.BindView;
import butterknife.OnClick;

import static com.self.link.utils.CommUtils.setClickBackEffect;

@CreateMvpPresenter(VerifyCodePersenter.class)
public class VerifyCodeActivity extends BaseMvpAct<VerifyVodeIView, VerifyCodePersenter> implements VerifyVodeIView {
    //获取到的验证码
    String code;
    //用户输入的电话和昵称
    String phoneNo, nickName;
    //用户输入的验证码
    String inputCode;

    @BindView(R.id.inputcode_phone)
    TextView phone_text;

    @BindView(R.id.inputcode_next)
    Button inputcode_next;

    @BindView(R.id.vciv_code)
    VerificationCodeInputView vciv_code;

    @BindView(R.id.inputcode_time)
    TextView inputcode_time;
    private int getVerifyKeyTimelips = 60;

    @Override
    protected int layoutId() {
        return R.layout.activity_verify_code;
    }

    private Handler mHandler = new Handler();
    private MyTimer timer;
    String isRegister = "Y";

    @Override
    protected void initUIData(Bundle bundle) {
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        ZTLUtils.StatusBarLightMode(this);

        Intent intent = getIntent();

        code = intent.getStringExtra("code");
        phoneNo = intent.getStringExtra("phoneNo");
        nickName = intent.getStringExtra("nickName");
        isRegister = intent.getStringExtra("nickName");
        phone_text.setText(phoneNo);
        inputcode_next.getBackground().setAlpha(160);
        setClickBackEffect(inputcode_next);
        setReGetKeyTime();
        inputcode_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setReGetKeyTime();
                new LoginModel().sendCode2phone(isRegister, phoneNo, new OnHttpCallBack<String>() {
                    @Override
                    public void onSuccessful(int id, String o) {
                        BaseResponse baseResponse = JSONObject.parseObject(o, BaseResponse.class);
//                        code = baseResponse.getCode();
                    }

                    @Override
                    public void onFaild(int id, String o, String err) {
                        showErrMsg("获取代码失败，失败原因："+err);
                    }
                });
            }
        });
        inputcode_time.setClickable(false);

        vciv_code.setOnInputListener(new VerificationCodeInputView.OnInputListener() {
            @Override
            public void onComplete(String code) {
                Toast.makeText(VerifyCodeActivity.this, code, Toast.LENGTH_SHORT).show();
                inputCode = code;
                inputcode_next.setClickable(true);
                inputcode_next.getBackground().setAlpha(255);
            }

            @Override
            public void onInput() {
                inputCode = "";
                inputcode_next.setClickable(false);
                inputcode_next.getBackground().setAlpha(160);

            }
        });
        inputcode_next.setClickable(false);
        //清除验证码
//        view.clearCode();
    }

    private void setReGetKeyTime() {
        inputcode_time.setText("重新获取（" + getVerifyKeyTimelips + "）");
        timer = new MyTimer(getVerifyKeyTimelips);
        final int[] time = {getVerifyKeyTimelips};
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    long tempTime = timer.getOutTime();
                    if (time[0] != tempTime) {
                        time[0] = (int) tempTime;
                        mHandler.post(new Runnable() {
                            @SuppressLint("SetTextI18n")
                            @Override
                            public void run() {
                                inputcode_time.setText("重新获取（" + timer.getOutTime() + "）");
                            }
                        });
                    }

                    if (timer.getOutTime() <= 0) {
                        mHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                inputcode_time.setClickable(true);
                                inputcode_time.setText("获取验证码");
//                                inputcode_time.setTextColor(Color.parseColor("#000000"));
                            }
                        });
                        break;
                    }
                }

            }
        }).start();
    }

    @OnClick({R.id.inputcode_next})
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.inputcode_next:
                //比较输入的验证码和获取的验证码
                getMvpPresenter().setCode2Phone(code);
                break;
        }
    }

    @Override
    public String getInputCode() {
        return inputCode;
    }

    @Override
    public void doNext() {
        Intent intent = new Intent(this, LoginActivity.class);
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
        Toast.makeText(mActivity, msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (timer != null) {
            timer.shutdown();
            timer = null;
        }
    }
}
