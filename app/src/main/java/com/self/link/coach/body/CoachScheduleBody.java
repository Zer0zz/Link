package com.self.link.coach.body;

import java.io.Serializable;

public class CoachScheduleBody implements Serializable {
    /**
     * coachId (string, optional): 教练id ,
     * endDate (string, optional): 结束时间 ,
     * fitnessId (string, optional): 健身房id ,
     * id (string, optional): 教练排期id ,
     * nowStatus (integer, optional): 当前状态(只包含私教，团课不允许选择)：1：已过预约时间 2：已被他人预约 3：已被自己预约 4：可以预约 ,
     * personalName ;//(string, optional): 私教课程名称 ,
     * pid (string, optional): 关联id：团课/私教 ,
     * presentation (string, optional): 私教课程描述 ,
     * startDate (string, optional): 开始时间 ,
     * status (integer, optional): 1.私教 2.团课 3.待定 ,
     * userId (string, optional): 已预约用户
     *
     * }
     */
    public String personalName;//(string, optional): 私教课程名称 ,
    public String presentation;
    public String coachId;
    public String endDate;
    public String fitnessId;
    public String id;
    public String nowStatus;
    public String pid;
    public String startDate;
    public String status;
    public String userId;

}
