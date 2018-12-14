package com.antelope.android.intelliagrished;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.antelope.android.intelliagrished.fragment.envFragment;
import com.antelope.android.intelliagrished.fragment.noteFragment;
import com.antelope.android.intelliagrished.fragment.paramFragment;
import com.antelope.android.intelliagrished.fragment.remoteFragment;
import com.antelope.android.intelliagrished.utils.ServerThread;
import com.antelope.android.intelliagrished.utils.WriteCmdTask;

import java.util.Timer;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    @BindView(R.id.nav_view)
    NavigationView mNavView;
    @BindView(R.id.drawer_layout)
    DrawerLayout mDrawerLayout;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    /*@BindView(R.id.about)
    LinearLayout mAbout;
    @BindView(R.id.history)
    LinearLayout mHistory;*/
    private boolean ExitState = false;
    private long ExitTime = 0;

    private Intent intent;

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
                case R.id.navigation_note:
                    mViewPager.setCurrentItem(3);
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
        ButterKnife.bind(this);

        //设置状态栏透明,实现抽屉沉浸式，另需在DrawerLayout布局设置 android:fitsSystemWindows="true"，还有灰层蒙版
        setStatusBar();

        mViewPager = findViewById(R.id.viewPager);
        mViewPager.addOnPageChangeListener(mOnPageChangeListener);
        navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        setUpViewPager(mViewPager);

        setSupportActionBar(mToolbar);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.ic_action_name);
        }

        mNavView.setItemIconTintList(null);
        mNavView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()){
                    case R.id.history:
                        mDrawerLayout.closeDrawers();
                        Toast.makeText(MainActivity.this,"历史记录",Toast.LENGTH_SHORT).show();
                        intent = new Intent(MainActivity.this,ChartActivity.class);
                        startActivity(intent);
                        break;
                    case R.id.about:
                        mDrawerLayout.closeDrawers();
                        Toast.makeText(MainActivity.this,"关于app",Toast.LENGTH_SHORT).show();
                        intent = new Intent(MainActivity.this,AboutActivity.class);
                        startActivity(intent);
                        break;
                }
                return true;
            }
        });

        mServerThread.start();

        //InitWriteCmdTimer();
    }

    /**
     * 设置状态栏透明
     */
    private void setStatusBar() {
        getWindow().setStatusBarColor(Color.TRANSPARENT);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                break;
            default:
        }
        return true;
    }

    private void setUpViewPager(ViewPager viewPager) {
        viewPagerAdapter viewPagerAdapter = new viewPagerAdapter(getSupportFragmentManager());
        viewPagerAdapter.addFragment(envFragment.newInstance());
        viewPagerAdapter.addFragment(remoteFragment.newInstance());
        viewPagerAdapter.addFragment(paramFragment.newInstance());
        viewPagerAdapter.addFragment(noteFragment.newInstance());
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
        Log.d(TAG, "InitWriteCmdTimer: " + "executed");
        Toast.makeText(MainActivity.this,"MainActivity InitWriteCmdTimer",Toast.LENGTH_SHORT).show();
    }

    /*@OnClick({R.id.about, R.id.history})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.about:
                mDrawerLayout.closeDrawers();
                Toast.makeText(MainActivity.this,"关于app",Toast.LENGTH_SHORT).show();
                break;
            case R.id.history:
                mDrawerLayout.closeDrawers();
                Toast.makeText(MainActivity.this,"历史记录",Toast.LENGTH_SHORT).show();
                break;
        }
    }*/
}