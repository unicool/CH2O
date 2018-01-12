package com.unicool.ch2o_bluetooth.util;

import android.content.Context;
import android.util.TypedValue;

/*
 *  @项目名：  CH2O 
 *  @包名：    com.unicool.ch2o_bluetooth.util
 *  @文件名:   CommUtils
 *  @创建者:   pc
 *  @创建时间:  2018/1/11 15:14
 *  @描述：    TODO
 */
public class CommUtils {

    public static float sp2px(Context context, float sp) {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, sp, context.getResources().getDisplayMetrics());
    }

    public static float px2sp(Context context, float px) {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_PX, px, context.getResources().getDisplayMetrics());
    }
}
