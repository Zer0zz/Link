package com.self.link.firstin;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;
import com.leconssoft.common.BaseMvp.model.BaseModel;
import com.leconssoft.common.NetService.OnHttpCallBack;
import com.leconssoft.common.base.BaseResponse;
import com.leconssoft.common.baseUtils.SPUtils;
import com.leconssoft.common.baseUtils.ZTLUtils;
import com.self.link.R;
import com.self.link.base.Constant;
import com.self.link.login.LoginRe;
import com.self.link.main.MainActivity;
import com.self.link.register.RegisterActivity;
import com.self.link.utils.CommUtils;
import com.self.link.utils.RSAUtils;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.self.link.base.Constant.RequsetCode.LOGIN;
import static com.self.link.base.Constant.SpKeyName.SessionId;

public class FirstInActivity extends Activity implements FirstInIView {

    @BindView(R.id.login_btn)
    Button login_btn;
    @BindView(R.id.register_btn)
    Button register_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_first_in);
        ButterKnife.bind(this);
        CommUtils.setClickBackEffect(login_btn);
        CommUtils.setClickBackEffect(register_btn);
        initUI();
    }

    protected void initUI() {
        ZTLUtils.StatusBarLightMode(this);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
    }

    @OnClick({R.id.login_btn, R.id.register_btn})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.login_btn:
//                login_btn.animate()
//                        .translationZ(15f).setDuration(300)
//                        .setListener(new AnimatorListenerAdapter() {
//                            @Override
//                            public void onAnimationEnd(Animator animation) {
//                                super.onAnimationEnd(animation);
//                                login_btn.animate()
//                                        .translationZ(1.0f).setDuration(500);
//                            }
//                        }).start();
                toLogin();
                break;
            case R.id.register_btn:
                toRegister();

                break;
        }
    }


    @Override
    public void toLogin() {
        new BaseModel().netReqModleNew.getHttp(Constant.RequestUrl.getGetpubkey, Constant.RequsetCode.GETPUBKEY,
                new OnHttpCallBack<BaseResponse>() {
                    @Override
                    public void onSuccessful(int id, BaseResponse stringBaseResponse) {
                        //                        if (!TextUtils.isEmpty(baseResponse)) {
//                    BaseResponse<LoginRe> result = JSONObject.parseObject(baseResponse, BaseResponse.class);
//                    BaseResponse<String> result = JSONObject.parseObject(baseResponse, BaseResponse.class);
                        Log.e("TAG", stringBaseResponse.toString());
                        SPUtils.saveStringValue(Constant.SpKeyName.PUBKEY, stringBaseResponse.getRe());
                        login();
                    }

                    @Override
                    public void onFaild(int id, BaseResponse stringBaseResponse, String err) {

                    }
                });

    }

    public void login() {
        String phone = "15871075295";
        String pwd = "admin";

        Log.e("pwd", SPUtils.getStringValue(Constant.SpKeyName.PUBKEY));
        try {
            pwd = RSAUtils.encryptByPublicKey(pwd, SPUtils.getStringValue(Constant.SpKeyName.PUBKEY));
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("LINK-", "密码加密失败," + e.getMessage());
        }
        Map params = new HashMap();
        params.put("password", pwd);
        params.put("phone", phone);
        new BaseModel().netReqModleNew.postJsonHttp(Constant.RequestUrl.accountLogin,
                LOGIN, params, new OnHttpCallBack<String>() {

                    @Override
                    public void onSuccessful(int id, String baseResponse) {
                        if (!TextUtils.isEmpty(baseResponse)) {
                            BaseResponse result = JSONObject.parseObject(baseResponse, BaseResponse.class);
                            Log.e("login-TAG", result.toString());
                            LoginRe loginRe = JSONObject.parseObject(result.getRe(), LoginRe.class);
                            SPUtils.saveStringValue(SessionId, loginRe.getSessionId());
                            Intent intent = new Intent(FirstInActivity.this, MainActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    }

                    @Override
                    public void onFaild(int id, String s, String err) {
                        Log.e("login-TAG", err);
                    }
                });
    }

    @Override
    public void toRegister() {
        Intent intent = new Intent(this, RegisterActivity.class);
        intent.putExtra("code", "111111");
        startActivity(intent);
    }

    @Override
    public void showLodingDialog() {

    }

    @Override
    public void hidLodingDialog() {

    }

    @Override
    public void showErrMsg(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }
}
