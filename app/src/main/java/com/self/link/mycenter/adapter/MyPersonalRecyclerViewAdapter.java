package com.self.link.mycenter.adapter;

import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.self.link.R;
import com.self.link.base.Constant;
import com.self.link.mycenter.dto.PersonalCourseDto;

import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link com.self.link.mycenter.dto.PersonalCourseDto}.
 */
public class MyPersonalRecyclerViewAdapter extends BaseQuickAdapter<PersonalCourseDto, BaseViewHolder> {


    public MyPersonalRecyclerViewAdapter(int layoutResId, @Nullable List<PersonalCourseDto> data) {
        super(layoutResId, data);
    }

    public MyPersonalRecyclerViewAdapter(int layoutResId) {
        super(layoutResId);
    }

    @Override
    protected void convert(BaseViewHolder helper, PersonalCourseDto item) {
        helper.setText(R.id.name_tv, item.name);
        helper.setText(R.id.basicInformation_tv, item.basicInformation);
        helper.setText(R.id.caurse_info_tv, item.personalName + "（距离上课还有 " + item.timeRemaining + ")");
        helper.setText(R.id.caurse_description_tv, item.address + ",  " + item.distance + "km");
        ImageView head_iv = helper.getView(R.id.head_iv);
        Glide.with(mContext).load(item.headPortrait).into(head_iv);
        //1.已结束 2.进行中 3.未开始
        TextView operate_caurse_tv=  helper.getView(R.id.operate_caurse_tv);
        if (item.isCancel == 1) {
            operate_caurse_tv.setBackgroundColor(mContext.getResources().getColor(R.color.oper_bg_grey));
            operate_caurse_tv.setText("课程\n已结束");
        } else if (item.isCancel == 2) {
            operate_caurse_tv.setBackgroundColor(mContext.getResources().getColor(R.color.oper_bg_grey));
            operate_caurse_tv.setText("课程\n进行中");
        } else if (item.isCancel == 3) {
            operate_caurse_tv.setBackgroundColor(mContext.getResources().getColor(R.color.oper_bg_orange));
            operate_caurse_tv.setText("取消\n课程");
        }

        helper.addOnClickListener(R.id.coach_info_ll).addOnClickListener(R.id.operate_caurse_tv);
    }


}