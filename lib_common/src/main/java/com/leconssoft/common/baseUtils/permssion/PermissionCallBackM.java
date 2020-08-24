package com.leconssoft.common.baseUtils.permssion;

/**
 * author: baiiu
 * date: on 16/12/20 19:32
 * description:
 */
public interface PermissionCallBackM {
    void onPermissionGrantedM(int requestCode, String... perms);

    void onPermissionDeniedM(int requestCode, String... perms);
}
