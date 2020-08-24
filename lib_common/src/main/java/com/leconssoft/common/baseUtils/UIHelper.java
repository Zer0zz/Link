package com.leconssoft.common.baseUtils;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;


import com.leconssoft.common.R;

import java.util.Timer;
import java.util.TimerTask;


/**
 * 弹出Toast消息
 * 
 * @author xrzh_one
 */
public class UIHelper {

	/**
	 * 弹出Toast消息
	 */
	private UIHelper(){}
	
	private static Toast toast;
	private static TextView tvContent;
	private static View view;
	private static void initUI(Context context) {
		LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		toast = new Toast(context);
		view = inflater.inflate(R.layout.toast_layout, null);
		toast.setGravity(Gravity.CENTER,0,0);
		tvContent = (TextView)view.findViewById(R.id.tvContent);
		
	}

	public static void ToastMessage(Context cont, String msg) {
		initUI(cont);
		tvContent.setText(msg);
		toast.setView(view);
		toast.setDuration(Toast.LENGTH_SHORT);
//		toast.show();
		showMyToast(toast,3000);
	}

	public static void ToastMessage(Context cont, int msg) {
		initUI(cont);
		tvContent.setText(msg);
		toast.setView(view);
		toast.setDuration(Toast.LENGTH_SHORT);
		toast.show();
	}

	public static void ToastMessage(Context cont, String msg, int time) {
		initUI(cont);
		tvContent.setText(msg);
		toast.setView(view);
		toast.setDuration(time);
		toast.show();
	}

	public static void showMyToast(final Toast toast, final int cnt) {
		final Timer timer = new Timer();
		timer.schedule(new TimerTask() {
			@Override
			public void run() {
				toast.show();
			}
		}, 0, 3500);
		new Timer().schedule(new TimerTask() {
			@Override
			public void run() {
				toast.cancel();
				timer.cancel();
			}
		}, cnt );
	}
}
