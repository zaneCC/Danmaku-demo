package com.mgtv.danmaku.util;



import com.mgtv.danmaku.DanmakuRendererEntity;
import com.mgtv.danmaku.entity.DanmakuListEntity;

import java.util.ArrayList;
import java.util.List;

/**
 * 后台给的弹幕数据不够完整， 做一个假数据啦
 *
 * @author zhouzhan
 * @since 2017-06-01
 */
public class MockData {

    // TODO: 17/6/1 for test
    public static DanmakuListEntity initMockEntries() {
        DanmakuListEntity entity = new DanmakuListEntity();
        DanmakuListEntity.Data data = new DanmakuListEntity.Data();
        List<DanmakuListEntity.Item> items = new ArrayList<>();

//        // 5 个头像弹幕
//        int avatarTime = 300;
//        for (int i = 0; i < 5; i++) {
//            DanmakuListEntity.Item item = new DanmakuListEntity.Item();
//            item.type = DanmakuRendererEntity.TYPE_AVATAR;
//            if (i % 2 == 0){
//                item.hot = true; // 人气弹幕
//                item.up = 666;
//            }else {
//                item.hot = false;
//            }
//
//            item.uname = "zane";
//            item.content = "我从未见过如此厚颜无耻之猴";
//            item.time = avatarTime;
//            avatarTime += 1000;
//            item.avatar = "http://p1.hunantv.com/2/ava2_6SoyOBRDsYNkD5yOiHZAK85QbCiiuvyR.jpg";
//            items.add(item);
//        }
//
//        // 5 个边框弹幕
//        int borderTime = avatarTime + 500;
//        for (int i = 0; i < 5; i++){
//            DanmakuListEntity.Item item = new DanmakuListEntity.Item();
//            item.type = DanmakuRendererEntity.TYPE_BORDER;
//            if (i % 2 == 0){
//                item.hot = true; // 人气弹幕
//                item.up = 666;
//                item.bg = 3;
//            }else {
//                item.hot = false;
//                item.bg = 4;
//            }
//
//            item.uname = "zane";
//            item.content = "迷之抖腿~~~~~";
//            item.time = borderTime;
//            borderTime += 1000;
//            items.add(item);
//        }
//        // 3 个普通弹幕
//        int textTime = borderTime + 500;
//        for (int i = 0; i < 3; i++){
//            DanmakuListEntity.Item item = new DanmakuListEntity.Item();
//            item.type = DanmakuRendererEntity.TYPE_TEXT;
//            if (i % 2 == 0){
//                item.hot = true; // 人气弹幕
//                item.up = 666;
//            }else {
//                item.hot = false;
//            }
//
//            item.uname = "zane";
//            item.content = "抖腿抖得都要痉挛了~~~";
//            item.time = textTime;
//            textTime += 1000;
//            items.add(item);
//        }
//        // 4 个本地弹幕
//        int localTime = textTime + 500;
//        for (int i = 0; i < 3; i++){
//            DanmakuListEntity.Item item = new DanmakuListEntity.Item();
//            item.type = DanmakuRendererEntity.TYPE_LOCAL;
//
//            item.uname = "zane";
//            item.content = "本地弹幕，吗的智障啊";
//            item.time = localTime;
//            localTime += 1000;
//            items.add(item);
//        }

        // 10 个头像弹幕
        int avatarTime2 = 3000; // 1s
        for (int i = 0; i < 120; i++) { // 2 min
            DanmakuListEntity.Item item = addBarrage(avatarTime2);
            items.add(item);
            avatarTime2 += 1000;
        }

        data.interval = 60;
        data.items = items;
        entity.data = data;

        return entity;
    }

    private static DanmakuListEntity.Item addBarrage(int time) {

        DanmakuListEntity.Item item = new DanmakuListEntity.Item();
        item.type = DanmakuRendererEntity.TYPE_AVATAR;

        item.uname = "zane";
        item.content = "我从未见过如此厚颜无耻之猴2";
        item.time = time;

        item.avatar = "http://p1.hunantv.com/2/ava2_6SoyOBRDsYNkD5yOiHZAK85QbCiiuvyR.jpg";

        return item;
    }
}
