package com.leconssoft.common.baseUtils;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

/**
 * description：
 * author：zhangCl on 2018/10/19 17:23
 */
public class SPUtils {


    private static Context mContext;
    private static final String SP_NAME = "Link_Share";
    public static final int DEFAULT_INT = -999;

    public static void setSPUtilsContext(Context context) {
        mContext = context;

    }

    /**
     * 存储Int值
     *
     * @param name
     * @param value
     */
    public static void saveIntValue(String name, int value) {
        SharedPreferences sp = mContext.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
        sp.edit().putInt(name, value).commit();
    }

    /**
     * 存储Boolean值
     *
     * @param name
     * @param value
     */
    public static void saveBooleanValue(String name, boolean value) {
        SharedPreferences sp = mContext.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
        sp.edit().putBoolean(name, value).commit();
    }

    /**
     * 存储String值
     *
     * @param name
     * @param value
     */
    public static void saveStringValue(String name, String value) {
        SharedPreferences sp = mContext.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
        sp.edit().putString(name, value).commit();
    }

    /**
     * get Int value
     *
     * @param name
     * @return
     */
    public static int getIntValue(String name) {
        SharedPreferences sp = mContext.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
        return sp.getInt(name, DEFAULT_INT);
    }

    /**
     * get Int value
     *
     * @param name
     * @param def
     * @return
     */
    public static int getIntValue(String name, int def) {
        SharedPreferences sp = mContext.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
        return sp.getInt(name, def);
    }

    /**
     * get boolean value
     *
     * @param name
     * @return
     */
    public static boolean getBooleanValue(String name) {
        SharedPreferences sp = mContext.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
        return sp.getBoolean(name, false);
    }

    public static boolean getBooleanValue(String name,
                                          boolean defValue) {
        SharedPreferences sp = mContext.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
        return sp.getBoolean(name, defValue);

    }

    /**
     * get String value
     *
     * @param name
     * @return
     */
    public static String getStringValue(String name) {
        SharedPreferences sp = mContext.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
        return sp.getString(name, "");
    }

    public static String getStringValue(String name, String defValue) {
        SharedPreferences sp = mContext.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
        return sp.getString(name, defValue);
    }

    public static void clearSession() {
        SharedPreferences sp = mContext.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = sp.edit();

//		edit.putString(SEARCH_HISTORY, "");
//		edit.putString(LOGIN_KEY, "");
//		edit.putString(USER_PHONE, "");
//		edit.putString(UPDATE_VERSION, "");
//		edit.putString(SP_BANNER, "");
        edit.commit();
    }

    public static boolean clearAll() {
//        SharedPreferences sp =
//                context.getSharedPreferences(name, Context.MODE_PRIVATE);
//        sp.edit().putString(name, value).commit();
        SharedPreferences.Editor editor = mContext.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE).edit();
        editor.clear();
        boolean succeed = editor.commit();

        return succeed;

    }

}
