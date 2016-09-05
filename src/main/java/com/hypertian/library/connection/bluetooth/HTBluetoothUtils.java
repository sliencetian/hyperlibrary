package com.hypertian.library.connection.bluetooth;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.util.Log;

import com.hypertian.library.util.HTLog;

import java.util.Set;

import static android.content.ContentValues.TAG;

/**
 * Created by hypertian on 2016/8/22.
 * Desc
 */

class HTBluetoothUtils {

    /**
     * 检测是否支持蓝牙设备
     *
     * @return false 不支持,true 支持
     */
    public static boolean getHaveBluetooth() {
        BluetoothAdapter adapter = BluetoothAdapter.getDefaultAdapter();
        if (null == adapter) {
            return false;
        }
        return true;
    }


    /**
     * 打开蓝牙
     */
    public static void openBluetooth() {
        BluetoothAdapter adapter = BluetoothAdapter.getDefaultAdapter();
        adapter.enable();
    }

    /**
     * 关闭蓝牙
     */
    public static boolean closeBluetooth() {
        BluetoothAdapter adapter = BluetoothAdapter.getDefaultAdapter();
        return adapter.disable();
    }

    /**
     * 检测蓝牙是否开启
     *
     * @return false 蓝牙没开,true 蓝牙已开
     */
    public static boolean getBluetoothIsOpen() {
        BluetoothAdapter adapter = BluetoothAdapter.getDefaultAdapter();
        return adapter.isEnabled();
    }

    /**
     * 检测蓝牙是否链接
     */
    public static boolean getBluetoothIsConn() {
        BluetoothAdapter adapter = BluetoothAdapter.getDefaultAdapter();
        if (adapter.getBondedDevices().size() > 0)
            return true;
        return false;
    }


    public static BluetoothDevice getConnectDevice() {
        BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        Set<BluetoothDevice> pairedDevices = bluetoothAdapter.getBondedDevices();
        for (BluetoothDevice device : pairedDevices) {
            Log.d("getConnectDevice", device.getName());
        }
        for (BluetoothDevice device : pairedDevices) {
            return device;
        }
        return null;
    }
}
