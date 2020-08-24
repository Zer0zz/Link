package com.leconssoft.common.baseUtils.customview;


import android.app.Activity;
import android.content.Context;
import android.view.Gravity;
import android.widget.LinearLayout;

import com.leconssoft.common.R;


public class ViewArea extends LinearLayout {

	private int imgDisplayW;
	private int imgDisplayH;
	public TouchImageView touchView;


	/**
	 * 
	 * @param context 上下文
	 * @param remoteUrl_localPath  网络图片url地址或者本地图片路径 
	 */
	public ViewArea(Context context, String remoteUrl_localPath) {

		super(context);
		imgDisplayW = ((Activity) context).getWindowManager()
				.getDefaultDisplay().getWidth();// 这里的宽高要和xml中的LinearLayout大小一致，如果要指定大小。xml中
												// LinearLayout的宽高一定要用px像素单位，因为这里的宽高是像素，用dp会有误差！
		imgDisplayH = ((Activity) context).getWindowManager()
				.getDefaultDisplay().getHeight();


		touchView = new TouchImageView(context, imgDisplayW, imgDisplayH);
		
		touchView.setDefaultImage(R.drawable.icon_loadings);
		touchView.setImageUrlAndSaveLocal(remoteUrl_localPath, true, null);
//		touchView.setImageUrlAndSaveLocal2(remoteUrl_localPath, true, null);
		
		
		

		// 定义图片控件显示的区域
		int layout_w = imgDisplayW;
		int layout_h = imgDisplayH;
		LayoutParams lp = new LayoutParams(layout_w,
				layout_h);
		lp.gravity = Gravity.CENTER;
//		lp.addRule(RelativeLayout.CENTER_IN_PARENT);
		this.addView(touchView, lp);
	}
}