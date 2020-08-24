package com.self.link.daka.sijiao;

import android.annotation.SuppressLint;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.self.link.R;
import com.self.link.daka.sijiao.dto.CoachFitnessPunchListDto;

import java.util.List;

/**
 *
 */
public class MySijiaoRecyclerViewAdapter extends BaseQuickAdapter<CoachFitnessPunchListDto, BaseViewHolder> {


    public MySijiaoRecyclerViewAdapter(int layoutResId, @Nullable List<CoachFitnessPunchListDto> data) {
        super(layoutResId, data);
    }

    public MySijiaoRecyclerViewAdapter(int layoutResId) {
        super(layoutResId);
    }


    @SuppressLint("ResourceAsColor")
    @Override
    protected void convert(BaseViewHolder helper, CoachFitnessPunchListDto item) {
        helper.setText(R.id.name_tv, item.name);
        helper.setText(R.id.basicInformation_tv, item.basicInformation);

        helper.setText(R.id.caurse_info_tv, item.personalName+"（距离上课还有 " + item.timeRemaining +"）");
        helper.setText(R.id.caurse_description_tv, item.address + "，" + item.distance+ "km");
        ImageView head_iv = helper.getView(R.id.head_iv);
        Glide.with(mContext).load( item.headPortrait).into(head_iv);

        TextView operate_caurse_tv=  helper.getView(R.id.operate_caurse_tv);

        if (item.isPunchCard == 1) {//1.不能打卡(不满足打卡条件：时间没到或者不在距离范围) 2.可以打卡 3.已打卡 ,
            operate_caurse_tv.setBackgroundColor(mContext.getResources().getColor(R.color.oper_bg_grey));
            operate_caurse_tv.setText("打卡");
        } else if (item.isPunchCard == 2) {
            operate_caurse_tv.setBackgroundColor(mContext.getResources().getColor(R.color.oper_bg_orange));
            operate_caurse_tv.setText("打卡");
        } else if (item.isPunchCard == 3) {
            operate_caurse_tv.setBackgroundColor(mContext.getResources().getColor(R.color.oper_bg_grey));
            operate_caurse_tv.setText("已打卡");
        }
        helper.addOnClickListener(R.id.operate_caurse_tv);
    }


}