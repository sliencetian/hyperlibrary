package com.hypertian.library.connection.bluetooth;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.os.Handler;
import android.os.Message;

import com.hypertian.library.framework.HTTimer;
import com.hypertian.library.util.HTLog;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.lang.ref.WeakReference;

/**
 * Created by hypertian on 2016/8/22.
 * Desc
 */

public class HTBluetoothHelper {

    private static final String TAG = "HTBluetoothHelper";

    private static final int MSG_CODE_ALREADY_CONN = 0x0001;//连接已经存在
    private static final int MSG_CODE_OPEN_BLUETOOTH = 0x0002;//正在开启蓝牙
    private static final int MSG_CODE_WAIT_CONN = 0x0003;//等待链接
    private static final int MSG_CODE_ING_CONN = 0x0004;//正在连接
    private static final int MSG_CODE_SUCCESS_CONN = 0x0005;//连接成功
    private static final int MSG_CODE_FAILURE_CONN = 0x0006;//连接失败
    private static final int MSG_CODE_EXCEPTION_CONN = 0x0007;//连接异常

    private static final int MSG_CODE_ACCEPT_EXCEPTION = 0x0008;//接收信息异常
    private static final int MSG_CODE_SEND_EXCEPTION = 0x0009;//发送信息异常
    private static final int MSG_CODE_INTERRUPT_CONN = 0x00010;//连接断开
    private static final int MSG_CODE_ACCEPT_MESSAGE = 0x00011;//接收到消息


    private static final String PROTOCOL_SCHEME_RFCOMM = "HYPER_BLUETOOTH";
    private static final String UUID = "00001101-0000-1000-8000-00805F9B34FB";

    //该socket只能为一种状态，即
    private BluetoothSocket socket = null;
    private BluetoothServerSocket mServerSocket;
    private HTBluetoothThread mBluetoothThread = null;

    private IBluetoothCallBack callBack;
    private BluetoothHandler bluetoothHandler = new BluetoothHandler(this);


    public void setBluetoothCallBack(IBluetoothCallBack callBack) {
        this.callBack = callBack;
    }

    /**
     * 开始接收蓝牙链接
     */
    public void startAccept() {
        if (socket != null) {
            bluetoothHandler.sendEmptyMessage(MSG_CODE_ALREADY_CONN);
        }
        if (callBack == null) {
            throw new RuntimeException("callBack is null");
        }
        if (!HTBluetoothUtils.getBluetoothIsOpen()) {//检测蓝牙是否打开，若没打开，则开启
            HTBluetoothUtils.openBluetooth();
            bluetoothHandler.sendEmptyMessage(MSG_CODE_OPEN_BLUETOOTH);
            HTLog.d(TAG, "101  正在打开蓝牙");
            new HTTimer(new HTTimer.HTTimerCallBack() {
                @Override
                public void timeTicked(HTTimer srcTimer) {
                    HTBluetoothHelper.getInstance().startAccept();
                }
            }).startTimer(1, 1);
            return;
        }


        new Thread(new Runnable() {
            @Override
            public void run() {
                BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
                try {
                    /**
                     * 创建一个蓝牙服务器 参数分别：服务器名称、UUID
                     */
                    HTLog.d(TAG, "120  创建一个蓝牙服务器");
                    mServerSocket = mBluetoothAdapter.listenUsingInsecureRfcommWithServiceRecord(PROTOCOL_SCHEME_RFCOMM, java.util.UUID.fromString(UUID));
                    bluetoothHandler.sendEmptyMessage(MSG_CODE_WAIT_CONN);
                    HTLog.d(TAG, "123  开始等待链接");
                    /**
                     * 接受客户端的连接请求
                     * 这是一个阻塞过程，直到建立一个连接或者连接失效
                     * 通过BluetoothServerSocket得到一个BluetoothSocket对象，管理这个连接
                     */
                    socket = mServerSocket.accept();
                    HTLog.d(TAG, "130  链接建立完成");
                    bluetoothHandler.sendEmptyMessage(MSG_CODE_ING_CONN);
                    mBluetoothThread = new HTBluetoothThread();
                    mBluetoothThread.start();
                    HTLog.d(TAG, "134  开始建立通信链接");
                } catch (IOException e) {
                    bluetoothHandler.sendEmptyMessage(MSG_CODE_EXCEPTION_CONN);
                    e.printStackTrace();
                }
            }
        }).start();
    }

    /**
     * 开始链接蓝牙设备
     */
    public void startConnect(final BluetoothDevice mBluetoothDevice) {
        if (socket != null) {
            bluetoothHandler.sendEmptyMessage(MSG_CODE_ALREADY_CONN);
            HTLog.d(TAG, "149  链接已经存在");
            return;
        }
        if (callBack == null) {
            throw new RuntimeException("callBack is null");
        }
        if (!HTBluetoothUtils.getBluetoothIsOpen()) {//检测蓝牙是否打开，若没打开，则开启
            HTBluetoothUtils.openBluetooth();
            bluetoothHandler.sendEmptyMessage(MSG_CODE_OPEN_BLUETOOTH);
            HTLog.d(TAG, "164  正在打开蓝牙");
            return;
        }
        new HTTimer(new HTTimer.HTTimerCallBack() {
            @Override
            public void timeTicked(HTTimer srcTimer) {
                if (srcTimer.currRoundCount == 10 && socket.isConnected()) {
                    try {
                        socket.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    socket = null;
                }
            }
        }).startTimer(1, 10);

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    HTLog.d(TAG, "178  开始建立连接");
                    socket = mBluetoothDevice.createInsecureRfcommSocketToServiceRecord(java.util.UUID.fromString(UUID));
                    // 通过socket连接服务器，这是一个阻塞过程，直到连接建立或者连接失效
                    socket.connect();
                    bluetoothHandler.sendEmptyMessage(MSG_CODE_ING_CONN);
                    HTLog.d(TAG, "连接建立完成");
                    mBluetoothThread = new HTBluetoothThread();
                    mBluetoothThread.start();
                    HTLog.d(TAG, "158  开始建立通信链接");
                } catch (IOException e) {
                    bluetoothHandler.sendEmptyMessage(MSG_CODE_EXCEPTION_CONN);
                    e.printStackTrace();
                }
            }
        }).start();
    }

    /**
     * 断开所有链接
     */
    public void stopConnect() {
        try {
            if (socket != null) {
                socket.close();
            }
            if (mServerSocket != null) {
                mServerSocket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (mBluetoothThread != null && mBluetoothThread.isAlive()) {
            mBluetoothThread.stopConn();
        }
        socket = null;
        mServerSocket = null;
        mBluetoothThread = null;
    }

    /**
     * 发送信息
     */
    public void sendMessage(String msg) {
        if (msg == null) {
            throw new RuntimeException("Send message is null");
        }
        if (mBluetoothThread != null && mBluetoothThread.isAlive()) {
            mBluetoothThread.sendMessage(msg);
        } else {
            throw new RuntimeException("BluetoothThread is die");
        }
    }

    /**
     * 关闭蓝牙
     */
    public static void closeBluetooth() {
        HTBluetoothUtils.closeBluetooth();
    }


    /*================  单例模式   ==================*/
    private static class BluetoothHolder {
        private static final HTBluetoothHelper INSTANCE = new HTBluetoothHelper();
    }

    private HTBluetoothHelper() {
    }

    public static HTBluetoothHelper getInstance() {
        return BluetoothHolder.INSTANCE;
    }


    private class HTBluetoothThread extends Thread {

        private BufferedReader br;

        HTBluetoothThread() {
            if (socket == null) {
                bluetoothHandler.sendEmptyMessage(MSG_CODE_FAILURE_CONN);
            }
        }

        @Override
        public void run() {
            String content;
            try {
                br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                bluetoothHandler.sendEmptyMessage(MSG_CODE_SUCCESS_CONN);
                //处理接收的信息
                while ((content = br.readLine()) != null) {
                    HTLog.d(TAG, "276 接收的信息：" + content);
                    if (callBack != null) {
                        Message msg = Message.obtain();
                        msg.what = MSG_CODE_ACCEPT_MESSAGE;
                        msg.obj = content;
                        bluetoothHandler.sendMessage(msg);
                    }
                }
            } catch (IOException e) {
                try {
                    if (br != null)
                        br.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
                if (socket == null) {
                    bluetoothHandler.sendEmptyMessage(MSG_CODE_ACCEPT_EXCEPTION);
                }
                e.printStackTrace();
            }
        }

        // 发送数据
        void sendMessage(String msg) {
            if (socket == null) {
                bluetoothHandler.sendEmptyMessage(MSG_CODE_INTERRUPT_CONN);
            }
            try {
                OutputStream os = socket.getOutputStream();
                os.write((msg + "\r\n").getBytes("utf-8"));
                HTLog.d(TAG, "306 发送消息：" + msg);
            } catch (IOException e) {
                if (socket == null) {
                    bluetoothHandler.sendEmptyMessage(MSG_CODE_SEND_EXCEPTION);
                }
                e.printStackTrace();
            }
        }

        void sendMessage(Object obj) {
            if (socket == null) {
                bluetoothHandler.sendEmptyMessage(MSG_CODE_INTERRUPT_CONN);
            }
            try {
                ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
                oos.writeObject(obj);
//                OutputStream os = socket.getOutputStream();
//                os.write((msg + "\r\n").getBytes("utf-8"));
                HTLog.d(TAG, "306 发送消息：" + obj.toString());
            } catch (IOException e) {
                if (socket == null) {
                    bluetoothHandler.sendEmptyMessage(MSG_CODE_SEND_EXCEPTION);
                }
                e.printStackTrace();
            }
        }

        void stopConn() {
            try {
                if (br != null)
                    br.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private static class BluetoothHandler extends Handler {
        WeakReference<HTBluetoothHelper> htBluetoothHelperWeakReference;

        BluetoothHandler(HTBluetoothHelper htBluetoothHelper) {
            htBluetoothHelperWeakReference = new WeakReference<HTBluetoothHelper>(htBluetoothHelper);
        }

        @Override
        public void handleMessage(Message msg) {
            HTBluetoothHelper helper = htBluetoothHelperWeakReference.get();
            if (helper.callBack == null) {
                return;
            }
            switch (msg.what) {
                case MSG_CODE_ALREADY_CONN:
                    helper.callBack.alreadyConnect();
                    break;
                case MSG_CODE_OPEN_BLUETOOTH:
                    helper.callBack.openBluetooth();
                    break;
                case MSG_CODE_WAIT_CONN:
                    helper.callBack.waitConnect();
                    break;
                case MSG_CODE_ING_CONN:
                    helper.callBack.connecting();
                    break;
                case MSG_CODE_SUCCESS_CONN:
                    helper.callBack.connectSuccess();
                    break;
                case MSG_CODE_FAILURE_CONN:
                    helper.callBack.connectFailure();
                    break;
                case MSG_CODE_EXCEPTION_CONN:
                    helper.callBack.connectException();
                    break;
                case MSG_CODE_ACCEPT_EXCEPTION:
                    helper.callBack.acceptMessageException();
                    break;
                case MSG_CODE_SEND_EXCEPTION:
                    helper.callBack.sendMessageException();
                    break;
                case MSG_CODE_INTERRUPT_CONN:
                    helper.callBack.connectInterrupt();
                    break;
                case MSG_CODE_ACCEPT_MESSAGE:
                    helper.callBack.acceptMessage((String) msg.obj);
                    break;
            }
        }
    }
}
