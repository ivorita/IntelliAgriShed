package com.antelope.android.intelliagrished.utils;

import android.util.Log;
import android.widget.Toast;

import java.util.TimerTask;

import static com.antelope.android.intelliagrished.utils.TCPUDInfo.mServerThread;

public class WriteCmdTask extends TimerTask {

    private static final String TAG = "WriteCmdTask";

    private int IndexValue = 0;
    private boolean ValidValue = false;
    private TCPUDInfo mTCPUDInfo = new TCPUDInfo();


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
                    mServerThread.sendHexData(TCPUDInfo.RelayOn1);
                    break;
                case 16:
                    mServerThread.sendHexData(TCPUDInfo.RelayOff1);
                    break;
                case 2:
                    mServerThread.sendHexData(TCPUDInfo.RelayOn2);
                    break;
                case 17:
                    mServerThread.sendHexData(TCPUDInfo.RelayOff2);
                    break;
                case 3:
                    mServerThread.sendHexData(TCPUDInfo.RelayOn3);
                    break;
                case 18:
                    mServerThread.sendHexData(TCPUDInfo.RelayOff3);
                    break;
                case 4:
                    mServerThread.sendHexData(TCPUDInfo.RelayOn4);
                    break;
                case 19:
                    mServerThread.sendHexData(TCPUDInfo.RelayOff4);
                    break;
                case 5:
                    mServerThread.sendHexData(TCPUDInfo.RelayOn5);
                    break;
                case 20:
                    mServerThread.sendHexData(TCPUDInfo.RelayOff5);
                    break;
                case 6:
                    mServerThread.sendHexData(TCPUDInfo.RelayOn6);
                    break;
                case 21:
                    mServerThread.sendHexData(TCPUDInfo.RelayOff6);
                    break;
                case 7:
                    mServerThread.sendHexData(TCPUDInfo.RelayOn7);
                    break;
                case 22:
                    mServerThread.sendHexData(TCPUDInfo.RelayOff7);
                    break;
                case 8:
                    mServerThread.sendHexData(TCPUDInfo.RelayOn8);
                    break;
                case 23:
                    mServerThread.sendHexData(TCPUDInfo.RelayOff8);
                    break;
                case 9:
                    mServerThread.sendHexData(TCPUDInfo.RelayOn9);
                    break;
                case 24:
                    mServerThread.sendHexData(TCPUDInfo.RelayOff9);
                    break;
                case 10:
                    mServerThread.sendHexData(TCPUDInfo.RelayOn10);
                    break;
                case 25:
                    mServerThread.sendHexData(TCPUDInfo.RelayOff10);
                    break;
                case 11:
                    mServerThread.sendHexData(TCPUDInfo.RelayOn11);
                    break;
                case 26:
                    mServerThread.sendHexData(TCPUDInfo.RelayOff11);
                    break;
                case 12:
                    mServerThread.sendHexData(TCPUDInfo.RelayOn12);
                    break;
                case 27:
                    mServerThread.sendHexData(TCPUDInfo.RelayOff12);
                    break;
                case 13:
                    mServerThread.sendHexData(TCPUDInfo.RelayOn13);
                    break;
                case 28:
                    mServerThread.sendHexData(TCPUDInfo.RelayOff13);
                    break;
                case 14:
                    mServerThread.sendHexData(TCPUDInfo.RelayOn14);
                    break;
                case 29:
                    mServerThread.sendHexData(TCPUDInfo.RelayOff14);
                    break;
                case 15:
                    mServerThread.sendHexData(TCPUDInfo.RelayOn15);
                    break;
                case 30:
                    mServerThread.sendHexData(TCPUDInfo.RelayOff15);
                    break;
            }
            TCPUDInfo.WriteArray[IndexValue] = 0; // 触发完毕，当前的处理值清零
        } else {
            Log.i(TAG, "未发现有效值");
        }
    }
}