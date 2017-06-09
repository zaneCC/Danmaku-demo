package com.mgtv.danmaku.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * desc
 * test url: http://galaxy.person.mgtv.com/rdbarrage?vid=7001&time=60&device=2
 *
 * @author zhouzhan
 * @since 2017-05-15
 */
public class DanmakuListEntity extends BarrageJsonEntity implements Serializable, Cloneable {

    public Data data;

    public static class Data implements Serializable, Cloneable {
        /**
         * 下一次触发拉取弹幕时间轴
         */
        public int next;
        /**
         * 弹幕拉取区间间隔
         */
        public int interval;
        /**
         * 弹幕具体数据
         */
        public List<Item> items;


        @Override
        public Object clone() {
            Data o = null;
            try {
                o = (Data) super.clone();
                if (items == null)
                    return o;

                List<Item> copy = new ArrayList<>(items.size());

                for (Item item : items) {
                    copy.add((Item) item.clone());
                }
                o.items = copy;

            } catch (CloneNotSupportedException e) {
                System.out.println(e.toString());
            }
            // 这样是不是也可以实现深copy?
//            List<Item> src = items;
//            List<Item> dic = new ArrayList<>(Arrays.asList(new Item[src
//                    .size()]));
//            Collections.copy(dic, src);
            return o;
        }

        @Override
        public String toString() {
            return "Data{" +
                    "next=" + next +
                    ", interval=" + interval +
                    ", items=" + items +
                    '}';
        }
    }

    /**
     * 单条弹幕实体
     */
    public static class Item implements Serializable, Cloneable {
        /**
         * 弹幕id (int64)
         */
        public long id;
        /**
         * 弹幕类型（文字弹幕）/1（边框弹幕）/2（头像弹幕）
         */
        public int type;
        /**
         * 边框背景编号
         */
        public int bg;
        /**
         * 是否热门弹幕 (optional)
         */
        public boolean hot;
        /**
         * 用户标识id（uuid经过转换处理）
         */
        public long uid;
        /**
         * 用户昵称（主播用户选填字段）(optional)
         */
        public String uname;
        /**
         * 头像地址（主播用户选填字段）(optional)
         */
        public String avatar;
        /**
         * 弹幕正文
         */
        public String content;
        /**
         * 弹幕相对视频时间轴(单位：秒)
         */
        public int time;
        /**
         * 是否vip (optional)
         */
        public boolean vip;
        /**
         * 弹幕字体大小 (optional)
         */
        public int size;
        /**
         * 弹幕字体颜色 (optional)
         */
        public int color;
        /**
         * 弹幕显示位置 (optional)
         */
        public int position;
        /**
         * 弹幕点赞数
         */
        public int up;


        @Override
        public Object clone() {
            Object o = null;
            try {
                o = super.clone();
            } catch (CloneNotSupportedException e) {
                System.out.println(e.toString());
            }
            return o;
        }

        @Override
        public String toString() {
            return "Item{" +
                    "id=" + id +
                    ", type=" + type +
                    ", bg=" + bg +
                    ", hot=" + hot +
                    ", uid=" + uid +
                    ", uname='" + uname + '\'' +
                    ", avatar='" + avatar + '\'' +
                    ", content='" + content + '\'' +
                    ", time=" + time +
                    ", vip=" + vip +
                    ", size=" + size +
                    ", color=" + color +
                    ", position=" + position +
                    ", up=" + up +
                    '}';
        }
    }

    @Override
    public Object clone() {
        DanmakuListEntity o = null;
        try {
            o = (DanmakuListEntity) super.clone();
            o.data = (Data) data.clone();
        } catch (CloneNotSupportedException e) {
            System.out.println(e.toString());
        }

        return o;
    }

    @Override
    public String toString() {
        return "DanmakuListEntity{" +
                "status=" + status +
                ", msg='" + msg + '\'' +
                ", seq='" + seq + '\'' +
                ", data=" + data +
                '}';
    }
}
