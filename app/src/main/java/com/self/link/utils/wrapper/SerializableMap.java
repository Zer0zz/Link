package com.self.link.utils.wrapper;

import java.io.Serializable;
import java.util.HashMap;

/**
 * description：
 * author：zhangCl on 2018/10/26 14:49
 */
public class SerializableMap<K,V>  implements Serializable {

    private HashMap<K, V> map;

    public HashMap<K, V> getMap() {
        return map;
    }

    public void setMap(HashMap<K, V> map) {
        this.map = map;
    }
}
