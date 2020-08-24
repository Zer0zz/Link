package com.leconssoft.common.baseUtils.csustomdialog;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;

import android.view.Display;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.Window;
import android.view.WindowManager;

import com.leconssoft.common.R;

import androidx.annotation.NonNull;
import androidx.annotation.StyleRes;


/**
 * Created by liuGang on 2017/4/26.
 */

public class MainProgressDialog extends Dialog {
    private Context context = null;
    private static MainProgressDialog mainProgressDialog = null;


    public MainProgressDialog(@NonNull Context context) {
        super(context);

    }

    public MainProgressDialog(@NonNull Context context, @StyleRes int themeResId) {
        super(context,themeResId );
        this.context=context;
        setContentView(R.layout.dlg_main_progress);
        setCanceledOnTouchOutside(false);


        setOnKeyListener(new OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialogInterface, int i, KeyEvent keyEvent) {
                if (keyEvent.getKeyCode()== KeyEvent.KEYCODE_BACK) {
                    if (isShowing()) {
                        dismiss();
                    }
                }
                return false;
            }
        });
    }

    public void setFullScreen(boolean isFullScreen){
        if (isFullScreen){
            Window window = getWindow();
            WindowManager m = getWindow().getWindowManager();
            Display d = m.getDefaultDisplay();
            WindowManager.LayoutParams p = window.getAttributes();
            p.width = d.getWidth();
            p.height = d.getHeight();
//            window.getDecorView().setPadding(0,0,0,0);
            window.setAttributes(p);
        }

    }


    public static MainProgressDialog createDialog(Context context){
        mainProgressDialog = new MainProgressDialog(context, R.style.MainProgressDialogStyle);
        mainProgressDialog.setContentView(R.layout.dlg_main_progress);
        mainProgressDialog.getWindow().getAttributes().gravity = Gravity.CENTER;
        Window window = mainProgressDialog.getWindow();
        window.setGravity(Gravity.CENTER); // 此处可以设置dialog显示的位置

        WindowManager m = window.getWindowManager();
        Display d = m.getDefaultDisplay();
        WindowManager.LayoutParams p = window.getAttributes();
        p.width = WindowManager.LayoutParams.MATCH_PARENT;
        p.height = WindowManager.LayoutParams.MATCH_PARENT;
        window.setAttributes(p);
        mainProgressDialog.setCanceledOnTouchOutside(false);

        return mainProgressDialog;
    }

}
