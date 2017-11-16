package com.unicool.ch2o_bluetooth.util;


import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;


/**
 * 获取 sharedPrefernces 对象
 * 读取或者操作本地配置文件
 */
public class SPUtil {

    private static String TAG = "sp-ch2o";
    /**
     * Application
     */
    public static Context context;


    private static SharedPreferences paramsPref;
    private static Editor paramsEditor;

    public static SharedPreferences paramsPref() {
        if (paramsPref == null) {
            paramsPref = context.getSharedPreferences(TAG, Context.MODE_PRIVATE);
        }
        return paramsPref;
    }

    public static Editor paramsEditor() {
        if (paramsEditor == null) {
            paramsEditor = paramsPref().edit();
        }
        return paramsEditor;
    }

    public static void clearParamsData() {
        paramsEditor().clear().commit();
    }


    private static SharedPreferences userPref;
    private static Editor userEditor;

    public static SharedPreferences userPref() {
        if (userPref == null) {
            userPref = context.getSharedPreferences(TAG, Context.MODE_PRIVATE);
        }
        return userPref;
    }

    public static Editor userEditor() {
        if (userEditor == null) {
            userEditor = userPref().edit();
        }
        return userEditor;
    }

    public static void clearUserData() {
        userEditor().clear().commit();
    }
}
