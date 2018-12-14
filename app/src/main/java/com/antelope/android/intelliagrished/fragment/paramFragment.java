package com.antelope.android.intelliagrished.fragment;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.SharedPreferences;
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
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.antelope.android.intelliagrished.R;
import com.antelope.android.intelliagrished.utils.ServerThread;
import com.antelope.android.intelliagrished.utils.TCPUDInfo;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class paramFragment extends Fragment {

    private static final String TAG = "paramFragment";

    String[] IPSplit = new String[4];

    private AlertDialog SaveDialog;

    private SharedPreferences sharedPreferences;

    @BindView(R.id.IpAddress1)
    EditText mIpAddress1;
    @BindView(R.id.IpAddress2)
    EditText mIpAddress2;
    @BindView(R.id.IpAddress3)
    EditText mIpAddress3;
    @BindView(R.id.IpAddress4)
    EditText mIpAddress4;
    @BindView(R.id.PortNumber)
    EditText mPortNumber;
    @BindView(R.id.TCPKeepAlive)
    EditText mTCPKeepAlive;
    @BindView(R.id.SendTimeInterval)
    EditText mSendTimeInterval;
    @BindView(R.id.save)
    Button mSave;
    @BindView(R.id.restore)
    Button mRestore;
    @BindView(R.id.deviceState)
    TextView mDeviceState;

    private Unbinder unbinder;

    ServerThread mServerThread = new ServerThread();

    public static paramFragment newInstance() {
        paramFragment fragment = new paramFragment();
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_param, container, false);
        unbinder = ButterKnife.bind(this, view);

        // 判断设备网络连接状态
        if (!TCPUDInfo.DeviceState) {
            mDeviceState.setText(R.string.network_server_offline);
        } else {
            mDeviceState.setText(R.string.init_complete);
        }

        SaveDialog();

        InitConfig();

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        //unbinder.unbind();
    }

    //修改保存对话框
    private void SaveDialog() {
        SaveDialog = new AlertDialog.Builder(getActivity()).setCancelable(false) // 屏幕外部区域点击无效
                .setTitle(R.string.tip).setMessage("确认要保存吗？").setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        if (mIpAddress1.getText().toString().equals("") || mIpAddress2.getText().toString().equals("")
                                || mIpAddress3.getText().toString().equals("")
                                || mIpAddress4.getText().toString().equals("")
                                || mPortNumber.getText().toString().equals("")
                                || mTCPKeepAlive.getText().toString().equals("")
                                || mSendTimeInterval.getText().toString().equals("")) {
                            Toast.makeText(getContext(), "参数不能为空！", Toast.LENGTH_SHORT).show();
                        } else if ((Integer.parseInt(mTCPKeepAlive.getText().toString()) < 10)
                                || (Integer.parseInt(mSendTimeInterval.getText().toString()) < 1)) {
                            Toast.makeText(getContext(), "发送间隔最少设置1秒钟，超时时间最少设置10秒钟，请修改", Toast.LENGTH_SHORT)
                                    .show();
                        } else {
                            TCPUDInfo.IpAddress = mIpAddress1.getText().toString() + "."
                                    + mIpAddress2.getText().toString() + "." + mIpAddress3.getText().toString() + "."
                                    + mIpAddress4.getText().toString(); // 写入IP地址

                            TCPUDInfo.PortNumber = Integer.parseInt(mPortNumber.getText().toString());

                            TCPUDInfo.TCPKeepAlive = Integer.parseInt(mTCPKeepAlive.getText().toString())
                                    * 1000; // 写入TCP超时时间

                            TCPUDInfo.SendTimeInterval = Integer
                                    .parseInt(mSendTimeInterval.getText().toString()) * 1000; // 写入TCP发送间隔

                            SharedPreferences mySharedPreferences = getActivity().getSharedPreferences("TCPData",
                                    Activity.MODE_PRIVATE);
                            SharedPreferences.Editor editor = mySharedPreferences.edit();

                            editor.putString("IPAddress", TCPUDInfo.IpAddress);
                            editor.putString("PortNumber", Integer.toString(TCPUDInfo.PortNumber));
                            editor.putString("TCPKeepAlive",
                                    Integer.toString(TCPUDInfo.TCPKeepAlive));
                            editor.putString("SendTimeInterval",
                                    Integer.toString(TCPUDInfo.SendTimeInterval));

                            editor.apply();

                            Toast.makeText(getContext(), "设置成功，重新连接中。", Toast.LENGTH_SHORT).show();
                            if (!TCPUDInfo.DeviceState) {
                                mDeviceState.setText("网络未开或服务器关");
                            } else {
                                //mServerThread.stopSocket();
                                Log.d(TAG, "关闭socket");
                                mDeviceState.setText("立即断线重连");
                            }
                        }
                    }
                }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        // 取消不需要任何代码
                    }
                }).create();
        Window window = SaveDialog.getWindow();
        window.setWindowAnimations(R.style.dialog_anim);
    }

    //初始化参数设置
    private void InitConfig() {
        // 如果不存在，系统会自己创建
        sharedPreferences = getActivity().getSharedPreferences("TCPData", Activity.MODE_PRIVATE);
        // 读当前IP地址
        TCPUDInfo.IpAddress = sharedPreferences.getString("IPAddress", "");
        // 如果读出来的是空，则恢复默认值
        if (TCPUDInfo.IpAddress.equals("")) {
            SharedPreferences mySharedPreferences = getActivity().getSharedPreferences("TCPData", Activity.MODE_PRIVATE);
            SharedPreferences.Editor editor = mySharedPreferences.edit();
            editor.putString("IPAddress", "192.168.200.20");
            editor.apply();
            // 读当前IP地址
            TCPUDInfo.IpAddress = sharedPreferences.getString("IPAddress", "");
        }

        // 读当前端口号
        String PortNumber = sharedPreferences.getString("PortNumber", "");
        if (PortNumber.equals("")) {
            SharedPreferences mySharedPreferences = getActivity().getSharedPreferences("TCPData", Activity.MODE_PRIVATE);
            SharedPreferences.Editor editor = mySharedPreferences.edit();
            editor.putString("PortNumber", "60000");
            editor.apply();
        }
        // 存当前端口号
        TCPUDInfo.PortNumber = Integer.parseInt(sharedPreferences.getString("PortNumber", ""));

        // 读TCP保活时间
        String TCPKeepAliveName = sharedPreferences.getString("TCPKeepAlive", "");
        // 如果读出来的是空，则恢复默认值
        if (TCPKeepAliveName.equals("")) {
            SharedPreferences mySharedPreferences = getActivity().getSharedPreferences("TCPData", Activity.MODE_PRIVATE);
            SharedPreferences.Editor editor = mySharedPreferences.edit();
            editor.putString("TCPKeepAlive", "30000");
            editor.apply();
        }
        // 读当前TCP保活时间
        TCPUDInfo.TCPKeepAlive = Integer.parseInt(sharedPreferences.getString("TCPKeepAlive", ""));

        String SendTimeInterval = sharedPreferences.getString("SendTimeInterval", ""); // 读当前IP地址
        // 如果读出来的是空，则恢复默认值
        if (SendTimeInterval.equals("")) {
            SharedPreferences mySharedPreferences = getActivity().getSharedPreferences("TCPData", Activity.MODE_PRIVATE);
            SharedPreferences.Editor editor = mySharedPreferences.edit();
            editor.putString("SendTimeInterval", "3000");
            editor.apply();
        }
        TCPUDInfo.SendTimeInterval = Integer
                .parseInt(sharedPreferences.getString("SendTimeInterval", "")); // 读当前TCP超时时间
        Log.d("SendTimeInterval", String.valueOf(TCPUDInfo.SendTimeInterval / 1000));


        IPSplit = TCPUDInfo.IpAddress.split("\\.");
        Log.i(TAG, "InitConfig: " + IPSplit[0] + IPSplit[1] + IPSplit[2] + IPSplit[3]);
        mIpAddress1.setText(IPSplit[0]);
        mIpAddress2.setText(IPSplit[1]);
        mIpAddress3.setText(IPSplit[2]);
        mIpAddress4.setText(IPSplit[3]);
        mPortNumber.setText(String.valueOf(TCPUDInfo.PortNumber));
        mTCPKeepAlive.setText(String.valueOf(TCPUDInfo.TCPKeepAlive / 1000));
        mSendTimeInterval.setText(String.valueOf(TCPUDInfo.SendTimeInterval / 1000));
    }

    /**
     * Fragment is active.
     */
    @Override
    public void onResume() {
        super.onResume();

        //保存修改
        mSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SaveDialog.show();
            }
        });

        //恢复默认
        mRestore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //恢复默认值
                SharedPreferences mySharedPreferences = getActivity().getSharedPreferences("TCPData", Activity.MODE_PRIVATE);
                SharedPreferences.Editor editor = mySharedPreferences.edit();
                editor.putString("IPAddress", "192.168.200.20");
                editor.putString("PortNumber", "60000");
                editor.putString("TCPKeepAlive", "30000");
                editor.putString("SendTimeInterval", "3000");
                editor.apply();

                //读取放置的默认值
                TCPUDInfo.IpAddress = sharedPreferences.getString("IPAddress", "");
                TCPUDInfo.PortNumber = Integer.parseInt(sharedPreferences.getString("PortNumber", ""));
                TCPUDInfo.TCPKeepAlive = Integer.parseInt(sharedPreferences.getString("TCPKeepAlive", ""));
                TCPUDInfo.SendTimeInterval = Integer
                        .parseInt(sharedPreferences.getString("SendTimeInterval", ""));

                IPSplit = TCPUDInfo.IpAddress.split("\\.");
                mIpAddress1.setText(IPSplit[0]);
                Log.i(TAG, "onClick: " + IPSplit[3]);
                mIpAddress2.setText(IPSplit[1]);
                mIpAddress3.setText(IPSplit[2]);
                mIpAddress4.setText(IPSplit[3]);
                mPortNumber.setText(String.valueOf(TCPUDInfo.PortNumber));
                mTCPKeepAlive.setText(String.valueOf(TCPUDInfo.TCPKeepAlive / 1000));
                mSendTimeInterval.setText(String.valueOf(TCPUDInfo.SendTimeInterval / 1000));
            }
        });

        TCPUDInfo.mHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case 0xffff:
                        mDeviceState.setText("服务器连接成功");
                        break;
                    case 0xfffe:
                        mDeviceState.setText("服务器未开启/断网");
                        break;
                    case 0xfffd:
                        mDeviceState.setText("正常收发数据");
                        break;
                    case 0xfffc:
                        mDeviceState.setText("服务器断开稍后重试");
                        break;
                    default:
                        super.handleMessage(msg);
                        break;
                }
            }
        };
        /*mMyHandler.post(new Runnable() {
            @Override
            public void run() {
            }
        });*/

        /*private static class MyHandler extends Handler{

        //弱引用
        WeakReference<paramFragment> mTarget;

        public MyHandler(paramFragment fragment) {
            mTarget = new WeakReference<paramFragment>(fragment);
        }

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 0xffff:
                    mDeviceState.setText("服务器连接成功");
                    break;
                case 0xfffe:
                    mDeviceState.setText("服务器未开启/断网");
                    break;
                case 0xfffd:
                    mDeviceState.setText("正常收发数据");
                    break;
                case 0xfffc:
                    mDeviceState.setText("服务器断开稍后重试");
                    break;
                default:
                    super.handleMessage(msg);
                    break;
            }
        }
    }*/

    }

}