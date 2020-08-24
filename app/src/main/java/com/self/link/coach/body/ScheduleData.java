package com.self.link.coach.body;

import android.os.Parcelable;

import java.io.Serializable;

public class ScheduleData implements Serializable {
    public int day;//课程日期 列
    public int timeStartPoint;//课程开始时间点 行

    public int timeLenth; //课程时长（小时） lenth

    public String coachScheduleId;//课程id,
    public int status;//课程状态  1.可选 2.选中----- 3.待定 ,
    public String personalName; //课程名
    public String coourseId;
    public String coachName;

}
