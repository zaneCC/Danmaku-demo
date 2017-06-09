package com.mgtv.danmaku.parser;

import android.text.TextUtils;

import com.google.gson.Gson;
import com.mgtv.danmaku.entity.DanmakuListEntity;
import com.mgtv.danmaku.util.MockData;

import java.io.InputStream;
import java.net.URL;
import java.util.Iterator;
import java.util.List;

import master.flame.danmaku.danmaku.parser.IDataSource;
import master.flame.danmaku.danmaku.util.IOUtils;

/**
 * desc
 *
 * @author zhouzhan
 * @since 2017-05-17
 */
public class DanmakuListSource implements IDataSource<DanmakuListEntity> {

    private DanmakuListEntity mDataSource;
    private Gson gson;

    // "http://galaxy.person.mgtv.com/rdbarrage?vid=7001&time=60&device=2"
    public DanmakuListSource(String urlStr) {
        // TODO: 17/5/17 数据请求
//        try {
//            URL url = new URL(urlStr);
//            InputStream inputStream = url.openStream();
//            init(inputStream);
//        } catch (java.io.IOException e) {
//            e.printStackTrace();
//        }

        // Mock data
        mDataSource = MockData.initMockEntries();
    }

    private void init(InputStream in) {
        if (in == null)
            throw new NullPointerException("input stream cannot be null!");
        String json = IOUtils.getString(in);
        init(json);
    }

    private void init(String json) {
        if (!TextUtils.isEmpty(json)) {
            gson = new Gson();
            mDataSource = gson.fromJson(json, DanmakuListEntity.class);
        }
    }

    @Override
    public DanmakuListEntity data() {
        return mDataSource;
    }

    @Override
    public void release() {
        mDataSource = null;
    }
}
