package com.mgtv.danmaku.util;

import android.content.Context;
import android.provider.Settings;
import android.support.annotation.Px;

public class ScreenUtil {

    /**
     * convert dip value to px value.
     * 
     * @param context
     *            the context.
     * @param dpValue
     *            dip value.
     * @return px value.
     */
    @Px
    public static int dip2px(Context context, float dpValue) {
        try {
            final float scale = context.getResources().getDisplayMetrics().density;
            return (int) (dpValue * scale + 0.5f);
        } catch (Exception e) {
            return -1;
        }
    }

    /**
     * convert px value to dip value.
     * 
     * @param context
     *            the context.
     * @param pxValue
     *            px value.
     * @return dip value.
     */
    public static int px2dip(Context context, float pxValue) {
        try {
            final float scale = context.getResources().getDisplayMetrics().density;
            return (int) (pxValue / scale + 0.5f);
        } catch (Exception e) {
            return -1;
        }
    }

    /**
     * convert sp value to px value.
     * 
     * @param context
     *            the context.
     * @param spValue
     *            sp value.
     * @return px value.
     */
    public static int sp2px(Context context, float spValue) {
        try {
            final float scale = context.getResources().getDisplayMetrics().scaledDensity;
            return (int) (spValue * scale + 0.5f);
        } catch (Exception e) {
            return -1;
        }
    }

    /**
     * convert px value to sp value.
     * 
     * @param context
     *            the context.
     * @param pxValue
     *            px value.
     * @return sp value.
     */
    public static int px2sp(Context context, float pxValue) {
        try {
            final float scale = context.getResources().getDisplayMetrics().scaledDensity;
            return (int) (pxValue / scale + 0.5f);
        } catch (Exception e) {
            return -1;
        }
    }

    /**
     * get screen width pixels
     * 
     * @return
     */

    public static int getScreenWidth(Context context) {
        try {
            return context.getResources().getDisplayMetrics().widthPixels;
        } catch (Exception e) {
            return -1;
        }
    }


    /**
     * get screen height pixels
     * 
     * @return
     */

    public static int getScreenHeight(Context context) {
        try {
             return context.getResources().getDisplayMetrics().heightPixels;
        } catch (Exception e) {
            return -1;
        }
    }

    /**
     * get screen density dpi.
     * 
     * @return
     */
    public static int getScreenDpi(Context context) {
        try {
            return context.getResources().getDisplayMetrics().densityDpi;
        } catch (Exception e) {
        return -1;
    }
    }

    public static boolean isSystemRotationLocked(Context context) {
        try {
            return Settings.System.getInt(context.getContentResolver(),
                    Settings.System.ACCELEROMETER_ROTATION) == 0;
        } catch (Settings.SettingNotFoundException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 获取系统状态栏的高度
     */
    public static int getStatusBarHeight(Context context) {
        int result = 0;
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = context.getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

}