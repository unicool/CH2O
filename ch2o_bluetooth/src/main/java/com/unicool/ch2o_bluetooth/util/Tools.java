package com.unicool.ch2o_bluetooth.util;

import android.support.annotation.NonNull;

import java.io.UnsupportedEncodingException;

import static java.lang.Integer.parseInt;

/*
 *  @项目名：  CH2O 
 *  @包名：    com.unicool.ch2o_bluetooth.util
 *  @文件名:   Tools
 *  @创建者:   cjf
 *  @创建时间:  2017/11/14 20:49
 *  @描述：    TODO
 */
public class Tools {


    /**
     * 将二进制转换成16进制
     *
     * @param src
     * @return
     */
    public static String parseByte2HexStr(byte[] src) {
        if (src == null || src.length <= 0) return "";
        StringBuilder sb = new StringBuilder("");
        for (int i = 0; i < src.length; i++) {
            int v = src[i] & 0xFF; //
            String hv = Integer.toHexString(v);
            if (hv.length() == 1) {
                sb.append(0);
            }
            sb.append(hv);
        }
        return sb.toString().toUpperCase();
    }

    /**
     * 将二进制转换成16进制(加-间隔，方便观察)
     */
    public static String parseByte2HexStr2(byte buf[]) {
        if (buf == null || buf.length <= 0) return "";
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < buf.length; i++) {
            String hex = Integer.toHexString(buf[i] & 0xFF);
            if (hex.length() == 1) {
                hex = '0' + hex;
            }
            sb.append(hex.toUpperCase() + " ");
        }
        if (sb.length() > 0) {
            sb.delete(sb.length() - 1, sb.length());
        }
        return sb.toString();
    }

    /**
     * 字符串转换成 ASCII 的2进制
     */
    @NonNull
    public static byte[] stringToByte(String value) {
        StringBuilder sbu = new StringBuilder();
        char[] chars = value.toCharArray();
        for (char aChar : chars) {
            sbu.append(Integer.toHexString((int) aChar));
        }
        return Tools.parseHexStr2Byte(sbu.toString());
    }

    /**
     * 将16进制字符串转换为二进制
     */
    @NonNull
    public static byte[] parseHexStr2Byte(String hexStr) {
        if (hexStr == null || hexStr.length() == 0) return new byte[0];
        byte[] result = new byte[hexStr.length() / 2];
        for (int i = 0; i < hexStr.length() / 2; i++) {
            int high = parseInt(hexStr.substring(i * 2, i * 2 + 1), 16);
            int low = parseInt(hexStr.substring(i * 2 + 1, i * 2 + 2), 16);
            result[i] = (byte) (high * 16 + low);
        }
        return result;
    }

    /**
     * 将二进制转换成字符串
     */
    public static String byteToString(byte[] src, int startIndex, int length) {
        if (src == null) {
            return "";
        }
        byte[] des = new byte[length];
        int i = 0;
        for (int j = startIndex; i < length; ++j) {
            des[i] = src[j];
            ++i;
        }
        String str = null;
        try {
            str = new String(des, "gb2312").trim();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return str;
    }

    /**
     * 获取指定长度字节数
     */
    public static byte[] byteTobyte(byte[] src, int startIndex, int length) {
        int len = src.length;
        byte[] des = new byte[length];
        int i = 0;
        for (int j = startIndex; i < length && j < len; ++j) {
            des[i] = src[j];
            ++i;
        }
        return des;
    }
}
