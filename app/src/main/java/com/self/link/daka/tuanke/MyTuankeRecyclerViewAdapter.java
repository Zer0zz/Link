package com.self.link.daka.tuanke;

import android.annotation.SuppressLint;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.self.link.R;
import com.self.link.daka.tuanke.dto.CoursePunchListDto;

import java.util.List;

/**
 *
 */
public class MyTuankeRecyclerViewAdapter extends BaseQuickAdapter<CoursePunchListDto, BaseViewHolder> {


    public MyTuankeRecyclerViewAdapter(int layoutResId, @Nullable List<CoursePunchListDto> data) {
        super(layoutResId, data);
    }

    public MyTuankeRecyclerViewAdapter(int layoutResId) {
        super(layoutResId);
    }

    @SuppressLint("ResourceAsColor")
    @Override
    protected void convert(BaseViewHolder helper, CoursePunchListDto item) {
        helper.setText(R.id.title_tv, item.courseName);
        helper.setText(R.id.subtitle_tv, "开始时间：" + item.startDate.substring(5) + "," + item.distance+"km");
        ImageView pic_img = helper.getView(R.id.pic_img);

        Glide.with(mContext).load(item.courseImage).into(pic_img);

        TextView operate_caurse_tv = helper.getView(R.id.oper_tv);
        if (item.isPunchCard == 1) {//1.不能打卡(不满足打卡条件：时间没到或者不在距离范围) 2.可以打卡 3.已打卡 ,
            operate_caurse_tv.setBackgroundColor(mContext.getResources().getColor(R.color.oper_bg_grey));
            operate_caurse_tv.setText("打卡");
        } else if (item.isPunchCard == 2) {//
            operate_caurse_tv.setBackgroundColor(mContext.getResources().getColor(R.color.oper_bg_orange));
            operate_caurse_tv.setText("打卡");
//            helper.addOnClickListener(R.id.oper_tv);
        } else if (item.isPunchCard == 3) {
            operate_caurse_tv.setBackgroundColor(mContext.getResources().getColor(R.color.oper_bg_grey));
            operate_caurse_tv.setText("已打卡");
        }

        helper.addOnClickListener(R.id.oper_tv);

    }


}