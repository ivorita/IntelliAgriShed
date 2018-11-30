package com.antelope.android.intelliagrished.utils;

import android.os.Message;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

public class ServerThread extends Thread {

    private static final String TAG = "ServerThread";
    private Socket mSocket;
    private InputStream in;
    private OutputStream out;
    private boolean First_Create_TCP = false;
    TCPUDInfo mTCPUDInfo = new TCPUDInfo();

    @Override
    public void run() {
        super.run();
        while (true) {
            createSocket();
            while (mSocket != null) {
                TimerDelay(TCPUDInfo.SendTimeInterval);
                sendHexData(TCPUDInfo.ReadSomething);
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
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void sendHexData(byte[] SendData) {
        try {
            out.write(SendData);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * 创建Socket
     */
    private void createSocket() {
        if (mSocket == null) {
            try {
                //前面的参数为host，指定一个本机端口号，要不然有点乱
                mSocket = new Socket(TCPUDInfo.IpAddress, TCPUDInfo.PortNumber);
                mSocket.setSoTimeout(TCPUDInfo.TCPKeepAlive);
                in = mSocket.getInputStream();
                out = mSocket.getOutputStream();
                TCPUDInfo.DeviceState = true;

                Message msg1 = new Message();
                msg1.what = 0xffff;
                TCPUDInfo.mHandler.sendMessage(msg1);    //服务器连接成功！

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
