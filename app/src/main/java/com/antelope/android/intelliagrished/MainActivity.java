package com.antelope.android.intelliagrished;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.widget.Toast;

import com.antelope.android.intelliagrished.fragment.envFragment;
import com.antelope.android.intelliagrished.fragment.paramFragment;
import com.antelope.android.intelliagrished.fragment.remoteFragment;
import com.antelope.android.intelliagrished.utils.ServerThread;
import com.antelope.android.intelliagrished.utils.TCPUDInfo;
import com.antelope.android.intelliagrished.utils.WriteCmdTask;

import java.util.Timer;

public class MainActivity extends AppCompatActivity {

    private boolean ExitState = false;
    private long ExitTime = 0;

    private MenuItem mMenuItem;
    private BottomNavigationView navigation;
    private ViewPager mViewPager;

    private ServerThread mServerThread = new ServerThread();

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_env:
                    mViewPager.setCurrentItem(0);
                    return true;
                case R.id.navigation_remote:
                    mViewPager.setCurrentItem(1);
                    return true;
                case R.id.navigation_params:
                    mViewPager.setCurrentItem(2);
                    return true;
            }
            return false;
        }
    };

    private ViewPager.OnPageChangeListener mOnPageChangeListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            if (mMenuItem != null) {
                mMenuItem.setChecked(false);
            } else {
                navigation.getMenu().getItem(0).setChecked(false);
            }
            mMenuItem = navigation.getMenu().getItem(position);
            mMenuItem.setChecked(true);
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mViewPager = findViewById(R.id.viewPager);
        mViewPager.addOnPageChangeListener(mOnPageChangeListener);
        navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        setUpViewPager(mViewPager);

        mServerThread.start();

        InitWriteCmdTimer();
    }

    private void setUpViewPager(ViewPager viewPager) {
        viewPagerAdapter viewPagerAdapter = new viewPagerAdapter(getSupportFragmentManager());
        viewPagerAdapter.addFragment(envFragment.newInstance());
        viewPagerAdapter.addFragment(remoteFragment.newInstance());
        viewPagerAdapter.addFragment(paramFragment.newInstance());
        viewPager.setAdapter(viewPagerAdapter);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // 如果是返回键,直接返回到桌面
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (!ExitState) {
                Toast.makeText(MainActivity.this.getApplicationContext(), "再按一次返回键退出", Toast.LENGTH_SHORT).show();
                ExitState = true;
                ExitTime = System.currentTimeMillis();
                return false;
            } else {
                if (System.currentTimeMillis() - ExitTime > 3000) {
                    ExitState = false;
                    ExitTime = 0;
                    return false;
                } else {
                    finish();
                }
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    // 开机1秒钟之后，每隔2秒钟检索一次发送队列，并操作发送队列发送写指令
    private void InitWriteCmdTimer() {
        Timer time = new Timer();
        time.schedule(new WriteCmdTask(), 1000, 2000);
    }

}
