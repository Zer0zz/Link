package com.self.link.identify;

import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.leconssoft.common.BaseMvp.BaseMvpHeadAct;
import com.leconssoft.common.BaseMvp.factory.CreateMvpPresenter;
import com.leconssoft.common.baseUtils.SPUtils;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;
import com.self.link.R;
import com.self.link.base.Constant;
import com.self.link.identify.adapter.NineGridAdapter;
import com.self.link.identify.listener.OnPicturesClickListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

import static com.self.link.utils.CommUtils.setClickBackEffect;

@CreateMvpPresenter(IdentifyPersenter.class)
public class IdentifyActivity extends BaseMvpHeadAct<IdentifyIView, IdentifyPersenter> implements IdentifyIView {


    @BindView(R.id.realname_et)
    EditText realName;
    @BindView(R.id.phone_et)
    EditText phone_et;
    @BindView(R.id.rv_images)

    RecyclerView rvImages;

    List<String> mSelect;

    @BindView(R.id.identifying_btn)
    Button identifying_btn;

    @BindView(R.id.upload_pb)
    ProgressBar upload_pb;

    String fitnessId;
    String mRealName;
    String mPhone;
    List<LocalMedia> resultList;
    List<String> fileNames;
    NineGridAdapter adapter;
    private final static int Pic_OK = 1001;

    @Override
    public int layoutId() {
        return R.layout.activity_identify;
    }

    @Override
    protected void initUIData() {
        setTvLeftMsg("健身房认证");
        setIvLeftSrc(R.mipmap.back_icon);
        Intent intent = getIntent();

        fitnessId = intent.getStringExtra("fitnessId");
        mRealName = SPUtils.getStringValue(Constant.SpKeyName.realName);
        mPhone = SPUtils.getStringValue(Constant.SpKeyName.phone);
        phone_et.setEnabled(false);
        realName.setEnabled(false);
        if (!TextUtils.isEmpty(mRealName)) {
            realName.setText(mRealName);
        } else {
            showErrMsg("请到个人中心实名认证");
        }
        if (!TextUtils.isEmpty(mPhone)) {
            phone_et.setText(mPhone);
        } else {

        }
        mSelect = new ArrayList<>();
        resultList = new ArrayList<>();
        fileNames = new ArrayList<>();

        setClickBackEffect(identifying_btn);

        rvImages.setLayoutManager(new GridLayoutManager(this, 3));
        adapter = new NineGridAdapter(this, mSelect);
        rvImages.setAdapter(adapter);
        adapter.setOnAddPicturesListener(new OnPicturesClickListener() {
            @Override
            public void onClick(int position) {
                //预览照片
                if (resultList != null)
                    PictureSelector.create(IdentifyActivity.this)
                            .externalPicturePreview(position, resultList);
            }

            @Override
            public void onAdd() {
                gallery();
            }
        });
        adapter.setOnDeleteItemListener(new NineGridAdapter.OnDeleteItemListener() {
            @Override
            public void onSelecting(int position) {

            }

            @Override
            public void onDeleted(int position) {
                resultList.remove(position);
            }
        });
    }


    @OnClick({R.id.identifying_btn})
    public void onClick(View view) {
        int viewId = view.getId();
        switch (viewId) {
            case R.id.identifying_btn:
                identifying();
                break;


        }
    }


    public void identifying() {
        if (TextUtils.isEmpty(mRealName)){
            showErrMsg("请到个人中心实名认证");
            return;
        }
        if (adapter.getItemCount() < 1 || resultList.size() < 1) {
            showErrMsg("最少传入一张验证图片！");
            return;
        }
        if (realName.getText().toString().trim().isEmpty()) {
            showErrMsg("输入真实姓名！");
            return;
        }
        if (phone_et.getText().toString().trim().isEmpty()) {
            showErrMsg("输入电话号码！");
            return;
        }
        showLodingDialog();
        fileNames = new ArrayList<>();
        for (int i = 0; i < resultList.size(); i++) {
            fileNames.add(resultList.get(i).getCompressPath());

        }
        upload_pb.setMax(mSelect.size() == 6 ? 6 : (mSelect.size() - 1));
        getMvpPresenter().uploadPics(fileNames, new IdentifyPersenter.OnFilesUploadListener() {
            @Override
            public void onComplited(List<String> uploadedFileNames, List<String> uploadedPath) {
//                upload_pb.set
                getMvpPresenter().authentication(fitnessId, uploadedFileNames, uploadedPath);
            }

            @Override
            public void onUploading(int succeedCount, int unUpload) {
                upload_pb.setVisibility(View.VISIBLE);
                upload_pb.setProgress(succeedCount);

            }

            @Override
            public void onError(String errMsgs, int position) {
                hidLodingDialog();
            }
        });

    }

    @Override
    public void showLodingDialog() {
        showProgress();
    }

    @Override
    public void hidLodingDialog() {
        hindProgress();
    }

    @Override
    public void showErrMsg(String msg) {
        Toast.makeText(mActivity, msg, Toast.LENGTH_SHORT).show();
    }


    //去图库选择
    private void gallery() {
        PictureSelector.create(this)
                .openGallery(PictureMimeType.ofImage())// 全部.PictureMimeType.ofAll()、图片.ofImage()、视频.ofVideo()、音频.ofAudio()
                .theme(R.style.picture_default_mstyle)// 主题样式设置 具体参考 values/styles   用法：R.style.picture.white.style
                .maxSelectNum(6) // 最大图片选择数量
                .minSelectNum(1) // 最小选择数量
                .isCamera(true) // 是否显示拍照按钮
                .imageSpanCount(3) // 每行显示个数
                .compress(true) // 是否压缩 true or false
                .isZoomAnim(true) // 图片列表点击 缩放效果 默认true
                .synOrAsy(true) //同步true或异步false 压缩 默认同步
                .glideOverride(120, 120) // glide 加载宽高，越小图片列表越流畅，但会影响列表图片浏览的清晰度
                .selectionMedia(resultList) //是否传入已选图片
                .minimumCompressSize(100) //小于100kb的图片不压缩
                .forResult(Pic_OK); //结果回调onActivityResult code
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data == null)
            return;

        switch (requestCode) {
            case Pic_OK:
                // 图片选择结果回调
                List<LocalMedia> resultCamera = PictureSelector.obtainMultipleResult(data);
                resultList = resultCamera;
                if (resultCamera != null && resultCamera.size() > 0) {
                    mSelect.clear();
                    for (LocalMedia localMedia : resultCamera) {
                        mSelect.add(localMedia.getCompressPath());
//                        Log.i("压缩地址:", localMedia.getCompressPath());
                    }
                    if (mSelect.size() < 6) {
                        mSelect.add("");
                    }
                    adapter.notifyDataSetChanged();
//                    Glide.with(mActivity).load(path).into(upload_head_iv);
                }
                break;

        }
    }
}
