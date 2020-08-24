package com.self.link.coach;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;
import com.bumptech.glide.Glide;
import com.leconssoft.common.BaseMvp.BaseMvpHeadAct;
import com.leconssoft.common.BaseMvp.factory.CreateMvpPresenter;
import com.leconssoft.common.widgt.RoundImageView;
import com.self.link.R;
import com.self.link.base.Constant;
import com.self.link.coach.body.CoachDetailBody;
import com.self.link.coach.body.CoachScheduleBody;
import com.self.link.coach.body.ScheduleData;
import com.self.link.course.CourseActivity;
import com.self.link.utils.wrapper.CourseView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

import static com.self.link.fitness.FitnessActivity.FITNESS_NAME;
import static com.self.link.utils.DateUtils.StringToDate;
import static com.self.link.utils.DateUtils.betweenDate;
import static com.self.link.utils.DateUtils.dateToWeek;
import static com.self.link.utils.DateUtils.getMonthSpell;
import static com.self.link.utils.DateUtils.getNextWeekStr;
import static com.self.link.utils.DateUtils.getTimeByCalendar;
import static com.self.link.utils.DateUtils.getWeekDayStr;

@CreateMvpPresenter(CoachPersenter.class)
public class CoachActivity extends BaseMvpHeadAct<CoachIView, CoachPersenter> implements CoachIView {

    @BindView(R.id.course_table)
    CourseView course_table;

    @BindView(R.id.course_comfirm_btn)//上一周
    Button comfirm_btn_up;
    @BindView(R.id.course_cancle_btn)//下一周
    Button cancle_btn_down;

    @BindView(R.id.coach_name)
    TextView coach_name;

    @BindView(R.id.upload_head_iv)
    RoundImageView head_iv;

    @BindView(R.id.description_tv)
    TextView description_tv;

    @BindView(R.id.month_tv)
    TextView month_tv;

    @BindView(R.id.day1_tv)
    TextView day1_tv;
    @BindView(R.id.day2_tv)
    TextView day2_tv;
    @BindView(R.id.day3_tv)
    TextView day3_tv;
    @BindView(R.id.day4_tv)
    TextView day4_tv;
    @BindView(R.id.day5_tv)
    TextView day5_tv;
    @BindView(R.id.day6_tv)
    TextView day6_tv;
    @BindView(R.id.day7_tv)
    TextView day7_tv;

    public static final String COACH_ID = "CoachId";
    public static final String COACH_NAME = "CoachName";
    String coachName;
    String coachId;
    String fitnessName;
    List<CoachScheduleBody> mCoachSchedules;
    public static int requestCode = 0x10;
    CoachDetailBody mCoachDetail;
    private int dateMode = 1;//1、从当天起的下一周，2、本周

    private boolean isNextWeek = false;

    @Override
    public int layoutId() {
        return R.layout.activity_coach;
    }

    @OnClick({R.id.course_comfirm_btn, R.id.course_cancle_btn})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.course_comfirm_btn:
                preWeek();
                break;
            case R.id.course_cancle_btn:
                nextWeek();
                break;
        }
    }


    @Override
    protected void initUIData() {
        Intent intent = getIntent();
        fitnessName = intent.getStringExtra(FITNESS_NAME);
        coachId = intent.getStringExtra(COACH_ID);
        coachName = intent.getStringExtra(COACH_NAME);

        EventBus.getDefault().register(this);

        initDate(getWeekDayStr());

        //初始状态的  上一周和下一周
        comfirm_btn_up.setClickable(false);
        comfirm_btn_up.getBackground().setAlpha(160);
        cancle_btn_down.setClickable(true);

        setTvLeftMsg(fitnessName + "（" + coachName + "）");
        setIvLeftSrc(R.mipmap.back_icon);
        coach_name.setText(coachName);

        course_table.setOnCourseClickListener(new CourseView.OnCourseClickListener() {
            @Override
            public void onCourseClick(ScheduleData scheduleData) {
                Intent intent = new Intent(CoachActivity.this, CourseActivity.class);
                if (mCoachSchedules != null) {
                    if (scheduleData.status == 4) {//1：已过预约时间 2：已被他人预约 3：已被自己预约 4：可以预约
                        for (CoachScheduleBody coachScheduleBody : mCoachSchedules) {
                            if (scheduleData.coachScheduleId.equals(coachScheduleBody.id)) {
                                intent.putExtra("scheduleData", coachScheduleBody);
                                intent.putExtra("coachDetail", mCoachDetail);
                                startActivityForResult(intent, requestCode);
                                break;
                            }
                        }
                    } else if (scheduleData.status == 2) {
                        showErrMsg("该私教课被其他小伙伴预约啦！");
                    } else if (scheduleData.status == 3) {
                        showErrMsg("您已经预约过啦！");
                    } else if (scheduleData.status == 1) {
                        showErrMsg("课程已过预约时间啦，请预约其他课程哟！");
                    }

                }

            }
        });

        getMvpPresenter().getCoachDetail(coachId);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private void nextWeek() {
        initDate(getNextWeekStr());
        isNextWeek = true;
        //初始状态的  上一周和下一周
        cancle_btn_down.setClickable(false);
        cancle_btn_down.getBackground().setAlpha(160);
        comfirm_btn_up.setClickable(true);
        comfirm_btn_up.getBackground().setAlpha(255);

        getMvpPresenter().getCoachDetail(coachId,getNextWeekStr().get(0),getNextWeekStr().get(6));
    }

    private void preWeek() {
        initDate(getWeekDayStr());
        isNextWeek = false;
        //初始状态的  上一周和下一周
        comfirm_btn_up.setClickable(false);
        comfirm_btn_up.getBackground().setAlpha(160);
        cancle_btn_down.setClickable(true);
        cancle_btn_down.getBackground().setAlpha(255);
        getMvpPresenter().getCoachDetail(coachId);
    }

    private void initDate(List<String> dateStr) {
        //yyyy-MM-dd
//        List<String> dateStr = getWeekDayStr();//获取本周的格式话日期串

        String month = dateStr.get(0).substring(5, 7);
        int mon = Integer.parseInt(month);

        String monSpell = getMonthSpell(mon);
        month_tv.setText(mon + "月\n" + monSpell);
        int dayInt[] = new int[7];
        for (int i = 0; i < dateStr.size(); i++) {
            String dayS = dateStr.get(i).substring(8);
            dayInt[i] = Integer.parseInt(dayS);
        }
        day1_tv.setText("周一\n" + dayInt[0] + "号");

        day2_tv.setText("周二\n" + dayInt[1] + "号");

        day3_tv.setText("周三\n" + dayInt[2] + "号");

        day4_tv.setText("周四\n" + dayInt[3] + "号");

        day5_tv.setText("周五\n" + dayInt[4] + "号");

        day6_tv.setText("周六\n" + dayInt[5] + "号");

        day7_tv.setText("周日\n" + dayInt[6] + "号");

    }

    @Subscribe(threadMode = ThreadMode.MAIN, priority = 1, sticky = false)
    public void setCoachDetail(CoachDetailBody coachDetail) {
        mCoachDetail = coachDetail;
        description_tv.setText(coachDetail.basicInformation);
        String path = Constant.RequestUrl.imageBaseUrl + coachDetail.headPortrait;
        Glide.with(mActivity).load(path).into(head_iv);
//        List<CoachScheduleBody> coachSchedules = JSONObject.parseArray(coachDetail.coachScheduleDtoList, CoachScheduleBody.class);
    }

    @Override
    public void showLodingDialog() {
        showProgress();
    }

    @Override
    public void hidLodingDialog() {
        hidLodingDialog();
    }

    @Override
    public void showErrMsg(String msg) {
        Toast.makeText(mActivity, msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void setScheduleData(List<CoachScheduleBody> coachSchedules) {
        List<ScheduleData> scheduleDatas = new ArrayList<>();
        mCoachSchedules = coachSchedules;
        for (CoachScheduleBody coachSchedule : coachSchedules) {
            ScheduleData scheduleData = new ScheduleData();
            if (!TextUtils.isEmpty(coachSchedule.nowStatus)) {
                scheduleData.status = Integer.parseInt(coachSchedule.nowStatus);
            }
            Date startDate = StringToDate(coachSchedule.startDate);
            Date endDate = StringToDate(coachSchedule.endDate);
            betweenDate(endDate, startDate);

            scheduleData.personalName = coachSchedule.personalName;
            scheduleData.day = dateToWeek(startDate);//从0开始
            scheduleData.timeStartPoint = getTimeByCalendar(startDate);
            scheduleData.timeLenth = betweenDate(endDate, startDate);

            scheduleData.coachScheduleId = coachSchedule.id;
            scheduleData.coourseId = coachSchedule.pid;
            scheduleData.coachName = coachName;
            scheduleDatas.add(scheduleData);
        }
        course_table.setCoachScheduleData(scheduleDatas);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == this.requestCode) {
            if (data == null) {
                return;
            }
            CoachScheduleBody coachScheduleBody = (CoachScheduleBody) data.getSerializableExtra("scheduleData");
//            List<CoachScheduleBody> coachSchedules;

            if (mCoachSchedules != null) {
                for (int i = 0; i < mCoachSchedules.size(); i++) {
                    CoachScheduleBody scheduleBody = mCoachSchedules.get(i);
                    if (scheduleBody.id.equals(coachScheduleBody.id)) {
                        scheduleBody.nowStatus = "3";//3：已被自己预约
                        mCoachSchedules.remove(i);
                        mCoachSchedules.add(i, scheduleBody);
                    }
                }
                setScheduleData(mCoachSchedules);
            }


        }
    }
}
