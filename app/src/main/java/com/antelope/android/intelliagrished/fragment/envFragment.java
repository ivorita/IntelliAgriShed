package com.antelope.android.intelliagrished.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.antelope.android.intelliagrished.R;
import com.antelope.android.intelliagrished.utils.TCPUDInfo;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class envFragment extends Fragment {

    @BindView(R.id.temp_value)
    TextView mTempValue;
    @BindView(R.id.humidity_value)
    TextView mHumidityValue;
    Unbinder unbinder;

    public static envFragment newInstance() {
        envFragment fragment = new envFragment();
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_envir, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        TCPUDInfo.mHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what) {
                    case 0x0010:
                        if (msg.arg1 > 100) {
                            msg.arg1 = 100;
                        }
                        mTempValue.setText(Integer.toString(msg.arg1) + "℃"); // 空气环境：温度
                        if (msg.arg2 > 100) {
                            msg.arg2 = 100;
                        }
                        mHumidityValue.setText(Integer.toString(msg.arg2) + "%"); // 空气环境：湿度
                        break;
                }
            }
        };
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
