package com.leconssoft.common.NetService.subscriber;

public enum HttpExceptionReason {
    /**
     * 服务器失联
     */
    SERVE_ERROR,
    /**
     * 解析数据失败
     */
    PARSE_ERROR,
    /**
     * 网络问题
     */
    BAD_NETWORK,
    /**
     * 连接错误
     */
    CONNECT_ERROR,
    /**
     * 连接超时
     */
    CONNECT_TIMEOUT,
    /**
     * 未知错误
     */
    UNKNOWN_ERROR,

    /**
     * 访问出错
     */
    LODING_ERROR,

}
