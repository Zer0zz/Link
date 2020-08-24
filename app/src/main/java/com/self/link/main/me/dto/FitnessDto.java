package com.self.link.main.me.dto;

public class FitnessDto {

    /**
     * address (string, optional): 详细地址 ,
     * distance (number, optional): 距离 ,
     * id (string, optional): 健身房id ,
     * intro (string, optional): 简介 ,
     * latitude (number, optional): 纬度 ,
     * longitude (number, optional): 经度 ,
     * maturityDate (string, optional): 会员到期时间 ,
     * memberLength (integer, optional),
     * name (string, optional): 健身房名称 ,
     * status (integer, optional): 认证状态：1.审核中 2.已同意 3.已拒绝
     * surfacePlot (string, optional): 封面图
     */

    public String address;
    public String distance;
    public String id;
    public String intro;
    public String latitude;
    public String longitude;
    public String maturityDate;
    public String memberLength;
    public String name;
    public int status;
    public String surfacePlot;

}
