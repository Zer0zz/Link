package com.self.link;

import android.content.Intent;

import com.leconssoft.common.base.BaseApp;
import com.leconssoft.common.baseUtils.SPUtils;
import com.self.link.service.LinkService;



/**
 * description：
 * author：Administrator on 2020/5/19 17:22
 */
public class LinkApp extends BaseApp {
    public static LinkApp sInstance;

    public synchronized static LinkApp getIns() {
        return sInstance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        sInstance = this;
        SPUtils.setSPUtilsContext(sInstance);
//        startService(new Intent(this, LinkService.class));
    }


    @Override
    public void onTerminate() {
        // 程序终止的时候执行
        stopService(new Intent(this, LinkService.class));
        super.onTerminate();
    }
}
