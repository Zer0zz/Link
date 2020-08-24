package com.leconssoft.common.dialog;


import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Build;
import android.text.Html;
import android.text.TextUtils;
import android.view.Display;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.widget.EditText;
import android.widget.TextView;

import com.leconssoft.common.R;

import androidx.annotation.RequiresApi;


public class IphoneDialog extends Dialog implements View.OnClickListener{
	
	IphoneListener mlinsener;
	public TextView tvTitle;
	public EditText ed_content;
	public EditText tv_content;
	public TextView tv_ok;
	public TextView tv_canl;
	public View line;
	public boolean isText=false;
	protected boolean isneedFinish=false;
	public Context context;
	
	protected int layout;


	/**
	 * @param context
	 * @param linsener
	 * @param
	 * @param str 标题显示
	 * @param content 可输入dialog
	 */
	public IphoneDialog(Context context, IphoneListener linsener, String str, String content) {
		super(context, R.style.DialogTheme);
		this.context=context;
		mlinsener=linsener;
		setupLayout();
		setContentView(layout);
		tvTitle=(TextView) findViewById(R.id.id_tv_title);
		ed_content=(EditText) findViewById(R.id.ed_contents);
		tv_content=(EditText) findViewById(R.id.id_tv_contents);
		tv_ok=(TextView) findViewById(R.id.id_bt_ok);
		tv_canl=(TextView) findViewById(R.id.id_bt_cancel);
		line = findViewById(R.id.line);
		tv_canl.setOnClickListener(this);
		tv_ok.setOnClickListener(this);
		tvTitle.setText(str);
		tv_content.setVisibility(View.GONE);
		ed_content.setVisibility(View.VISIBLE);
		ed_content.setHint(content);
		ed_content.requestFocus();
		ed_content.setSelection(0);
		setWindow();
	}

	/**
	 * @param context
	 * @param linsener
	 * @param flag true=内容为输入 ,false=内容展示
	 * @param str 标题显示
	 * @param content 内容显示
	 */
	public IphoneDialog(Context context, IphoneListener linsener, boolean flag, String str, String content) {
		super(context,R.style.DialogTheme);
		this.context=context;
		mlinsener=linsener;
		setupLayout();
		 setContentView(layout);
		 tvTitle=(TextView) findViewById(R.id.id_tv_title);
		 ed_content=(EditText) findViewById(R.id.ed_contents);
		 tv_content=(EditText) findViewById(R.id.id_tv_contents);
		 tv_ok=(TextView) findViewById(R.id.id_bt_ok);
		 tv_canl=(TextView) findViewById(R.id.id_bt_cancel);
		 line = findViewById(R.id.line);
		 tv_canl.setOnClickListener(this);
		 tv_ok.setOnClickListener(this);
			isText=flag;
			tvTitle.setText(str);
			tv_content.setText(Html.fromHtml(content));
		    ed_content.setText(Html.fromHtml(content));
		    ed_content.setBackgroundResource(0);
			if(isText){
				tv_content.setVisibility(View.GONE);
				ed_content.setVisibility(View.VISIBLE);
				ed_content.setHint(content);
			}else {
				tv_content.setVisibility(View.GONE);
				ed_content.setVisibility(View.VISIBLE);
				ed_content.setFocusable(false);
			}
		 setWindow();
	}

	/**
	 * 
	 * @param context
	 * @param linsener
	 * @param flag true=内容为输入 ,false=内容展示
	 * @param str 标题显示
	 * @param content 内容显示
	 * @param btn 取消及确定按钮文字，只输入一个或不传则只显示确定按钮
	 */
	public IphoneDialog(Context context, IphoneListener linsener, boolean flag, String str, String content, String...btn) {
		super(context,R.style.DialogTheme);
		this.context=context;
		// TODO Auto-generated constructor stub
		mlinsener=linsener;
		 setupLayout();
		 setContentView(layout);
		 tvTitle=(TextView) findViewById(R.id.id_tv_title);
		 ed_content=(EditText) findViewById(R.id.ed_contents);
		 tv_content=(EditText) findViewById(R.id.id_tv_contents);
		 tv_ok=(TextView) findViewById(R.id.id_bt_ok);
		 tv_canl=(TextView) findViewById(R.id.id_bt_cancel);
		 line = findViewById(R.id.line);
		 tv_canl.setOnClickListener(this);
		 tv_ok.setOnClickListener(this);
		 if(btn == null || btn.length == 0){
			 tv_canl.setVisibility(View.GONE);
			 tv_ok.setText("确定");
			 tv_ok.setBackgroundResource(R.drawable.text_bottom);
		 }else if(btn.length == 1){
			 tv_canl.setVisibility(View.GONE);
			 tv_ok.setText(btn[0]);
			 tv_ok.setBackgroundResource(R.drawable.text_bottom);
		 }else{
			 tv_ok.setText(btn[0]);
			 tv_canl.setText(btn[1]);
		 }
			isText=flag;
			tvTitle.setText(str);
			tv_content.setText(content);
			if(isText){
				tv_content.setVisibility(View.GONE);
				ed_content.setVisibility(View.VISIBLE);
				ed_content.setHint(content);
			}else {
				tv_content.setVisibility(View.VISIBLE);
				ed_content.setVisibility(View.GONE);
			}
		 setWindow();
	}
	
	public IphoneDialog(Context context, final DialogClickListener leftListener, final DialogClickListener rightListener, boolean flag, String str, String content, String left, String right) {
		super(context,R.style.DialogTheme);
		this.context=context;
		setupLayout();
		 setContentView(layout);
		 tvTitle=(TextView) findViewById(R.id.id_tv_title);
		 ed_content=(EditText) findViewById(R.id.ed_contents);
		 tv_content=(EditText) findViewById(R.id.id_tv_contents);
		 tv_ok=(TextView) findViewById(R.id.id_bt_ok);
		 tv_canl=(TextView) findViewById(R.id.id_bt_cancel);
		 line = findViewById(R.id.line);
		 if(leftListener != null)
			 tv_canl.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					dismiss();
					leftListener.onDialogClick(v);
				}
			});
		 if(rightListener != null)
			 tv_ok.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					dismiss();
					rightListener.onDialogClick(v);
				}
			});
		 if(TextUtils.isEmpty(left)){
			 tv_canl.setVisibility(View.GONE);
			 line.setVisibility(View.GONE);
		 }
		 if(TextUtils.isEmpty(right)){
			 tv_ok.setVisibility(View.GONE);
			 line.setVisibility(View.GONE);
		 }
		 tv_canl.setText(left);
		 tv_ok.setText(right);
		isText=flag;
		if(TextUtils.isEmpty(str))
			tvTitle.setVisibility(View.GONE);
		else{
			tvTitle.setVisibility(View.VISIBLE);
			tvTitle.setText(str);
		}
		if(TextUtils.isEmpty(content))
			tv_content.setVisibility(View.GONE);
		else{
			tv_content.setVisibility(View.VISIBLE);
			tv_content.setText(content);
		}
		 setWindow();
	}
	
	protected void setupLayout(){
		layout = R.layout.dialog_iphone_view;
	}
	
	public interface IphoneListener{
		public void upContent(String string);
	}

	@Override
	public void onClick(View v) {
		int i = v.getId();
		if (i == R.id.id_bt_cancel) {
			dismiss();
		} else if (i == R.id.id_bt_ok) {
			if (isText) {
				mlinsener.upContent(ed_content.getText().toString());
			} else {
				mlinsener.upContent("");
			}
			dismiss();

		} else {
		}
	}



	public void setWindow() {
		// TODO Auto-generated method stub
		Window window = this.getWindow();
        window.setGravity(Gravity.CENTER); // 此处可以设置dialog显示的位置
        
        WindowManager m = window.getWindowManager();
        Display d = m.getDefaultDisplay();
        LayoutParams p = window.getAttributes();
        p.width = LayoutParams.MATCH_PARENT;
        p.height = LayoutParams.WRAP_CONTENT;
        window.setAttributes(p);
        window.setWindowAnimations(android.R.style.Animation);
        setCanceledOnTouchOutside(false);
	}
	/**
	 * 根据需求改变 取消确定的字体颜色 和提示文字
	 * @param tvColor 取消与确定的颜色
	 * @param tvLeft  取消的text
	 * @param tvRight 确定text
	 */
	public void changeStyle(int tvColor, String tvLeft, String tvRight){
		 tv_ok.setTextColor(tvColor);
		 tv_canl.setTextColor(tvColor);
		 tv_ok.setText(tvRight);
		 tv_canl.setText(tvLeft);
	}
	/**
	 * 
	 * @param visableorgone 参数隐藏 消失
	 * @return 
	 */
	public IphoneDialog setCacelBtuttonInvisable(int visableorgone){
		
		if(visableorgone== View.GONE||visableorgone== View.VISIBLE||visableorgone== View.INVISIBLE)
		tv_canl.setVisibility(visableorgone);
		return this;
	}
	/**
	 * 按返回键是否要销毁当前activity
	 * @param isneedFinish if true means finish
	 */
	public IphoneDialog setKeyBackTofinishActivity(boolean isneedFinish){
		this.isneedFinish=isneedFinish;
		return this;
	}
	public interface DialogClickListener{
		public void onDialogClick(View view);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(isneedFinish&&context instanceof Activity){
			((Activity)context).finish();
		}
		return super.onKeyDown(keyCode, event);
	}


	@RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
	@Override
	public void show() {
		try{
			if(context instanceof Activity && ((Activity)context).isFinishing())
				return;
			super.show();
		}catch (Exception e){}
	}
}
