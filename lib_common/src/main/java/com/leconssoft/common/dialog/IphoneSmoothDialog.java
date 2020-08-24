package com.leconssoft.common.dialog;

import android.content.Context;

import com.leconssoft.common.R;

public class IphoneSmoothDialog extends IphoneDialog {

	public IphoneSmoothDialog(Context context, IphoneListener linsener,
                              boolean flag, String str, String content) {
		super(context, linsener, flag, str, content);
	}

	public IphoneSmoothDialog(Context context,
                              final DialogClickListener leftListener,
                              final DialogClickListener rightListener, boolean flag, String str,
                              String content, String left, String right) {
		super(context, leftListener, rightListener, flag, str, content, left, right);
	}

	public IphoneSmoothDialog(Context context, IphoneListener linsener,
                              boolean flag, String str, String content, String[] btn) {
		super(context, linsener, flag, str, content, btn);
	}

	@Override
	protected void setupLayout() {
		layout = R.layout.iphone_smooth_layout;
	}
	
	public void setTitle(String title){
		tvTitle.setText(title);
	}
	
	public void setContent(String content){
		tv_content.setText(content);
	}
	
	public void setLeftText(String text){
		tv_canl.setText(text);
	}
	
	public void setRightText(String text){
		tv_ok.setText(text);
	}

	public void setLeftTextColor(int color){
		tv_canl.setTextColor(context.getResources().getColor(color));
	}

	public void setRightTextColor(int color){
		tv_ok.setTextColor(context.getResources().getColor(color));
	}
}
