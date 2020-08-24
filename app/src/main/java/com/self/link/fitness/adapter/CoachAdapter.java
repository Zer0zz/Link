package com.self.link.fitness.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.leconssoft.common.recyclerView.SimpleRecAdapter;
import com.self.link.R;
import com.self.link.fitness.body.CoachBody;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * <p>类说明</p>
 *
 * @author zhang-chenglin
 * @date 2018/9/26 14:17
 * @Description
 */
public class CoachAdapter extends SimpleRecAdapter<CoachBody, CoachAdapter.ViewHolder> {
    public static final int TAG_VIEW = 0;
    Context mContext;

    public CoachAdapter(Context context) {
        super(context);
        this.mContext = context;
    }

    @Override
    public ViewHolder newViewHolder(View itemView) {
        return new ViewHolder(itemView);
    }

    @Override
    public int getLayoutId() {
        return R.layout.coach_item_layout;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final CoachBody coachBody = data.get(position);
        if (!TextUtils.isEmpty(coachBody.headPortrait)) {
            Glide.with(mContext).load(coachBody.headPortrait).into(holder.head_iv);
            //applyDefaultRequestOptions
        }
        holder.name_tv.setText(coachBody.name);
        holder.totalClassHour_tv.setText(coachBody.totalClassHour);
        holder.totalStudent_tv.setText(coachBody.totalStudent);
        holder.basicInformation_tv.setText(coachBody.basicInformation);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (getRecItemClick() != null) {
                    getRecItemClick().onItemClick(position, coachBody, TAG_VIEW, holder);
                }
            }
        });
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.head_iv)
        ImageView head_iv;
        @BindView(R.id.name_tv)
        TextView name_tv;
        @BindView(R.id.basicInformation_tv)
        TextView basicInformation_tv;
        @BindView(R.id.totalClassHour_tv)
        TextView totalClassHour_tv;
        @BindView(R.id.totalStudent_tv)
        TextView totalStudent_tv;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
