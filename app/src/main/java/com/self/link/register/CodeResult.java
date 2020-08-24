package com.self.link.register;

import java.io.Serializable;

/**
 * description：
 * author：Administrator on 2020/6/16 20:52
 */
public class CodeResult implements Serializable {
   /* "status": 0,
            "msg": "ok",
            "code": null,
            "re": true,
            "success": true*/

    int status;
    String msg;
    String Code ;
    String re;



    boolean success;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getCode() {
        return Code;
    }

    public void setCode(String code) {
        Code = code;
    }

    public String getRe() {
        return re;
    }

    public void setRe(String re) {
        this.re = re;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }
    @Override
    public String toString() {
        return "CodeResult{" +
                "status=" + status +
                ", msg='" + msg + '\'' +
                ", Code='" + Code + '\'' +
                ", re='" + re + '\'' +
                ", success=" + success +
                '}';
    }

}
