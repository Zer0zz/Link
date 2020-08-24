package com.self.link.mycenter.tuan;

import android.annotation.SuppressLint;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.self.link.R;
import com.self.link.base.Constant;
import com.self.link.daka.tuanke.dto.CoursePunchListDto;

import java.util.List;

public class MyTuankeAdapter extends BaseQuickAdapter<CoursePunchListDto, BaseViewHolder> {

    public MyTuankeAdapter(int layoutResId, @Nullable List<CoursePunchListDto> data) {
        super(layoutResId, data);
    }


    public MyTuankeAdapter(int layoutResId) {
        super(layoutResId);
    }

    @Override
    protected void convert(BaseViewHolder helper, CoursePunchListDto item) {
        helper.setText(R.id.title_tv, item.courseName);
        helper.setText(R.id.subtitle_tv, "开始时间:" + item.startDate.substring(5) + ", 距离:" + item.distance+"km");
        ImageView pic_img = helper.getView(R.id.pic_img);
        Glide.with(mContext).load(item.courseImage).into(pic_img);
        TextView operate_caurse_tv = helper.getView(R.id.oper_tv);
        if (item.isCancel == 1) {// 团课状态：1.已结束 2.进行中 3.未开始 ,
            operate_caurse_tv.setBackgroundColor(mContext.getResources().getColor(R.color.oper_bg_grey));
            operate_caurse_tv.setText("课程\n已结束");
        } else if (item.isCancel == 2) {
            operate_caurse_tv.setBackgroundColor(mContext.getResources().getColor(R.color.oper_bg_grey));
            operate_caurse_tv.setText("课程\n已开始");
//            helper.addOnClickListener(R.id.oper_tv);
        } else if (item.isCancel == 3) {
            operate_caurse_tv.setBackgroundColor(mContext.getResources().getColor(R.color.oper_bg_orange));
            operate_caurse_tv.setText("取消\n课程");
        }

        helper.addOnClickListener(R.id.oper_tv);
    }
}
