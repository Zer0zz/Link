package com.leconssoft.common.baseUtils;

/**
 * Created by yucheng on 2017-10-31.
 */

public interface Common {


    /**
     * 帮助文档
     */
    String WEBVIEW_LOADURL = "webview_url";

    String WEBVIEW_TITLE = "webview_title";

    String WEBVIEW_ADVERTISEMENT = "webview_open";
    // 日志
    public static boolean LOGD_ENABLE = true;
    //是否打印到sd卡上
    public static boolean LOG_SDCARD_ENABLE = true;

    public static String LOG_FILE_NAME = "superEnntrance/log.txt";

    /**
     * 版本验证传递的参数
     */
    String APP_KEY = "84277D2B80038BCC0DD3075DBF6C4DE3";



    /**
     * 查看大图类别Id
     **/
    String KEY_CHANGE_TYPE_ID = "change_typeId";
    /**
     * 点击小图看大图的本地路径或者网络地址
     **/
    String CHECK_FILE_PATH = "check_file_path";

    /**
     * 当前图片
     **/
    String KEY_PIC_NUMBER = "weibo";


    int PERMISSIONS_REQUEST = 199;//权限获取返回
    public static final int DECODE = 1;
    public static final int DECODE_FAILED = 2;
    public static final int DECODE_SUCCEEDED = 3;
    public static final int QUIT = 5;
    public static final int RESTART_PREVIEW = 6;
    public static final int RETURN_SCAN_RESULT = 7;
    public static final int FLASH_OPEN = 8;
    public static final int FLASH_CLOSE = 9;
    public static final int REQUEST_IMAGE = 10;
    public static final String CODED_CONTENT = "codedContent";

    /*传递的zxingconfing*/

    public static final String INTENT_ZXING_CONFIG = "zxingConfig";
    //二维码扫描回调值
    int REQUEST_CODE_SCAN = 1001;

    int REQUEST_1=1;
    int REQUEST_2=2;
    int REQUEST_3=3;
    int REQUEST_4=4;
    int REQUEST_5=5;
    int REQUEST_6=6;
    int REQUEST_7=7;
    int REQUEST_8=8;
    int PAGE_SIZE = 10;
    int REQ_DATA = 1000;
    int REQ_LIST = 100;
    int REQ_DELECT = 200;
    int REQ_ADD = 300;
    int REQ_REFRESH = 400;

    String USER_PERSONID = "personid";//用户ID（主键）
    String USER_PHONE_NUM = "userName";//用户姓名
    String USER_PASSWORD = "userPassword";
    String USER_GXBHPC = "gxbhpc";//用户企业关系表主键
    String personsfzhm = "personsfzhm";//身份证号
    String persondhhm = "persondhhm";//手机号（用户帐号）
    String nickName = "nickName";//用户昵称

    String yhzh = "yhzh";//用户帐号（手机号）
    String djsj = "djsj";//登记时间
    String zhdlsj = "zhdlsj";//最后登录时间
    String type = "type";//用户类型
    String personkey = "personkey";//用户编号
    String phonekey = "phonekey";//手机唯一识别码

    String isUserLoginOut = "isUserLoginOut";

    String USERINFO_JSON = "USERINFO_JSON";

    String base64Img = "base64Img";

    String TEMP_VERIFY_KEY = "Temp_Verify_Key";
}
