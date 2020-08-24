package com.leconssoft.common.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.leconssoft.common.R;
import com.leconssoft.common.baseUtils.Constants;
import com.leconssoft.common.baseUtils.UIHelper;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;


/**
 * Created by yucheng on 2017-09-26.
 */

public class ShareSaveDialog extends Dialog implements
        View.OnClickListener {
    Context context;
    String imageUrl;

    public ShareSaveDialog(Context context, String imageUrl) {
        super(context, R.style.DialogTheme);
        setContentView(R.layout.dlg_share_save);
        this.context = context;
        this.imageUrl = imageUrl;
        setWindow();
        findViewById(R.id.btn_save).setOnClickListener(this);
        findViewById(R.id.btn_share).setOnClickListener(this);
        findViewById(R.id.btn_cancel).setOnClickListener(this);
    }

    private void setWindow() {
        Window window = getWindow();
        window.setGravity(Gravity.BOTTOM); // 此处可以设置dialog显示的位置
        WindowManager m = window.getWindowManager();
        Display d = m.getDefaultDisplay();
        WindowManager.LayoutParams p = window.getAttributes();
        p.width = d.getWidth();
        p.height = WindowManager.LayoutParams.WRAP_CONTENT;
        window.setAttributes(p);
    }

    @Override
    public void onClick(View v) {

        int i = v.getId();
        if (i == R.id.btn_share) {
            // TODO: 2018/9/28 这里有一个shareSDK的封装
//            OnekeyShare share = new OnekeyShare();
//            share.disableSSOWhenAuthorize();
//            share.addHiddenPlatform(WechatFavorite.NAME);
//            share.addHiddenPlatform(SinaWeibo.NAME);
//            share.addHiddenPlatform(WechatMoments.NAME);
//            if (imageUrl.contains("http")) {
//                share.setImageUrl(imageUrl);
//            } else {
//                share.setImagePath(GenerateImage(imageUrl, false));
//            }
//            share.show(context);
            dismiss();

        } else if (i == R.id.btn_save) {
            if (imageUrl.contains("http")) {
                downLoadImage(imageUrl);
            } else {
                GenerateImage(imageUrl, true);
            }
            dismiss();

        } else if (i == R.id.btn_cancel) {
            dismiss();

        }
    }

    public String spString(String string){
        String[]array=string.split(",");
        if(array.length>1){
            return array[1];
        }else {
            return string;
        }
    }

    public String GenerateImage(String imgStr, boolean flag) {// 对字节数组字符串进行Base64解码并生成图片
        File dirFile = new File(Constants.STORAGE_PICTURE);
        if (!dirFile.exists()) {
            dirFile.mkdir();
        }
        String fileName = "share" + ".jpg";
        File file=new File(Constants.STORAGE_PICTURE,fileName);
        if (imgStr == null) // 图像数据为空
            UIHelper.ToastMessage(context,"图像数据为空");
//        BASE64Decoder decoder = new BASE64Decoder();
        // TODO: 2018/9/28 这里有个一个base64编码的处理
        try {
            // Base64解码
//            byte[] bytes = decoder.decodeBuffer(spString(imgStr));
            byte[] bytes = new byte[0];
            for (int i = 0; i < bytes.length; ++i) {
                if (bytes[i] < 0) {// 调整异常数据
                    bytes[i] += 256;
                }
            }
            // 生成jpeg图片
            OutputStream out = new FileOutputStream(file);
            out.write(bytes);
            out.flush();
            out.close();
            if(flag){
            UIHelper.ToastMessage(context, "下载成功，请到SDCard下工程宝文件夹查看");
            }
            return file.getAbsolutePath();
        } catch (Exception e) {
            UIHelper.ToastMessage(context,"操作失败");
            return file.getAbsolutePath();
        }
    }

    public void downLoadImage(String imagePateh){
        File dirFile = new File(Constants.STORAGE_PICTURE);
        if (!dirFile.exists()) {
            dirFile.mkdir();
        }
        String fileName = "share" + ".jpg";
        File file=new File(Constants.STORAGE_PICTURE,fileName);
        // TODO: 2018/9/28  这里需要写一个图片下载的功能 
//        HttpUtils util = new HttpUtils();
//        util.download(imagePateh, file.getAbsolutePath(), false,false, new RequestCallBack<File>()
//        {
//            /**
//             * 重载方法
//             */
//            @Override
//            public void onStart()
//            {
//
//            }
//
//            @Override
//            public void onSuccess(ResponseInfo<File> arg0)
//            {
//                UIHelper.ToastMessage(context, "下载成功，请到SDCard下工程宝文件夹查看");
//
//            }
//
//            @Override
//            public void onFailure(HttpException arg0, String arg1)
//            {
//
//                UIHelper.ToastMessage(context, "下载失败");
//            }
//        });
    }
}
