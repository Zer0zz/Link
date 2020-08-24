package com.self.link.course;

import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.leconssoft.common.BaseMvp.BaseMvpHeadAct;
import com.leconssoft.common.BaseMvp.factory.CreateMvpPresenter;
import com.self.link.R;
import com.self.link.base.Constant;
import com.self.link.coach.CoachActivity;
import com.self.link.coach.body.CoachDetailBody;
import com.self.link.coach.body.CoachScheduleBody;
import com.self.link.mycenter.personal.PersonalCourseModel;

import butterknife.BindView;
import butterknife.OnClick;

@CreateMvpPresenter(CourseDetailPersenter.class)
public class CourseActivity extends BaseMvpHeadAct<CourseDetailView, CourseDetailPersenter> implements CourseDetailView {


    @BindView(R.id.description_tv)
    TextView description_tv;

    @BindView(R.id.head_iv)
    ImageView head_iv;

    @BindView(R.id.title_tv)
    TextView title_tv;

    @BindView(R.id.subtitle_tv)
    TextView subtitle_tv;

    @BindView(R.id.name_tv)
    TextView name_tv;

    @BindView(R.id.basicInformation_tv)
    TextView basicInformation_tv;

//    @BindView(R.id.oper_tv)
//    TextView oper_tv;//报名

    CoachScheduleBody scheduleData;

    CoachDetailBody mCoachDetail;

    @Override
    public int layoutId() {
        return R.layout.activity_course;
    }

    @Override
    protected void initUIData() {
        Intent intent = getIntent();
        scheduleData = (CoachScheduleBody) intent.getSerializableExtra("scheduleData");
        mCoachDetail = (CoachDetailBody) intent.getSerializableExtra("coachDetail");
        String title = "私教课程";
        if (scheduleData != null)
            title = mCoachDetail.name + "私教课程（" + scheduleData.personalName + ")";
        setTvLeftMsg(title);
        setIvLeftSrc(R.mipmap.back_icon);
        initUI();
    }

    private void initUI() {
        if (scheduleData != null) {
            title_tv.setText(scheduleData.personalName);
            subtitle_tv.setText(scheduleData.presentation);
            Log.e("CourseActivity",mCoachDetail.headPortrait);
            if (scheduleData != null) {
                Glide.with(mActivity).load(Constant.RequestUrl.imageBaseUrl+mCoachDetail.headPortrait).into(head_iv);
            }
            name_tv.setText(mCoachDetail.name);
            basicInformation_tv.setText(mCoachDetail.basicInformation);
        }
    }

    @OnClick({R.id.oper_tv})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.oper_tv:
                baoming();
                break;
        }
    }

    private void baoming() {
        getMvpPresenter().subscribePersonaltrainer(scheduleData.id, new PersonalCourseModel.OnOperatePersonaltrainerListener() {
            @Override
            public void oncomplited() {
                showErrMsg("报名成功，个人中心查看您的私教！");
                Intent intent = new Intent();
                intent.putExtra("scheduleData", scheduleData);
                setResult(CoachActivity.requestCode, intent);
                finish();
            }

            @Override
            public void onfailed(String err) {
                showErrMsg(err);
            }
        });
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
        Toast.makeText(mActivity, msg, Toast.LENGTH_LONG).show();
    }
}