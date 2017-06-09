package com.mgtv.danmaku.parser;

import com.mgtv.danmaku.DanmakuRendererEntity;

import java.util.Iterator;

import master.flame.danmaku.danmaku.model.BaseDanmaku;
import master.flame.danmaku.danmaku.model.android.Danmakus;

/**
 *
 * @author zhouzhan
 * @since 2017-05-25
 */
public class FilterDanmakus extends Danmakus {

    @Override
    public void forEachSync(Consumer<? super BaseDanmaku, ?> consumer) {
        synchronized (this.mLockObject) {
            forEach(consumer);
        }
    }

    @Override
    public void forEach(Consumer<? super BaseDanmaku, ?> consumer) {
        consumer.before();
        Iterator<BaseDanmaku> it = items.iterator();
        while (it.hasNext()) {
            BaseDanmaku next = it.next();
            if (next == null) {
                continue;
            }

            if (!(next.tag instanceof DanmakuRendererEntity)){
                break;
            }

            long time = next.getTime();
            if (time > 3000){
                items.remove(next);
            }

        }

        consumer.after();

        // 循环渲染
        super.forEach(consumer);
    }
}
