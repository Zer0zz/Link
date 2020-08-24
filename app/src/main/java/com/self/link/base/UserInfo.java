package com.self.link.base;

import java.io.Serializable;

public class UserInfo implements Serializable {
    /**
     * age (integer, optional): 年龄 ,
     * dateOfBirth (string, optional): 出生日期 ,
     * headPortrait (string, optional): 头像 ,
     * id (string, optional): 用户id ,
     * linkAccount (string, optional): link账号 ,
     * name (string, optional): 昵称 ,
     * phone (string, optional): 手机号码 ,
     * realName (string, optional): 真实姓名 ,
     * sex (integer, optional): 性别：1.男 2.女
     */

    public String id;
    public String name;
    public int sex;
    public String dateOfBirth;
    public int age;
    public String linkAccount;
    public String phone;
    public String headPortrait;
    public String realName;
}
