package com.leconssoft.common.baseUtils.csustomdialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.leconssoft.common.R;


public class CommonAlertDialog extends Dialog implements View.OnClickListener {
    private Context mContext = null;
    private onMenuClickListener listener;

    private TextView tv_title = null;
    private TextView tv_content = null;
    private TextView tv_center_contents = null;
    private Button bt_cancel = null, bt_ok = null, bt_middle = null;
    private LinearLayout ll_content, ll_addition_content;
    private RadioGroup rg_selectFile;
    private RadioButton rb_isOpen, rb_isDownload;
    public int nowType = 1;//1为打开，2为下载

    public CommonAlertDialog(Context context) {
        super(context, R.style.dialog_hint_menu);
        mContext = context;
    }

    public void setOnMenuClickListener(onMenuClickListener listener) {
        this.listener = listener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Window window = this.getWindow();
        window.setGravity(Gravity.CENTER);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.common_alertdialog_view);

        WindowManager wm = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.width = wm.getDefaultDisplay().getWidth() - 30;
        getWindow().setAttributes(lp);

        tv_title = (TextView) findViewById(R.id.id_tv_title);
        tv_content = (TextView) findViewById(R.id.id_tv_contents);
        tv_center_contents = (TextView) findViewById(R.id.id_tv_center_contents);
        bt_ok = (Button) findViewById(R.id.id_bt_ok);
        bt_cancel = (Button) findViewById(R.id.id_bt_cancel);
        bt_middle = (Button) findViewById(R.id.id_bt_middle);
        ll_content = (LinearLayout) findViewById(R.id.ll_content);
        ll_addition_content = (LinearLayout) findViewById(R.id.ll_addition_content);

        rg_selectFile = (RadioGroup) findViewById(R.id.rg_selectFile);
        rb_isOpen = (RadioButton) findViewById(R.id.rb_isOpen);
        rb_isDownload = (RadioButton) findViewById(R.id.rb_isDownload);

        bt_cancel.setOnClickListener(this);
        bt_ok.setOnClickListener(this);
        bt_middle.setOnClickListener(this);
        rg_selectFile.setOnCheckedChangeListener(checkedChangeListener);
    }

    @Override
    public void onClick(View v) {
        if (null == listener) {
            onBackPressed();
            return;
        }
        int id = v.getId();
        if (id == R.id.id_bt_ok) {
            listener.onMenuClick(1);
            onBackPressed();
        }
        if (id == R.id.id_bt_cancel) {
            listener.onMenuClick(0);
            onBackPressed();
        }
        if (id == R.id.id_bt_middle) {
            listener.onMenuClick(2);
            onBackPressed();
        }
    }

    public interface onMenuClickListener {
        /**
         * 从左边起,0,1
         **/
        public void onMenuClick(int index);
    }

    /**
     * 设置标题
     **/
    public void setMenuTitle(String title) {
        tv_title.setText(title);
    }

    /**
     * 设置提示内容
     **/
    public void setContent(String content) {
        tv_content.setText(content);
    }

    /**
     * 设置提示内容res
     **/
    public void setContent(int contentRes) {
        tv_content.setText(contentRes);
    }

    /**
     * 设置ok按钮标题
     **/
    public void setBtnOKTitle(String title) {
        bt_ok.setText(title);
    }

    /**
     * 设置取消按钮标题
     **/
    public void setBtnCancelTitle(String title) {
        bt_cancel.setText(title);
    }

    /**
     * 设置middle按钮标题
     **/
    public void setBtnMdlTitle(String title) {
        bt_middle.setText(title);
    }

    /**
     * 设置是否显示取消按钮
     **/
    public void setBtnVisible(int leftVisible, int mdltVisible, int rightVisible) {
        bt_cancel.setVisibility(leftVisible);
        bt_middle.setVisibility(mdltVisible);
        bt_ok.setVisibility(rightVisible);
    }

    /**
     * 设置居中提示内容
     */
    public void setCenterContent(String centerContent) {
        tv_title.setVisibility(View.GONE);
        ll_content.setVisibility(View.GONE);
        ll_addition_content.setVisibility(View.VISIBLE);
        tv_center_contents.setText(centerContent);
    }

    /**
     * 设置提示打开和下载内容
     */
    public void setOpenAndDownloadContent(String content) {
        tv_content.setVisibility(View.GONE);
        rg_selectFile.setVisibility(View.VISIBLE);
        rb_isDownload.setText("是否重新下载" + content + "?");
    }

    private RadioGroup.OnCheckedChangeListener checkedChangeListener = new RadioGroup.OnCheckedChangeListener() {

        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {
            if (checkedId == rb_isOpen.getId()) {
                nowType = 1;
            } else {
                nowType = 2;
            }
        }
    };


}
