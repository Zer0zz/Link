package com.self.link.cancle_tuanke;

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
import com.self.link.daka.tuanke.dto.CoursePunchListDto;
import com.self.link.selectseat.SelectSeatPersenter;

import butterknife.BindView;
import butterknife.OnClick;

@CreateMvpPresenter(CancleTuankePersenter.class)
public class CancleTuankeActivity extends BaseMvpHeadAct<CancleTuankeVIew, CancleTuankePersenter> implements CancleTuankeVIew {

    private CoursePunchListDto punchListDto;
    @BindView(R.id.pic_img)
    ImageView pic_img;

    @BindView(R.id.title_tv)
    TextView title_tv;

    @BindView(R.id.subtitle_tv)
    TextView subtitle_tv;

    @BindView(R.id.distance_tv)
    TextView distance_tv;


    @Override
    public int layoutId() {
        return R.layout.activity_cancle_tuanke;
    }

    @Override
    protected void initUIData() {
        setTvLeftMsg("取消预约");
        setIvLeftSrc(R.mipmap.back_icon);
        Intent intent = getIntent();
        punchListDto = (CoursePunchListDto) intent.getSerializableExtra("CoursePunchListDto");
        if (punchListDto != null) {
            if (!TextUtils.isEmpty(punchListDto.courseImage))
                Glide.with(mActivity).load(punchListDto.courseImage).into(pic_img);

            title_tv.setText(punchListDto.courseName + " (距离上课还有" + punchListDto.timeRemaining + ")");
            subtitle_tv.setText(punchListDto.address);
            distance_tv.setText(punchListDto.distance + "km");
        }

    }

    @OnClick({R.id.canle_no_btn, R.id.cancle_comfirm_btn})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.canle_no_btn:
                finish();
                break;
            case R.id.cancle_comfirm_btn:
                comfirm();
                break;
        }

    }

    private void comfirm() {
        getMvpPresenter().cancelTheCourse(punchListDto.fitnessId, punchListDto.courseId, punchListDto.courseSeatId);

    }

    @Override
    public void showLodingDialog() {

    }

    @Override
    public void hidLodingDialog() {

    }

    @Override
    public void showErrMsg(String msg) {
        Toast.makeText(mActivity, msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onSuccess() {
        Intent intent = new Intent();
        setResult(1, intent);
        finish();

    }
}