package com.example.walle9.roundindicatorview;

import android.content.Context;
import android.content.res.Resources;

/**
 * Created by walle9 on 2018/3/26.
 * 描述:  封装和UI相关的操作
 */

public class UIUtils {
    /**
     * 得到上下文
     *
     * @return
     */
    public static Context getContext() {
        return MyApplication.getContext();
    }

    /**
     * 得到Resources对象
     *
     * @return
     */
    public static Resources getResources() {
        return getContext().getResources();
    }

    /**
     * 得到String.xml中的字符串信息
     *
     * @param resId
     * @return
     */
    public static String getString(int resId) {
        return getResources().getString(resId);
    }

    /**
     * 得到String.xml中的字符串数组信息
     *
     * @param resId
     * @return
     */
    public static String[] getStrings(int resId) {
        return getResources().getStringArray(resId);
    }

    /**
     * 得到Colol.xml中的颜色信息
     *
     * @param resId
     * @return
     */
    public static int getColor(int resId) {
        return getResources().getColor(resId);
    }

    /**
     * 得到应用程序的包名
     *
     * @return
     */
    public static String getPackageName() {
        return getContext().getPackageName();
    }

    /**
     * dip转px
     * @param dip
     * @return
     */
    public static int dip2Px(int dip) {
        //公式1.px/(ppi/160) = dp
        //公式2.px/dp = density

        //int ppi = getResources().getDisplayMetrics().densityDpi;//ppi
        //取得当前手机px和dp的倍数关系
        float density = getResources().getDisplayMetrics().density;
        int px = (int) (dip * density + .5f);
        return px;
    }

    public static int px2Dip(int px) {
        //公式1.px/(ppi/160) = dp
        //公式2.px/dp = density

        //int ppi = getResources().getDisplayMetrics().densityDpi;//ppi
        //取得当前手机px和dp的倍数关系
        float density = getResources().getDisplayMetrics().density;
        int dip = (int) (px / density + .5f);
        return dip;
    }
}
