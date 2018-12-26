package com.antelope.android.intelliagrished.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.antelope.android.intelliagrished.R;
import com.antelope.android.intelliagrished.bmob.User;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;


public class LoginActivity extends AppCompatActivity {
    @BindView(R.id.login)
    TextView mLogin;
    @BindView(R.id.register)
    TextView mRegister;
    @BindView(R.id.username)
    EditText mUsername;
    @BindView(R.id.password)
    EditText mPassword;
    @BindView(R.id.forget_pass)
    TextView mForgetPass;
    @BindView(R.id.login_btn)
    Button mLoginBtn;
    @BindView(R.id.remember_pass)
    CheckBox mRememberPass;
    private SharedPreferences sp;
    private SharedPreferences.Editor editor;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        //初始化Bmob，这里的Application ID 写上自己创建项目得到的那个Application ID
        Bmob.initialize(this, "e1f434dc45c21cb6f58647a3d0eddaee");
        ButterKnife.bind(this);

        mRegister.setTextColor(getResources().getColor(R.color.hint));
        //获取SharedPreferences对象
        sp = PreferenceManager.getDefaultSharedPreferences(this);
        //判断记住密码多选框的状态
        boolean isRemember = sp.getBoolean("remember_password", false);
        if (isRemember) {
            //设置默认是记录密码状态
            mRememberPass.setChecked(true);
            mUsername.setText(sp.getString("UserName", ""));
            mPassword.setText(sp.getString("password", ""));
        }

        setStatusBar();

    }

    private void setStatusBar() {
        if (Build.VERSION.SDK_INT >= 21) {
            View decorView = getWindow().getDecorView();

            //表示活动的布局会显示在状态栏上面
            decorView.setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);

            //将状态栏设置为透明色
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
    }

    @OnClick({R.id.register, R.id.login_btn})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.register:
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                //设置无跳转动画
                intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(intent);
                finish();
                break;
            case R.id.login_btn:
                final BmobUser user = new BmobUser();
                final String uName = mUsername.getText().toString();
                final String pass = mPassword.getText().toString();
                //判断账号密码是否为空
                if (uName.isEmpty() && pass.isEmpty()) {
                    Snackbar.make(view, "账号和密码不能为空", Snackbar.LENGTH_SHORT).show();
                    return;
                }
                user.setUsername(uName);
                user.setPassword(pass);
                //判断登陆是否成功
                user.login(new SaveListener<User>() {
                    @Override
                    public void done(User user, BmobException e) {
                        if (e == null) {
                            editor = sp.edit();
                            if (mRememberPass.isChecked()) {
                                editor.putBoolean("remember_password", true);
                                editor.putString("UserName", uName);
                                editor.putString("password", pass);
                            } else {
                                editor.clear();
                            }
                            editor.apply();
                            //获取当前用户信息
                            User user1 = BmobUser.getCurrentUser(User.class);
                            Toast.makeText(LoginActivity.this,"登录成功",Toast.LENGTH_LONG).show();
//                            Snackbar snackbar = Snackbar.make(view, "登录成功", Snackbar.LENGTH_SHORT);
//                            snackbar.getView().setBackgroundColor(getResources().getColor(R.color.green));
//                            snackbar.show();
                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                            intent.putExtra("name",uName);
                            startActivity(intent);
                            finish();
                        } else if (user == null) {
                            Snackbar snackbar = Snackbar.make(view, "未注册或用户名、密码错误", Snackbar.LENGTH_SHORT);
                            snackbar.getView().setBackgroundColor(getResources().getColor(R.color.green));
                            snackbar.show();
                        } else if (uName.equals(user.getName()) || pass.equals(user.getPassword())){
                            Snackbar snackbar = Snackbar.make(view, "用户名或密码错误", Snackbar.LENGTH_SHORT);
                            snackbar.getView().setBackgroundColor(getResources().getColor(R.color.green));
                            snackbar.show();
                        }
                    }
                });
                break;
        }
    }
}
