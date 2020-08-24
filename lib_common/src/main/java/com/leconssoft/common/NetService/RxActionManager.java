package com.leconssoft.common.NetService;

import rx.Subscription;

/**
 * Created by yucheng on 2017-04-11.
 */

public interface RxActionManager<T> {
    void add(T tag, Subscription subscription);
    void remove(T tag);

    void cancel(T tag);

    void cancelAll();
}
