package com.self.link.selectseat;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;
import com.leconssoft.common.BaseMvp.BaseMvpHeadAct;
import com.leconssoft.common.BaseMvp.factory.CreateMvpPresenter;
import com.leconssoft.common.baseUtils.SPUtils;
import com.self.link.R;
import com.self.link.fitness.body.Attachment;
import com.self.link.selectseat.body.SeatBody;
import com.self.link.selectseat.body.SeatBodyList;
import com.self.link.utils.wrapper.SeatView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

import static com.self.link.base.Constant.SpKeyName.userId;
import static com.self.link.fitness.FitnessActivity.FITNESS_ID;

@CreateMvpPresenter(SelectSeatPersenter.class)
public class SelectSeatActivity extends BaseMvpHeadAct<SelectSeatIView, SelectSeatPersenter> implements SelectSeatIView {

    @BindView(R.id.seat)
    SeatView seatView;
    @BindView(R.id.seat_certain_tv)
    TextView seat_certain_tv;

    @BindView(R.id.seat_comfirm_btn)
    Button seat_comfirm_btn;//确认
    @BindView(R.id.seat_cancle_btn)
    Button seat_cancle_btn;//弃选

    private static final String COURSE_ID = "courseId";
    private static final String COURSE_NAME = "courseName";
    private static final String FITNESS_NAME = "fitnessName";
    String courseId;
    String courseName;
    String fitnessName;
    String fitnessId;
    int[][] data = new int[10][10];

    String mySeatSerialNumber;
    List<SeatBody> mSeatBodys;

    @Override
    public int layoutId() {
        return R.layout.activity_select_seat;
    }

    @Override
    protected void initUIData() {

        Intent intent = getIntent();
        courseId = intent.getStringExtra(COURSE_ID);
        courseName = intent.getStringExtra(COURSE_NAME);
        fitnessName = intent.getStringExtra(FITNESS_NAME);
        fitnessId = intent.getStringExtra(FITNESS_ID);
        setTvLeftMsg(fitnessName + "（" + courseName + "）");
        setIvLeftSrc(R.mipmap.back_icon);
        seatView.setData(data);
        EventBus.getDefault().register(this);
        loadData(courseId);
    }

    @OnClick({R.id.seat_cancle_btn, R.id.seat_comfirm_btn})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.seat_cancle_btn:
                if (!TextUtils.isEmpty(mySeatSerialNumber)) {
                    String courseSeatId = "";
                    for (int i = 0; i < mSeatBodys.size(); i++) {
                        if (mSeatBodys.get(i).serialNumber.equals(mySeatSerialNumber + "")) {
                            courseSeatId = mSeatBodys.get(i).id;
                            break;
                        }
                    }
                    getMvpPresenter().cancelTheCourse(fitnessId, courseId, courseSeatId, mySeatSerialNumber);
                } else {
                    showErrMsg("您没有预约过该团课！");
                }

                break;
            case R.id.seat_comfirm_btn:
                int SeatSerialNumber = seatView.getSeatSerialNumber();
                String courseSeatId = "";
                if (SeatSerialNumber != 0) {
                    for (int i = 0; i < mSeatBodys.size(); i++) {
                        if (mSeatBodys.get(i).serialNumber.equals(SeatSerialNumber + "")) {
                            courseSeatId = mSeatBodys.get(i).id;
                            break;
                        }
                    }

                    getMvpPresenter().curriculaVariable(fitnessId, courseId, courseSeatId,SeatSerialNumber+"");
                } else {
                    showErrMsg("请选择座位");
                }
                break;
        }
    }

    private void loadData(String courseId) {
        getMvpPresenter().getCourseSeatData(courseId);

    }


    @SuppressLint("SetTextI18n")
    @Subscribe(threadMode = ThreadMode.MAIN, priority = 1, sticky = false)
    public void setUIData(SeatBodyList seatBodyList) {
        if (seatBodyList == null)
            return;

        String presentation = seatBodyList.presentation;

        presentation+= "\n开始时间："+seatBodyList.startDate;
        presentation+= "\n结束时间："+seatBodyList.endDate;
        Attachment attachment = JSONObject.parseObject(seatBodyList.attachment, Attachment.class);
        List<SeatBody> seatBodys = JSONObject.parseArray(seatBodyList.courseSeatList, SeatBody.class);

        seat_certain_tv.setText(presentation);
        mSeatBodys = seatBodys;
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                SeatBody seatBody = seatBodys.get(i * 10 + j);
                int status = seatBody.status;
                String userid = seatBody.userId;
                data[i][j] = status;
                if (userid != null) {
                    data[i][j] = 3;
                }
                if (SPUtils.getStringValue(userId).equals(userid)) {
                    data[i][j] = 4;
                    mySeatSerialNumber = seatBody.serialNumber;
                }


//                if (1 == status) {//未选中 可以选择的
//                    data[i][j - 1] = status;
//                } else if (2 == status) {// 空位置 不画
//                    data[i][j - 1] = 2;
//                } else if (status == 3) { //已经被选中
//                    data[i][j - 1] = 3;
//                } else if (status == 4) { //你的选择
//
//                }
            }
        }
        seatView.setData(data);
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
    public void cancleSuccess(String seatSerialNumber) {
        mySeatSerialNumber = "";
        int status = Integer.parseInt(seatSerialNumber)-1;
        int i = status / 10;
        int j = status % 10;

        data[i][j] = 1;
        seatView.clearMySelect();
        seatView.setData(data);
    }

    @Override
    public void selectSuccess(String seatSerialNumber) {
        mySeatSerialNumber =seatSerialNumber;
        int status = Integer.parseInt(seatSerialNumber)-1;
        int i = status / 10;
        int j = status % 10;
        data[i][j] =4;
        seatView.setData(data);
    }
}
