package com.self.link.personal.change_info;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.leconssoft.common.BaseMvp.BaseMvpHeadAct;
import com.leconssoft.common.BaseMvp.factory.CreateMvpPresenter;
import com.self.link.R;
import com.self.link.base.UserInfo;
import com.self.link.utils.CommUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

@CreateMvpPresenter(ChangePersonInfoPersenter.class)
public class ChangePersonInfoActivity extends BaseMvpHeadAct<ChangePersonInfoView, ChangePersonInfoPersenter> implements ChangePersonInfoView {

    private int type;
    private String[] titles = {"更新昵称", "更新真实姓名"};
    //    List<String> titles;
    private UserInfo userInfo;

    @BindView(R.id.change_et)
    EditText change_et;

    @Override
    public int layoutId() {
        return R.layout.activity_change_person_info;
    }

    @Override
    protected void initUIData() {
        Intent intent = getIntent();
        type = intent.getIntExtra("type", 0);
        userInfo = (UserInfo) intent.getSerializableExtra("userinfo");
        if (type == 0) {
            change_et.setText(userInfo.name);
        } else if (type == 1) {
            change_et.setText(userInfo.realName);
        }

        setTvLeftMsg(titles[type]);
        setIvLeftSrc(R.mipmap.back_icon);
        hideTvRight(View.VISIBLE);
        setTvRightMsg("完成");
    }

    @Override
    public void setOnClickTvRight() {
        super.setOnClickTvRight();

        String changeTextStr = change_et.getText().toString();
        if (TextUtils.isEmpty(changeTextStr)) {
            CommUtils.setRequestFocusWarming(change_et, "内容不能为空！");
            return;
        }
        if (type == 0) {
            if (!(changeTextStr.equals(userInfo.name)))
                userInfo.name = change_et.getText().toString();
            else {
                CommUtils.setRequestFocusWarming(change_et, "昵称没有改变！");
                return;
            }
        } else if (type == 1) {
            if (!(changeTextStr.equals(userInfo.realName)))
                userInfo.realName = change_et.getText().toString();
            else {
                CommUtils.setRequestFocusWarming(change_et, "真实姓名没有改变！");
                return;
            }
        }
        getMvpPresenter().updateUserInfo(userInfo);

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
    public void uploadSuccess() {
        if (type==0){
            showErrMsg("昵称更新成功！");
        }else if (type==1){
            showErrMsg("真实姓名更新成功！");
        }

        finish();
    }

    @Override
    public void uploadFialed() {
        if (type==0){
            showErrMsg("昵称更新失败！");
        }else if (type==1){
            showErrMsg("真实姓名更新失败！");
        }
    }
}