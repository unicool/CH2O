package com.unicool.ch2o_bluetooth.presenter;

import android.bluetooth.BluetoothSocket;
import android.util.Log;

import com.unicool.ch2o_bluetooth.ui.fragment.HomeFragment;
import com.unicool.ch2o_bluetooth.util.Tools;

/*
 *  @项目名：  CH2O 
 *  @包名：    com.unicool.ch2o_bluetooth.presenter
 *  @文件名:   Decoder
 *  @创建者:   cjf
 *  @创建时间:  2017/11/14 21:40
 *  @描述：    TODO
 */
public class Decoder {

    public static synchronized void onReceiverData(BluetoothSocket socket, byte[] bytes) {
        decoderData(bytes);
    }

    private static void decoderData(byte[] bytes) {
        String data = Tools.parseByte2HexStr(bytes);
        Log.e("HomeFragment", "data:" + data);

        byte[] ch2o = Tools.byteTobyte(bytes, 4, 2);
        byte[] p = Tools.byteTobyte(bytes, 6, 1);
        String s = Tools.parseByte2HexStr(ch2o);
        Integer c = Integer.valueOf(s, 16);
        float i = c / (float) Math.pow(10, p[0]);

        if (HomeFragment.mHandler != null) {
            HomeFragment.mHandler.obtainMessage(1001, i).sendToTarget();
        }

        /*//校验码
        byte[] verify = Tools.byteTobyte(bytes, bytes.length - 2, 1);
        int i1 = bytes.length - 2;
        byte vf = bytes[1];
        for (int i = 2; i < i1; i++) {
            vf ^= bytes[i];
        }
        if (verify[0] != vf) {
            Log.e(I.TAG_TCP, "\t消息校验错误！\t消息体校验码:" + verify[0] + "\t实际校验结果:" + vf);
            return;
        }

        int nOff = 1;

        //消息ID
        byte[] idByte = Tools.byteTobyte(bytes, nOff, 2);
        int msgId = Integer.parseInt(Tools.parseByte2HexStr(idByte), 16);
        nOff += 2;

        //消息体属性
        byte[] mBodyAttr = Tools.byteTobyte(bytes, nOff, 2);
        nOff += 2;

        //消息体长度
        int bodylen = bytes.length - 15;

        // ISU标识：0x10+厂商编号1+设备类型1+序列号3
        String terminalMark = Tools.bcd2Str(Tools.byteTobyte(bytes, nOff, 6));
        nOff += 6;

        // 消息体流水号
        int seq = Integer.parseInt(Tools.parseByte2HexStr(Tools.byteTobyte(bytes, nOff, 2)), 16);
        nOff += 2;

        //消息体
        byte[] data = Tools.byteTobyte(bytes, nOff, bodylen);

        //分发处理
        Dispatcher.disMsg(msgId, seq, data, context);*/
    }
}
