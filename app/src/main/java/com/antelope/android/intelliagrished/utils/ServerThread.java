package com.antelope.android.intelliagrished.utils;

import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.io.InterruptedIOException;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

import static com.antelope.android.intelliagrished.utils.TCPUDInfo.out;

public class ServerThread extends Thread {

    private static final String TAG = "ServerThread";
    private Socket mSocket;
    private InputStream in;
    //private OutputStream out;
    private boolean First_Create_TCP = false;
    private TCPUDInfo mTCPUDInfo = new TCPUDInfo();

    @Override
    public void run() {
        super.run();
        Log.d(TAG, "线程启动");
        while (true) {
            createSocket();
            while (mSocket != null) {
                TimerDelay(TCPUDInfo.SendTimeInterval);
                sendHexData(TCPUDInfo.ReadSomething);
                //sendHexData(TCPUDInfo.RelayOff5);
                try {
                    if ((in.read(mTCPUDInfo.ReceiveBuffer)) != -1) {
                        //一旦出现06，过滤掉
                        if (mTCPUDInfo.ReceiveBuffer[7] != 0x06) {

                            //处理03数据
                            Message msg3 = new Message();
                            msg3.what = 0xfffd;
                            TCPUDInfo.mHandler.sendMessage(msg3);

                            mTCPUDInfo.SendRelayState();
                            //发送基本03信息
                            mTCPUDInfo.SendBaseInfo();
                        }
                    }  else {
                        // ------------------------------------------
                        Message msg4 = new Message();
                        msg4.what = 0xfffc;
                        TCPUDInfo.mHandler.sendMessage(msg4); // 服务器断开！
                        // ------------------------------------------
                        mSocket.close();
                        mSocket = null; // 并未真正关闭，要手动清空
                        Log.d(TAG, "服务器断开");
                        TCPUDInfo.DeviceState = false;
                        createSocket();
                    }
                } catch (InterruptedIOException e) {
                    e.printStackTrace();
                    TCPUDInfo.DeviceState = false;
                    try {
                        mSocket.close();
                        mSocket = null; // 并未真正关闭，要手动清空
                        createSocket(); // 超时之后马上连接socket
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    Log.e(TAG, "错误" + e.toString() );
                }
            }
        }
    }

    void sendHexData(byte[] SendData) {
        try {
            /*if (out != null) {

            } else {
                Log.d(TAG, "out null" );
            }*/
            out.write(SendData);
        } catch (Exception e2) {
            e2.printStackTrace();
            Log.e(TAG, "error " + e2.toString() );
        }
    }


    /**
     * 创建Socket
     */
    private void createSocket() {
        if (mSocket == null) {
            try {
                TCPUDInfo.firstInitButtonName[0] = false;
                TCPUDInfo.firstInitButtonName[1] = false;
                TCPUDInfo.firstInitButtonName[2] = false;
                TCPUDInfo.firstInitButtonName[3] = false;
                TCPUDInfo.firstInitButtonName[4] = false;
                TCPUDInfo.firstInitButtonName[5] = false;
                TCPUDInfo.firstInitButtonName[6] = false;
                TCPUDInfo.firstInitButtonName[7] = false;
                //前面的参数为host，指定一个本机端口号，要不然有点乱
                mSocket = new Socket(TCPUDInfo.IpAddress, TCPUDInfo.PortNumber);
                mSocket.setSoTimeout(TCPUDInfo.TCPKeepAlive);
                in = mSocket.getInputStream();
                out = mSocket.getOutputStream();
                TCPUDInfo.DeviceState = true;

                Message msg1 = new Message();
                msg1.what = 0xffff;
                TCPUDInfo.mHandler.sendMessage(msg1);    //服务器连接成功！
                //sendHexData(TCPUDInfo.ReadSomething);
                //第一次连接成功，则显示true,此后该变量值不变
                First_Create_TCP = true;
            } catch (UnknownHostException e) {
                Log.e(TAG, "createSocket: " + "链接发生异常" );
                TCPUDInfo.DeviceState = false;
                e.printStackTrace();
            } catch (IOException e) {
                TCPUDInfo.DeviceState = false;
                if (First_Create_TCP) {
                    //这时候可以原地等待的原因是不连上socket,什么都干不了
                    TimerDelay(30000);
                }
                try {
                    if (mSocket != null) {
                        mSocket.close();
                        mSocket = null;
                        Log.d(TAG, "关闭socket");
                    }
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        }
    }


    /**
     * 停止Socket
     */
    public void stopSocket() {
        try {
            while (mSocket != null) {
                mSocket.close();
                in.close();
                out.close();
                mSocket = null;
            }
        } catch (IOException e) {
            e.printStackTrace();
            Log.e(TAG, "error: " + e.toString());
        }
    }


    /**
     * 延时
     * @param DelayTime 延时时间
     */
    private void TimerDelay(long DelayTime) {
        long NowTime;
        NowTime = System.currentTimeMillis(); //存基准时间值
        while (NowTime > 0) {
            if ((System.currentTimeMillis() - NowTime) > DelayTime)   //如果当前时间值和记录的时间基准之前的差值大于某个值
            {
                NowTime = 0;
            }
        }
    }
}