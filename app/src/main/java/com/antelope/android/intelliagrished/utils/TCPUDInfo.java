package com.antelope.android.intelliagrished.utils;


import android.os.Handler;
import android.os.Message;
import android.util.Log;

import java.io.OutputStream;


/**
 * TCP数据信息
 */
public class TCPUDInfo {

    public static int env_values;

    //IP
    public static String IpAddress;

    //端口号
    public static int PortNumber;

    //TCP保活时间（TCP超时）
    public static int TCPKeepAlive;

    //查询指令发送间隔
    public static int SendTimeInterval;

    //设备连接状态
    public static boolean DeviceState = false;

    public static Handler mHandler;

    public static Handler mHandler1;

    // 发送队列,每次定时器会检索发送队列里的数据，如果非零（则取1-15，用来操作1号到15号继电器）数量暂定20
    public static int[] WriteArray = new int[20];

    public static int WriteArrayIndex = 0;

    public static boolean[] firstInitButtonName = new boolean[15];

    public static int AffirmFlag = 0; // 默认是不按下的状态

    static ServerThread mServerThread = new ServerThread();

    static OutputStream out;

    //查询指令用数组
    static byte[] RelayOn1 = new byte[]{-2, -2, 0x00, 0x00, 0x00, 0x06, 0x01, 0x06, 0x00, 0x09, 0x00, 0x01};
    static byte[] RelayOff1 = new byte[]{-2, -2, 0x00, 0x00, 0x00, 0x06, 0x01, 0x06, 0x00, 0x09, 0x00, 0x00};

    static byte[] RelayOn2 = new byte[]{-2, -2, 0x00, 0x00, 0x00, 0x06, 0x01, 0x06, 0x00, 0x0A, 0x00, 0x01};
    static byte[] RelayOff2 = new byte[]{-2, -2, 0x00, 0x00, 0x00, 0x06, 0x01, 0x06, 0x00, 0x0A, 0x00, 0x00};

    static byte[] RelayOn3 = new byte[]{-2, -2, 0x00, 0x00, 0x00, 0x06, 0x01, 0x06, 0x00, 0x0B, 0x00, 0x01};
    static byte[] RelayOff3 = new byte[]{-2, -2, 0x00, 0x00, 0x00, 0x06, 0x01, 0x06, 0x00, 0x0B, 0x00, 0x00};

    static byte[] RelayOn4 = new byte[]{-2, -2, 0x00, 0x00, 0x00, 0x06, 0x01, 0x06, 0x00, 0x0C, 0x00, 0x01};
    static byte[] RelayOff4 = new byte[]{-2, -2, 0x00, 0x00, 0x00, 0x06, 0x01, 0x06, 0x00, 0x0C, 0x00, 0x00};

    static byte[] RelayOn5 = new byte[]{-2, -2, 0x00, 0x00, 0x00, 0x06, 0x01, 0x06, 0x00, 0x0D, 0x00, 0x01};
    static byte[] RelayOff5 = new byte[]{-2, -2, 0x00, 0x00, 0x00, 0x06, 0x01, 0x06, 0x00, 0x0D, 0x00, 0x00};

    static byte[] RelayOn6 = new byte[]{-2, -2, 0x00, 0x00, 0x00, 0x06, 0x01, 0x06, 0x00, 0x0E, 0x00, 0x01};
    static byte[] RelayOff6 = new byte[]{-2, -2, 0x00, 0x00, 0x00, 0x06, 0x01, 0x06, 0x00, 0x0E, 0x00, 0x00};

    static byte[] RelayOn7 = new byte[]{-2, -2, 0x00, 0x00, 0x00, 0x06, 0x01, 0x06, 0x00, 0x0F, 0x00, 0x01};
    static byte[] RelayOff7 = new byte[]{-2, -2, 0x00, 0x00, 0x00, 0x06, 0x01, 0x06, 0x00, 0x0F, 0x00, 0x00};

    static byte[] RelayOn8 = new byte[]{-2, -2, 0x00, 0x00, 0x00, 0x06, 0x01, 0x06, 0x00, 0x10, 0x00, 0x01};
    static byte[] RelayOff8 = new byte[]{-2, -2, 0x00, 0x00, 0x00, 0x06, 0x01, 0x06, 0x00, 0x10, 0x00, 0x00};

    public static byte[] RelayOn9 = new byte[]{-2, -2, 0x00, 0x00, 0x00, 0x06, 0x01, 0x06, 0x00, 0x11, 0x00, 0x01};
    public static byte[] RelayOff9 = new byte[]{-2, -2, 0x00, 0x00, 0x00, 0x06, 0x01, 0x06, 0x00, 0x11, 0x00, 0x00};

    static byte[] RelayOn10 = new byte[]{-2, -2, 0x00, 0x00, 0x00, 0x06, 0x01, 0x06, 0x00, 0x12, 0x00, 0x01};
    static byte[] RelayOff10 = new byte[]{-2, -2, 0x00, 0x00, 0x00, 0x06, 0x01, 0x06, 0x00, 0x12, 0x00, 0x00};

    static byte[] RelayOn11 = new byte[]{-2, -2, 0x00, 0x00, 0x00, 0x06, 0x01, 0x06, 0x00, 0x13, 0x00, 0x01};
    static byte[] RelayOff11 = new byte[]{-2, -2, 0x00, 0x00, 0x00, 0x06, 0x01, 0x06, 0x00, 0x13, 0x00, 0x00};

    static byte[] RelayOn12 = new byte[]{-2, -2, 0x00, 0x00, 0x00, 0x06, 0x01, 0x06, 0x00, 0x14, 0x00, 0x01};
    static byte[] RelayOff12 = new byte[]{-2, -2, 0x00, 0x00, 0x00, 0x06, 0x01, 0x06, 0x00, 0x14, 0x00, 0x00};

    static byte[] RelayOn13 = new byte[]{-2, -2, 0x00, 0x00, 0x00, 0x06, 0x01, 0x06, 0x00, 0x15, 0x00, 0x01};
    static byte[] RelayOff13 = new byte[]{-2, -2, 0x00, 0x00, 0x00, 0x06, 0x01, 0x06, 0x00, 0x15, 0x00, 0x00};

    static byte[] RelayOn14 = new byte[]{-2, -2, 0x00, 0x00, 0x00, 0x06, 0x01, 0x06, 0x00, 0x16, 0x00, 0x01};
    static byte[] RelayOff14 = new byte[]{-2, -2, 0x00, 0x00, 0x00, 0x06, 0x01, 0x06, 0x00, 0x16, 0x00, 0x00};

    static byte[] RelayOn15 = new byte[]{-2, -2, 0x00, 0x00, 0x00, 0x06, 0x01, 0x06, 0x00, 0x17, 0x00, 0x01};
    static byte[] RelayOff15 = new byte[]{-2, -2, 0x00, 0x00, 0x00, 0x06, 0x01, 0x06, 0x00, 0x17, 0x00, 0x00};

    static byte[] ReadSomething = new byte[]{-2, -2, 0x00, 0x00, 0x00, 0x06, 0x01, 0x03, 0x00, 0x00, 0x00, 0x18};

    //接收缓冲区
    byte[] ReceiveBuffer = new byte[300];

    /**
     * 基本控制变量定义,设计成可以直接引用的全局变量型
     */
    // 模拟量采集值（数字量）存储数组
    public static int[] Digital = new int[8];

    // 模拟量采集值（电流值）存储数组
    public static double[] Analog = new double[8];

    // 15路继电器输出状态值
    public static boolean[] DigitalOutput = new boolean[15];

    // 8路数字量输入状态值
    public static boolean[] DigitalInput = new boolean[8];

    //发送基本信息
    void SendBaseInfo(){
        Log.d("TCPDUINFO", "run: 基本信息发送");
        int Temp = 0;
        int Temp1 = 0;

        Message BaseInfo1 = new Message();
        BaseInfo1.what = 0x0010;

        if (ReceiveBuffer[9] < 0) {
            Temp = 256;
        } else {
            Temp = 0;
        }
        if (ReceiveBuffer[10] < 0) {
            Temp1 = 256;
        } else {
            Temp1 = 0;
        }
        //空气环境：温度
        BaseInfo1.arg1 = (ReceiveBuffer[9] + Temp) * 256 + ReceiveBuffer[10] + Temp1;
        Log.d("TCPUDInfo", "温度:" + BaseInfo1.arg1);

        if (ReceiveBuffer[11] < 0) {
            Temp = 256;
        } else {
            Temp = 0;
        }
        if (ReceiveBuffer[12] < 0) {
            Temp1 = 256;
        } else {
            Temp1 = 0;
        }
        //空气环境：湿度
        BaseInfo1.arg2 = (ReceiveBuffer[11] + Temp) * 256 + ReceiveBuffer[12] + Temp1;

        mHandler.sendMessage(BaseInfo1);

        Message BaseInfo2 = new Message();
        BaseInfo2.what = 0x0011;

        if (ReceiveBuffer[13] < 0) {
            Temp = 256;
        } else {
            Temp = 0;
        }
        if (ReceiveBuffer[14] < 0) {
            Temp1 = 256;
        } else {
            Temp1 = 0;
        }
        //空气环境：光照
        BaseInfo2.arg1 = (ReceiveBuffer[13] + Temp) * 256 + ReceiveBuffer[14] + Temp1;

        if (ReceiveBuffer[15] < 0) {
            Temp = 256;
        } else {
            Temp = 0;
        }

        if (ReceiveBuffer[16] < 0) {
            Temp1 = 256;
        } else {
            Temp1 = 0;
        }
        //空气环境：二氧化碳浓度
        BaseInfo2.arg2 = (ReceiveBuffer[15] + Temp) * 256 + ReceiveBuffer[16] + Temp1;
        mHandler.sendMessage(BaseInfo2);

        Message BaseInfo3 = new Message();
        BaseInfo3.what = 0x0012;

        if (ReceiveBuffer[17] < 0) {
            Temp = 256;
        } else {
            Temp = 0;
        }

        if (ReceiveBuffer[18] < 0) {
            Temp1 = 256;
        } else {
            Temp1 = 0;
        }
        //土壤环境：温度
        BaseInfo3.arg1 = (ReceiveBuffer[17] + Temp) * 256 + ReceiveBuffer[18] + Temp1;

        if (ReceiveBuffer[19] < 0) {
            Temp = 256;
        } else {
            Temp = 0;
        }

        if (ReceiveBuffer[20] < 0) {
            Temp1 = 256;
        } else {
            Temp1 = 0;
        }
        //土壤环境：湿度
        BaseInfo3.arg2 = (ReceiveBuffer[19] + Temp) * 256 + ReceiveBuffer[20] + Temp1;
        mHandler.sendMessage(BaseInfo3);

        Message BaseInfo4 = new Message();
        BaseInfo4.what = 0x0013;

        if (ReceiveBuffer[21] < 0) {
            Temp = 256;
        } else {
            Temp = 0;
        }
        if (ReceiveBuffer[22] < 0) {
            Temp1 = 256;
        } else {
            Temp1 = 0;
        }
        //土壤环境：盐溶解度
        BaseInfo4.arg1 = (ReceiveBuffer[21] + Temp) * 256 + ReceiveBuffer[22] + Temp1;

        if (ReceiveBuffer[23] < 0) {
            Temp = 256;
        } else {
            Temp = 0;
        }
        if (ReceiveBuffer[24] < 0) {
            Temp1 = 256;
        } else {
            Temp1 = 0;
        }
        //土壤环境：PH值
        BaseInfo4.arg2 = (ReceiveBuffer[23] + Temp) * 256 + ReceiveBuffer[24] + Temp1;
        mHandler.sendMessage(BaseInfo4);

        Message BaseInfo5 = new Message();
        BaseInfo5.what = 0x0014;

        if (ReceiveBuffer[25] < 0) {
            Temp = 256;
        } else {
            Temp = 0;
        }
        if (ReceiveBuffer[26] < 0) {
            Temp1 = 256;
        } else {
            Temp1 = 0;
        }
        //灌溉环境：水箱水位
        BaseInfo5.arg1 = (ReceiveBuffer[25] + Temp) * 256 + ReceiveBuffer[26] + Temp1;

        BaseInfo5.arg2 = 0;
        mHandler.sendMessage(BaseInfo5);
    }

    //发送继电器状态
    public void SendRelayState() {
        Log.d("TCPODInfo", "SendRelayState success");
        if ((ReceiveBuffer[27] * 256 + ReceiveBuffer[28]) > 0) {
            Message msg = new Message();
            msg.what = 0x0001;
            mHandler1.sendMessage(msg);
        } else {
            Message msg = new Message();
            msg.what = 0x1001;
            mHandler1.sendMessage(msg);
        }
        if ((ReceiveBuffer[29] * 256 + ReceiveBuffer[30]) > 0) {
            Message msg = new Message();
            msg.what = 0x0002;
            mHandler1.sendMessage(msg);
        } else {
            Message msg = new Message();
            msg.what = 0x1002;
            mHandler1.sendMessage(msg);
        }
        if ((ReceiveBuffer[31] * 256 + ReceiveBuffer[32]) > 0) {
            Message msg = new Message();
            msg.what = 0x0003;
            mHandler1.sendMessage(msg);
        } else {
            Message msg = new Message();
            msg.what = 0x1003;
            mHandler1.sendMessage(msg);
        }
        if ((ReceiveBuffer[33] * 256 + ReceiveBuffer[34]) > 0) {
            Message msg = new Message();
            msg.what = 0x0004;
            mHandler1.sendMessage(msg);
        } else {
            Message msg = new Message();
            msg.what = 0x1004;
            mHandler1.sendMessage(msg);
        }
        if ((ReceiveBuffer[35] * 256 + ReceiveBuffer[36]) > 0) {
            Message msg = new Message();
            msg.what = 0x0005;
            msg.arg1=5;
            mHandler1.sendMessage(msg);
            Log.d("TCPUDInfo", "handleMessage: 顶窗开启");
        } else {
            Message msg = new Message();
            msg.what = 0x01005;
            mHandler1.sendMessage(msg);
        }
        if ((ReceiveBuffer[37] * 256 + ReceiveBuffer[38]) > 0) {
            Message msg = new Message();
            msg.what = 0x0006;
            mHandler1.sendMessage(msg);
        } else {
            Message msg = new Message();
            msg.what = 0x1006;
            mHandler1.sendMessage(msg);
        }
        if ((ReceiveBuffer[39] * 256 + ReceiveBuffer[40]) > 0) {
            Message msg = new Message();
            msg.what = 0x0007;
            mHandler1.sendMessage(msg);
        } else {
            Message msg = new Message();
            msg.what = 0x1007;
            mHandler1.sendMessage(msg);
        }
        if ((ReceiveBuffer[41] * 256 + ReceiveBuffer[42]) > 0) {
            Message msg = new Message();
            msg.what = 0x0008;
            mHandler1.sendMessage(msg);
        } else {
            Message msg = new Message();
            msg.what = 0x1008;
            mHandler1.sendMessage(msg);
        }
        if ((ReceiveBuffer[43] * 256 + ReceiveBuffer[44]) > 0) {
            Log.d("TCPUDInfo", "ReceiveBuffer: " + ReceiveBuffer[43] * 256 + ReceiveBuffer[44] );
            Message msg = new Message();
            msg.what = 0x0009;
            Log.d("TCPUDInfo", "msg.what9: "+ msg.what);
            mHandler1.sendMessage(msg);
        } else {
            Message msg = new Message();
            msg.what = 0x1009;
            mHandler1.sendMessage(msg);
        }
        if ((ReceiveBuffer[45] * 256 + ReceiveBuffer[46]) > 0) {
            Message msg = new Message();
            msg.what = 0x0000a;
            mHandler1.sendMessage(msg);
        } else {
            Message msg = new Message();
            msg.what = 0x100a;
            mHandler1.sendMessage(msg);
        }
        if ((ReceiveBuffer[47] * 256 + ReceiveBuffer[48]) > 0) {
            Message msg = new Message();
            msg.what = 0x000b;
            mHandler1.sendMessage(msg);
        } else {
            Message msg = new Message();
            msg.what = 0x100b;
            mHandler1.sendMessage(msg);
        }
        if ((ReceiveBuffer[49] * 256 + ReceiveBuffer[50]) > 0) {
            Message msg = new Message();
            msg.what = 0x000c;
            mHandler1.sendMessage(msg);
        } else {
            Message msg = new Message();
            msg.what = 0x100c;
            mHandler1.sendMessage(msg);
        }
        if ((ReceiveBuffer[51] * 256 + ReceiveBuffer[52]) > 0) {
            Message msg = new Message();
            msg.what = 0x000d;
            mHandler1.sendMessage(msg);
        } else {
            Message msg = new Message();
            msg.what = 0x100d;
            mHandler1.sendMessage(msg);
        }
        if ((ReceiveBuffer[53] * 256 + ReceiveBuffer[54]) > 0) {
            Message msg = new Message();
            msg.what = 0x000e;
            mHandler1.sendMessage(msg);
        } else {
            Message msg = new Message();
            msg.what = 0x100e;
            mHandler1.sendMessage(msg);
        }
        if ((ReceiveBuffer[55] * 256 + ReceiveBuffer[56]) > 0) {
            Message msg = new Message();
            msg.what = 0x000f;
            mHandler1.sendMessage(msg);
        } else {
            Message msg = new Message();
            msg.what = 0x100f;
            mHandler1.sendMessage(msg);
        }
    }

}