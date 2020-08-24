package com.self.link.coach.body;

import java.io.Serializable;

public class CoachDetailBody implements Serializable {
    /**
     * age (integer, optional): 年龄 ,
     * basicInformation (string, optional): 简介 ,
     * coachScheduleDtoList (Array[CoachScheduleDto], optional): 时间排期 ,
     * fitnessId (string, optional): 健身房id ,
     * headPortrait (string, optional): 头像 ,
     * id (string, optional): 教练id ,
     * name (string, optional): 教练名称 ,
     * nowDate (string, optional): 当前系统时间（格式：年月日） ,
     * phone (string, optional): 手机号码 ,
     * sex (string, optional): 性别：1.男 2.女 ,
     * userId (string, optional): 当前登录用户id
     */

    public String userId;
    public String id;
    public String fitnessId;
    public String name;
    public String phone;
    public String sex;
    public int age;
    public String headPortrait;
    public String basicInformation;
    public String coachScheduleDtoList;
    public String nowDate;
}
