package com.antelope.android.intelliagrished.fragment;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.antelope.android.intelliagrished.R;
import com.antelope.android.intelliagrished.utils.TCPUDInfo;
import com.antelope.android.intelliagrished.utils.WriteCmdTask;

import java.util.Timer;

public class remoteFragment extends Fragment {

    private AlertDialog Dialog;
    private AlertDialog AffirmDialog;

    //默认不是按下的状态
    private int AffirmFlag = 0;


    public static remoteFragment newInstance() {
        remoteFragment fragment = new remoteFragment();
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_remote, container, false);
        InitDialog();
        InitAffirmDialog();
        InitWriteCmdTimer();
        return view;
    }

    private void InitDialog() {
        Dialog = new AlertDialog.Builder(getContext()).setCancelable(false) // 屏幕外部区域点击无效
                .setTitle("错误：").setMessage("网络断开或服务器未开启！请检查网络！").setPositiveButton("确定", null).create();
    }

    private void InitAffirmDialog() {
        AffirmDialog = new AlertDialog.Builder(getContext()).setCancelable(false) // 屏幕外部区域点击无效
                .setTitle("提示：").setMessage("确认要操作吗？").setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        switch (AffirmFlag) {
                            case 1: // 第一个继电器
                                // 现在是关的状态
                                if (!TCPUDInfo.DigitalOutput[0]) {
                                    TCPUDInfo.WriteArray[TCPUDInfo.WriteArrayIndex] = 1;
                                    TCPUDInfo.DigitalOutput[0] = true;
                                } else {
                                    TCPUDInfo.WriteArray[TCPUDInfo.WriteArrayIndex] = 16;
                                    TCPUDInfo.DigitalOutput[0] = false;
                                }
                                AffirmFlag = 0;
                                break;
                            case 2: // 第二个继电器
                                if (!TCPUDInfo.DigitalOutput[1]) {
                                    TCPUDInfo.WriteArray[TCPUDInfo.WriteArrayIndex] = 2;
                                    TCPUDInfo.DigitalOutput[1] = true;
                                } else {
                                    TCPUDInfo.WriteArray[TCPUDInfo.WriteArrayIndex] = 17;
                                    TCPUDInfo.DigitalOutput[1] = false;
                                }
                                AffirmFlag = 0;
                                break;
                            case 3: // 第三个继电器
                                if (!TCPUDInfo.DigitalOutput[2]) {
                                    TCPUDInfo.WriteArray[TCPUDInfo.WriteArrayIndex] = 3;
                                    TCPUDInfo.DigitalOutput[2] = true;
                                } else {
                                    TCPUDInfo.WriteArray[TCPUDInfo.WriteArrayIndex] = 18;
                                    TCPUDInfo.DigitalOutput[2] = false;
                                }
                                AffirmFlag = 0;
                                break;
                            case 4: // 第四个继电器
                                if (!TCPUDInfo.DigitalOutput[3]) {
                                    TCPUDInfo.WriteArray[TCPUDInfo.WriteArrayIndex] = 4;
                                    TCPUDInfo.DigitalOutput[3] = true;
                                } else {
                                    TCPUDInfo.WriteArray[TCPUDInfo.WriteArrayIndex] = 19;
                                    TCPUDInfo.DigitalOutput[3] = false;
                                }
                                AffirmFlag = 0;
                                break;
                            case 5: // 第五个继电器
                                if (!TCPUDInfo.DigitalOutput[4]) {
                                    TCPUDInfo.WriteArray[TCPUDInfo.WriteArrayIndex] = 5;
                                    TCPUDInfo.DigitalOutput[4] = true;
                                } else {
                                    TCPUDInfo.WriteArray[TCPUDInfo.WriteArrayIndex] = 20;
                                    TCPUDInfo.DigitalOutput[4] = false;
                                }
                                AffirmFlag = 0;
                                break;
                            case 6: // 第六个继电器
                                if (!TCPUDInfo.DigitalOutput[5]) {
                                    TCPUDInfo.WriteArray[TCPUDInfo.WriteArrayIndex] = 6;
                                    TCPUDInfo.DigitalOutput[5] = true;
                                } else {
                                    TCPUDInfo.WriteArray[TCPUDInfo.WriteArrayIndex] = 21;
                                    TCPUDInfo.DigitalOutput[5] = false;
                                }
                                AffirmFlag = 0;
                                break;
                            case 7: // 第七个继电器
                                if (!TCPUDInfo.DigitalOutput[6]) {
                                    TCPUDInfo.WriteArray[TCPUDInfo.WriteArrayIndex] = 7;
                                    TCPUDInfo.DigitalOutput[6] = true;
                                } else {
                                    TCPUDInfo.WriteArray[TCPUDInfo.WriteArrayIndex] = 22;
                                    TCPUDInfo.DigitalOutput[6] = false;
                                }
                                AffirmFlag = 0;
                                break;
                            case 8: // 第八个继电器
                                if (!TCPUDInfo.DigitalOutput[7]) {
                                    TCPUDInfo.WriteArray[TCPUDInfo.WriteArrayIndex] = 8;
                                    TCPUDInfo.DigitalOutput[7] = true;
                                } else {
                                    TCPUDInfo.WriteArray[TCPUDInfo.WriteArrayIndex] = 23;
                                    TCPUDInfo.DigitalOutput[7] = false;
                                }
                                AffirmFlag = 0;

                                break;
                            case 9: // 第九个继电器
                                if (!TCPUDInfo.DigitalOutput[8]) {
                                    TCPUDInfo.WriteArray[TCPUDInfo.WriteArrayIndex] = 9;
                                    TCPUDInfo.DigitalOutput[8] = true;
                                } else {
                                    TCPUDInfo.WriteArray[TCPUDInfo.WriteArrayIndex] = 24;
                                    TCPUDInfo.DigitalOutput[8] = false;
                                }
                                AffirmFlag = 0;
                                break;
                            case 10: // 第十个继电器
                                if (!TCPUDInfo.DigitalOutput[9]) {
                                    TCPUDInfo.WriteArray[TCPUDInfo.WriteArrayIndex] = 10;
                                    TCPUDInfo.DigitalOutput[9] = true;
                                } else {
                                    TCPUDInfo.WriteArray[TCPUDInfo.WriteArrayIndex] = 25;
                                    TCPUDInfo.DigitalOutput[9] = false;
                                }
                                AffirmFlag = 0;
                                break;
                            case 11: // 第十一个继电器
                                if (!TCPUDInfo.DigitalOutput[10]) {
                                    TCPUDInfo.WriteArray[TCPUDInfo.WriteArrayIndex] = 11;
                                    TCPUDInfo.DigitalOutput[10] = true;
                                } else {
                                    TCPUDInfo.WriteArray[TCPUDInfo.WriteArrayIndex] = 26;
                                    TCPUDInfo.DigitalOutput[10] = false;
                                }
                                AffirmFlag = 0;
                                break;
                            case 12: // 第十二个继电器
                                if (!TCPUDInfo.DigitalOutput[11]) {
                                    TCPUDInfo.WriteArray[TCPUDInfo.WriteArrayIndex] = 12;
                                    TCPUDInfo.DigitalOutput[11] = true;
                                } else {
                                    TCPUDInfo.WriteArray[TCPUDInfo.WriteArrayIndex] = 27;
                                    TCPUDInfo.DigitalOutput[11] = false;
                                }
                                AffirmFlag = 0;
                                break;
                            case 13: // 第十三个继电器
                                if (!TCPUDInfo.DigitalOutput[12]) {
                                    TCPUDInfo.WriteArray[TCPUDInfo.WriteArrayIndex] = 13;
                                    TCPUDInfo.DigitalOutput[12] = true;
                                } else {
                                    TCPUDInfo.WriteArray[TCPUDInfo.WriteArrayIndex] = 28;
                                    TCPUDInfo.DigitalOutput[12] = false;
                                }
                                AffirmFlag = 0;
                                break;
                            case 14: // 第十四个继电器
                                if (!TCPUDInfo.DigitalOutput[13]) {
                                    TCPUDInfo.WriteArray[TCPUDInfo.WriteArrayIndex] = 14;
                                    TCPUDInfo.DigitalOutput[13] = true;
                                } else {
                                    TCPUDInfo.WriteArray[TCPUDInfo.WriteArrayIndex] = 29;
                                    TCPUDInfo.DigitalOutput[13] = false;
                                }
                                AffirmFlag = 0;
                                break;
                            case 15: // 第十五个继电器
                                if (!TCPUDInfo.DigitalOutput[14]) {
                                    TCPUDInfo.WriteArray[TCPUDInfo.WriteArrayIndex] = 15;
                                    TCPUDInfo.DigitalOutput[14] = true;
                                } else {
                                    TCPUDInfo.WriteArray[TCPUDInfo.WriteArrayIndex] = 30;
                                    TCPUDInfo.DigitalOutput[14] = false;
                                }
                                AffirmFlag = 0;
                                break;
                            default:
                                AffirmFlag = 0;
                                break;
                        }
                        TCPUDInfo.WriteArrayIndex++;
                        // 消息队列送够19，则送满，这时候返回第一个,也就是说5秒钟之内最多能接收50条指令
                        if (TCPUDInfo.WriteArrayIndex > 19) {
                            TCPUDInfo.WriteArrayIndex = 0;
                        }
                    }
                }).setNegativeButton("取消", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        AffirmFlag = 0;
                    }
                }).create(); // 取消不需要任何代码
    }

    /**
     * 开机1秒钟之后，每隔2秒钟检索一次发送队列，并操作发送队列发送写指令
     */
    private void InitWriteCmdTimer() {
        Timer time = new Timer();
        time.schedule(new WriteCmdTask(), 1000, 2000);
    }
}
