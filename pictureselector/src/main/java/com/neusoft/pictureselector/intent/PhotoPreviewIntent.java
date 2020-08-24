package com.neusoft.pictureselector.intent;

import android.content.Context;
import android.content.Intent;

import com.neusoft.pictureselector.PhotoPickerActivity;
import com.neusoft.pictureselector.PhotoPreviewActivity;

import java.util.ArrayList;

/**
 * 预览照片
 */
public class PhotoPreviewIntent extends Intent{

    public PhotoPreviewIntent(Context packageContext) {
        super(packageContext, PhotoPreviewActivity.class);
    }

    /**
     * 照片地址
     * @param paths
     */
    public void setPhotoPaths(ArrayList<String> paths){
        this.putStringArrayListExtra(PhotoPreviewActivity.EXTRA_PHOTOS, paths);
    }

    /**
     * 照片地址
     * @param paths
     */
    public void setPhotoPaths(String[] paths){
        this.putExtra(PhotoPreviewActivity.EXTRA_PHOTOS,paths);
    }
    /**
     * 当前照片的下标
     * @param currentItem
     */
    public void setCurrentItem(int currentItem){
        this.putExtra(PhotoPreviewActivity.EXTRA_CURRENT_ITEM, currentItem);
    }
    public void setWhichPosition(int position){
        this.putExtra(PhotoPreviewActivity.WHICH_POSITION,position);
    }
}
