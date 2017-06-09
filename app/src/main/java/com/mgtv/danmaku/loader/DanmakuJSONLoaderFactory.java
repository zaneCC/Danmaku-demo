package com.mgtv.danmaku.loader;

import master.flame.danmaku.danmaku.loader.ILoader;
import master.flame.danmaku.danmaku.loader.android.AcFunDanmakuLoader;
import master.flame.danmaku.danmaku.loader.android.BiliDanmakuLoader;

/**
 * desc
 *
 * @author zhouzhan
 * @since 2017-05-17
 */
public class DanmakuJSONLoaderFactory {
    public static String TAG_BILI = "bili";
    public static String TAG_ACFUN = "acfun";
    public static String TAG_JSON = "json";

    public static ILoader create(String tag) {
        if (TAG_JSON.equalsIgnoreCase(tag)) {
            return DanmakuJSONLoader.instance();
        } else if (TAG_BILI.equalsIgnoreCase(tag)) {
            return BiliDanmakuLoader.instance();
        } else if(TAG_ACFUN.equalsIgnoreCase(tag)){
            return AcFunDanmakuLoader.instance();
        }
        return null;
    }
}
