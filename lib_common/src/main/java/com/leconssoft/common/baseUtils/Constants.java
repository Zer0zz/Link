package com.leconssoft.common.baseUtils;

import android.os.Environment;

/**
 * Created by yucheng on 2017-10-31.
 */

public class Constants {

    public static boolean isDebug = false;
    public static boolean isShowLog = true;
    public static String REQUEST_URL_HD;
    public static String REQUSEST_IMG_URL;
    public static String REQUSET_IP;
    public static String STORAGE_PATH = Environment.getExternalStorageDirectory().getAbsolutePath() + "/scans";//相关文档存放路径
    public static String STORAGE_PICTURE = STORAGE_PATH + "/pic";// 相关图片存放路径
    public static String STORAGE_VIDEO = STORAGE_PATH + "/video";// 相关视频存放路径
    public static String STORAGE_FILE = STORAGE_PATH + "/file";// 相关文件存放路径

    /**
     * 请求地址
     */
    public static boolean isDebug() {
        return isDebug;
    }

    static {
        if (isDebug) {
            /**
             * 测试服务器
             */
            REQUSET_IP="http://59.175.169.110";
        } else {
            /**
             * 服务器
             */
//            REQUSET_IP="http://59.175.169.112";
            REQUSET_IP="http://cjrk.hbcic.net.cn";
        }
        REQUEST_URL_HD = REQUSET_IP+"/Ewmwz/App/AppServer.ashx/?";
        REQUSEST_IMG_URL = REQUSET_IP+"/Ewmwz/";
    }




}
