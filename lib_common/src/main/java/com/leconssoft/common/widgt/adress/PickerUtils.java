package com.leconssoft.common.widgt.adress;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.leconssoft.common.baseUtils.LogUtil;
import com.leconssoft.common.baseUtils.UIHelper;
import com.leconssoft.common.widgt.adress.entity.City;
import com.leconssoft.common.widgt.adress.entity.County;
import com.leconssoft.common.widgt.adress.entity.FundInfo;
import com.leconssoft.common.widgt.adress.entity.Province;
import com.leconssoft.common.widgt.adress.entity.SfInfo;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>类说明</p>
 * 日期，地区选择控件使用工具类
 * 这些都是使用的方法 具体使用自己看情况选择
 *
 * @author yucheng
 * @date 2018/12/27 14:14
 * @Description
 */
public class PickerUtils {

    /**
     * 年月日
     *
     * @param activity
     */
    public void onYearMonthDayPicker(final Activity activity) {
        final DatePicker picker = new DatePicker(activity);
        picker.setTopPadding(2);
        picker.setRangeStart(2016, 8, 29);
        picker.setRangeEnd(2111, 1, 11);
        picker.setSelectedItem(2050, 10, 14);
        picker.setOnDatePickListener(new DatePicker.OnYearMonthDayPickListener() {
            @Override
            public void onDatePicked(String year, String month, String day) {
                UIHelper.ToastMessage(activity, year + "-" + month + "-" + day);
            }
        });
        picker.setOnWheelListener(new DatePicker.OnWheelListener() {
            @Override
            public void onYearWheeled(int index, String year) {
                picker.setTitleText(year + "-" + picker.getSelectedMonth() + "-" + picker.getSelectedDay());
            }

            @Override
            public void onMonthWheeled(int index, String month) {
                picker.setTitleText(picker.getSelectedYear() + "-" + month + "-" + picker.getSelectedDay());
            }

            @Override
            public void onDayWheeled(int index, String day) {
                picker.setTitleText(picker.getSelectedYear() + "-" + picker.getSelectedMonth() + "-" + day);
            }
        });
        picker.show();
    }


    /**
     * 年月日时分
     *
     * @param activity
     */
    public void onYearMonthDayTimePicker(final Activity activity) {
        DateTimePicker picker = new DateTimePicker(activity, DateTimePicker.HOUR_24);
        picker.setDateRangeStart(2017, 1, 1);
        picker.setDateRangeEnd(2025, 11, 11);
        picker.setTimeRangeStart(9, 0);
        picker.setTimeRangeEnd(20, 30);
        picker.setOnDateTimePickListener(new DateTimePicker.OnYearMonthDayTimePickListener() {
            @Override
            public void onDateTimePicked(String year, String month, String day, String hour, String minute) {
                UIHelper.ToastMessage(activity, year + "-" + month + "-" + day + " " + hour + ":" + minute);
            }
        });
        picker.show();
    }


    /**
     * 年月
     *
     * @param activity
     */
    public void onYearMonthPicker(final Activity activity) {
        DatePicker picker = new DatePicker(activity, DatePicker.YEAR_MONTH);
        picker.setGravity(Gravity.TOP | Gravity.CENTER_HORIZONTAL);
        picker.setWidth((int) (picker.getScreenWidthPixels() * 0.6));
        picker.setRangeStart(2016, 10, 14);
        picker.setRangeEnd(2020, 11, 11);
        picker.setSelectedItem(2017, 9);
        picker.setOnDatePickListener(new DatePicker.OnYearMonthPickListener() {
            @Override
            public void onDatePicked(String year, String month) {
                UIHelper.ToastMessage(activity, year + "-" + month);
            }
        });
        picker.show();
    }

    /**
     * 月日
     *
     * @param activity
     */
    public void onMonthDayPicker(final Activity activity) {
        DatePicker picker = new DatePicker(activity, DatePicker.MONTH_DAY);
        picker.setGravity(Gravity.CENTER);//弹框居中
        picker.setRangeStart(5, 1);
        picker.setRangeEnd(12, 31);
        picker.setSelectedItem(10, 14);
        picker.setOnDatePickListener(new DatePicker.OnMonthDayPickListener() {
            @Override
            public void onDatePicked(String month, String day) {
                UIHelper.ToastMessage(activity, month + "-" + day);
            }
        });
        picker.show();
    }

    /**
     * 时分 选择
     *
     * @param activity
     */
    public void onTimePicker(final Activity activity) {
        TimePicker picker = new TimePicker(activity, TimePicker.HOUR_24);
        picker.setRangeStart(9, 0);//09:00
        picker.setRangeEnd(18, 0);//18:30
        picker.setTopLineVisible(false);
        picker.setLineVisible(false);
        picker.setOnTimePickListener(new TimePicker.OnTimePickListener() {
            @Override
            public void onTimePicked(String hour, String minute) {
                UIHelper.ToastMessage(activity, hour + ":" + minute);
            }
        });
        picker.show();
    }


    public static void getNetAddress(Activity context, List<SfInfo> data, GovernmentAddressPicker.OnAddressPickListener listener) {
        GovernmentAddressPicker picker = new GovernmentAddressPicker(context, data);
        picker.setColumnWeight(2 / 8.0, 3 / 8.0, 3 / 8.0);//省级、地级和县级的比例为2:3:3
        picker.setOnAddressPickListener(listener);
        picker.show();
    }

    /**
     * 省/市/区
     *
     * @param context
     */
    public static void getAddress(Activity context, AddressPickTask.Callback callback, String... value) {
        AddressPickTask task = new AddressPickTask(context);
        task.setHideProvince(false);
        task.setHideCounty(false);
        task.setCallback(callback);
        if (value != null) {
            task.execute(value[0], value[1], value[2]);
        } else {
            task.execute("湖北", "武汉", "江岸");
        }
    }


    /**
     * 地级/县级
     *
     * @param context
     */
    public static void getAddress2(final Activity context, String p) {
        try {
            ArrayList<Province> data = new ArrayList<>();
            String json = ConvertUtils.toString(context.getAssets().open("city2.json"));
            data.addAll(JSON.parseArray(json, Province.class));
            AddressPicker picker = new AddressPicker(context, data);
            picker.setHideProvince(true);
            if (TextUtils.isEmpty(p)) {
                picker.setSelectedItem("贵州", "贵阳", "花溪");
            } else {
                picker.setSelectedItem(p, "", "");
            }
            picker.setOnAddressPickListener(new AddressPicker.OnAddressPickListener() {
                @Override
                public void onAddressPicked(Province province, City city, County county) {
                    UIHelper.ToastMessage(context, "province : " + province + ", city: " + city + ", county: " + county);
                }
            });
            picker.show();
        } catch (Exception e) {
            UIHelper.ToastMessage(context, LogUtils.toStackTraceString(e));
        }
    }

    /**
     * 选择省/区
     *
     * @param context
     */
    public void getAddress3(final Activity context) {
        AddressPickTask task = new AddressPickTask(context);
        task.setHideCounty(true);
        task.setCallback(new AddressPickTask.Callback() {
            @Override
            public void onAddressInitFailed() {
                UIHelper.ToastMessage(context, "数据初始化失败");
            }

            @Override
            public void onAddressPicked(Province province, City city, County county) {
                UIHelper.ToastMessage(context, province.getAreaName() + " " + city.getAreaName());
            }
        });
        task.execute("四川", "阿坝");
    }


    public static void getFundPicker(Activity activity, SinglePicker.OnClickPickListener onClickPickListener)  {
      SinglePicker singlePicker=new SinglePicker(activity);
      singlePicker.setOnClickPickListener(onClickPickListener);
      singlePicker.show();
    }


}
