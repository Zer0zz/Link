package com.self.link.cancle_sijiao;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.leconssoft.common.BaseMvp.BaseMvpHeadAct;
import com.leconssoft.common.BaseMvp.factory.CreateMvpPresenter;
import com.self.link.R;
import com.self.link.mycenter.dto.PersonalCourseDto;
import com.self.link.mycenter.personal.PersonalCourseModel;

import butterknife.BindView;
import butterknife.OnClick;

@CreateMvpPresenter(CancleTheReservationPersenter.class)
public class CancleTheReservationActivity extends BaseMvpHeadAct<CancleTheReservationIView, CancleTheReservationPersenter> implements CancleTheReservationIView {

    private PersonalCourseDto personalCourseDto;

    @BindView(R.id.head_iv)
    ImageView head_iv;


    @BindView(R.id.title_tv)
    TextView title_tv;

    @BindView(R.id.subtitle_tv)
    TextView subtitle_tv;

    @BindView(R.id.distance_tv)
    TextView distance_tv;

    @BindView(R.id.name_tv)
    TextView name_tv;

    @BindView(R.id.basicInformation_tv)
    TextView basicInformation_tv;

    @Override
    public int layoutId() {
        return R.layout.activity_cancle_the_reservation;
    }

    @Override
    protected void initUIData() {
        setTvLeftMsg("取消预约");
        setIvLeftSrc(R.mipmap.back_icon);
        Intent intent = getIntent();
        personalCourseDto = (PersonalCourseDto) intent.getSerializableExtra("personalCourseDto");
        if (personalCourseDto != null) {
            title_tv.setText(personalCourseDto.personalName + " (距离上课还有" + personalCourseDto.timeRemaining + ")");
            subtitle_tv.setText(personalCourseDto.presentation);
            distance_tv.setText(personalCourseDto.distance + "km");
            if (!TextUtils.isEmpty(personalCourseDto.personalImage))
                Glide.with(mActivity).load(personalCourseDto.headPortrait).into(head_iv);

            name_tv.setText(personalCourseDto.name);
            basicInformation_tv.setText(personalCourseDto.basicInformation);
        }

    }

    @OnClick({R.id.canle_no_btn, R.id.cancle_comfirm_btn})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.canle_no_btn:
                finish();
                break;
            case R.id.cancle_comfirm_btn:
//                if (personalCourseDto.timeRemaining < 60){
//                    Toast.makeText(mActivity, "课程还有一个小时就开始啦，不能取消啦", Toast.LENGTH_SHORT).show();
//                    return;
//                }
                comfirm();
                break;
        }

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

    public void comfirm() {
        new PersonalCourseModel().cancelThereservation(personalCourseDto.coachScheduleId, new PersonalCourseModel.OnOperatePersonaltrainerListener() {
            @Override
            public void oncomplited() {
                Toast.makeText(CancleTheReservationActivity.this, "成功取消预约私教", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent();

                setResult(0, intent);
                finish();
//                TextView operate_caurse_tv = (TextView) adapter.getViewByPosition(position, R.id.operate_caurse_tv);
//                operate_caurse_tv.getBackground().setAlpha(160);
//                operate_caurse_tv.setText("取消报名");
            }

            @Override
            public void onfailed(String err) {
                Toast.makeText(CancleTheReservationActivity.this, err, Toast.LENGTH_SHORT).show();
            }
        });
    }
}