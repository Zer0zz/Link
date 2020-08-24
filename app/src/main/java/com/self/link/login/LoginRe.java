package com.self.link.login;

import java.io.Serializable;

public class LoginRe implements Serializable {

    /**
     *  headPortrait (string, optional): 头像 ,
     * name (string, optional): 昵称 ,
     * sessionId (string, optional): 登录会话唯一标识 ,
     * userId (string, optional): 用户id
     */
    private String headPortrait;
    private String sessionId;
    private String name;
    private String userId;

    public String getHeadPortrait() {
        return headPortrait;
    }

    public void setHeadPortrait(String headPortrait) {
        this.headPortrait = headPortrait;
    }



    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    @Override
    public String toString() {
        return "LoginRe{" +
                "sessionId='" + sessionId + '\'' +
                ", name='" + name + '\'' +
                ", userId='" + userId + '\'' +
                '}';
    }
}
