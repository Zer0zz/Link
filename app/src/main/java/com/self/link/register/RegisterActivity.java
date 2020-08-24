package com.self.link.register;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Handler;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.leconssoft.common.BaseMvp.BaseMvpHeadAct;
import com.leconssoft.common.BaseMvp.factory.CreateMvpPresenter;
import com.leconssoft.common.base.BaseHeadActivity;
import com.leconssoft.common.baseUtils.ZTLUtils;
import com.self.link.R;
import com.self.link.utils.CommUtils;
import com.self.link.utils.MyTimer;
import com.self.link.verifycode.VerifyCodeActivity;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.OnClick;

import static com.self.link.utils.CommUtils.setClickBackEffect;
import static com.self.link.utils.CommUtils.setRequestFocusWarming;

@CreateMvpPresenter(RegisterPersenter.class)
public class RegisterActivity extends BaseMvpHeadAct<RegisterIView, RegisterPersenter> implements RegisterIView {

    @BindView(R.id.register_btn)
    Button register_btn;

    @BindView(R.id.phone_et)
    EditText phone_et;

    @BindView(R.id.nick_name_et)
    EditText nickname_et;

    @BindView(R.id.link_account_et)
    EditText link_account_et;

    @BindView(R.id.get_code_tv)
    TextView get_code_tv;
    @BindView(R.id.get_code_ed)
    EditText get_code_et;

    private String phoneNo;
    private String nickName;
    private String code;
//    private String linkAccount;

    TextView zhuce;
    private MyTimer timer;

    private int getVerifyKeyTimelips = 60;
    private Handler mHandler = new Handler();

    @Override
    public int layoutId() {
        return R.layout.activity_register;
    }

    @Override
    protected void initUIData() {
        ZTLUtils.StatusBarLightMode(this);
        setTvLeftMsg("注册");
        setIvLeftSrc(R.mipmap.back_icon);
        setClickBackEffect(register_btn);
    }


    @OnClick({R.id.register_btn, R.id.get_code_tv})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.get_code_tv:
                phoneNo = phone_et.getText().toString().trim();
                if (!TextUtils.isEmpty(phoneNo)) {
                    if (!CommUtils.isPhoneNum(phoneNo)) {
                        showErrMsg("请输入正确的手机号码");
                        return;
                    }
                } else {
                    showErrMsg("手机号不能为空");
                    return;
                }
                setReGetKeyTime();
                getMvpPresenter().setCode2Phone(phoneNo);

                break;
            case R.id.register_btn:
                if (isOkRegisterInfo()) {
                    getMvpPresenter().register(nickName, phoneNo, code);
                }
                break;
        }
    }

    private boolean isOkRegisterInfo() {

        nickName = nickname_et.getText().toString();
        phoneNo = phone_et.getText().toString().trim();
        code = get_code_et.getText().toString().trim();
//        linkAccount = link_account_et.getText().toString();
        if (TextUtils.isEmpty(nickName)) {
            showErrMsg("请输入昵称");
            setRequestFocusWarming(nickname_et, "请输入昵称");
            return false;
        }
        if (!TextUtils.isEmpty(phoneNo)) {
            if (!CommUtils.isPhoneNum(phoneNo)) {
                showErrMsg("请输入正确的手机号码");
                return false;
            }
        } else {
            setRequestFocusWarming(phone_et, "请输入手机号码");
//            showErrMsg("请输入手机号码");
            return false;
        }
//        if (TextUtils.isEmpty(linkAccount)) {
//            setRequestFocusWarming(link_account_et, "请输入Link账号");
////            showErrMsg("请输入Link账号");
//            return false;
//        }
        if (TextUtils.isEmpty(code)) {
//            showErrMsg("请输入验证码");
            setRequestFocusWarming(get_code_et, "请输入验证码");
            return false;
        }
        return true;
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
        Toast.makeText(getApplication(), msg, Toast.LENGTH_SHORT).show();
    }


    @Override
    public void registerSucceed() {
        finish();
    }

    private void setReGetKeyTime() {
        get_code_tv.setClickable(false);
        get_code_tv.setText("重新获取（" + getVerifyKeyTimelips + "s）");
        timer = new MyTimer(getVerifyKeyTimelips);
        final int[] time = {getVerifyKeyTimelips};
        new Thread(new Runnable() {
            @Override
            public void run() {
                get_code_tv.setTextColor(getResources().getColor(R.color.colorPrimary));
                while (true) {
                    long tempTime = timer.getOutTime();
                    if (time[0] != tempTime) {
                        time[0] = (int) tempTime;
                        mHandler.post(new Runnable() {
                            @SuppressLint("SetTextI18n")
                            @Override
                            public void run() {

                                get_code_tv.setText("重新获取（" + timer.getOutTime() + "）");
                            }
                        });
                    }

                    if (timer.getOutTime() <= 0) {
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
