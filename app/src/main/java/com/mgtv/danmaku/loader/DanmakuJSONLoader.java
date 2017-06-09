package com.mgtv.danmaku.loader;

import android.net.Uri;

import com.google.gson.Gson;
import com.mgtv.danmaku.parser.DanmakuListSource;

import java.io.InputStream;

import master.flame.danmaku.danmaku.loader.ILoader;
import master.flame.danmaku.danmaku.loader.IllegalDataException;
import master.flame.danmaku.danmaku.parser.IDataSource;

/**
 * desc
 *
 * @author zhouzhan
 * @since 2017-05-17
 */
public class DanmakuJSONLoader implements ILoader {

    private static volatile DanmakuJSONLoader mInstance;
    private DanmakuListSource mDataSource;

    public DanmakuJSONLoader() {
    }

    public static ILoader instance() {
        if(mInstance == null){
            synchronized (DanmakuJSONLoader.class){
                if(mInstance == null)
                    mInstance = new DanmakuJSONLoader();
            }
        }
        return mInstance;
    }

    @Override
    public IDataSource<?> getDataSource() {
        return mDataSource;
    }

    @Override
    public void load(String uri) throws IllegalDataException {
        try {
            mDataSource = new DanmakuListSource(uri);
        } catch (Exception e) {
            throw new IllegalDataException(e);
        }
    }

    @Override
    public void load(InputStream in) throws IllegalDataException {
        // 不想写
    }
}
