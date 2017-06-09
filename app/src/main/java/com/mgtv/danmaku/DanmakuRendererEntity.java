package com.mgtv.danmaku;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;

import com.mgtv.danmaku.entity.DanmakuListEntity;

import master.flame.danmaku.danmaku.model.BaseDanmaku;

/**
 * 单条弹幕渲染信息
 *
 * @author zhouzhan
 * @since 2017-05-15
 */
public class DanmakuRendererEntity {
    /** 普通弹幕，默认类型 */
    public static final int TYPE_TEXT = 0;
    /** 边框弹幕 */
    public static final int TYPE_BORDER = 1;
    /** 头像弹幕 */
    public static final int TYPE_AVATAR = 2;
    /** 本地弹幕 */
    public static final int TYPE_LOCAL = 3;

    /** 边框弹幕 颜色类型 */
    public static final int[] BG_BORDER_TYPES = {3, 4, 5};

    public static final int BG_3 = Color.parseColor("#50E59C08");
    public static final int BG_4 = Color.parseColor("#5014C71D");
    public static final int BG_5 = Color.parseColor("#50F51D1D");

    /** 头像弹幕 样式类型 */
    public static final int[] BG_AVATAR_TYPE = {0, 1, 2};

    /** 边框弹幕 默认颜色 */
    public static final int DEFAULT_BG = BG_4;

    /** 本地弹幕颜色 */
    public static final int BG_LOCAL = Color.WHITE;

    /** 弹幕点击之后的颜色 */
    public int clickedColor = Color.parseColor("#F06000");

    /** 点赞数 */
    public int praiseCount;
    /** 弹幕类型 */
    public int type = TYPE_TEXT;
    /** 主播头像 */
    public Bitmap avatarBitmap;
    /** 主播昵称 */
    public String uname;
    /** 内容 */
    public String content;
    /** 内容颜色 */
    public int contentColor = Color.WHITE;
    /** 是否是人气弹幕 */
    public boolean isHot;
    /** 是否是本地弹幕 */
    public boolean isLocal;
    /** 是否已经点赞 */
    public boolean isPraised;
    /** 头像的url */
    public String avatar;
    /** 边框背景编号(3\4\5) */
    public int bg;
    /** 弹幕字体颜色 */
    public int color;
    /** 弹幕字体大小 */
    public int size;
    /** API 返回的每条弹幕对象 */
    public DanmakuListEntity.Item item;

    /** 弹幕渲染类型－默认从右往左渲染 */
    public int renderType = BaseDanmaku.TYPE_SCROLL_RL;

    public void increasePraiseCount(){
        praiseCount++;
    }

    public static Drawable getAvatar0(Context context){
        return context.getResources().getDrawable(R.drawable.bulletscreen_anchor_bg);
    }

    public static Drawable getAvatar1(Context context){
        return context.getResources().getDrawable(R.drawable.bulletscreen_item_bg);
    }

    public static Drawable getAvatar2(Context context){
        return context.getResources().getDrawable(R.drawable.bulletscreen_reserved_bg);
    }

    public void initEntry(DanmakuListEntity.Item item){
        this.content = item.content;
        this.uname = item.uname;
        this.type = item.type;
        this.isHot = item.hot;
        this.avatar = item.avatar;
        this.bg = item.bg;
        this.praiseCount = item.up;
        this.color = item.color;
        this.size = item.size;
        this.item = item;
    }

}
