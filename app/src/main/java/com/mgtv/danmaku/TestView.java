package com.mgtv.danmaku;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.NinePatchDrawable;
import android.util.AttributeSet;

/**
 * desc
 *
 * @author zhouzhan
 * @since 2017-05-26
 */
public class TestView extends android.support.v7.widget.AppCompatTextView {


    public TestView(Context context) {
        super(context);
    }

    public TestView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public TestView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        NinePatchDrawable bg =  (NinePatchDrawable) getContext().getResources().getDrawable(R.drawable.bulletscreen_anchor_bg);
        if (bg != null) {
            bg.setBounds(10, 10, 400, 200);
            bg.draw(canvas);
        }
    }
}
