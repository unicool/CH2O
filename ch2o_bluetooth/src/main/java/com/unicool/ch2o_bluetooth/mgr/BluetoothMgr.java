package com.unicool.ch2o_bluetooth.mgr;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.ParcelUuid;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.widget.Toast;

import com.unicool.ch2o_bluetooth.model.bean.BHTDevBean;
import com.unicool.ch2o_bluetooth.presenter.Decoder;
import com.unicool.ch2o_bluetooth.ui.fragment.HomeFragment;
import com.unicool.ch2o_bluetooth.util.Tools;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.ref.WeakReference;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

/*
 *  @项目名：  CH2O 
 *  @包名：    com.unicool.ch2o_bluetooth.mgr
 *  @文件名:   BluetoothMgr
 *  @创建者:   cjf
 *  @创建时间:  2017/11/14 16:08
 *  @描述：    TODO
 */
public class BluetoothMgr {
    private static final String TAG = "HomeFragment";
    private static final BluetoothMgr ourInstance = new BluetoothMgr();
    private BluetoothAdapter mBluetoothAdapter = null;
    private WeakReference<Context> mContext = null;

    public static final int REQUEST_COMMUNICATE = 1001;
    public static final int REQUEST_CODE_CONFIG = 1002;

    private static volatile Map<String, BluetoothSocket> mSockets = new HashMap<>();

    public static BluetoothMgr getInstance() {
        return ourInstance;
    }

    private BluetoothMgr() {
    }

    public void onDestroy() {
        //mContext.get().unregisterReceiver(mReceiver);
        closeAllConn();
    }

    public void init(Context context) {
        this.mContext = new WeakReference<Context>(context.getApplicationContext());

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN_MR2) {
            BluetoothManager bhtMgr = (BluetoothManager) context.getSystemService(Context.BLUETOOTH_SERVICE);
            mBluetoothAdapter = bhtMgr.getAdapter();
        } else {
            mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        }
//        IntentFilter intent = new IntentFilter();
//        intent.addAction(BluetoothDevice.ACTION_FOUND);//搜索发现设备
//        intent.addAction(BluetoothDevice.ACTION_BOND_STATE_CHANGED);//配对状态改变
//        intent.addAction(BluetoothAdapter.ACTION_SCAN_MODE_CHANGED);//行动扫描模式改变了
//        intent.addAction(BluetoothAdapter.ACTION_STATE_CHANGED);//动作状态发生了变化
//        intent.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);//
//        context.registerReceiver(mReceiver, intent);
    }


    public BluetoothAdapter getBluetoothAdapter() {
        return mBluetoothAdapter;
    }

    public boolean isSelfOpen(Fragment context) {
        if (!mBluetoothAdapter.isEnabled()) {
            //请求用户开启
            Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivityForResult(intent, REQUEST_COMMUNICATE);
        }
        return mBluetoothAdapter.isEnabled();
    }

    public void setDiscoverable(int second) {
        if (mBluetoothAdapter.isEnabled()) {
            if (mBluetoothAdapter.getScanMode() != BluetoothAdapter.SCAN_MODE_CONNECTABLE_DISCOVERABLE) {
                Intent discoverableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
                discoverableIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, second);
                discoverableIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                mContext.get().startActivity(discoverableIntent);
            }
        }
    }

    public List<BHTDevBean> getBondedDevices() {
        List<BHTDevBean> list = new ArrayList<>();
        if (!mBluetoothAdapter.isEnabled()) {
            return list;
        }
        Set<BluetoothDevice> bondedDevices = mBluetoothAdapter.getBondedDevices();
        for (BluetoothDevice bondedDevice : bondedDevices) {
            BHTDevBean bonded = new BHTDevBean();
            bonded.name = bondedDevice.getName();
            bonded.address = bondedDevice.getAddress();
            bonded.bondState = bondedDevice.getBondState();
            bonded.uuids = bondedDevice.getUuids();
            list.add(bonded);
        }
        return list;
    }

    public boolean scanDevices() {
        if (!mBluetoothAdapter.isEnabled()) {
            return false;
        }
        if (mBluetoothAdapter.isDiscovering()) {
            mBluetoothAdapter.cancelDiscovery();
        }
        return mBluetoothAdapter.startDiscovery();
    }

    public boolean cancelScanDevices() {
        if (!mBluetoothAdapter.isEnabled()) {
            return false;
        }
        if (!mBluetoothAdapter.isDiscovering()) {
            return true;
        }
        return mBluetoothAdapter.cancelDiscovery();
    }


    public boolean bondDevices(BHTDevBean dev) {
        if (!mBluetoothAdapter.isEnabled()) {
            return false;
        }
        if (mBluetoothAdapter.isDiscovering()) {
            mBluetoothAdapter.cancelDiscovery();
        }

        BluetoothDevice btDev = mBluetoothAdapter.getRemoteDevice(dev.address);
        if (btDev.getBondState() == BluetoothDevice.BOND_NONE) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                return btDev.createBond();
            } else {
                try {
                    Method createBondMethod = BluetoothDevice.class.getMethod("createBond");
                    return (Boolean) createBondMethod.invoke(btDev);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } // BluetoothDevice.BOND_BONDING
        return btDev.getBondState() == BluetoothDevice.BOND_BONDED;
    }

    public boolean cancelbonded(BHTDevBean dev) {
        if (!mBluetoothAdapter.isEnabled()) {
            return false;
        }
        BluetoothDevice device = mBluetoothAdapter.getRemoteDevice(dev.address);
        try {
            Method m = device.getClass().getMethod("removeBond");
            return (Boolean) m.invoke(device);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }


    public void starConnect(BHTDevBean dev) {
        if (dev == null) return;
        if (!mBluetoothAdapter.isEnabled()) {
            if (HomeFragment.mHandler != null) {
                HomeFragment.mHandler.sendEmptyMessage(1002);
            }
            return;
        }
        if (!bondDevices(dev)) { //cancelDiscovery
            Toast.makeText(mContext.get(), dev.name + " 尚未配对或正在配对", Toast.LENGTH_SHORT).show();
            if (HomeFragment.mHandler != null) {
                HomeFragment.mHandler.sendEmptyMessage(1003);
            }
            return;
        }

        final BluetoothDevice bhtDev = mBluetoothAdapter.getRemoteDevice(dev.address);
        ParcelUuid[] uuids = bhtDev.getUuids();
        Log.i(TAG, "uuids:" + Arrays.toString(uuids));

        ThdMgr.executeOnNetWorkThread(new Runnable() {

            @Override
            public void run() {

                BluetoothSocket remove = mSockets.remove(bhtDev.getAddress());
                if (remove != null && remove.isConnected()) {
                    try {
                        remove.close();
                        Log.e(TAG, "重新建立连接：" + bhtDev.getAddress());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                BluetoothSocket socket = null;
                try {
                    UUID uuid;
                    ParcelUuid[] uuids1 = bhtDev.getUuids();
                    if (uuids1 != null && uuids1.length > 0) {
                        uuid = uuids1[0].getUuid();
                    } else {
                        uuid = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB"); //00001101串口
                    }
                    /**
                     *
                     00001103-0000-1000-8000-00805f9b34fb, 0000110a-0000-1000-8000-00805f9b34fb, 
                     00001105-0000-1000-8000-00805f9b34fb, 00001106-0000-1000-8000-00805f9b34fb, 
                     00001115-0000-1000-8000-00805f9b34fb, 00001116-0000-1000-8000-00805f9b34fb, 
                     0000112d-0000-1000-8000-00805f9b34fb, 0000110e-0000-1000-8000-00805f9b34fb, 
                     0000112f-0000-1000-8000-00805f9b34fb, 00001112-0000-1000-8000-00805f9b34fb, 
                     0000111f-0000-1000-8000-00805f9b34fb, 00001132-0000-1000-8000-00805f9b34fb, 
                     00000000-0000-1000-8000-00805f9b34fb, 00000000-0000-1000-8000-00805f9b34fb

                     0000110a-0000-1000-8000-00805f9b34fb, 00001105-0000-1000-8000-00805f9b34fb, 
                     00001115-0000-1000-8000-00805f9b34fb, 00001116-0000-1000-8000-00805f9b34fb, 
                     0000112f-0000-1000-8000-00805f9b34fb, 00001112-0000-1000-8000-00805f9b34fb, 
                     0000111f-0000-1000-8000-00805f9b34fb, 00001132-0000-1000-8000-00805f9b34fb, 
                     00000000-0000-1000-8000-00805f9b34fb, 00000000-0000-1000-8000-00805f9b34fb

                     */
                    //socket = bhtDev.createRfcommSocketToServiceRecord(uuid);
                    // TODO: 2017/11/14  
                    socket = bhtDev.createInsecureRfcommSocketToServiceRecord(uuid);
                    if (HomeFragment.mHandler != null) {
                        HomeFragment.mHandler.sendEmptyMessage(1005);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    if (HomeFragment.mHandler != null) {
                        HomeFragment.mHandler.sendEmptyMessage(1008);
                    }
                }

                if (socket == null) {
                    Log.e(TAG, bhtDev.getName() + "\t" + bhtDev.getAddress() + "\nSocket 创建失败。");
                    return;
                }

                try {
                    if (HomeFragment.mHandler != null) {
                        HomeFragment.mHandler.sendEmptyMessage(1006);
                    }
                    if (!socket.isConnected()) {
                        socket.connect();
                    }
                    mSockets.put(bhtDev.getAddress(), socket);
                    if (HomeFragment.mHandler != null) {
                        HomeFragment.mHandler.sendEmptyMessage(1007);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    //mSockets.remove(bhtDev.getAddress());
                    Log.e(TAG, bhtDev.getName() + "\t" + bhtDev.getAddress() + "\nSocket 连接失败：" + e.getMessage());
                    try {
                        socket.close();
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                    if (HomeFragment.mHandler != null) {
                        HomeFragment.mHandler.obtainMessage(1008, e.getMessage()).sendToTarget();
                    }
                }

                onRcvData(socket);
            }
        });
    }

    public void writeData(@NonNull final BHTDevBean dev, @NonNull final byte[] b) {
        if (dev == null || b == null || b.length == 0) {
            return;
        }
        final BluetoothSocket socket = mSockets.get(dev.address);
        if (socket == null) {
            Log.e(TAG, "连接不存在 - socket:" + socket);
            return;
        }
        if (!socket.isConnected()) {
            Log.e(TAG, "连接未打开 - socket:" + socket);
            Toast.makeText(mContext.get(), dev.name + "连接未打开 - socket:" + socket, Toast.LENGTH_SHORT).show();
            return;
        }
        OutputStream out = null;
        try {
            out = socket.getOutputStream();
            out.write(b);
            out.flush();
            if (HomeFragment.mHandler != null) {
                HomeFragment.mHandler.obtainMessage(1009, Tools.byteToString(b, 0, b.length)).sendToTarget();
            }
        } catch (IOException e) {
            e.printStackTrace();
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        }
    }

    public void onRcvData(final BluetoothSocket socket) {

        InputStream in = null;
        byte[] buffer = new byte[1024];
        try {
            in = socket.getInputStream();
            while (socket.isConnected()) {
                int len = in.available();
                //Log.e(TAG, "    ==========    " + len);
                //Log.i(TAG, "    ==========    " + len);
                if (len == 0) {
                    try {
                        Thread.sleep(512);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    continue;
                }
                while (in.read(buffer) != -1) {
                    // TODO: 2017/11/14 缓存不够大怎么办？ 
                    Log.e(TAG, socket.getRemoteDevice().getName() + " 发来了数据：\n\t" + Tools.parseByte2HexStr2(buffer));
                    if (len > buffer.length) len = buffer.length;
                    Decoder.onReceiverData(socket, Tools.byteTobyte(buffer, 0, len));
                }
            }
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e2) {
                    e2.printStackTrace();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        }
    }

    public synchronized void closeAllConn() {
        try {
            Iterator<Map.Entry<String, BluetoothSocket>> iterator = mSockets.entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry<String, BluetoothSocket> next = iterator.next();
                next.getValue().close();
                iterator.remove();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
