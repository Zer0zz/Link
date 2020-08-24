package com.self.link.notification;

import android.os.Handler;
import android.os.Looper;

import com.leconssoft.common.pageMvp.BasePageModel;

import java.util.ArrayList;
import java.util.List;

/**
 * description：
 * author：Administrator on 2020/6/1 15:30
 */
public class NotificationModel extends BasePageModel {
    NotificationPersenter mNotificationPersenter;

    Handler handler = new android.os.Handler(Looper.getMainLooper());

    public NotificationModel(NotificationPersenter notificationPersenter) {
        mNotificationPersenter = notificationPersenter;

    }


    @Override
    public void loadData(int page, String url) {
        List<NotificationResult> list = new ArrayList<>();
        NotificationResult notificationResult = new NotificationResult("消息通知", "10:54", "今天是个好日子啊 就是一个好日子啊，美国是一个大坏蛋证的坏呀  不是东旭\n明天中国就能崛起");
        list.add(notificationResult);
        list.add(notificationResult);
        list.add(notificationResult);

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        mNotificationPersenter.reqSuccessful(100, list);
                    }
                });
            }
        }).start();


    }

    @Override
    public int reqPageNo() {

        return 0;
    }
}
