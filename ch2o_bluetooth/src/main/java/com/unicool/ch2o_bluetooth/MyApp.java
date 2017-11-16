package com.unicool.ch2o_bluetooth;

import android.app.Application;

import com.unicool.ch2o_bluetooth.mgr.BluetoothMgr;
import com.unicool.ch2o_bluetooth.util.SPUtil;


/*
 *  @项目名：  CH2O 
 *  @包名：    com.unicool.ch2o_bluetooth
 *  @文件名:   MyApp
 *  @创建者:   cjf
 *  @创建时间:  2017/11/14 16:29
 *  @描述：    TODO
 */
public class MyApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        SPUtil.context = this;
        BluetoothMgr.getInstance().init(this);
    }
}
