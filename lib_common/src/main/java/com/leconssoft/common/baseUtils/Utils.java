/*
 * 苏州快云软件有限公司
 *
 * --------------+--------------+--------------- 更新时间 更新者 更新内容 --------------+--------------+--------------- 2015年9月21日
 * check_000 创建
 *
 * 文 件 名: Utils.java 版 权: SuZhou KuaiYunSoft. Copyright 2015-2025 开发人员: check_000
 */
package com.leconssoft.common.baseUtils;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.text.TextUtils;


import com.leconssoft.common.baseUtils.permssion.EasyPermission;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


/**
 * <一句话功能简述> <功能详细描述>
 *
 * @author 姓名
 * @version [版本号, 2015年9月21日]
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
public class Utils {
    private Utils() {
    }

    /**
     * <获取系统版本号> <功能详细描述>
     *
     * @param context
     * @return
     * @see [类、类#方法、类#成员]
     */
    public static String getVersionName(Context context) {
        String versionName = "";
        try {
            PackageInfo info = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            versionName = info.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return versionName;
    }

    public static boolean isOverMarshmallow() {
        return Build.VERSION.SDK_INT >= 23;
    }

    public static List<String> findDeniedPermissions(Activity activity, String... permission) {
        if (activity == null) {
            return null;
        }

        List<String> denyPermissions = new ArrayList<>();

        if (Build.VERSION.SDK_INT >= 23) {
            for (String value : permission) {
                if (activity.checkCallingPermission(value) != PackageManager.PERMISSION_GRANTED) {
                    denyPermissions.add(value);
                }
            }
        }

        return denyPermissions;
    }

    @TargetApi(Build.VERSION_CODES.M)
    public static boolean shouldShowRequestPermissionRationale(Object object, String perm) {
        if (object instanceof Activity) {
            return ActivityCompat.shouldShowRequestPermissionRationale((Activity) object, perm);
        } else if (object instanceof Fragment) {
            return ((Fragment) object).shouldShowRequestPermissionRationale(perm);
        } else {
            return object instanceof Fragment && Build.VERSION.SDK_INT >= 23 && ((Fragment) object).shouldShowRequestPermissionRationale(perm);
        }
    }

    public static Activity getActivity(Object object) {
        if (object instanceof Activity) {
            return ((Activity) object);
        } else if (object instanceof Fragment) {
            return ((Fragment) object).getActivity();
        } else if (object instanceof Fragment) {
            return ((Fragment) object).getActivity();
        } else {
            return null;
        }
    }

    public static boolean isEmpty(List list) {
        return list == null || list.isEmpty();
    }

    /**
     * 手机权限判断
     *
     * @param context
     * @return
     */
    public static boolean hasPermission(Context context, String... reqPermissions) {
        if (EasyPermission.hasPermissions(context, reqPermissions)) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 授权权限
     *
     * @param context
     * @param permissions 需要的权限数组
     * @param mention     提示信息
     */
    public static void grantedPermissions(final Context context, final String[] permissions, String mention) {
        EasyPermission.requestPermissions(context, mention, Common.PERMISSIONS_REQUEST, permissions);

    }

    /**
     * 授权权限
     *
     * @param context
     * @param requestCode 对应执行赋予权限回调
     * @param permissions 需要的权限数组
     * @param mention     提示信息
     */
    public static void grantedPermissions(final Context context, int requestCode, final String[] permissions, String mention) {
        EasyPermission.requestPermissions(context, mention, requestCode, permissions);

    }

    /**
     * The {@code fragment} is added to the container view with id {@code frameId}. The operation is
     * performed by the {@code fragmentManager}.
     */
    public static void addFragmentToActivity(@NonNull FragmentManager fragmentManager, @NonNull Fragment fragment, int frameId) {
        checkNotNull(fragmentManager);
        checkNotNull(fragment);
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.add(frameId, fragment);

        transaction.commit();
    }


    public static <T> T checkNotNull(T obj) {
        if (obj == null) {
            throw new NullPointerException();
        }
        return obj;
    }


    public static String sfzEncryption(String idNo) {
        String str = "****";
        StringBuilder stringBuilder = new StringBuilder(idNo);
        if (!TextUtils.isEmpty(idNo)) {
            if (idNo.length() == 18) {
                stringBuilder.replace(10, 14, str);
            }
        }
        return stringBuilder.toString();
    }

    public static List<String> getImages() {
        List<String> imags = new ArrayList<>();
        imags.add("http://bpic.588ku.com/element_origin_min_pic/00/00/05/115732f19cc0079.jpg");
        imags.add("http://bpic.588ku.com/element_origin_min_pic/00/00/05/115732f1ac12d1d.jpg");
        imags.add("http://bpic.588ku.com/element_origin_min_pic/00/00/05/115732f1bad97d1.jpg");
        imags.add("http://bpic.588ku.com/element_origin_min_pic/00/00/05/115732f1c83c228.jpg");
        return imags;
    }

    /**
     * dip转为 px
     */
    public static int dip2px(Context context, float dipValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }

    /**
     * 将sp值转换为px值，保证文字大小不变
     *
     * @param spValue （DisplayMetrics类中属性scaledDensity）
     * @return
     */
    public static int sp2px(Context context, float spValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }


    /**
     * @return 获取当前系统时间
     */
    public static String getNowTime() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss");
        // HH:mm:ss//获取当前时间
        Date date = new Date(System.currentTimeMillis());
        return simpleDateFormat.format(date);
    }

    public static String toString(InputStream is) {
        return toString(is, "utf-8");
    }

    public static String toString(InputStream is, String charset) {
        StringBuilder sb = new StringBuilder();
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(is, charset));
            while (true) {
                String line = reader.readLine();
                if (line == null) {
                    break;
                } else {
                    sb.append(line).append("\n");
                }
            }
            reader.close();
            is.close();
        } catch (IOException e) {
            LogUtil.e("IOException", e.toString());
        }
        return sb.toString();
    }


    /**
     * 注:这里解析主要用到的是eventType(事件类型),
     * 如果是START_DOCUMENT则表示读到文档开始位置,
     * 如果是START_TAG,则表示读到文档中的元素开始位置,
     * 如<name>,类似的结束位置为END_DOCUMENT和END_TAG;
     * 其中要注意判断其事件类型开始的名称是什么,并且取值时,注意其不同位置取值方式也不一样,
     * 如<person id="1122">,
     * 那么取id的方式为getAttributeValue(int index),
     * 另外取<name >amos</name>,
     * 则要用nextText()方法进行取值.
     *
     * @param string
     * @return
     */
    public static String getXmlString(String string) {
        XmlPullParserFactory factory = null;
        XmlPullParser parser = null;
        String str = null;
        try {
            factory = XmlPullParserFactory.newInstance();
            parser = factory.newPullParser();
            parser.setInput(new StringReader(string.toString()));
            int eventType = parser.getEventType();
            while (eventType != XmlPullParser.END_DOCUMENT) {
                switch (eventType) {
                    case XmlPullParser.START_DOCUMENT:
                        break;
                    case XmlPullParser.START_TAG:
                        if (parser.getName().equals("string")) {
                            str = parser.nextText();
                            eventType = parser.next();

                        }

                        break;
                    case XmlPullParser.END_TAG:
                        break;
                }
                eventType = parser.next();

            }
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return str;
    }
}
