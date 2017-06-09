package com.mgtv.danmaku.parser;

import android.graphics.Color;
import android.support.annotation.Nullable;
import android.util.Log;

import com.mgtv.danmaku.DanmakuRendererEntity;
import com.mgtv.danmaku.entity.DanmakuListEntity;

import java.util.List;

import master.flame.danmaku.danmaku.model.BaseDanmaku;
import master.flame.danmaku.danmaku.model.IDanmakus;
import master.flame.danmaku.danmaku.model.IDisplayer;
import master.flame.danmaku.danmaku.model.android.Danmakus;
import master.flame.danmaku.danmaku.parser.BaseDanmakuParser;
import master.flame.danmaku.danmaku.parser.IDataSource;

/**
 * 弹幕解析器
 *
 * @author zhouzhan
 * @since 2017-05-17
 */
public class DanmakuListParser extends BaseDanmakuParser {

    private static final String TAG = DanmakuListParser.class.getSimpleName();

    public Danmakus mResult = new Danmakus();

    public DanmakuListParser() {
    }

    @Nullable
    @Override
    protected IDanmakus parse() {
        if (mDataSource == null) {
            Log.e(TAG, "dataSource == null");
            return null;
        }
        if (!(mDataSource instanceof DanmakuListSource)){
            Log.e(TAG, "dataSource 必须是 " + DanmakuListEntity.class.getSimpleName() + "实例");
            return null;
        }
        DanmakuListSource dataSource = (DanmakuListSource) mDataSource;

        DanmakuListEntity listEntity = dataSource.data();
        if (null == listEntity)
            return null;
        DanmakuListEntity.Data data = listEntity.data;
        if (null == data)
            return null;

        List<DanmakuListEntity.Item> items = data.items;
        if (items.isEmpty())
            return null;

        for (DanmakuListEntity.Item item : items){
            DanmakuRendererEntity rendererEntity = new DanmakuRendererEntity();

            rendererEntity.initEntry(item);

            BaseDanmaku danmaku = mContext.mDanmakuFactory.createDanmaku(rendererEntity.renderType, mContext);
            if (null != danmaku){
                danmaku.tag = rendererEntity;
                danmaku.text = "";

                danmaku.padding = 5;
                danmaku.priority = 0;  // 可能会被各种过滤器过滤并隐藏显示

                // 新增弹幕登场时间，单位 毫秒
                danmaku.setTime(item.time);

                int color = Color.WHITE;
                danmaku.textColor = color;
                // 描边的颜色
                danmaku.textShadowColor = Color.BLACK;
//                danmaku.paintWidth = 4;
                // 弹幕是30号字体大小
//                float textSize = 24;
//                danmaku.textSize = textSize * (mDispDensity - 0.6f);

                danmaku.flags = mContext.mGlobalFlagValues;
                danmaku.setTimer(mTimer);

                Object lock = mResult.obtainSynchronizer();
                synchronized (lock) {
                    mResult.addItem(danmaku);
                }
            }
        }

        return mResult;
    }

    @Override
    public BaseDanmakuParser load(IDataSource<?> source) {
        this.mDataSource = source;
        return this;
    }

    @Override
    public BaseDanmakuParser setDisplayer(IDisplayer disp) {
        super.setDisplayer(disp);
        return this;
    }

    public DanmakuListEntity getDataSource(){
        return (DanmakuListEntity) mDataSource;
    }
}
