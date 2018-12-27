package com.antelope.android.intelliagrished.fragment;

import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.antelope.android.intelliagrished.R;
import com.antelope.android.intelliagrished.utils.TCPUDInfo;
import com.antelope.android.intelliagrished.utils.WriteCmdTask;

import java.util.Timer;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

import static com.antelope.android.intelliagrished.utils.TCPUDInfo.AffirmFlag;

public class remoteFragment extends Fragment {

    private static final String TAG = "remoteFragment";

    @BindView(R.id.light)
    ImageView mLight;
    @BindView(R.id.curtain)
    ImageView mCurtain;
    @BindView(R.id.irrigation)
    ImageView mIrrigation;
    @BindView(R.id.door_1)
    ImageView mDoor1;
    @BindView(R.id.door_2)
    ImageView mDoor2;
    @BindView(R.id.door_3)
    ImageView mDoor3;
    @BindView(R.id.fan)
    ImageView mFan;
    @BindView(R.id.light_status)
    TextView mLightStatus;
    @BindView(R.id.curtain_status)
    TextView mCurtainStatus;
    @BindView(R.id.irrigation_status)
    TextView mIrrigationStatus;
    @BindView(R.id.door_1_status)
    TextView mDoor1Status;
    @BindView(R.id.door_2_status)
    TextView mDoor2Status;
    @BindView(R.id.door_3_status)
    TextView mDoor3Status;
    @BindView(R.id.fan_status)
    TextView mFanStatus;
    Unbinder unbinder1;
    @BindView(R.id.warm_air_1)
    ImageView mWarmAir1;
    @BindView(R.id.warm_air_2)
    ImageView mWarmAir2;
    @BindView(R.id.warm_status_1)
    TextView mWarmStatus1;
    @BindView(R.id.warm_status_2)
    TextView mWarmStatus2;
    private AlertDialog Dialog;
    private AlertDialog AffirmDialog;

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
        unbinder1 = ButterKnife.bind(this, view);
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
                            case 4: // 4继电器 1号侧帘
                                if (!TCPUDInfo.DigitalOutput[3]) {
                                    TCPUDInfo.WriteArray[TCPUDInfo.WriteArrayIndex] = 4;
                                    TCPUDInfo.DigitalOutput[3] = true;
                                } else {
                                    TCPUDInfo.WriteArray[TCPUDInfo.WriteArrayIndex] = 19;
                                    TCPUDInfo.DigitalOutput[3] = false;
                                }
                                AffirmFlag = 0;
                                break;
                            case 5: // 5继电器 一号顶窗
                                if (!TCPUDInfo.DigitalOutput[4]) {
                                    TCPUDInfo.WriteArray[TCPUDInfo.WriteArrayIndex] = 5;
                                    TCPUDInfo.DigitalOutput[4] = true;
                                } else {
                                    TCPUDInfo.WriteArray[TCPUDInfo.WriteArrayIndex] = 20;
                                    TCPUDInfo.DigitalOutput[4] = false;
                                }
                                AffirmFlag = 0;
                                break;
                            case 6: // 6继电器 2号顶窗
                                if (!TCPUDInfo.DigitalOutput[5]) {
                                    TCPUDInfo.WriteArray[TCPUDInfo.WriteArrayIndex] = 6;
                                    TCPUDInfo.DigitalOutput[5] = true;
                                } else {
                                    TCPUDInfo.WriteArray[TCPUDInfo.WriteArrayIndex] = 21;
                                    TCPUDInfo.DigitalOutput[5] = false;
                                }
                                AffirmFlag = 0;
                                break;
                            case 7: // 7继电器 1号侧窗
                                if (!TCPUDInfo.DigitalOutput[6]) {
                                    TCPUDInfo.WriteArray[TCPUDInfo.WriteArrayIndex] = 7;
                                    TCPUDInfo.DigitalOutput[6] = true;
                                } else {
                                    TCPUDInfo.WriteArray[TCPUDInfo.WriteArrayIndex] = 22;
                                    TCPUDInfo.DigitalOutput[6] = false;
                                }
                                AffirmFlag = 0;
                                break;
                            case 8: // 8继电器 通风机
                                if (!TCPUDInfo.DigitalOutput[7]) {
                                    TCPUDInfo.WriteArray[TCPUDInfo.WriteArrayIndex] = 8;
                                    TCPUDInfo.DigitalOutput[7] = true;
                                } else {
                                    TCPUDInfo.WriteArray[TCPUDInfo.WriteArrayIndex] = 23;
                                    TCPUDInfo.DigitalOutput[7] = false;
                                }
                                AffirmFlag = 0;

                                break;
                            case 9: // 9 补光灯
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
                            case 12: // 12 1号暖风机
                                if (!TCPUDInfo.DigitalOutput[11]) {
                                    TCPUDInfo.WriteArray[TCPUDInfo.WriteArrayIndex] = 12;
                                    TCPUDInfo.DigitalOutput[11] = true;
                                } else {
                                    TCPUDInfo.WriteArray[TCPUDInfo.WriteArrayIndex] = 27;
                                    TCPUDInfo.DigitalOutput[11] = false;
                                }
                                AffirmFlag = 0;
                                break;
                            case 13: // 13 2号暖风机
                                if (!TCPUDInfo.DigitalOutput[12]) {
                                    TCPUDInfo.WriteArray[TCPUDInfo.WriteArrayIndex] = 13;
                                    TCPUDInfo.DigitalOutput[12] = true;
                                } else {
                                    TCPUDInfo.WriteArray[TCPUDInfo.WriteArrayIndex] = 28;
                                    TCPUDInfo.DigitalOutput[12] = false;
                                }
                                AffirmFlag = 0;
                                break;
                            case 14: // 14 喷雾器
                                if (!TCPUDInfo.DigitalOutput[13]) {
                                    TCPUDInfo.WriteArray[TCPUDInfo.WriteArrayIndex] = 14;
                                    TCPUDInfo.DigitalOutput[13] = true;
                                } else {
                                    TCPUDInfo.WriteArray[TCPUDInfo.WriteArrayIndex] = 29;
                                    TCPUDInfo.DigitalOutput[13] = false;
                                }
                                AffirmFlag = 0;
                                break;
                            case 15: // 15继电器 灌溉阀门
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
        Log.d(TAG, "InitWriteCmdTimer " + "executed");
        //Toast.makeText(getContext(), "InitWriteCmdTimer executed", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onResume() {

        super.onResume();

        TCPUDInfo.mHandler1 = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                // 如果该消息是本程序所发送的
                switch (msg.what) {

                    case 0x0004: // 继电器4 1号侧帘 的消息句柄
                        mCurtain.setImageResource(R.drawable.curtain_on);
                        mCurtainStatus.setText(R.string.turn_on);
                        mCurtainStatus.setTextColor(getResources().getColor(R.color.on_state));
                        if (TCPUDInfo.firstInitButtonName[3] == false) {
                            TCPUDInfo.firstInitButtonName[3] = true;
                            TCPUDInfo.DigitalOutput[3] = true;
                        }
                        break;
                    case 0x1004: // 继电器4 1号侧帘 关闭的消息句柄
                        mCurtain.setImageResource(R.drawable.curtain_off);
                        mCurtainStatus.setText(R.string.turn_off);
                        mCurtainStatus.setTextColor(getResources().getColor(R.color.off_state));
                        if (TCPUDInfo.firstInitButtonName[3] == false) {
                            TCPUDInfo.firstInitButtonName[3] = true;
                            TCPUDInfo.DigitalOutput[3] = false;
                        }
                        break;
                    case 0x0005: // 继电器5 1号顶窗 开启的消息句柄
                        Log.d(TAG, "handleMessage: 顶窗开启");
                        Log.d(TAG, "handleMessage: " + msg.arg1);
                        mDoor1.setImageResource(R.drawable.door_open);
                        mDoor1Status.setText(R.string.turn_on);
                        mDoor1Status.setTextColor(getResources().getColor(R.color.on_state));
                        if (TCPUDInfo.firstInitButtonName[4] == false) {
                            TCPUDInfo.firstInitButtonName[4] = true;
                            TCPUDInfo.DigitalOutput[4] = true;
                        }
                        break;
                    case 0x1005: // 继电器5 1号顶窗 关闭的消息句柄
                        mDoor1.setImageResource(R.drawable.door_close);
                        mDoor1Status.setText(R.string.turn_off);
                        mDoor1Status.setTextColor(getResources().getColor(R.color.off_state));
                        if (TCPUDInfo.firstInitButtonName[4] == false) {
                            TCPUDInfo.firstInitButtonName[4] = true;
                            TCPUDInfo.DigitalOutput[4] = false;
                        }
                        break;
                    case 0x0006: // 继电器6 2号顶窗 开启的消息句柄
                        mDoor2.setImageResource(R.drawable.door_open);
                        mDoor2Status.setText(R.string.turn_on);
                        mDoor2Status.setTextColor(getResources().getColor(R.color.on_state));
                        if (TCPUDInfo.firstInitButtonName[5] == false) {
                            TCPUDInfo.firstInitButtonName[5] = true;
                            TCPUDInfo.DigitalOutput[5] = true;
                        }
                        break;
                    case 0x1006: // 继电器6 2号顶窗 关闭的消息句柄
                        mDoor2.setImageResource(R.drawable.door_close);
                        mDoor2Status.setText(R.string.turn_off);
                        mDoor2Status.setTextColor(getResources().getColor(R.color.off_state));
                        if (TCPUDInfo.firstInitButtonName[5] == false) {
                            TCPUDInfo.firstInitButtonName[5] = true;
                            TCPUDInfo.DigitalOutput[5] = false;
                        }
                        break;
                    case 0x0007: // 继电器7 1号侧窗 开启的消息句柄
                        mDoor3.setImageResource(R.drawable.door_open);
                        mDoor3Status.setText(R.string.turn_on);
                        mDoor3Status.setTextColor(getResources().getColor(R.color.on_state));
                        if (TCPUDInfo.firstInitButtonName[6] == false) {
                            TCPUDInfo.firstInitButtonName[6] = true;
                            TCPUDInfo.DigitalOutput[6] = true;
                        }
                        break;
                    case 0x1007: // 继电器7 1号侧窗 关闭的消息句柄
                        mDoor3.setImageResource(R.drawable.door_close);
                        mDoor3Status.setText(R.string.turn_off);
                        mDoor3Status.setTextColor(getResources().getColor(R.color.off_state));
                        if (TCPUDInfo.firstInitButtonName[6] == false) {
                            TCPUDInfo.firstInitButtonName[6] = true;
                            TCPUDInfo.DigitalOutput[6] = false;
                        }
                        break;
                    case 0x0008: // 继电器8 通风机 开启的消息句柄
                        Animation rotate = AnimationUtils.loadAnimation(getContext(), R.anim.fan_rotate);
                        if (rotate != null) {
                            mFan.setImageResource(R.drawable.fan_on);
                            mFan.startAnimation(rotate);
                        }
                        mFanStatus.setText(R.string.turn_on);
                        mFanStatus.setTextColor(getResources().getColor(R.color.on_state));
                        if (TCPUDInfo.firstInitButtonName[7] == false) {
                            TCPUDInfo.firstInitButtonName[7] = true;
                            TCPUDInfo.DigitalOutput[7] = true;
                        }
                        break;
                    case 0x1008: // 继电器8 通风机 关闭的消息句柄
                        mFan.clearAnimation();
                        mFan.setImageResource(R.drawable.fan_off);
                        mFanStatus.setText(R.string.turn_off);
                        mFanStatus.setTextColor(getResources().getColor(R.color.off_state));
                        if (TCPUDInfo.firstInitButtonName[7] == false) {
                            TCPUDInfo.firstInitButtonName[7] = true;
                            TCPUDInfo.DigitalOutput[7] = false;
                        }
                        break;
                    case 0x0009: // 继电器9 补光灯 打开的消息句柄
                        Log.d(TAG, "handleMessage: 9");
                        mLight.setImageResource(R.drawable.light_on);
                        mLightStatus.setText(R.string.turn_on);
                        mLightStatus.setTextColor(getResources().getColor(R.color.on_state));
                        if (TCPUDInfo.firstInitButtonName[8] == false) {
                            TCPUDInfo.firstInitButtonName[8] = true;
                            TCPUDInfo.DigitalOutput[8] = true;
                        }
                        break;
                    case 0x1009: // 继电器9 补光灯 关闭的消息句柄
                        mLight.setImageResource(R.drawable.light_off);
                        mLightStatus.setText(R.string.turn_off);
                        mLightStatus.setTextColor(getResources().getColor(R.color.off_state));
                        if (TCPUDInfo.firstInitButtonName[8] == false) {
                            TCPUDInfo.firstInitButtonName[8] = true;
                            TCPUDInfo.DigitalOutput[8] = false;
                        }
                        break;
                    case 0x000a: // 继电器10关闭的消息句柄
                        if (TCPUDInfo.firstInitButtonName[9] == false) {
                            TCPUDInfo.firstInitButtonName[9] = true;
                            TCPUDInfo.DigitalOutput[9] = true;
                        }
                        break;
                    case 0x100a: // 继电器10关闭的消息句柄
                        if (TCPUDInfo.firstInitButtonName[9] == false) {
                            TCPUDInfo.firstInitButtonName[9] = true;
                            TCPUDInfo.DigitalOutput[9] = false;
                        }
                        break;
                    case 0x000c: // 继电器12 1号暖风机 开启的消息句柄
                        Animation rotate_w1 = AnimationUtils.loadAnimation(getContext(), R.anim.fan_rotate);
                        if (rotate_w1 != null) {
                            mWarmAir1.setImageResource(R.drawable.warm_air_on);
                            mWarmAir1.startAnimation(rotate_w1);
                        }
                        mWarmStatus1.setText(R.string.turn_on);
                        mWarmStatus1.setTextColor(getResources().getColor(R.color.on_state));
                        if (TCPUDInfo.firstInitButtonName[11] == false) {
                            TCPUDInfo.firstInitButtonName[11] = true;
                            TCPUDInfo.DigitalOutput[11] = true;
                        }
                        break;
                    case 0x100c: // 继电器12 1号暖风机 关闭的消息句柄
                        mWarmAir1.clearAnimation();
                        mWarmAir1.setImageResource(R.drawable.warm_air_off);
                        mWarmStatus1.setText(R.string.turn_off);
                        mWarmStatus1.setTextColor(getResources().getColor(R.color.off_state));
                        if (TCPUDInfo.firstInitButtonName[11] == false) {
                            TCPUDInfo.firstInitButtonName[11] = true;
                            TCPUDInfo.DigitalOutput[11] = false;
                        }
                        break;
                    case 0x000d: // 继电器13 2号暖风机 开启的消息句柄
                        Animation rotate_w2 = AnimationUtils.loadAnimation(getContext(), R.anim.fan_rotate);
                        if (rotate_w2 != null) {
                            mWarmAir2.setImageResource(R.drawable.warm_air_on);
                            mWarmAir2.startAnimation(rotate_w2);
                        }
                        mWarmStatus2.setText(R.string.turn_on);
                        mWarmStatus2.setTextColor(getResources().getColor(R.color.on_state));
                        if (TCPUDInfo.firstInitButtonName[12] == false) {
                            TCPUDInfo.firstInitButtonName[12] = true;
                            TCPUDInfo.DigitalOutput[12] = true;
                        }
                        break;
                    case 0x100d: // 继电器13 2号暖风机 关闭的消息句柄
                        mWarmAir2.clearAnimation();
                        mWarmAir2.setImageResource(R.drawable.warm_air_off);
                        mWarmStatus2.setText(R.string.turn_off);
                        mWarmStatus2.setTextColor(getResources().getColor(R.color.off_state));
                        if (TCPUDInfo.firstInitButtonName[12] == false) {
                            TCPUDInfo.firstInitButtonName[12] = true;
                            TCPUDInfo.DigitalOutput[12] = false;
                        }
                        break;
                    case 0x000e: // 继电器14 喷雾器 开启的消息句柄
                        if (TCPUDInfo.firstInitButtonName[13] == false) {
                            TCPUDInfo.firstInitButtonName[13] = true;
                            TCPUDInfo.DigitalOutput[13] = true;
                        }
                        break;
                    case 0x100e: // 继电器14 喷雾器 关闭的消息句柄
                        if (TCPUDInfo.firstInitButtonName[13] == false) {
                            TCPUDInfo.firstInitButtonName[13] = true;
                            TCPUDInfo.DigitalOutput[13] = false;
                        }
                        break;
                    case 0x000f: // 继电器15 灌溉阀门 开启的消息句柄
                        mIrrigation.setImageResource(R.drawable.irrigation_on);
                        mIrrigationStatus.setText(R.string.turn_on);
                        mIrrigationStatus.setTextColor(getResources().getColor(R.color.on_state));
                        if (TCPUDInfo.firstInitButtonName[14] == false) {
                            TCPUDInfo.firstInitButtonName[14] = true;
                            TCPUDInfo.DigitalOutput[14] = true;
                        }
                        break;
                    case 0x100f: // 继电器15 灌溉阀门 关闭的消息句柄
                        mIrrigation.setImageResource(R.drawable.irrigation_off);
                        mIrrigationStatus.setText(R.string.turn_off);
                        mIrrigationStatus.setTextColor(getResources().getColor(R.color.off_state));
                        if (TCPUDInfo.firstInitButtonName[14] == false) {
                            TCPUDInfo.firstInitButtonName[14] = true;
                            TCPUDInfo.DigitalOutput[14] = false;
                        }
                        break;
                    default:
                        super.handleMessage(msg);
                        Log.d(TAG, "handleMessage: null");
                        break;
                }
            }

        };

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        //unbinder1.unbind();
    }

    @OnClick({R.id.light, R.id.curtain, R.id.irrigation, R.id.door_1, R.id.door_2, R.id.door_3, R.id.warm_air_1,R.id.warm_air_2,R.id.fan})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.light:
                if (TCPUDInfo.DeviceState) {
                    AffirmFlag = 9; // 指示：现在点击的是第一个按钮
                    AffirmDialog.show();
                } else {
                    AffirmFlag = 0; // 无效的情况下，不做任何操作
                    Dialog.show();
                }
                break;
            case R.id.curtain:
                if (TCPUDInfo.DeviceState) {
                    AffirmFlag = 4; // 指示：现在点击的是第一个按钮
                    AffirmDialog.show();
                } else {
                    AffirmFlag = 0; // 无效的情况下，不做任何操作
                    Dialog.show();
                }
                //isOn_2 = !isOn_2;
                break;
            case R.id.irrigation:
                if (TCPUDInfo.DeviceState) {
                    AffirmFlag = 15; // 指示：现在点击的是第一个按钮
                    AffirmDialog.show();
                } else {
                    AffirmFlag = 0; // 无效的情况下，不做任何操作
                    Dialog.show();
                }
                //isOn_3 = !isOn_3;
                break;
            case R.id.door_1:
                if (TCPUDInfo.DeviceState) {
                    AffirmFlag = 5; // 指示：现在点击的是第一个按钮
                    AffirmDialog.show();
                } else {
                    AffirmFlag = 0; // 无效的情况下，不做任何操作
                    Dialog.show();
                }
                //isOn_4 = !isOn_4;
                break;
            case R.id.door_2:
                if (TCPUDInfo.DeviceState) {
                    AffirmFlag = 6; // 指示：现在点击的是第一个按钮
                    AffirmDialog.show();
                } else if (TCPUDInfo.DeviceState) {
                    AffirmFlag = 0; // 无效的情况下，不做任何操作
                    Dialog.show();
                }
                break;
            case R.id.door_3:
                if (TCPUDInfo.DeviceState) {
                    AffirmFlag = 7; // 指示：现在点击的是第一个按钮
                    AffirmDialog.show();
                } else {
                    AffirmFlag = 0; // 无效的情况下，不做任何操作
                    Dialog.show();
                }
                break;
            case R.id.warm_air_1:
                if (TCPUDInfo.DeviceState) {
                    AffirmFlag = 12; // 指示：现在点击的是第一个按钮
                    AffirmDialog.show();
                } else {
                    AffirmFlag = 0; // 无效的情况下，不做任何操作
                    Dialog.show();
                }
                break;
            case R.id.warm_air_2:
                if (TCPUDInfo.DeviceState) {
                    AffirmFlag = 13; // 指示：现在点击的是第一个按钮
                    AffirmDialog.show();
                } else {
                    AffirmFlag = 0; // 无效的情况下，不做任何操作
                    Dialog.show();
                }
                break;
            case R.id.fan:
                if (TCPUDInfo.DeviceState) {
                    AffirmFlag = 8; // 指示：现在点击的是第一个按钮
                    AffirmDialog.show();
                } else {
                    AffirmFlag = 0; // 无效的情况下，不做任何操作
                    Dialog.show();
                }
                break;
        }
    }
}