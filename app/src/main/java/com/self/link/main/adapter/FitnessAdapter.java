package com.self.link.main.adapter;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.leconssoft.common.recyclerView.SimpleRecAdapter;
import com.self.link.R;
import com.self.link.main.home.FitnessBody;
import com.self.link.main.me.dto.FitnessDto;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.droidlover.xrecyclerview.RecyclerItemCallback;

public class FitnessAdapter extends SimpleRecAdapter<FitnessDto, FitnessAdapter.ViewHolder> {
    public static final int TAG_VIEW = 1;
    Activity mContext;

    public FitnessAdapter(Activity context) {
        super(context);
        this.mContext = context;
    }


    @Override
    public ViewHolder newViewHolder(View itemView) {
        return new FitnessAdapter.ViewHolder(itemView);
    }

    @Override
    public int getLayoutId() {
        return R.layout.fitness_item_layout;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {
        final FitnessDto fitnessBody = data.get(position);
        viewHolder.title_tv.setText(fitnessBody.name);
        viewHolder.subtitle_tv.setText(fitnessBody.address);
        viewHolder.distance_tv.setText(fitnessBody.distance);

        if (!TextUtils.isEmpty(fitnessBody.surfacePlot) && !mContext.isFinishing()) {
            Glide.with(mContext).load(fitnessBody.surfacePlot).into(viewHolder.pic_img);
        }

        viewHolder.itemView.setOnClickListener(view -> {
            if (getRecItemClick() != null) {
                getRecItemClick().onItemClick(position, fitnessBody, TAG_VIEW, viewHolder);
            }
        });
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.title_tv)
        TextView title_tv;

        @BindView(R.id.subtitle_tv)
        TextView subtitle_tv;

        @BindView(R.id.distance_tv)
        TextView distance_tv;

        @BindView(R.id.pic_img)
        ImageView pic_img;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
