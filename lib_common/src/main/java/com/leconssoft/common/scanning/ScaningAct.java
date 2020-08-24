package com.leconssoft.common.scanning;

import android.Manifest;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.WriterException;
import com.leconssoft.common.R;
import com.leconssoft.common.base.BaseHeadActivity;
import com.leconssoft.common.baseUtils.Common;
import com.leconssoft.common.baseUtils.Utils;
import com.leconssoft.common.scanning.android.CaptureActivity;
import com.leconssoft.common.scanning.bean.ZxingConfig;
import com.leconssoft.common.scanning.encode.CodeCreator;


/**
 * <p>类说明</p>
 * 这个是二维码扫描功能 用的时候记得导jar包
 * @author yucheng
 * @date 2018-9-26
 * @Description
 */
public class ScaningAct extends BaseHeadActivity implements View.OnClickListener{
    Button scanBtn;
    TextView result;
    EditText contentEt;
    Button encodeBtn;
    ImageView contentIv;
    private int REQUEST_CODE_SCAN = 111;
    /**
     * 生成带logo的二维码
     */
    Button encodeBtnWithLogo;
    ImageView contentIvWithLogo;
    private String contentEtString;





    @Override
    public int layoutId() {
        return R.layout.act_login_scanning;
    }



    @Override
    protected void initUIData() {
        scanBtn =findViewById(R.id.scanBtn);
        result=findViewById(R.id.result);
        contentEt=findViewById(R.id.contentEt);
        encodeBtn=findViewById(R.id.encodeBtn);
        contentIv=findViewById(R.id.contentIv);
        encodeBtnWithLogo= findViewById(R.id.encodeBtnWithLogo);
        contentIvWithLogo=findViewById(R.id.contentIvWithLogo);
        scanBtn.setOnClickListener(this);
        encodeBtn.setOnClickListener(this);
        encodeBtnWithLogo.setOnClickListener(this);

    }

   @Override
    public void onClick(View v) {
        Bitmap bitmap = null;
       int i = v.getId();
       if (i == R.id.scanBtn) {/**
        * 判断是否获取了拍照权限
        */
           if (Utils.hasPermission(mActivity, new String[]{Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE})) {
               Intent intent = new Intent(ScaningAct.this, CaptureActivity.class);
               /*ZxingConfig是配置类
                *可以设置是否显示底部布局，闪光灯，相册，
                * 是否播放提示音  震动
                * 设置扫描框颜色等
                * 也可以不传这个参数
                * */
               ZxingConfig config = new ZxingConfig();
               config.setPlayBeep(true);//是否播放扫描声音 默认为true
               config.setShake(true);//是否震动  默认为true
               config.setDecodeBarCode(false);//是否扫描条形码 默认为true
               config.setReactColor(R.color.white);//设置扫描框四个角的颜色 默认为淡蓝色
               config.setFrameLineColor(R.color.white);//设置扫描框边框颜色 默认无色
               config.setFullScreenScan(false);//是否全屏扫描  默认为true  设为false则只会在扫描框中扫描
               intent.putExtra(Common.INTENT_ZXING_CONFIG, config);
               startActivityForResult(intent, REQUEST_CODE_SCAN);
           } else {
               Utils.grantedPermissions(mActivity, new String[]{Manifest.permission.CAMERA,}, "请打开拍照权限");
           }
//        /**
//         * 判断是否获取了拍照权限
//         */
//        if (Utils.hasPermission(mActivity, new String[]{Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE})) {
//            /**
//             * 是否有权限读取本地图片
//             */
//            Uri packageURI = Uri.parse("package:" + getPackageName());
//            Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, packageURI);
//            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//
//            startActivity(intent);
//        } else {
//            Utils.grantedPermissions(mActivity, new String[]{
//                            Manifest.permission.CAMERA,
//                            Manifest.permission.READ_EXTERNAL_STORAGE
//                    },
//                    "请打开拍照权限,存储权限");
//        }

       } else if (i == R.id.encodeBtn) {
           contentEtString = contentEt.getText().toString().trim();
           if (TextUtils.isEmpty(contentEtString)) {
               Toast.makeText(this, "请输入要生成二维码图片的字符串", Toast.LENGTH_SHORT).show();
               return;
           }


           try {
               bitmap = CodeCreator.createQRCode(contentEtString, 400, 400, null);

           } catch (WriterException e) {
               e.printStackTrace();
           }
           if (bitmap != null) {
               contentIv.setImageBitmap(bitmap);
           }


       } else if (i == R.id.encodeBtnWithLogo) {
           contentEtString = contentEt.getText().toString().trim();
           if (TextUtils.isEmpty(contentEtString)) {
               Toast.makeText(this, "请输入要生成二维码图片的字符串", Toast.LENGTH_SHORT).show();
               return;
           }

           bitmap = null;
           try {
               Bitmap logo = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);
               bitmap = CodeCreator.createQRCode(contentEtString, 400, 400, logo);

           } catch (WriterException e) {
               e.printStackTrace();
           }
           if (bitmap != null) {
               contentIvWithLogo.setImageBitmap(bitmap);
           }


       } else {
       }

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // 扫描二维码/条码回传
        if (requestCode == REQUEST_CODE_SCAN && resultCode == RESULT_OK) {
            if (data != null) {

                String content = data.getStringExtra(Common.CODED_CONTENT);
                result.setText("扫描结果为：" + content);
            }
        }
    }
}
