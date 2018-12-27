package com.antelope.android.intelliagrished.activity;

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
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.antelope.android.intelliagrished.R;
import com.antelope.android.intelliagrished.adapter.viewPagerAdapter;
import com.antelope.android.intelliagrished.fragment.envFragment;
import com.antelope.android.intelliagrished.fragment.noteFragment;
import com.antelope.android.intelliagrished.fragment.paramFragment;
import com.antelope.android.intelliagrished.fragment.remoteFragment;
import com.antelope.android.intelliagrished.utils.ServerThread;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.nav_view)
    NavigationView mNavView;
    @BindView(R.id.drawer_layout)
    DrawerLayout mDrawerLayout;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.exit)
    LinearLayout mExit;
    @BindView(R.id.night)
    ImageView mNight;
    @BindView(R.id.setting)
    LinearLayout mSetting;
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
            actionBar.setHomeAsUpIndicator(R.drawable.drawer_menu);
        }

        mNavView.setItemIconTintList(null);
        mNavView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.history:
                        mDrawerLayout.closeDrawers();
                        intent = new Intent(MainActivity.this, ChartActivity.class);
                        startActivity(intent);
                        break;
                    case R.id.about:
                        mDrawerLayout.closeDrawers();
                        intent = new Intent(MainActivity.this, AboutActivity.class);
                        startActivity(intent);
                        break;
                }
                return true;
            }
        });

        setHeaderValue();

        mServerThread.start();
    }

    //设置抽屉用户名
    private void setHeaderValue() {
        intent = getIntent();
        String name = intent.getStringExtra("name");
        TextView userName = mNavView.getHeaderView(0).findViewById(R.id.nav_header_root).findViewById(R.id.user_name);
        userName.setText(name);
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

    @OnClick({R.id.night, R.id.setting, R.id.exit})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.night:
                Toast.makeText(MainActivity.this, "夜间模式", Toast.LENGTH_SHORT).show();
                break;
            case R.id.setting:
                Intent intent = new Intent(MainActivity.this, SettingActivity.class);
                startActivity(intent);
                break;
            case R.id.exit:
                finish();
                break;
            default:
                break;
        }
    }

}