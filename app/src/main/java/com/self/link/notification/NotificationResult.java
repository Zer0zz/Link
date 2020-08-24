package com.self.link.notification;

import java.io.Serializable;

/**
 * description：
 * author：Administrator on 2020/6/1 15:29
 */
public class NotificationResult implements Serializable {

    private String title;
    private String time;

    private String content;

    public NotificationResult() {

    }


    public NotificationResult(String title, String time, String content) {
        this.title = title;
        this.time = time;
        this.content = content;
    }


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return "NotificationResult{" +
                "title='" + title + '\'' +
                ", time='" + time + '\'' +
                ", content='" + content + '\'' +
                '}';
    }
}
