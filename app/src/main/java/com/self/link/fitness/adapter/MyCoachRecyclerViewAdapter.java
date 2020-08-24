package com.self.link.fitness.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.self.link.R;
import com.self.link.base.Constant;
import com.self.link.fitness.body.CoachBody;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * {@link RecyclerView.Adapter} that can display a {@link CoachBody}.
 */
public class MyCoachRecyclerViewAdapter extends RecyclerView.Adapter<MyCoachRecyclerViewAdapter.ViewHolder> {
    private List<CoachBody> mValues;
    private Context mContext;
    private OnItemClickListener mOnItemClickListener;

    public MyCoachRecyclerViewAdapter(List<CoachBody> items, Context context) {
        mValues = items;
        mContext = context;
    }

    public MyCoachRecyclerViewAdapter(Context context) {
        mContext = context;
    }

    public void addData(List<CoachBody> items) {
        if (mValues != null)
            mValues.addAll(items);
        else mValues = items;
        notifyDataSetChanged();
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mOnItemClickListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.coach_item_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {

        if (mValues != null) {
            holder.mItem = mValues.get(position);
            final CoachBody coachBody = mValues.get(position);
            if (!TextUtils.isEmpty(coachBody.headPortrait)) {
                String path = Constant.RequestUrl.imageBaseUrl + coachBody.headPortrait;
                Glide.with(mContext).load(path).into(holder.head_iv);
                //applyDefaultRequestOptions
            }
            holder.name_tv.setText(coachBody.name);
            holder.totalClassHour_tv.setText(coachBody.totalClassHour);
            holder.totalStudent_tv.setText(coachBody.totalStudent);
            holder.basicInformation_tv.setText(coachBody.basicInformation);
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mOnItemClickListener != null) {
                        mOnItemClickListener.onItemClick(position, coachBody, holder);
                    }
                }
            });
        }


    }

    @Override
    public int getItemCount() {
        if (mValues != null)
            return mValues.size();
        else return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public CoachBody mItem;

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


        public ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, itemView);
            mView = view;

        }
    }

    public interface OnItemClickListener {
        void onItemClick(int position, CoachBody model, ViewHolder holder);
    }
}