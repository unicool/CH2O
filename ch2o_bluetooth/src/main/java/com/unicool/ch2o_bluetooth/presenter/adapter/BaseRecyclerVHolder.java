package com.unicool.ch2o_bluetooth.presenter.adapter;

import android.bluetooth.BluetoothDevice;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.unicool.ch2o_bluetooth.R;
import com.unicool.ch2o_bluetooth.model.bean.BHTDevBean;

/*
 *  @项目名：  CH2O 
 *  @包名：    com.unicool.ch2o_bluetooth.presenter.adapter
 *  @文件名:   BaseRecyclerVHolder
 *  @创建者:   cjf
 *  @创建时间:  2017/11/14 11:43
 *  @描述：    TODO
 */
public class BaseRecyclerVHolder<D> extends RecyclerView.ViewHolder {

    private final ImageView mIv_state;
    private final TextView mText1;
    private final TextView mText2;

    public BaseRecyclerVHolder(View inflate) {
        super(inflate);
        mIv_state = inflate.findViewById(R.id.iv_state);
        mText1 = inflate.findViewById(R.id.text1);
        mText2 = inflate.findViewById(R.id.text2);
    }

    public void setData(D d) {
        if (!(d instanceof BHTDevBean)) return;
        BHTDevBean bonded = (BHTDevBean) d;
        switch (bonded.bondState) {
            case BluetoothDevice.BOND_BONDING:
                mIv_state.setImageResource(R.drawable.ic_dashboard_black_24dp);
                break;
            case BluetoothDevice.BOND_BONDED:
                mIv_state.setImageResource(R.drawable.ic_menu_share);
                break;
            case BluetoothDevice.BOND_NONE:
                mIv_state.setImageResource(R.drawable.ic_menu_send);
                break;
        }
        mText1.setText(bonded.name);
        mText2.setText(bonded.address);
    }
}
