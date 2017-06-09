package com.mgtv.danmaku.entity;

import java.io.Serializable;

/**
 * desc
 *
 * @author zhouzhan
 * @since 2017-05-23
 */
public class BarrageJsonEntity implements Serializable {

    public static final int SUCCESS = 0;

    /**
     * 返回码，0表示成功
     */
    public int status;
    /**
     * 返回错误说明，在失败情况下会有详细信息
     */
    public String msg;
    /**
     * 请求唯一标识，方便服务定位问题
     */
    public String seq;
}
