/*
 * Copyright (C) 2010 ZXing authors
 * Download by http://www.codefans.net
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.self.link.utils;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;

public final class MyTimer {

    private ScheduledExecutorService service = Executors
            .newSingleThreadScheduledExecutor(new DaemonThreadFactory());
    private ScheduledFuture<?> mFuture;

    public MyTimer(int timeout) {
        cancel();
        mFuture = service.schedule(new Runnable() {
            @Override
            public void run() {

            }
        }, timeout, TimeUnit.SECONDS);

    }

    /**
     * 创建定时器对象并开始定时任务
     *
     * @param timeout  定时器超时时间
     * @param runnable 定时器任务对象
     */
    public MyTimer(int timeout, Runnable runnable) {
        cancel();
        mFuture = service.schedule(runnable, timeout,
                TimeUnit.SECONDS);
    }


    /**
     * 获取定时器剩余时间
     *
     * @return 剩余时间（单位秒,-1超时）
     */
    public long getOutTime() {
        if (mFuture != null) {
            long time = mFuture.getDelay(TimeUnit.SECONDS);
            if (time < 0) {
                time = -1;
                shutdown();
            }
            return time;
        }

        return -1;
    }


    /**
     * 结束定时器
     */
    public void shutdown() {
        service.shutdown();
        cancel();

    }

    private void cancel() {
        if (mFuture != null) {
            mFuture.cancel(true);
            mFuture = null;
        }
    }

    private static final class DaemonThreadFactory implements ThreadFactory {
        @Override
        public Thread newThread(Runnable runnable) {
            Thread thread = new Thread(runnable);
            thread.setDaemon(true);
            return thread;
        }
    }
}
