package com.unicool.ch2o_bluetooth.ui.fragment;

import android.app.Activity;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.unicool.ch2o_bluetooth.R;
import com.unicool.ch2o_bluetooth.mgr.BluetoothMgr;
import com.unicool.ch2o_bluetooth.model.I;
import com.unicool.ch2o_bluetooth.model.bean.BHTDevBean;
import com.unicool.ch2o_bluetooth.presenter.adapter.BaseRecyclerAdapter;
import com.unicool.ch2o_bluetooth.ui.fragment.base.BaseFragment;
import com.unicool.ch2o_bluetooth.util.SPUtil;
import com.unicool.ch2o_bluetooth.util.Tools;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;

/*
 *  @项目名：  CH2O 
 *  @包名：    com.unicool.ch2o_bluetooth.ui.fragment
 *  @文件名:   HomeFragment
 *  @创建者:   cjf
 *  @创建时间:  2017/11/13 20:01
 *  @描述：    TODO
 */
public class HomeFragment extends BaseFragment implements BaseRecyclerAdapter.OnItemViewClickListener {

    private BaseRecyclerAdapter<BHTDevBean> mRecyclerAdapter = null;
    private boolean acl_connected = false;
    private BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.e("HomeFragment", "action:" + intent.getAction());
            //mlogs.append("\n" + intent.getAction());
            switch (intent.getAction()) {
                case BluetoothDevice.ACTION_ACL_CONNECTED:
                    acl_connected = true;
                    mStatus.setText("状态：\n连接建立 --- 1007");
                    break;
                case BluetoothDevice.ACTION_ACL_DISCONNECT_REQUESTED:
                    break;
                case BluetoothDevice.ACTION_ACL_DISCONNECTED:
                    acl_connected = false;
                    mStatus.setText("状态：\n连接断开 --- 1008");
                    break;
                case BluetoothDevice.ACTION_BOND_STATE_CHANGED:
                    BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                    if (device == null || device.getAddress() == null) return;
                    BHTDevBean bhtDevBean = new BHTDevBean(device.getName(), device.getAddress(), device.getBondState(), device.getUuids());
                    if (mRecyclerAdapter != null) {
                        mRecyclerAdapter.addData(0, 0, bhtDevBean);
                    }
                    if (mBonded != null && !mBonded.equals(bhtDevBean)) {
                        return;
                    }
                    mBonded = bhtDevBean; //mBonded == null
                    Log.i("HomeFragment", "ACTION_BOND_STATE_CHANGED - mBonded:" + mBonded);
                    mTarget.setText("目标设备：\n" + mBonded.name + "\n" + mBonded.address + "\n" + mBonded.bondState);
                    switch (device.getBondState()) {
                        case BluetoothDevice.BOND_BONDING:
                            isBonded = false;
                            mStatus.setText("状态：\n" + mBonded.name + "正在配对...");
                            break;
                        case BluetoothDevice.BOND_BONDED:
                            isBonded = true;
                            mStatus.setText(String.format("状态：\n配对成功：%s", mBonded.name));
                            SPUtil.userEditor().putString(I.Target, mBonded.address).commit();
                            break;
                        case BluetoothDevice.BOND_NONE:
                            isBonded = false;
                            mStatus.setText(String.format("状态：\n配对失败：%s", mBonded.name));
                            SPUtil.userEditor().putString(I.Target, "").commit();
                            break;
                    }
                    break;
                case BluetoothDevice.ACTION_CLASS_CHANGED:
                    //远一个远程设备的绑定状态发生改变时发出广播
                    BluetoothDevice scanDevice3 = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                    if (scanDevice3 == null || scanDevice3.getAddress() == null) return;
                    if (mRecyclerAdapter == null) return;
                    mRecyclerAdapter.addData(0, 0, new BHTDevBean(
                            scanDevice3.getName(), scanDevice3.getAddress(), scanDevice3.getBondState(), scanDevice3.getUuids()));
                    break;
                case BluetoothDevice.ACTION_FOUND:
                    //每扫描到一个设备，系统都会发送此广播
                    BluetoothDevice scanDevice = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                    if (scanDevice == null || scanDevice.getAddress() == null) return;
                    if (mRecyclerAdapter == null) return;
                    mRecyclerAdapter.addData(0, 0, new BHTDevBean(
                            scanDevice.getName(), scanDevice.getAddress(), scanDevice.getBondState(), scanDevice.getUuids()));
                    break;
                case BluetoothDevice.ACTION_NAME_CHANGED:
                    //远程蓝牙设备的名称被发现改变 或者 第一次发现远程蓝牙设备的名称
                    BluetoothDevice scanDevice2 = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                    if (scanDevice2 == null || scanDevice2.getAddress() == null) return;
                    if (mRecyclerAdapter == null) return;
                    mRecyclerAdapter.addData(0, 0, new BHTDevBean(
                            scanDevice2.getName(), scanDevice2.getAddress(), scanDevice2.getBondState(), scanDevice2.getUuids()));
                    break;
                case BluetoothDevice.ACTION_UUID:
                    BluetoothDevice devUUID = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                    if (devUUID == null || devUUID.getUuids() == null || devUUID.getUuids().length == 0) {
                        return;
                    }
                    BHTDevBean bean = new BHTDevBean(devUUID.getName(), devUUID.getAddress(), devUUID.getBondState(), devUUID.getUuids());
                    if (mRecyclerAdapter != null) {
                        mRecyclerAdapter.addData(0, 0, bean);
                    }
                    if (mBonded == null) {
                        return;
                    }
                    mBonded.uuids = devUUID.getUuids();
                    break;

                case BluetoothAdapter.ACTION_CONNECTION_STATE_CHANGED:
                    break;
                case BluetoothAdapter.ACTION_DISCOVERY_FINISHED:
                    mStatus.setText("状态：\n已扫描完成");
                    mRecyclerAdapter.setLock();
                    break;
                case BluetoothAdapter.ACTION_DISCOVERY_STARTED:
                    mStatus.setText("状态：\n正在扫描...");
                    mRecyclerAdapter.clearDatas();
                    break;
                case BluetoothAdapter.ACTION_LOCAL_NAME_CHANGED:
                    break;
                case BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE:
                    break;
                case BluetoothAdapter.ACTION_REQUEST_ENABLE:
                    break;
                case BluetoothAdapter.ACTION_SCAN_MODE_CHANGED:
                    break;
                case BluetoothAdapter.ACTION_STATE_CHANGED:
                    mStatus.setText(String.format("状态：\n蓝牙打开状态：%s", BluetoothMgr.getInstance().getBluetoothAdapter().isEnabled()));
                    break;
            }
        }
    };

    public static Handler mHandler = null;


    private TextView mlogs, mStatus, mTarget, mDesc, mData1, mData2;
    private boolean isBonded;
    private BHTDevBean mBonded;
    private ProgressDialog pdc;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true); //getActivity().invalidateOptionsMenu(); 刷新菜单

        IntentFilter intent = new IntentFilter();
        intent.addAction(BluetoothDevice.ACTION_ACL_CONNECTED);
        intent.addAction(BluetoothDevice.ACTION_ACL_DISCONNECT_REQUESTED);
        intent.addAction(BluetoothDevice.ACTION_ACL_DISCONNECTED);
        intent.addAction(BluetoothDevice.ACTION_BOND_STATE_CHANGED);
        intent.addAction(BluetoothDevice.ACTION_CLASS_CHANGED);
        intent.addAction(BluetoothDevice.ACTION_FOUND);
        intent.addAction(BluetoothDevice.ACTION_NAME_CHANGED);
        intent.addAction(BluetoothDevice.ACTION_UUID);
        //intent.addAction(BluetoothDevice.ACTION_PAIRING_REQUEST);
        intent.addAction(BluetoothAdapter.ACTION_CONNECTION_STATE_CHANGED);//
        intent.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);//
        intent.addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED);//
        intent.addAction(BluetoothAdapter.ACTION_LOCAL_NAME_CHANGED);//
        intent.addAction(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);//
        intent.addAction(BluetoothAdapter.ACTION_REQUEST_ENABLE);//
        intent.addAction(BluetoothAdapter.ACTION_SCAN_MODE_CHANGED);//
        intent.addAction(BluetoothAdapter.ACTION_STATE_CHANGED);//
        this.getContext().registerReceiver(mReceiver, intent);


        mHandler = new Handler(Looper.getMainLooper()) {

            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                Log.e("HomeFragment", "msg.what:" + msg.what);
                switch (msg.what) {
                    case 1001:
                        setCH2O((Float) msg.obj);
                        break;
                    case 1002:
                        mStatus.setText("状态：\n请打开蓝牙->");
                        break;
                    case 1003:
                        mStatus.setText("状态：\n设备尚未配对或正在配对->");
                        break;
                    case 1004:
                        mStatus.setText("状态：\n正在配对..."); //x
                        break;
                    case 1005:
                        mStatus.setText("状态：\n已经获取 Socket --- 01");
                        break;
                    case 1006:
                        mStatus.setText("状态：\n开始连接目标设备 --- 02");
                        break;
                    case 1007:
                        mStatus.setText("状态：\n连接成功 --- 03");
                        break;
                    case 1008:
                        mStatus.setText(String.format("状态：连接失败 --- %s", (String) msg.obj));
                        break;
                    case 1009:
                        mStatus.setText(String.format("状态：发出数据 --- \n%s", (String) msg.obj));
                        break;
                    case 1010:
                        break;
                }
            }
        };
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.getContext().unregisterReceiver(mReceiver);
        BluetoothMgr.getInstance().cancelScanDevices();
        BluetoothMgr.getInstance().closeAllConn();
        mHandler = null;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, null, false);
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        view.findViewById(R.id.btn_send).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                send2BHT(view);
            }
        });

        mlogs = view.findViewById(R.id.log);
        mDesc = view.findViewById(R.id.desc);
        mTarget = view.findViewById(R.id.target);
        mStatus = view.findViewById(R.id.status);
        mData1 = view.findViewById(R.id.data1);
        mData2 = view.findViewById(R.id.data2);

        RecyclerView recyclerView = view.findViewById(R.id.recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext(), OrientationHelper.VERTICAL, false));
        mRecyclerAdapter = new BaseRecyclerAdapter<>(this.getContext(), recyclerView);
        mRecyclerAdapter.setOnItemViewClickListener(this);
        recyclerView.setAdapter(mRecyclerAdapter);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new DividerItemDecoration(this.getContext(), DividerItemDecoration.VERTICAL));//x

        setCH2O(0f);

        mDesc.setText(String.format("本机设备名称：%s", BluetoothMgr.getInstance().getBluetoothAdapter().getName()));
        mDesc.append(String.format("\nMAC地址：%s", BluetoothMgr.getInstance().getBluetoothAdapter().getAddress()));
        mStatus.setText(String.format("状态：\n蓝牙打开状态：%s", BluetoothMgr.getInstance().getBluetoothAdapter().isEnabled()));

        List<BHTDevBean> bondedDevices = BluetoothMgr.getInstance().getBondedDevices();
        String address = SPUtil.userPref().getString(I.Target, "");
        for (BHTDevBean bean : bondedDevices) {
            if (bean.address.equals(address)) {
                mBonded = new BHTDevBean(bean.name, bean.address, bean.bondState, bean.uuids);
                Log.i("HomeFragment", "onViewCreated - getBondedDevices - mBonded:" + mBonded);
                mTarget.setText("目标设备：\n" + bean.name + "\n" + bean.address + "\n" + bean.bondState);
                break;
            }
        }
//        mRecyclerAdapter.setDatas(bondedDevices);
//        mRecyclerAdapter.notifyDataSetChanged();
    }

    public BaseRecyclerAdapter getRecyclerAdapter() {
        return mRecyclerAdapter;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.home, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_open:
                BluetoothMgr.getInstance().isSelfOpen(this);
                return true;
            case R.id.action_scan:
                boolean isScanning = BluetoothMgr.getInstance().scanDevices();
                if (isScanning) {
                    mStatus.setText("状态：正在扫描...");
                    mRecyclerAdapter.clearDatas();
                } else {
                    //mStatus.setText("状态：\n扫描失败！");
                    Toast.makeText(this.getContext(), "扫描失败", Toast.LENGTH_SHORT).show();
                }
                return true;
            case R.id.action_disscan:
                boolean isDisScanning = BluetoothMgr.getInstance().cancelScanDevices();
                if (isDisScanning) {
                    mStatus.setText("状态：已停止扫描");
                    mRecyclerAdapter.setLock();
                }
                return true;
            case R.id.action_match:
                if (mBonded == null) {
                    Toast.makeText(this.getContext(), "目标设备为空", Toast.LENGTH_SHORT).show();
                    return true;
                }
                isBonded = BluetoothMgr.getInstance().bondDevices(mBonded);
                if (isBonded) {
                    SPUtil.userEditor().putString(I.Target, mBonded.address).commit();
                    mTarget.setText("目标设备：\n" + mBonded.name + "\n" + mBonded.address + "\n" + mBonded.bondState);
                } else {
                    Toast.makeText(this.getContext(),
                            "设备：" + mBonded.name + "\t配对失败", Toast.LENGTH_SHORT).show();
                }
                return true;
            case R.id.action_dismatch:
                // TODO: 2017/11/16  
                Toast.makeText(getContext(), "开发中", Toast.LENGTH_SHORT).show();
                break;
            case R.id.action_conn: //send2BHT(view);
                if (mBonded == null) {
                    Toast.makeText(this.getContext(), "目标设备为空", Toast.LENGTH_SHORT).show();
                    return true;
                }
                boolean open = BluetoothMgr.getInstance().isSelfOpen(this);
                if (!open) return true;
                mStatus.setText("状态：正在连接设备" + mBonded.name + "\t" + mBonded.address + "\n请稍后...");
                BluetoothMgr.getInstance().starConnect(mBonded);
                return true;
            case R.id.action_disconn:
                // TODO: 2017/11/16  
                Toast.makeText(getContext(), "开发中", Toast.LENGTH_SHORT).show();
                break;
            case R.id.action_see:
                BluetoothMgr.getInstance().setDiscoverable(255);
                return true;
            case R.id.action_set:
                mHandler.obtainMessage(1001, 10.24f).sendToTarget();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        if (requestCode == BluetoothMgr.REQUEST_COMMUNICATE) {
            mStatus.setText(String.format("状态：蓝牙打开状态：%s", resultCode == Activity.RESULT_OK));
            if (mBonded == null) {
                String address = SPUtil.userPref().getString(I.Target, "");
                if (!TextUtils.isEmpty(address)) {
                    List<BHTDevBean> bondedDevices = BluetoothMgr.getInstance().getBondedDevices();
                    for (BHTDevBean bean : bondedDevices) {
                        if (bean.address.equals(address)) {
                            mBonded = new BHTDevBean(bean.name, bean.address, bean.bondState, bean.uuids);
                            Log.i("HomeFragment", "onActivityResult - getBondedDevices - mBonded:" + mBonded);
                            mTarget.setText("目标设备：\n" + bean.name + "\n" + bean.address + "\n" + bean.bondState);
                            break;
                        }
                    }
                }
            }
            if (mBonded != null) {
                isBonded = BluetoothMgr.getInstance().bondDevices(mBonded);
                if (isBonded) {
                    SPUtil.userEditor().putString(I.Target, mBonded.address).commit();
                    mTarget.setText("目标设备：\n" + mBonded.name + "\n" + mBonded.address + "\n" + mBonded.bondState);
                }
            }
        }
    }

    @Override
    public void onItemClick(View view, final Object o, int itemViewType) {
        if (!(o instanceof BHTDevBean)) return;
        BHTDevBean bonded = (BHTDevBean) o;
        isBonded = BluetoothMgr.getInstance().bondDevices(bonded);
        if (isBonded) {
            mBonded = bonded;
            SPUtil.userEditor().putString(I.Target, mBonded.address).commit();
            mTarget.setText("目标设备：\n" + mBonded.name + "\n" + mBonded.address + "\n" + mBonded.bondState);
        } else {
            Toast.makeText(this.getContext(),
                    "设备：" + ((BHTDevBean) o).name + "\t配对失败", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onItemLongClick(View view, final Object o, int itemViewType) {
        if (!(o instanceof BHTDevBean)) return;
        boolean b = BluetoothMgr.getInstance().cancelbonded((BHTDevBean) o);
        Toast.makeText(this.getContext(),
                "取消配对：" + ((BHTDevBean) o).name + (b ? "\t成功" : "\t失败"), Toast.LENGTH_SHORT).show();
    }


    public void send2BHT(View v) {
        if (mBonded == null) {
            Toast.makeText(this.getContext(), "目标设备为空", Toast.LENGTH_SHORT).show();
            return;
        }
        boolean open = BluetoothMgr.getInstance().isSelfOpen(this);
        if (!open) return;
        if (acl_connected) {
            BluetoothMgr.getInstance().writeData(mBonded, Tools.stringToByte("这是一条测试用的数据1/2"));
            BluetoothMgr.getInstance().writeData(mBonded, Tools.stringToByte(Arrays.toString(mBonded.uuids)));
        } else {
            mStatus.setText("状态：正在连接设备" + mBonded.name + "\t" + mBonded.address + "\n请稍后...");
            BluetoothMgr.getInstance().starConnect(mBonded);
        }
    }

    private final String numberColor = "#E78828";

    public void setCH2O(float i) {
        mData1.setText(Html.fromHtml(String.format(Locale.getDefault(),
                "气体浓度\t甲醛：<font color='%s'>%f</font> mg/m3", numberColor, i)));
        mData2.setText(Html.fromHtml(String.format(Locale.getDefault(),
                "气体浓度\t\t\t苯：<font color='%s'>%f</font> mg/m3", numberColor, i * (Math.random() + 2))));
    }
}