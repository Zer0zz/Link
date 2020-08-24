package com.leconssoft.common.base;

import java.io.Serializable;

public class BaseResponse implements Serializable {
    /**
     * {
     *   "status": 0,
     *   "msg": "ok",
     *   "code": null,
     *   "re": {
     *     "sessionId": "user_d43714a232584c75a9b27d559c14652e",
     *     "name": "张三",
     *     "userId": "1"
     *   },
     *   "success": true
     * }
     */

    private int status;

    private String msg;

    private int code ;

    private String re;

    private boolean success;

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

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
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
        return "BaseResponse{" +
                "status=" + status +
                ", msg='" + msg + '\'' +
                ", code='" + code + '\'' +
                ", re=" + re +
                ", success=" + success +
                '}';
    }
}
