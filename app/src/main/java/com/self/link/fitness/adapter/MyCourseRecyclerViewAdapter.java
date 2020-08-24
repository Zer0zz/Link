package com.self.link.fitness.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.fastjson.JSONObject;
import com.bumptech.glide.Glide;
import com.self.link.R;
import com.self.link.base.Constant;
import com.self.link.fitness.body.Attachment;
import com.self.link.fitness.body.CoachBody;
import com.self.link.fitness.body.CourseBody;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * {@link RecyclerView.Adapter} that can display a {@link CourseBody}.
 */
public class MyCourseRecyclerViewAdapter extends RecyclerView.Adapter<MyCourseRecyclerViewAdapter.ViewHolder> {

    private List<CourseBody> mValues;
    private Context mContext;
    private OnItemClickListener mOnItemClickListener;

    public void addData(List<CourseBody> items) {
        if (mValues != null)
            mValues.addAll(items);
        else mValues = items;
        notifyDataSetChanged();
    }

    public MyCourseRecyclerViewAdapter(Context context) {
        mContext = context;
    }

    public MyCourseRecyclerViewAdapter(List<CourseBody> items, Context context) {
        if (items != null)
            mValues = items;
        mContext = context;
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mOnItemClickListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fitness_item_layout, parent, false);
        return new ViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, int position) {
        if (mValues != null) {
            CourseBody courseBody = mValues.get(position);
            viewHolder.mItem = mValues.get(position);
            viewHolder.title_tv.setText(courseBody.name);
            viewHolder.subtitle_tv.setText(courseBody.startDate + " ~ " + courseBody.endDate);
            Attachment attachment = JSONObject.parseObject(courseBody.attachment, Attachment.class);
            if (!TextUtils.isEmpty(attachment.path)) {
                Glide.with(mContext).load(Constant.RequestUrl.imageBaseUrl + attachment.path).into(viewHolder.pic_img);
            }
            viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mOnItemClickListener != null) {
                        mOnItemClickListener.onItemClick(position, courseBody, viewHolder);
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
        public CourseBody mItem;

        @BindView(R.id.title_tv)
        TextView title_tv;

        @BindView(R.id.subtitle_tv)
        TextView subtitle_tv;

        @BindView(R.id.distance_tv)
        TextView distance_tv;

        @BindView(R.id.pic_img)
        ImageView pic_img;

        public ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, itemView);
            mView = view;
        }


    }

    public interface OnItemClickListener {
        void onItemClick(int position, CourseBody model, ViewHolder holder);
    }
}