package com.self.link.daka.sijiao.dto;

import java.io.Serializable;

public class CoachFitnessPunchListDto implements Serializable {

    /**
     * address (string, optional): 健身房地址 ,
     * basicInformation (string, optional): 教练简介 ,
     * coachId (string, optional): 教练id ,
     * coachScheduleId (string, optional): 教练排期id ,
     * distance (number, optional): 距离(单位：千米) ,
     * endDate (string, optional): 结束时间 ,
     * fitnessId (string, optional): 健身房id ,
     * fitnessName (string, optional): 健身房名称 ,
     * headPortrait (string, optional): 教练头像 ,
     * isCancel (integer, optional): 私教状态：1.已结束 2.进行中 3.未开始 ,
     * isPunchCard (integer, optional): 打卡状态：1.不能打卡(不满足打卡条件：时间没到或者不在距离范围) 2.可以打卡 3.已打卡 ,
     * name (string, optional): 教练名字 ,
     * personalImage (string, optional): 私教课程图片 ,
     * personalName (string, optional): 私教课程名称 ,
     * presentation (string, optional): 私教课程描述 ,
     * startDate (string, optional): 开始时间 ,
     * timeRemaining (string, optional): 距离开始剩余时间
     */

    public String address;
    public String basicInformation;
    public String coachId;
    public String coachScheduleId;
    public String distance;
    public String endDate;
    public String fitnessId;
    public String fitnessName;
    public String headPortrait;
    public int isPunchCard;
    public String name;
    public String personalImage;
    public String timeRemaining;
    public String personalName;
    public String isCancel;
    public String presentation;
    public String startDate;
}
