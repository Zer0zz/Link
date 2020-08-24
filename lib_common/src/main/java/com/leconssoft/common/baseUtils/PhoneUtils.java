package com.leconssoft.common.baseUtils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.view.Display;
import android.view.Window;
import android.view.WindowManager;


import java.io.ByteArrayOutputStream;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PhoneUtils {
    public static final String DEFAULT_DATA_NETWORK = "default_data_network";
    public static final String SAVED_DATA_NETWORK = "saved_data_network";
    public static final String CDMA_DATA_NETWORK = "cdma";
    public static final String NONE_DATA_NETWORK = "none";

    public static Uri PREFERRED_APN_URI = Uri
            .parse("content://telephony/carriers/preferapn");

    /**
     * 获取手机imsi码
     * <p>
     * 国际移动用户识别码（IMSI） International Mobile Subscriber Identity
     * 国际上为唯一识别一个移动用户所分配的号码。
     * <p>
     * IMSI共有15位，其结构如下： MCC+MNC+MIN MCC：Mobile Country Code，移动国家码，共3位，中国为460;
     * MNC:Mobile Network
     * Code，移动网络码，共2位，联通CDMA系统使用03，一个典型的IMSI号码为460030912121001; MIN共有10位，其结构如下：
     * 09+M0M1M2M3+ABCD 其中的M0M1M2M3和MDN号码中的H0H1H2H3可存在对应关系，ABCD四位为自由分配。
     */
    public static String getImsi(Context ctx) {
        TelephonyManager telephonyManager = (TelephonyManager) ctx
                .getSystemService(Context.TELEPHONY_SERVICE);
        String imsi = telephonyManager.getSubscriberId();
        return TextUtils.isEmpty(imsi) ? "none" : imsi;
    }

    /**
     * 获取手机号码
     */
    public static String getMdn(Context ctx) {
        TelephonyManager telephonyManager = (TelephonyManager) ctx
                .getSystemService(Context.TELEPHONY_SERVICE);
        String mdn = telephonyManager.getLine1Number();
        return mdn;
    }

    /**
     * 获取手机ESN CDMA手机机身号简称ESN-Electronic Serial Number的缩写。
     * <p>
     * 它是一个32bits长度的参数，是手机的惟一标识。
     * <p>
     * GSM手机是IMEI码(International Mobile Equipment Identity)，
     * <p>
     * 国际移动身份码。
     */
    public static String getEsn(Context ctx) {
        TelephonyManager tm = (TelephonyManager) ctx
                .getSystemService(Context.TELEPHONY_SERVICE);
        String esn = tm.getDeviceId();// DeviceId(IMEI)
        return esn;
    }

    /**
     * iccmd 号码
     **/
    public static String getICCMD(Context ctx) {
        TelephonyManager tm = (TelephonyManager) ctx.getSystemService(Context.TELEPHONY_SERVICE);
        String iccmd = tm.getSimSerialNumber();
        return iccmd;
    }


    /**
     * 获取手机MODLE,手机型号
     */
    public static String getModel() {
        String model = Build.MODEL;
        // 模拟器环境默认使用XT800
        if ("sdk".equals(model)) {
            model = "XT800";
        }
        return model;
    }

    /**
     * 获取手机ROM
     */
    public static String getRom() {
        // String PRODUCT = Build.PRODUCT; //如 titanium
        // String DEVICE = Build.DEVICE;//如 titanium
        String DISPLAY = Build.DISPLAY; // 如 titanium-userdebug 2.1
        // TITA_K29_00.13.01I 173018 test-keys
        // String ID = Build.ID;//如 TITA_K29_00.13.01I
        // String RELEASE = Build.VERSION.RELEASE; //如 2.1
        // String INCREMENTAL = Build.VERSION.INCREMENTAL; //如 173018
        return DISPLAY;
    }


    /**
     * 获取手机厂商
     */
    public static String getManufacturer() {
        return Build.MANUFACTURER;
    }

    /**
     * @return获取系统版本
     */
    public static String getAndroidSystem() {
        return Build.VERSION.RELEASE;
    }

    /**
     * 获取屏幕分辨率
     *
     * @param mActivity
     * @return int[]{width,height}
     */
    public static int[] getResolution(Activity mActivity) {
        Display screen = mActivity.getWindowManager().getDefaultDisplay();
        return new int[]{screen.getWidth(), screen.getHeight()};
    }

    /**
     * 获取状态栏的高度 (需在屏幕显示后台调用)
     *
     * @param mActivity
     * @return
     */
    public static int getStatusBarHeight(Activity mActivity) {
        Rect frame = new Rect();
        mActivity.getWindow().getDecorView()
                .getWindowVisibleDisplayFrame(frame);
        return frame.top;
    }

    /**
     * 获取标题栏高度 (需在屏幕显示后台调用)
     *
     * @param mActivity
     * @return
     */
    public static int getTitleBarHeight(Activity mActivity) {
        int contentTop = mActivity.getWindow()
                .findViewById(Window.ID_ANDROID_CONTENT).getTop();
        return contentTop - getStatusBarHeight(mActivity);
    }

    // 检验是否电信手机号
    // Add By LiuCun 2011-11-22
    public static boolean checkMobile(String value) {
        return value.matches("^(133|153|189|180|181)+\\d{8}$");
    }

    /**
     * 判定某一组号码是否是手机号码
     * <p>
     * 故先要整清楚现在已经开放了多少个号码段，国家号码段分配如下：
     * <pre>
     * 移动：134、135、136、137、138、139、150、151、157(TD)、158、159、187、188
     * 联通：130、131、132、152、155、156、185、186
     * 电信：133、153、180、189、（1349卫通）
     * </pre>
     *
     * @param mobiles 号码
     * @return
     */
    public static boolean isMobileNumber(String mobiles) {

        if (!TextUtils.isEmpty(mobiles)) {
            Pattern p = Pattern.compile("^1(3[0-9]|4[57]|5[0-35-9]|8[0-9]|7[0678])\\d{8}$");
            Matcher m = p.matcher(mobiles);
            return m.matches();
        }
        return false;
    }

    /**
     * 判断是否WIFI
     *
     * @return
     */
    public static boolean isWIFI(Context icontext) {
        Context context = icontext.getApplicationContext();
        ConnectivityManager connectivity = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo[] info;
        if (connectivity != null) {
            info = connectivity.getAllNetworkInfo();
            if (info != null) {
                for (int i = 0; i < info.length; i++) {
                    if (info[i].getTypeName().equalsIgnoreCase("WIFI") && info[i].isConnected()) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /**
     * 判断是否CDMA网络
     *
     * @param context
     * @return
     */
    public static boolean isCDMANetwork(Context context) {
        boolean flag = true;

        String preferredDataNetwork = android.provider.Settings.System
                .getString(context.getContentResolver(), DEFAULT_DATA_NETWORK);

        if (NONE_DATA_NETWORK.equalsIgnoreCase(preferredDataNetwork) || null == preferredDataNetwork) {
            preferredDataNetwork = android.provider.Settings.System.getString(
                    context.getContentResolver(), SAVED_DATA_NETWORK);
        }

        if (!CDMA_DATA_NETWORK.equalsIgnoreCase(preferredDataNetwork)) {
            flag = false;
        }

        return flag;
    }

    /**
     * 获取软件版本
     *
     * @param context
     * @return
     */
    public static String getSoftVersion(Context context) {
        PackageInfo info = null;
        try {
            info = context.getPackageManager()
                    .getPackageInfo(
                            context.getPackageName(), 0);
            return info.versionName;
        } catch (NameNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return "";
    }

    /**
     * 获取软件VersionCode
     *
     * @param context
     * @return
     */
    public static int getSoftVersionCode(Context context) {
        PackageInfo info = null;
        try {
            info = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            return info.versionCode;
        } catch (NameNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return 1;
    }

    /**
     * 获取屏幕分辨率
     *
     * @return int[]{width,height}
     */
    public static int[] getResolution(Context ctx) {
        WindowManager wm = (WindowManager) ctx
                .getSystemService(Context.WINDOW_SERVICE);
        Display screen = wm.getDefaultDisplay();
        return new int[]
                {screen.getWidth(), screen.getHeight()};
    }


    /**
     * 验证是否为电话号码， 包含：+86， 86
     **/
    public static boolean isPhoneNum(String phoneNum) {
        Pattern pattern = Pattern.compile("^((\\+{0,1}86){0,1})1[0-9]{10}");
        Matcher matcher = pattern.matcher(phoneNum);
        return matcher.matches();
    }

    /**
     * 移除 +86， 86
     **/
    public static String removePhoneNumWith86(String phoneNum) {
        Pattern pattern = Pattern.compile("^((\\+{0,1}86){0,1})");
        Matcher matcher = pattern.matcher(phoneNum);
        StringBuffer sb = new StringBuffer();
        while (matcher.find()) {
            matcher.appendReplacement(sb, "");
        }
        matcher.appendTail(sb);
        return sb.toString();
    }

    /**
     * Bitmap转为字节
     **/
    public static byte[] Bitmap2Bytes(Bitmap bm) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        return baos.toByteArray();
    }

    /**
     * 发送短信
     **/
    public static void sendSMS(Activity act, String mobile, String content) {
        if (null == content) {
            content = "";
        }
        Uri smsToUri = Uri.parse("smsto:" + mobile);
        Intent mIntent = new Intent(Intent.ACTION_SENDTO, smsToUri);
        mIntent.putExtra("sms_body", content);
        act.startActivity(mIntent);
    }

    /**
     * 直接打电话
     **/
    public static void callPhone(Activity act, String mobile) {
        Intent phoneIntent = new Intent("android.intent.action.CALL",
                Uri.parse("tel:" + mobile));
        //启动
        act.startActivity(phoneIntent);
    }

    /**
     * 拨号界面
     **/
    public static void dialPhone(Activity act, String mobile) {
        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + mobile));
        act.startActivity(intent);
    }



}
