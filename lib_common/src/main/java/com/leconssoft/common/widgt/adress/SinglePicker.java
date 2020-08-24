package com.leconssoft.common.widgt.adress;

import android.app.Activity;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;

import com.alibaba.fastjson.JSONArray;
import com.leconssoft.common.baseUtils.LogUtil;
import com.leconssoft.common.widgt.adress.entity.FundInfo;
import com.leconssoft.common.widgt.adress.entity.WheelPicker;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;

/**
 * <p>类说明</p>
 *
 * @author yucheng
 * @date 2019/1/14 14:23
 * @Description
 */
public class SinglePicker extends WheelPicker {
    WheelView wheelView;
    private OnClickPickListener onClickPickListener;
    private List<String> showList=new ArrayList<>();
    private List<FundInfo> fundInfos;
    FundInfo info;
    protected int selectedIndex = 0;
    public SinglePicker(Activity activity) {
        super(activity);
        String json = null;
        try {
            json = ConvertUtils.toString(activity.getAssets().open("fund.json"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        LogUtil.e("SinglePicker", "=====>"+json);
        fundInfos= JSONArray.parseArray(json, FundInfo.class);
        for(FundInfo info:fundInfos){
            showList.add(info.getAreaName());
        }
    }

    public OnClickPickListener getOnClickPickListener() {
        return onClickPickListener;
    }

    public void setOnClickPickListener(OnClickPickListener onClickPickListener) {
        this.onClickPickListener = onClickPickListener;
    }

    @NonNull
    @Override
    protected View makeCenterView() {
        LinearLayout layout = new LinearLayout(activity);
        layout.setOrientation(LinearLayout.HORIZONTAL);
        layout.setGravity(Gravity.CENTER);
        wheelView = new WheelView(activity);
        wheelView.setLayoutParams(new LinearLayout.LayoutParams(MATCH_PARENT, WRAP_CONTENT));
        wheelView.setTextSize(textSize);
        wheelView.setTextColor(textColorNormal, textColorFocus);
        wheelView.setLineConfig(lineConfig);
        wheelView.setOffset(offset);
        wheelView.setCycleDisable(cycleDisable);
        layout.addView(wheelView);
        wheelView.setItems(showList,selectedIndex);
        wheelView.setOnWheelListener(new WheelView.OnWheelListener() {
            @Override
            public void onSelected(boolean isUserScroll, int index, String item) {
                selectedIndex=index;
                info=fundInfos.get(selectedIndex);
                if (!isUserScroll) {
                    return;
                }
            }
        });
        return layout;
    }

    @Override
    public void onSubmit() {
        if (onClickPickListener != null) {
            onClickPickListener.onClickPicked(info);
        }
    }

    /**
     * 选择回调
     */
    public interface OnClickPickListener {

        /**
         * 选择回调
         * @param t
         */
        void onClickPicked(FundInfo t);

    }
}
