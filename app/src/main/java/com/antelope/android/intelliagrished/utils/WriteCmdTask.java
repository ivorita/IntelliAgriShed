package com.antelope.android.intelliagrished.utils;

import android.util.Log;
import java.util.TimerTask;

public class WriteCmdTask extends TimerTask {

    private static final String TAG = "WriteCmdTask";

    private int IndexValue = 0;
    private boolean ValidValue = false;
    private TCPUDInfo mTCPUDInfo = new TCPUDInfo();
    private ServerThread mServerThread = new ServerThread();

    @Override
    public void run() {
        // 检索目标目录中，第一个有效数据
        for (int i = 0; i < 20; i++) {
            if (TCPUDInfo.WriteArray[i] != 0) {
                IndexValue = i; // 存入有效数据的索引值
                ValidValue = true;
                break;
            }
        }
        // 出现了有效值
        if (ValidValue) {
            ValidValue = false;
            Log.d(TAG, "序号: " + IndexValue);
            Log.d(TAG, "操作: " + TCPUDInfo.WriteArray[IndexValue]);
            // 判断当前索引值要进行什么操作
            switch (TCPUDInfo.WriteArray[IndexValue]) {
                case 1:
                    mServerThread.sendHexData(mTCPUDInfo.RelayOn1);
                    break;
                case 16:
                    mServerThread.sendHexData(mTCPUDInfo.RelayOff1);
                    break;
                case 2:
                    mServerThread.sendHexData(mTCPUDInfo.RelayOn2);
                    break;
                case 17:
                    mServerThread.sendHexData(mTCPUDInfo.RelayOff2);
                    break;
                case 3:
                    mServerThread.sendHexData(mTCPUDInfo.RelayOn3);
                    break;
                case 18:
                    mServerThread.sendHexData(mTCPUDInfo.RelayOff3);
                    break;
                case 4:
                    mServerThread.sendHexData(mTCPUDInfo.RelayOn4);
                    break;
                case 19:
                    mServerThread.sendHexData(mTCPUDInfo.RelayOff4);
                    break;
                case 5:
                    mServerThread.sendHexData(mTCPUDInfo.RelayOn5);
                    break;
                case 20:
                    mServerThread.sendHexData(mTCPUDInfo.RelayOff5);
                    break;
                case 6:
                    mServerThread.sendHexData(mTCPUDInfo.RelayOn6);
                    break;
                case 21:
                    mServerThread.sendHexData(mTCPUDInfo.RelayOff6);
                    break;
                case 7:
                    mServerThread.sendHexData(mTCPUDInfo.RelayOn7);
                    break;
                case 22:
                    mServerThread.sendHexData(mTCPUDInfo.RelayOff7);
                    break;
                case 8:
                    mServerThread.sendHexData(mTCPUDInfo.RelayOn8);
                    break;
                case 23:
                    mServerThread.sendHexData(mTCPUDInfo.RelayOff8);
                    break;
                case 9:
                    mServerThread.sendHexData(mTCPUDInfo.RelayOn9);
                    break;
                case 24:
                    mServerThread.sendHexData(mTCPUDInfo.RelayOff9);
                    break;
                case 10:
                    mServerThread.sendHexData(mTCPUDInfo.RelayOn10);
                    break;
                case 25:
                    mServerThread.sendHexData(mTCPUDInfo.RelayOff10);
                    break;
                case 11:
                    mServerThread.sendHexData(mTCPUDInfo.RelayOn11);
                    break;
                case 26:
                    mServerThread.sendHexData(mTCPUDInfo.RelayOff11);
                    break;
                case 12:
                    mServerThread.sendHexData(mTCPUDInfo.RelayOn12);
                    break;
                case 27:
                    mServerThread.sendHexData(mTCPUDInfo.RelayOff12);
                    break;
                case 13:
                    mServerThread.sendHexData(mTCPUDInfo.RelayOn13);
                    break;
                case 28:
                    mServerThread.sendHexData(mTCPUDInfo.RelayOff13);
                    break;
                case 14:
                    mServerThread.sendHexData(mTCPUDInfo.RelayOn14);
                    break;
                case 29:
                    mServerThread.sendHexData(mTCPUDInfo.RelayOff14);
                    break;
                case 15:
                    mServerThread.sendHexData(mTCPUDInfo.RelayOn15);
                    break;
                case 30:
                    mServerThread.sendHexData(mTCPUDInfo.RelayOff15);
                    break;
            }
            TCPUDInfo.WriteArray[IndexValue] = 0; // 触发完毕，当前的处理值清零
        }
    }
}
