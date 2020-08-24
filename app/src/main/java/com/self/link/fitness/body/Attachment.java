package com.self.link.fitness.body;

public class Attachment {

    public Attachment(String name, String path) {
        this.name = name;
        this.path = path;
    }

    public Attachment() {

    }

    /**
     * createDate (string, optional),
     * creator (string, optional),
     * fileClass (string, optional): 附件分类 ,
     * id (string, optional),
     * isActive (string, optional),
     * modifier (string, optional),
     * modifyDate (string, optional),
     * name (string, optional): 文件名称 ,
     * path (string, optional): 文件路径 ,
     * pid (string, optional): 关联ID ,
     * size (string, optional): 文件大小 ,
     * subPath (string, optional): 压缩文件路径
     */
    public String id;
    public String createDate;
    public String modifyDate;
    public String creator;
    public String modifier;
    public String isActive;
    public String pid;
    public String fileClass;
    public String name;
    public String path;
    public String subPath;
    public String size;
}

