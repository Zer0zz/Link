package com.self.link.base;

/**
 * description：
 * author：Administrator on 2020/6/16 19:52
 */
public interface Constant {
  public static boolean isDebug = false;

    //SharedPreferences 常量类
    class SpKeyName {
        //SPUtiles保存获取的公钥
        public static final String PUBKEY = "publicKey";
        public static final String SessionId = "sessionId";
        public static final String userId = "userId";
        public static final String userName = "userName";
        public static final String realName = "realName";
        public static final String phone = "phone";
    }

    class RequsetCode {
        public static final int REGISTER = 10001;
        public static final int GETPUBKEY = 10002;
        public static final int LOGIN = 10003;
        public static final int USERINFO = 10004;

        public static final int UPDATEUSERINFO = 10005;

        public static final int BANNERLISTID = 10006;
        public static final int FITNESSLIST = 10007;

        public static final int SENDCODEID = 10008;
        public static final int COACHLISTID = 10009;
        public static final int FITNESSCOURSELIST_ID = 10010;

        public static final int COURSESEAT_LIST_ID = 10011;
        public static final int COACHDETAIL_ID = 10012;
        public static final int UPLOAD_IMAGEBASE64_ID = 10013;

        public static final int MYFITNESS_ID = 10014;

        public static final int SECURITYCODE_LOGIN_ID = 10015;

        public static final int FITNESS_SLIDESHOW_ID = 10016;

        public static final int CERTIFICATION_STATUS_ID = 10017;
        public static final int MYCOACH_ID = 10018;

        public static final int SUBSCRIBE_PERSONALTRAINER_ID = 10019;

        public static final int CANCEL_THERESERVATION_ID = 10020;
        public static final int AUTHENTICATION_ID = 10021;
        public static final int CURRICULA_VARIABLE_ID = 10022;

        public static final int CANCEL_THECOURSE_ID = 10023;

        //        public static final int SUBSCRIBE_PERSONALTRAINER_ID = 10024;
        public static final int COACHFITNESS_PUNCHLIST_ID = 10025;
        public static final int COURSE_PUNCHLIST_ID = 10026;

        public static final int PUNCH_ID = 10027;

        public static final int MYCOURSE_ID = 10028;

        public static final int LOGOUT_ID = 10029;

    }

    class RequestUrl {
        public static String baseUrl = "http://lixingrui.club";
        static String getBaseUrl(){
            if (isDebug){//
                baseUrl = "http://lixingrui.club";
            }else {//发版地址
                baseUrl = "http://www.dolinknow.com";
            }
            return baseUrl;
        }


        //图片地址的前面
        public static  final String imageBaseUrl = getBaseUrl()+"/file";

        /**************************************登录注册有关接口***************************************************/
        public static final String accountLogin = getBaseUrl()+"/api/system/accountLogin";
        public static final String getGetpubkey =  getBaseUrl()+"/api/system/getPublicKey";

        public static final String findPassword = getBaseUrl()+ "/api/system/findPassword";

        public static final String register = getBaseUrl()+ "/api/system/register";
        public static final String securityCodeLogin = getBaseUrl()+ "/api/system/securityCodeLogin";

        public static final String sendCode =  getBaseUrl()+"/api/system/sendCode";

        public static final String updatePassword = getBaseUrl()+ "/api/system/updatePassword";

        public static final String updateUserInfo = getBaseUrl()+ "/api/user/updateUserInfo";

        public static final String userInfo = getBaseUrl()+ "/api/user/userInfo";


        public static final String uploadImage =  getBaseUrl()+"/api/upload/uploadImage";
        //Base64上传图片
        public static final String uploadImageBase64 = getBaseUrl()+ "/api/upload/uploadImageBase64";

        //系统轮播图
        public static final String bannerList = getBaseUrl()+ "/api/banner/bannerList";

        /***********************************************健身房相关************************************************************/
        //取消选课（团课）
        public static final String cancelTheCourse = getBaseUrl()+ "/api/fitness/cancelTheCourse";
        //取消预约（私教）
        public static final String cancelTheReservation = getBaseUrl()+ "/api/fitness/cancelTheReservation";
        //教练详情
        public static final String coachDetail = getBaseUrl()+ "/api/fitness/coachDetail";
        //教练列表
        public static final String coachList =  getBaseUrl()+"/api/fitness/coachList";

        //课程详情 /api/fitness/courseSeatList
        public static final String courseSeatList = getBaseUrl()+ "/api/fitness/courseSeatList";

        //获取健身房列表
        public static final String fitnessList = getBaseUrl()+ "/api/fitness/fitnessList";

        public static final String fitnessCourseList =  getBaseUrl()+"/api/fitness/fitnessCourseList";


        public static final String myFitness =  getBaseUrl()+"/api/fitness/myFitness";
        //健身房轮播图
        public static final String fitnessSlideshow = getBaseUrl()+ "/api/fitness/fitnessSlideshow";

        //获取认证状态
        public static final String certificationStatus = getBaseUrl()+ "/api/fitness/certificationStatus";

        public static final String myCoach = getBaseUrl()+ "/api/fitness/myCoach";
        //预约私教
        public static final String subscribePersonalTrainer =  getBaseUrl()+"/api/fitness/subscribePersonalTrainer";


        //健身房认证
        public static final String authentication =  getBaseUrl()+"/api/fitness/authentication";

        ///api/fitness/curriculaVariable
        //预约团课
        public static final String curriculaVariable = getBaseUrl()+ "/api/fitness/curriculaVariable";

        //私教列表（打卡）
        public static final String coachFitnessPunchList = getBaseUrl()+ "/api/course/coachFitnessPunchList";
        //团课打卡列表
        public static final String coursePunchList = getBaseUrl()+ "/api/course/coursePunchList";
        //私教打卡
        public static final String punch = getBaseUrl()+ "/api/course/punch";

        //我的团课
        public static final String myCourse =  getBaseUrl()+"/api/fitness/myCourse";

        //登出
        public static final String logout = getBaseUrl()+ "/api/system/logOut";


    }
}
