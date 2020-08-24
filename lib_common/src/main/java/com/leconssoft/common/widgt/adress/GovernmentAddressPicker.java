package com.leconssoft.common.widgt.adress;

import android.app.Activity;

import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;

import com.alibaba.fastjson.JSONArray;
import com.leconssoft.common.NetService.NetReqModleNew;
import com.leconssoft.common.NetService.OnHttpCallBack;
import com.leconssoft.common.baseUtils.UIHelper;
import com.leconssoft.common.widgt.adress.entity.City;
import com.leconssoft.common.widgt.adress.entity.County;
import com.leconssoft.common.widgt.adress.entity.DszInfo;
import com.leconssoft.common.widgt.adress.entity.Province;
import com.leconssoft.common.widgt.adress.entity.QxInfo;
import com.leconssoft.common.widgt.adress.entity.SfInfo;
import com.leconssoft.common.widgt.adress.entity.WheelPicker;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.annotation.FloatRange;
import androidx.annotation.NonNull;
import androidx.annotation.Size;

/**
 * <p>类说明</p>
 * 动态地区选择
 * @author yucheng
 * @date 2019/1/9 14:12
 * @Description
 */
public class GovernmentAddressPicker extends WheelPicker {
    private OnAddressPickListener onAddressPickListener;
    final int REQ_SF = 1001;
    final int REQ_DSZ = 1002;
    final int REQ_QX = 1003;
    Activity mActivity;
    List<SfInfo> sfInfos;
    List<DszInfo> dszInfos;
    List<QxInfo> qxInfos;
    private List<String> firstList = new ArrayList<>();
    private List<String> secondList = new ArrayList<>();
    private List<String> thirdList = new ArrayList<>();
    WheelView provinceView;
    WheelView cityView;
    WheelView countyView;
    String sfString;
    protected String selectedFirstItem = "", selectedSecondItem = "", selectedThirdItem = "";
    private double firstColumnWeight = 0;//第一级显示的宽度比
    private double secondColumnWeight = 0;//第二级显示的宽度比
    private double thirdColumnWeight = 0;//第三级显示的宽度比
    protected int selectedFirstIndex = 0, selectedSecondIndex = 0, selectedThirdIndex = 0;

    public GovernmentAddressPicker(Activity activity, List<SfInfo> data) {
        super(activity);
        mActivity = activity;
        sfInfos = data;
        int provinceSize = sfInfos.size();
        for (int x = 0; x < provinceSize; x++) {
            SfInfo pro = sfInfos.get(x);
            firstList.add(pro.getSfname());
        }
        secondList.add("请选择");
        thirdList.add("请选择");
    }


    public void setOnAddressPickListener(OnAddressPickListener listener) {
        this.onAddressPickListener = listener;
    }


    @NonNull
    @Override
    protected View makeCenterView() {
        int[] widths = getColumnWidths(false);
        int provinceWidth = widths[0];
        int cityWidth = widths[1];
        int countyWidth = widths[2];
        LinearLayout layout = new LinearLayout(activity);
        layout.setOrientation(LinearLayout.HORIZONTAL);
        layout.setGravity(Gravity.CENTER);


        provinceView = new WheelView(activity);
        provinceView.setLayoutParams(new LinearLayout.LayoutParams(provinceWidth, WRAP_CONTENT));
        provinceView.setTextSize(textSize);
        provinceView.setTextColor(textColorNormal, textColorFocus);
        provinceView.setLineConfig(lineConfig);
        provinceView.setOffset(offset);
        provinceView.setCycleDisable(cycleDisable);
        layout.addView(provinceView);

        cityView = new WheelView(activity);
        cityView.setLayoutParams(new LinearLayout.LayoutParams(cityWidth, WRAP_CONTENT));
        cityView.setTextSize(textSize);
        cityView.setTextColor(textColorNormal, textColorFocus);
        cityView.setLineConfig(lineConfig);
        cityView.setOffset(offset);
        cityView.setCycleDisable(cycleDisable);
        layout.addView(cityView);

        countyView = new WheelView(activity);
        countyView.setLayoutParams(new LinearLayout.LayoutParams(countyWidth, WRAP_CONTENT));
        countyView.setTextSize(textSize);
        countyView.setTextColor(textColorNormal, textColorFocus);
        countyView.setLineConfig(lineConfig);
        countyView.setOffset(offset);
        countyView.setCycleDisable(cycleDisable);
        layout.addView(countyView);

        if (firstList != null) {
            provinceView.setItems(firstList, selectedFirstIndex);
        }
        cityView.setItems(secondList, selectedSecondIndex);
        countyView.setItems(thirdList, selectedThirdIndex);
        provinceView.setOnWheelListener(new WheelView.OnWheelListener() {
            @Override
            public void onSelected(boolean isUserScroll, int index, String item) {
                selectedFirstItem = item;
                selectedFirstIndex = index;
                SfInfo info = sfInfos.get(index);
                sfString=info.getSfname();
                getAddressData(2, info.getSfname(), info.getSfcode());
                if (!isUserScroll) {
                    return;
                }
                selectedSecondIndex = 0;//重置地级索引
                selectedThirdIndex = 0;//重置县级索引
            }
        });
        cityView.setOnWheelListener(new WheelView.OnWheelListener() {
            @Override
            public void onSelected(boolean isUserScroll, int index, String item) {
                selectedSecondItem = item;
                selectedSecondIndex = index;
                if (dszInfos != null) {
                    DszInfo info = dszInfos.get(index);
                    getAddressData(3, sfString, info.getDszcode());
                }
                if (!isUserScroll) {
                    return;
                }
                selectedThirdIndex = 0;//重置县级索引
                //根据地市获取区县
            }
        });
        countyView.setOnWheelListener(new WheelView.OnWheelListener() {
            @Override
            public void onSelected(boolean isUserScroll, int index, String item) {
                selectedThirdItem = item;
                selectedThirdIndex = index;
            }
        });
        return layout;
    }

    /**
     * 获取省 市  区
     *
     * @param dex  1=省 2=市 3=区
     * @param name
     * @param code
     */
    public void getAddressData(int dex, String name, String code) {
        Map<String, Object> map = new HashMap<>();
        map.clear();
        switch (dex) {
            case 1:
                map.put("type", "GetSf");
                httpClient(REQ_SF, map);
                break;
            case 2:
                map.put("type", "GetDsz");
                map.put("sfname", name);
                map.put("sfcode", code);
                httpClient(REQ_DSZ, map);
                break;
            case 3:
                map.put("type", "GetQx");
                map.put("sfname", name);
                map.put("dszcode", code);
                httpClient(REQ_QX, map);
                break;
        }

    }

    public void httpClient(int id, Map<String, Object> parm) {
        NetReqModleNew.getInstance().getBandParmHttp(id, parm, new OnHttpCallBack<String>() {
            @Override
            public void onSuccessful(int id, String o) {
                if (!TextUtils.isEmpty(o)) {
                    switch (id) {
                        case REQ_SF:
                            sfInfos = JSONArray.parseArray(o, SfInfo.class);
                            int provinceSize = sfInfos.size();
                            firstList.clear();
                            for (int x = 0; x < provinceSize; x++) {
                                SfInfo pro = sfInfos.get(x);
                                firstList.add(pro.getSfname());
                            }
                            provinceView.setItems(firstList, selectedFirstIndex);
                            break;
                        case REQ_DSZ:
                            dszInfos = JSONArray.parseArray(o, DszInfo.class);
                            int citySize = dszInfos.size();
                            secondList.clear();
                            for (int y = 0; y < citySize; y++) {
                                DszInfo cit = dszInfos.get(y);
                                secondList.add(cit.getDszname());
                            }
                            cityView.setItems(secondList, selectedSecondIndex);
                            List<String> cities = secondList;
                            if (cities.size() > 0) {
                                cityView.setItems(cities, selectedSecondIndex);
                            } else {
                                cityView.setItems(new ArrayList<String>());
                            }
                            break;
                        case REQ_QX:
                            qxInfos = JSONArray.parseArray(o, QxInfo.class);
                            int qxSize = qxInfos.size();
                            thirdList.clear();
                            for (int y = 0; y < qxSize; y++) {
                                QxInfo cit = qxInfos.get(y);
                                thirdList.add(cit.getQxname());
                            }
                            countyView.setItems(thirdList, selectedThirdIndex);
                            List<String> counties = thirdList;
                            if (counties.size() > 0) {
                                countyView.setItems(counties, selectedThirdIndex);
                            } else {
                                countyView.setItems(new ArrayList<String>());
                            }
                            break;

                    }
                } else {
                    UIHelper.ToastMessage(mActivity, "获取数据失败");
                }
            }

            @Override
            public void onFaild(int id, String o, String err) {
//                UIHelper.ToastMessage(mActivity, "未获取到数据");
            }
        });
    }



    /**
     * 地址选择回调
     */
    public interface OnAddressPickListener {

        /**
         * 选择地址
         *
         * @param province the province
         * @param city     the city
         * @param county   the county ，if {@code hideCounty} is true，this is null
         */
        void onAddressPicked(SfInfo province, DszInfo city, QxInfo county);

    }

    @Override
    public void onSubmit() {
        if (onAddressPickListener != null) {
            SfInfo sfInfo = sfInfos.get(selectedFirstIndex);
            DszInfo dszInfo = null;
            if (dszInfos != null) {
                dszInfo = dszInfos.get(selectedSecondIndex);
            }
            QxInfo qxInfo = null;
            if (qxInfos != null) {
                qxInfo = qxInfos.get(selectedThirdIndex);
            }
            if (null != dszInfo&&null != qxInfo) {
                onAddressPickListener.onAddressPicked(sfInfo, dszInfo, qxInfo);
            } else if (qxInfo != null) {
                onAddressPickListener.onAddressPicked(sfInfo, dszInfo, null);
            } else {
                onAddressPickListener.onAddressPicked(sfInfo, null, null);
            }
        }
    }

    /**
     * 设置每列的宽度比例，将屏幕分为三列，每列范围为0.0～1.0，如0.3333表示约占屏幕的三分之一。
     */
    public void setColumnWeight(@FloatRange(from = 0, to = 1) double firstColumnWeight, @FloatRange(from = 0, to = 1) double secondColumnWeight, @FloatRange(from = 0, to = 1) double thirdColumnWeight) {
        this.firstColumnWeight = firstColumnWeight;
        this.secondColumnWeight = secondColumnWeight;
        this.thirdColumnWeight = thirdColumnWeight;
    }

    /**
     * 根据比例计算，获取每列的实际宽度。
     * 三级联动默认每列宽度为屏幕宽度的三分之一，两级联动默认每列宽度为屏幕宽度的一半。
     */
    @Size(3)
    protected int[] getColumnWidths(boolean onlyTwoColumn) {
        LogUtils.verbose(this, String.format(java.util.Locale.CHINA, "column weight is: %f-%f-%f", firstColumnWeight, secondColumnWeight, thirdColumnWeight));
        int[] widths = new int[3];
        // fixed: 17-1-7 Equality tests should not be made with floating point values.
        if ((int) firstColumnWeight == 0 && (int) secondColumnWeight == 0 && (int) thirdColumnWeight == 0) {
            if (onlyTwoColumn) {
                widths[0] = screenWidthPixels / 2;
                widths[1] = widths[0];
                widths[2] = 0;
            } else {
                widths[0] = screenWidthPixels / 3;
                widths[1] = widths[0];
                widths[2] = widths[0];
            }
        } else {
            widths[0] = (int) (screenWidthPixels * firstColumnWeight);
            widths[1] = (int) (screenWidthPixels * secondColumnWeight);
            widths[2] = (int) (screenWidthPixels * thirdColumnWeight);
        }
        return widths;
    }

}


