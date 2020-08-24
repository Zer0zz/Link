package com.self.link.personal;

import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.leconssoft.common.BaseMvp.BaseMvpHeadAct;
import com.leconssoft.common.BaseMvp.factory.CreateMvpPresenter;
import com.leconssoft.common.base.ViewManager;
import com.leconssoft.common.baseUtils.SPUtils;
import com.leconssoft.common.widgt.adress.LogUtils;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;
import com.self.link.R;
import com.self.link.base.Constant;
import com.self.link.base.UserInfo;
import com.self.link.firstin.FirstInActivity;
import com.self.link.identify.IdentifyActivity;
import com.self.link.login.LoginActivity;
import com.self.link.personal.change_info.ChangePersonInfoActivity;
import com.self.link.picture.ClipImageActivity;
import com.self.link.utils.wrapper.ActionSheetDialog;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

import static com.self.link.utils.CommUtils.imageToBase64;

@CreateMvpPresenter(PersonalInfoPersenter.class)
public class PersonalInfoActivity extends BaseMvpHeadAct<PersonalInfoIView, PersonalInfoPersenter> implements PersonalInfoIView {

    @BindView(R.id.upload_head_iv)
    ImageView upload_head_iv;

    @BindView(R.id.name_tv)
    TextView name_tv;

    @BindView(R.id.realname_tv)
    TextView realname_tv;

    @BindView(R.id.link_account_tv)
    TextView account_tv;
    @BindView(R.id.phone_tv)
    TextView phone_tv;


    private final static int Pic_OK = 1001;
    private final static int Camea_OK = 1002;
    private static final int CLIP = 1003;//裁剪
    UserInfo mUserInfo;

    @Override
    public int layoutId() {
        return R.layout.activity_personal_info;
    }

    @Override
    protected void initUIData() {
        EventBus.getDefault().register(this);
        setTvLeftMsg("个人信息");
        setIvLeftSrc(R.mipmap.back_icon);
    }

    /**
     * 接收初始的传递进来的用户信息和每次更改成功的用户信息
     *
     * @param userInfo
     */
    @Subscribe(threadMode = ThreadMode.MAIN, priority = 1, sticky = true)
    public void setUserInfo(UserInfo userInfo) {
        mUserInfo = userInfo;
        if (mUserInfo == null)
            return;
        if (mUserInfo.name != null)
            name_tv.setText(mUserInfo.name);
        if (mUserInfo.linkAccount != null)
            account_tv.setText(mUserInfo.linkAccount);
        if (mUserInfo.phone != null)
            phone_tv.setText(mUserInfo.phone);
        if (mUserInfo.realName != null) {
            realname_tv.setText(mUserInfo.realName);
        }
        if (!TextUtils.isEmpty(mUserInfo.headPortrait)) {
            Glide.with(mActivity).load(Constant.RequestUrl.imageBaseUrl+mUserInfo.headPortrait).into(upload_head_iv);
        }
    }

    @OnClick({R.id.loginout_btn, R.id.upload_head_iv, R.id.name_tv, R.id.realname_tv})
    public void onClick(View view) {
        int viewId = view.getId();
        switch (viewId) {
            case R.id.loginout_btn:
//                loginout();
                getMvpPresenter().logout();
                break;
            case R.id.upload_head_iv:
                selectPic();
                break;
            case R.id.name_tv:
                updateNickname();
                break;
            case R.id.realname_tv:
                uploadRealname();
                break;

        }
    }

    @Override
    public void updateNickname() {
        Intent intent = new Intent(this, ChangePersonInfoActivity.class);
        intent.putExtra("type", 0);
        intent.putExtra("userinfo", mUserInfo);
        startActivity(intent);
    }

    public void uploadRealname() {
        Intent intent = new Intent(this, ChangePersonInfoActivity.class);
        intent.putExtra("type", 1);
        intent.putExtra("userinfo", mUserInfo);
        startActivity(intent);
    }

    @Override
    public void updateBindPhoneNo() {

    }

    @Override
    public void loginout() {
        SPUtils.clearAll();
        ViewManager.getInstance().finishAllActivity();
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }

    @Override
    public UserInfo getUserInfo() {
        return mUserInfo;
    }

    @Override
    public void showLodingDialog() {

    }

    @Override
    public void hidLodingDialog() {

    }

    @Override
    public void showErrMsg(String msg) {
        Toast.makeText(mActivity, msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    String[] fileFrom = {"拍照", "图库"};

    public void selectPic() {
        ActionSheetDialog actionSheetDialog = new ActionSheetDialog(
                mActivity).builder().setCancelable(false)
                .setCanceledOnTouchOutside(true);
        for (int i = 0; i < fileFrom.length; i++) {
            actionSheetDialog.addSheetItem(fileFrom[i],
                    ActionSheetDialog.SheetItemColor.Blue,
                    new ActionSheetDialog.OnSheetItemClickListener() {
                        @Override
                        public void onClick(int which) {
                            switch (which) {
                                case 1:
                                    takePic();
                                    break;
                                case 2:
                                    gallery();
                                    break;
//                                case 3:
//                                   preview();
//                                    break;
                            }
                        }
                    });
        }
        actionSheetDialog.show();
    }

    private void preview() {
        try {
            LocalMedia localMedia = new LocalMedia();
            localMedia.setPath(mUserInfo.headPortrait);
            List<LocalMedia> localMediaList = new ArrayList<>();
            localMediaList.add(localMedia);
            PictureSelector.create(this)
                    .externalPicturePreview(0, localMediaList);
        } catch (Exception e) {
            Log.e("tag---", e.getMessage());
        }

    }


    //去图库选择
    private void gallery() {
        PictureSelector.create(this)
                .openGallery(PictureMimeType.ofImage())// 全部.PictureMimeType.ofAll()、图片.ofImage()、视频.ofVideo()、音频.ofAudio()
                // .theme(R.style.AppTheme)// 主题样式设置 具体参考 values/styles   用法：R.style.picture.white.style
                .maxSelectNum(1) // 最大图片选择数量
                .minSelectNum(1) // 最小选择数量
                .isCamera(false) // 是否显示拍照按钮
                .imageSpanCount(4) // 每行显示个数
                .compress(false) // 是否压缩 true or false
                .isZoomAnim(true) // 图片列表点击 缩放效果 默认true
                .synOrAsy(true) //同步true或异步false 压缩 默认同步
                .glideOverride(120, 120) // glide 加载宽高，越小图片列表越流畅，但会影响列表图片浏览的清晰度
                //.selectionMedia(picList) //是否传入已选图片
                .minimumCompressSize(100) //小于100kb的图片不压缩
                .forResult(Pic_OK); //结果回调onActivityResult code
    }

    //拍照
    private void takePic() {
        //单独拍照
        PictureSelector.create(this)
                .openCamera(PictureMimeType.ofImage())
//                .theme(R.style.AppTheme)// 主题样式设置 具体参考 values/styles
                .minSelectNum(1)// 最小选择数量
                .compress(true)
                .selectionMode(PictureConfig.MULTIPLE)// 多选 or 单选
                .isCamera(false)// 是否显示拍照按钮
                .glideOverride(120, 120)// glide 加载宽高，越小图片列表越流畅，但会影响列表图片浏览的清晰度
                //.selectionMedia(picList)// 是否传入已选图片
                .imageSpanCount(4)// 每行显示个数
                .maxSelectNum(1)// 最大图片选择数量
                .isZoomAnim(true)// 图片列表点击 缩放效果 默认true
                .synOrAsy(true)//同步true或异步false 压缩 默认同步
                .minimumCompressSize(100)// 小于100kb的图片不压缩
                .forResult(Camea_OK);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data == null)
            return;

        switch (requestCode) {
            case Camea_OK:
                // 拍照选择结果回调
                List<LocalMedia> resultCamera = PictureSelector.obtainMultipleResult(data);
                if (resultCamera != null && resultCamera.size() > 0) {
                    Intent cameraIntent = new Intent(mActivity, ClipImageActivity.class);
                    cameraIntent.putExtra("path", resultCamera.get(0).getPath());
                    startActivityForResult(cameraIntent, CLIP);
                }
                break;
            case Pic_OK:
                // 图片选择结果回调
                List<LocalMedia> resultGallery = PictureSelector.obtainMultipleResult(data);
                if (resultGallery != null && resultGallery.size() > 0) {
                    Intent picIntent = new Intent(mActivity, ClipImageActivity.class);
                    picIntent.putExtra("path", resultGallery.get(0).getPath());
                    startActivityForResult(picIntent, CLIP);
                }
                break;
            case CLIP:
                if (data != null) {
                    String path = data.getStringExtra("result_path");
                    int startStr = path.lastIndexOf('/');
                    int endStr = path.lastIndexOf('.');
                    Log.d("tag", "result_path:" + path);
                    Log.d("tag", "fiel_name:" + path.substring(startStr + 1));
                    String base64Str = imageToBase64(path);
                    getMvpPresenter().upLoadImg(base64Str, path.substring(startStr + 1));
//                    Glide.with(mActivity).load(path).into(upload_head_iv);
//                    uploadHead(path);
                }
                break;
        }


    }
}
