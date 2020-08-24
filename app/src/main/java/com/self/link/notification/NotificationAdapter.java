package com.self.link.notification;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.leconssoft.common.recyclerView.SimpleRecAdapter;
import com.self.link.R;

import java.util.List;

import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * description：
 * author：Administrator on 2020/6/1 15:50
 */
public class NotificationAdapter extends SimpleRecAdapter<NotificationResult, NotificationAdapter.ViewHolder> {


    public NotificationAdapter(Context context) {
        super(context);
    }


    @Override
    public ViewHolder newViewHolder(View itemView) {
        return new ViewHolder(itemView);
    }

    @Override
    public int getLayoutId() {
        return R.layout.notification_item;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {

        NotificationResult notificationResult = data.get(position);
        viewHolder.notiTitle_tv.setText(notificationResult.getTitle());
        viewHolder.notiTime_tv.setText(notificationResult.getTime());
        viewHolder.notiContent_tv.setText(notificationResult.getContent());
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.notification_title_tv)
        TextView notiTitle_tv;
        @BindView(R.id.notification_time_tv)
        TextView notiTime_tv;
        @BindView(R.id.notification_content_tv)
        TextView notiContent_tv;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
