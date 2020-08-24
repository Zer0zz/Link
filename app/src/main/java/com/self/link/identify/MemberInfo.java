package com.self.link.identify;

import java.io.Serializable;

/**
 * description：
 * author：Administrator on 2020/5/24 14:28
 */
public class MemberInfo implements Serializable {

    String relname;
    String pnoneNo;
    String memberCardInfo;

    public String getRelname() {
        return relname;
    }

    public void setRelname(String relname) {
        this.relname = relname;
    }

    public String getPnoneNo() {
        return pnoneNo;
    }

    public void setPnoneNo(String pnoneNo) {
        this.pnoneNo = pnoneNo;
    }

    public String getMemberCardInfo() {
        return memberCardInfo;
    }

    public void setMemberCardInfo(String memberCardInfo) {
        this.memberCardInfo = memberCardInfo;
    }
}
