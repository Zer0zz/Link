package com.self.link.main.me.adapter;

import android.text.TextUtils;
import android.widget.ImageView;

import androidx.annotation.LayoutRes;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.self.link.R;
import com.self.link.base.Constant;
import com.self.link.main.me.dto.FitnessDto;

import java.util.List;

public class MeAdapter extends BaseQuickAdapter<FitnessDto, BaseViewHolder> {
    public MeAdapter(@LayoutRes int layoutResId, @Nullable List<FitnessDto> data) {
        super(layoutResId, data);
    }

    public MeAdapter(@LayoutRes int layoutResId) {
        super(layoutResId);
    }

    @Override
    protected void convert(BaseViewHolder viewHolder, FitnessDto item) {
        viewHolder.setText(R.id.title_tv, item.name);
        viewHolder.setText(R.id.subtitle_tv, item.intro);
        String distance;
        if (item.status == 1) {
            distance = "未认证";
        } else if (item.status == 2) {
            distance    = "认证已过期";
        }  else if (item.status == 4) {
            distance    = "认证审核中";
        } else {//已认证
            distance = "会员到期时间：" + item.maturityDate;
        }
        viewHolder.setText(R.id.distance_tv, distance);
        ImageView iv_cover = viewHolder.getView(R.id.surfacePlot);
        if (mContext != null)
            Glide.with(mContext).load(Constant.RequestUrl.imageBaseUrl + item.surfacePlot).into(iv_cover);
    }


}
