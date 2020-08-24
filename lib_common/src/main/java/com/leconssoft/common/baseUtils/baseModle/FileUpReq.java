package com.leconssoft.common.baseUtils.baseModle;

import java.util.ArrayList;
import java.util.List;

/**
 * 附件上传的请求体
 * Created by yucheng on 2017-04-14.
 */

public class FileUpReq {

    public String relType;
    public String relId;
    public String otherRelId;
    public List<String> files=new ArrayList<>();

    public String getRelType() {
        return relType;
    }

    public void setRelType(String relType) {
        this.relType = relType;
    }

    public String getRelId() {
        return relId;
    }

    public void setRelId(String relId) {
        this.relId = relId;
    }

    public String getOtherRelId() {
        return otherRelId;
    }

    public void setOtherRelId(String otherRelId) {
        this.otherRelId = otherRelId;
    }


    public void setFileList(List<String> filePath){
        files.addAll(filePath);
    }

    public void setFile(String filePath){
        files.add(filePath);
    }

    public List<String> getFiles(){
        return files;
    }
}
