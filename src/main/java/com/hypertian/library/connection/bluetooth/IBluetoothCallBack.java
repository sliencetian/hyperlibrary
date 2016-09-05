package com.hypertian.library.connection.bluetooth;

/**
 * Created by hypertian on 2016/8/22.
 * Desc
 */

public interface IBluetoothCallBack {

    void acceptMessage(String msg);//接收到的信息

    void alreadyConnect();

    void openBluetooth();

    void waitConnect();

    void connecting();

    void connectSuccess();

    void connectFailure();

    void connectException();

    void acceptMessageException();

    void sendMessageException();

    void connectInterrupt();
}
