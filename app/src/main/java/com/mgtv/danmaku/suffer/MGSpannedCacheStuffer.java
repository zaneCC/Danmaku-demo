package com.mgtv.danmaku.suffer;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.NinePatchDrawable;
import android.support.annotation.NonNull;
import android.text.TextPaint;
import android.text.TextUtils;
import android.util.Log;

import com.mgtv.danmaku.DanmakuRendererEntity;
import com.mgtv.danmaku.R;

import master.flame.danmaku.danmaku.model.BaseDanmaku;
import master.flame.danmaku.danmaku.model.android.AndroidDisplayer;
import master.flame.danmaku.danmaku.model.android.SimpleTextCacheStuffer;
import master.flame.danmaku.danmaku.model.android.SpannedCacheStuffer;

/**
 * 图文弹幕渲染层
 * <p>
 * 由以下部分组成：
 * 1、头像(optional)
 * 2、文字
 * 3、点赞数(optional)
 * <p>
 * 具体类型 see {@link DanmakuRendererEntity}
 *
 * @author zhouzhan
 * @since 2017-05-12
 */
public class MGSpannedCacheStuffer extends SimpleTextCacheStuffer {

    private static final String TAG = MGSpannedCacheStuffer.class.getSimpleName();

    private int mAvatorDiameter; //头像直径
    private int mAvatorPadding; // 头像边框宽度

    private int mContentLeftPadding; // 文字和头像间距
    private int mContentRightPadding; // 文字和右边线距离
    private int mContentTextSize; // 内容文字大小

    private int mPraiseRightPadding; // 点赞和右边线距离
    private int mPraiseTextSize; // 点赞文字大小
    private int mPraiseTextColor = 0xffeeeeee;  //点赞文字  白色
    private int mPraiseImgWidth;

    private float mAvatorWidth;
    private float mContentWidth;
    private float mPraiseWidth;

    private Context mContext;

    public MGSpannedCacheStuffer(Context context) {
        // 初始化固定参数，这些参数可以根据自己需求自行设定
        mAvatorDiameter = 100;
        mAvatorPadding = 2;
        mContentLeftPadding = 32;
        mContentRightPadding = 32;
        // TODO: 17/6/1 不对噢 ???
//        ScreenUtil.px2sp(context, context.getResources().getDimensionPixelSize(R.dimen.font_30));
        mContentTextSize = 40;
        mPraiseTextSize = 30;
        mPraiseRightPadding = 80;
        mPraiseImgWidth = 32;

        this.mContext = context;
    }

    @Override
    public void measure(BaseDanmaku danmaku, TextPaint paint, boolean fromWorkerThread) {
        // 初始化数据
        DanmakuRendererEntity danmakuInfo = (DanmakuRendererEntity) danmaku.tag;
        if (null == danmakuInfo) {
            return;
        }

        int type = danmakuInfo.type;
        switch (type) {
            case DanmakuRendererEntity.TYPE_AVATAR:
                measureAvatarBarrage(danmaku);
                break;
            case DanmakuRendererEntity.TYPE_BORDER:
                measureBorderBarrage(danmaku);
                break;
            case DanmakuRendererEntity.TYPE_LOCAL:
                measureLocalBarrage(danmaku);
                break;
            case DanmakuRendererEntity.TYPE_TEXT:
                measureTextBarrage(danmaku);
                break;
        }

    }

    private void measureTextBarrage(BaseDanmaku danmaku) {
        DanmakuRendererEntity danmakuInfo = (DanmakuRendererEntity) danmaku.tag;
        Paint paint = new Paint();
        paint.setTextSize(mContentTextSize);

        String content = danmakuInfo.content;
        float textWidth = paint.measureText(content);

        String praiseCount = String.valueOf(danmakuInfo.praiseCount);
        if (danmakuInfo.isHot){
            setPraiseWidth(praiseCount, paint);
        }else {
            mPraiseWidth = 0;
        }

        mContentWidth = textWidth;

        danmaku.paintWidth = mContentWidth + mPraiseWidth + mContentRightPadding;// 设置弹幕区域的宽度
        danmaku.paintHeight = mAvatorDiameter; // 设置弹幕区域的高度
    }

    private void measureLocalBarrage(BaseDanmaku danmaku) {
        DanmakuRendererEntity danmakuInfo = (DanmakuRendererEntity) danmaku.tag;

        Paint paint = new Paint();
        paint.setTextSize(mContentTextSize);

        String content = danmakuInfo.content;
        float textWidth = paint.measureText(content);

        mContentWidth = textWidth;

        danmaku.paintWidth = mContentRightPadding + mContentLeftPadding + mContentWidth;// 设置弹幕区域的宽度
        danmaku.paintHeight = mAvatorDiameter; // 设置弹幕区域的高度
    }

    private void measureBorderBarrage(BaseDanmaku danmaku) {
        DanmakuRendererEntity danmakuInfo = (DanmakuRendererEntity) danmaku.tag;

        Paint paint = new Paint();
        paint.setTextSize(mContentTextSize);

        String content = danmakuInfo.content;
        float textWidth = paint.measureText(content);

        String praiseCount = String.valueOf(danmakuInfo.praiseCount);
        if (danmakuInfo.isHot){
            setPraiseWidth(praiseCount, paint);
        }else {
            mPraiseWidth = 0;
        }

        mContentWidth = textWidth;

        danmaku.paintWidth = mContentLeftPadding + mContentWidth + mPraiseWidth + mContentRightPadding;// 设置弹幕区域的宽度
        danmaku.paintHeight = mAvatorDiameter; // 设置弹幕区域的高度
    }

    private void measureAvatarBarrage(BaseDanmaku danmaku) {
        DanmakuRendererEntity danmakuInfo = (DanmakuRendererEntity) danmaku.tag;

        Paint paint = new Paint();
        paint.setTextSize(mContentTextSize);

        String name = danmakuInfo.uname;
        String content = danmakuInfo.content;

        // 计算内容的长度
        String text = getContent(name, content);
        float textWidth = paint.measureText(text);

        String praiseCount = String.valueOf(danmakuInfo.praiseCount);
        if (danmakuInfo.isHot){
            setPraiseWidth(praiseCount, paint);
        }else {
            mPraiseWidth = 0;
        }

        // 设置弹幕区域的宽高
        mAvatorWidth = mAvatorDiameter + mAvatorPadding;
        mContentWidth = textWidth /**+ mContentRightPadding */;

        danmaku.paintWidth = mAvatorWidth + mContentLeftPadding + mContentWidth + mPraiseWidth + mContentRightPadding; // 设置弹幕区域的宽度
        danmaku.paintHeight = mAvatorDiameter; // 设置弹幕区域的高度
    }

    private void setPraiseWidth(String praiseText, Paint paint){
        float praiseTextWidth = paint.measureText(praiseText);
        mPraiseWidth = mPraiseImgWidth + praiseTextWidth;
    }

    @Override
    public void drawDanmaku(BaseDanmaku danmaku,
                            Canvas canvas,
                            float left,
                            float top,
                            boolean fromWorkerThread,
                            AndroidDisplayer.DisplayerConfig displayerConfig) {

        DanmakuRendererEntity danmakuInfo = (DanmakuRendererEntity) danmaku.tag;
        if (null == danmakuInfo)
            return;

        float praiseLeft = danmaku.paintWidth - mPraiseWidth - mContentRightPadding;
        int type = danmakuInfo.type;
        switch (type) {
            case DanmakuRendererEntity.TYPE_AVATAR:
                drawAvatarBarrage(canvas, top, left, danmaku);
                break;
            case DanmakuRendererEntity.TYPE_BORDER:
                drawBorderBarrage(canvas, top, left, danmaku);
                break;
            case DanmakuRendererEntity.TYPE_LOCAL:
                drawLocalBarrage(canvas, top, left, danmaku);
                break;
            case DanmakuRendererEntity.TYPE_TEXT:
                drawText(canvas, left, danmakuInfo.content, danmakuInfo.contentColor);
                break;
        }

//        if (needDrawPraiseCount(danmakuInfo)) {
            drawPraiseCount(canvas, praiseLeft, top, danmakuInfo);
//        }
    }

    /**
     * 绘制点赞数
     */
    private void drawPraiseCount(Canvas canvas, float praiseLeft, float top, @NonNull DanmakuRendererEntity danmakuInfo) {
        int praiseCount = danmakuInfo.praiseCount;
        if (praiseCount <= 0){
            return;
        }
        // 设置画笔
        Paint paint = new Paint();
        paint.setTextSize(mPraiseTextSize);

        paint.setColor(Color.WHITE);
        float contentBottom = mAvatorDiameter / 2 + mAvatorDiameter / 4;

        // 绘制点赞icon
        Bitmap bitmap = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.bulletscreen_smallnice_icon);
        float iconTop = contentBottom - mPraiseImgWidth;
        float iconRight = praiseLeft + mPraiseImgWidth;
        float iconBottom = contentBottom;
        canvas.drawBitmap(bitmap, null, new RectF(praiseLeft, iconTop, iconRight, iconBottom), paint);

        // 绘制点赞数
        setShadow(paint);
        canvas.drawText(String.valueOf(praiseCount), praiseLeft + mPraiseImgWidth, contentBottom, paint);
    }

    /**
     * 绘制边框（类型）弹幕
     */
    private void drawBorderBarrage(Canvas canvas, float top, float left, @NonNull BaseDanmaku danmaku) {

        DanmakuRendererEntity danmakuInfo = (DanmakuRendererEntity) danmaku.tag;

        int bg = danmakuInfo.bg;
        int color = DanmakuRendererEntity.DEFAULT_BG;

        if (bg == DanmakuRendererEntity.BG_BORDER_TYPES[0]) {
            color = DanmakuRendererEntity.BG_3;
        } else if (bg == DanmakuRendererEntity.BG_BORDER_TYPES[1]) {
            color = DanmakuRendererEntity.BG_4;
        } else if (bg == DanmakuRendererEntity.BG_BORDER_TYPES[2]) {
            color = DanmakuRendererEntity.BG_5;
        }

        Rect rect = new Rect();
        Paint bgPaint = new Paint();
        bgPaint.getTextBounds(danmakuInfo.content, 0, danmakuInfo.content.length(), rect);
        bgPaint.setColor(color);
        bgPaint.setAntiAlias(true);

        int bgLeft = (int) (left);
        int bgRight = (int) danmaku.paintWidth;
        int bgBottom = (int) (top + mAvatorDiameter);

        canvas.drawRoundRect(new RectF(bgLeft, top, bgRight, bgBottom), 45, 45, bgPaint);
        // 绘制弹幕内容
        float contentLeft = left + mContentLeftPadding;
        drawText(canvas, contentLeft, danmakuInfo.content, danmakuInfo.contentColor);
    }

    /**
     * 绘制本地（类型）弹幕
     */
    private void drawLocalBarrage(Canvas canvas, float top, float left, @NonNull BaseDanmaku danmaku) {
        DanmakuRendererEntity danmakuInfo = (DanmakuRendererEntity) danmaku.tag;

        Rect rect = new Rect();
        Paint bgPaint = new Paint();
        bgPaint.setStrokeWidth(2);
        bgPaint.getTextBounds(danmakuInfo.content, 0, danmakuInfo.content.length(), rect);
        bgPaint.setStyle(Paint.Style.STROKE);
        bgPaint.setColor(DanmakuRendererEntity.BG_LOCAL);
        bgPaint.setAntiAlias(true);

        int bgLeft = (int) (left);
        int bgRight = (int) danmaku.paintWidth;
        int bgBottom = (int) (top + mAvatorDiameter);

        canvas.drawRoundRect(new RectF(bgLeft, top, bgRight, bgBottom), 45, 45, bgPaint);
        // 绘制弹幕内容
        float contentLeft = left + mContentLeftPadding;
        drawText(canvas, contentLeft, danmakuInfo.content, danmakuInfo.contentColor);
    }

    /**
     * 绘制头像（类型）弹幕
     */
    private void drawAvatarBarrage(Canvas canvas,
                                   float top,
                                   float left,
                                   @NonNull BaseDanmaku danmaku) {
        DanmakuRendererEntity danmakuInfo = (DanmakuRendererEntity) danmaku.tag;

        int bg = danmakuInfo.bg;
        NinePatchDrawable bgDrawable = (NinePatchDrawable) DanmakuRendererEntity.getAvatar0(mContext);

        if (bg == DanmakuRendererEntity.BG_AVATAR_TYPE[0]) {
            bgDrawable = (NinePatchDrawable) DanmakuRendererEntity.getAvatar0(mContext);

        } else if (bg == DanmakuRendererEntity.BG_AVATAR_TYPE[1]) {
            bgDrawable = (NinePatchDrawable) DanmakuRendererEntity.getAvatar1(mContext);

        } else if (bg == DanmakuRendererEntity.BG_AVATAR_TYPE[2]) {
            bgDrawable = (NinePatchDrawable) DanmakuRendererEntity.getAvatar2(mContext);
        }

        int bgLeft = (int) (left + mAvatorWidth);
        int bgRight = (int) danmaku.paintWidth;
        int bgBottom = (int) (top + mAvatorDiameter);

        // 绘制背景
        if (bgDrawable != null) {
            bgDrawable.setBounds(bgLeft, (int) top, bgRight, bgBottom);
            bgDrawable.draw(canvas);
        }

        // 绘制头像
        Paint paint = new Paint();

        float avatarLeft = left;
        float avatarTop = top;
        float avatarRight = left + mAvatorDiameter;
        float avatarBottom = top + mAvatorDiameter;

        Bitmap avatarBitmap = danmakuInfo.avatarBitmap;

        if (null != avatarBitmap)
            canvas.drawBitmap(avatarBitmap, null, new RectF(avatarLeft, avatarTop, avatarRight, avatarBottom), paint);

        // 绘制弹幕内容
        // 头像弹幕展示，展示不超过5个中文字符，超出后"..."代替，无值不展示
        String uname = danmakuInfo.uname;
        String text = getContent(uname, danmakuInfo.content);

        float contentLeft = left + mAvatorWidth + mContentLeftPadding;

        drawText(canvas, contentLeft, text, danmakuInfo.contentColor);
    }

    private String getContent(String uname, String content) {
        String name = "";
        if (!TextUtils.isEmpty(uname)) {

            int length = uname.length();
            if (length > 5) {
                uname = uname.substring(0, 5);
                uname += "...";
            }
            uname += "：";
            name = uname;
        }
        return name + content;
    }

    private void drawText(Canvas canvas, float contentLeft, String text, int textColor) {
        // 设置画笔
        Paint paint = new Paint();
        paint.setTextSize(mContentTextSize);

        paint.setColor(textColor);
        // 居中???
        float contentBottom = mAvatorDiameter / 2 + mAvatorDiameter / 4;
        // 描边
        setShadow(paint);
        canvas.drawText(text, contentLeft, contentBottom, paint);
    }

    /**
     * 统一描边
     */
    private void setShadow(Paint paint) {
        paint.setShadowLayer(2, 0, 0, Color.BLACK);
    }

    /**
     * 是否绘制点赞
     *
     * @param info 弹幕渲染信息
     */
    public boolean needDrawPraiseCount(DanmakuRendererEntity info) {
        return info.isHot;
    }


    @Override
    public void clearCache(BaseDanmaku danmaku) {
        super.clearCache(danmaku);
    }

    @Override
    public void releaseResource(BaseDanmaku danmaku) {
        super.releaseResource(danmaku);
    }


}
