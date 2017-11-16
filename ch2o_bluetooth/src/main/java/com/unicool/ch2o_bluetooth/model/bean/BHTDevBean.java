package com.unicool.ch2o_bluetooth.model.bean;

import android.bluetooth.BluetoothDevice;
import android.os.ParcelUuid;

import java.util.Arrays;

/*
 *  @项目名：  CH2O 
 *  @包名：    com.unicool.ch2o_bluetooth.model.bean
 *  @文件名:   BHTDevBean
 *  @创建者:   cjf
 *  @创建时间:  2017/11/14 16:47
 *  @描述：    TODO
 */
public class BHTDevBean {

    public String name;
    public String address;
    public int bondState = BluetoothDevice.BOND_NONE;
    public ParcelUuid[] uuids;

    public BHTDevBean() {
    }

    public BHTDevBean(String name, String address, int bondState, ParcelUuid[] uuids) {
        this.name = name;
        this.address = address;
        this.bondState = bondState;
        this.uuids = uuids;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        BHTDevBean that = (BHTDevBean) o;

        return address != null ? address.equals(that.address) : that.address == null;

    }

    @Override
    public int hashCode() {
        return address != null ? address.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "BHTDevBean{" +
                "name='" + name + '\'' +
                ", address='" + address + '\'' +
                ", bondState=" + bondState +
                ", uuids=" + Arrays.toString(uuids) +
                '}';
    }
}
