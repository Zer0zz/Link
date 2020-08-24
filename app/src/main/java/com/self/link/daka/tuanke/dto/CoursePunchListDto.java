package com.self.link.daka.tuanke.dto;

import java.io.Serializable;

public class CoursePunchListDto implements Serializable {

    /**
     * address (string, optional): 健身房地址 ,
     * courseId (string, optional): 课程id ,
     * courseImage (string, optional): 课程图片 ,
     * courseName (string, optional): 课程名称 ,
     * courseSeatId (string, optional): 位置id ,
     * distance (number, optional): 距离 ,
     * endDate (string, optional): 开始时间 ,
     * fitnessId (string, optional): 健身房id ,
     * fitnessName (string, optional): 健身房名称 ,
     * isCancel (integer, optional): 团课状态：1.已结束 2.进行中 3.未开始 ,
     * isPunchCard (integer, optional): 打卡状态：1.不能打卡(不满足打卡条件：时间没到或者不在距离范围) 2.可以打卡 3.已打卡 ,
     * startDate (string, optional): 开始时间 ,
     * timeRemaining (string, optional): 距离开始剩余时间
     */

    public String address;
    public String courseId;
    public String courseImage;
    public String courseName;
    public String courseSeatId;
    public double distance;
    public String endDate;
    public String fitnessId;
    public String fitnessName;
    public int isCancel;
    public int isPunchCard;
    public String startDate;
    public String timeRemaining;
}
