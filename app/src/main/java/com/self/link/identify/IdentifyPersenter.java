package com.self.link.identify;

import android.util.Log;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.leconssoft.common.BaseMvp.model.BaseModel;
import com.leconssoft.common.BaseMvp.presenter.BasePresenter;
import com.leconssoft.common.NetService.OnHttpCallBack;
import com.leconssoft.common.base.BaseResponse;
import com.self.link.base.Constant;
import com.self.link.base.UploadImgBack;
import com.self.link.base.UserInfo;
import com.self.link.fitness.body.Attachment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static com.self.link.base.Constant.RequsetCode.AUTHENTICATION_ID;
import static com.self.link.base.Constant.RequsetCode.UPLOAD_IMAGEBASE64_ID;
import static com.self.link.utils.CommUtils.imageToBase64;

/**
 * description：
 * author：Administrator on 2020/5/24 14:33
 */
public class IdentifyPersenter extends BasePresenter<IdentifyIView> {
    int succeedCount = 0;
    int unUpload;
    List<String> fileNames;
    List<String> path;
    boolean isFailed = false;

    /**
     * @param filePaths
     * @param listener
     */
    public void uploadPics(List<String> filePaths, OnFilesUploadListener listener) {
        fileNames = new ArrayList<>();
        path = new ArrayList<>();
        succeedCount = 0;
        unUpload = filePaths.size();
        for (int i = 0; i < filePaths.size() && !isFailed; i++) {
            String base64Str = imageToBase64(filePaths.get(i));
            int startStr = filePaths.get(i).lastIndexOf('/');
            String fileName = filePaths.get(i).substring(startStr + 1);
            Map map = new HashMap();
            map.put("base64Str", base64Str);
            map.put("fileName", fileName);
            map.put("thuSizeX", 180);
            map.put("thuSizeY", 180);
            if (listener != null)
                listener.onUploading(0, unUpload);

            new BaseModel().netReqModleNew.postJsonHttp(Constant.RequestUrl.uploadImageBase64, UPLOAD_IMAGEBASE64_ID, map, new OnHttpCallBack<String>() {
                @Override
                public void onSuccessful(int id, String res) {
                    if (UPLOAD_IMAGEBASE64_ID == id) {
                        Log.e("tag", res);
                        BaseResponse baseResponse = JSONObject.parseObject(res, BaseResponse.class);
                        if (baseResponse.isSuccess()) {
                            UploadImgBack back = JSONObject.parseObject(baseResponse.getRe(), UploadImgBack.class);
                            succeedCount++;
                            unUpload--;
                            fileNames.add(fileName);
                            path.add(back.path);
                        } else {//失败
                            getMvpView().showErrMsg(baseResponse.getMsg());
                            isFailed = true;
                        }

                        if (listener != null) {
                            if (isFailed) {//失败了
                                listener.onError(baseResponse.getCode()+"", succeedCount++);
                                return;
                            } else {//成功
                                listener.onUploading(succeedCount, unUpload);
                            }
                            if (succeedCount == filePaths.size()) {
                                listener.onComplited(fileNames, path);
                            }
                        }
                    }

                }

                @Override
                public void onFaild(int id, String res, String err) {
                    if (UPLOAD_IMAGEBASE64_ID == id) {

                        getMvpView().showErrMsg(err);
                        if (listener != null) {
                            isFailed = true;
                            listener.onError(err, succeedCount++);
                        }
                    }
                }
            });
        }
    }

    //照片上传
    public void uploadPic(String fileName, String base64Str) {
        Map map = new HashMap();
        map.put("base64Str", base64Str);
        map.put("fileName", fileName);
        map.put("thuSizeX", 180);
        map.put("thuSizeY", 180);
        new BaseModel().netReqModleNew.postJsonHttp(Constant.RequestUrl.uploadImageBase64, UPLOAD_IMAGEBASE64_ID, map, new OnHttpCallBack<String>() {
            @Override
            public void onSuccessful(int id, String res) {
                if (UPLOAD_IMAGEBASE64_ID == id) {
                    Log.e("tag", res);
                    BaseResponse baseResponse = JSONObject.parseObject(res, BaseResponse.class);
                    if (baseResponse.isSuccess()) {
                        UploadImgBack back = JSONObject.parseObject(baseResponse.getRe(), UploadImgBack.class);

                    } else {//失败
                        getMvpView().showErrMsg(baseResponse.getMsg());
                    }

                }

            }

            @Override
            public void onFaild(int id, String res, String err) {
                if (UPLOAD_IMAGEBASE64_ID == id) {
                    getMvpView().showErrMsg(err);
                }
            }
        });
    }

    public void authentication(String fitnessid, List<String> fileNames, List<String> paths) {
        Map map = new HashMap();
        map.put("fitnessId", fitnessid);
        List<Attachment> attachmentList = new ArrayList();

//        StringBuffer attachmentList = new StringBuffer();
//        attachmentList.append("[");
        for (int i = 0; i < fileNames.size(); i++) {
//            attachmentList.put("name", fileNames.get(i));
//            attachmentList.put("path", paths.get(i));
//            attachmentList.append("{");
//            attachmentList.append("\"name\":\"" + fileNames.get(i)+"\",");
//            attachmentList.append("\"path\":\"" + paths.get(i)+"\"");
//            attachmentList.append("},");

            Attachment attachment = new Attachment(fileNames.get(i), paths.get(i));
            attachmentList.add(attachment);

        }
//        attachmentList.append("]");
        map.put("attachmentList", attachmentList);
        Object l = JSONObject.toJSON(map);
        new BaseModel().netReqModleNew.postJsonHttp(Constant.RequestUrl.authentication, AUTHENTICATION_ID, map, new OnHttpCallBack<String>() {
            @Override
            public void onSuccessful(int id, String res) {
                BaseResponse baseResponse = JSONObject.parseObject(res, BaseResponse.class);
                if (baseResponse.isSuccess()) {
                    getMvpView().showErrMsg("提交认证成功");
                } else {
                    getMvpView().showErrMsg("提交认证失败:" + baseResponse.getMsg());
                }
                getMvpView().hidLodingDialog();
            }

            @Override
            public void onFaild(int id, String res, String err) {
                getMvpView().showErrMsg("提交认证失败:" + err);
                getMvpView().hidLodingDialog();
            }
        });
    }

    public interface OnFilesUploadListener {
        void onComplited(List<String> fileNames, List<String> path);

        void onUploading(int succeedCount, int unUpload);

        void onError(String errMsgs, int position);

    }
}
