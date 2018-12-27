package com.antelope.android.intelliagrished.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;

import com.antelope.android.intelliagrished.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SettingActivity extends AppCompatActivity {

    @BindView(R.id.switch_account)
    Button mSwitchAccount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.switch_account)
    public void onViewClicked() {
        Intent intent = new Intent(SettingActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }
}
