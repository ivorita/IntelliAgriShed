package com.antelope.android.intelliagrished.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.antelope.android.intelliagrished.R;
import com.antelope.android.intelliagrished.bmob.User;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;

public class RegisterActivity extends AppCompatActivity {

    @BindView(R.id.login)
    TextView mLogin;
    @BindView(R.id.username)
    EditText mUsername;
    @BindView(R.id.password)
    EditText mPassword;
    @BindView(R.id.mail)
    EditText mMail;
    @BindView(R.id.register_btn)
    Button mRegisterBtn;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ButterKnife.bind(this);

        mLogin.setTextColor(getResources().getColor(R.color.hint));

        setStatusBar();
    }

    private void setStatusBar(){
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

    @OnClick({R.id.login, R.id.register_btn})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.login:
                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                //设置无跳转动画
                intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(intent);
                finish();
                break;
            case R.id.register_btn:
                String uName = mUsername.getText().toString();
                String pass = mPassword.getText().toString();
                String email = mMail.getText().toString();
                BmobUser user = new BmobUser();
                user.setUsername(uName);
                user.setPassword(pass);
                user.setEmail(email);
                user.signUp(new SaveListener<User>() {
                    @Override
                    public void done(User user, BmobException e) {
                        if (e == null) {
                            Snackbar snackbar = Snackbar.make(view, "注册成功", Snackbar.LENGTH_SHORT);
                            snackbar.getView().setBackgroundColor(getResources().getColor(R.color.green));
                            snackbar.show();
                        } else {
                            BmobQuery<User> bmobQuery = new BmobQuery<>();
                            bmobQuery.findObjects(new FindListener<User>() {
                                @Override
                                public void done(List<User> list, BmobException e) {
                                    if (e == null) {
                                        Snackbar snackbar = Snackbar.make(view, "已有该用户", Snackbar.LENGTH_SHORT);
                                        snackbar.getView().setBackgroundColor(getResources().getColor(R.color.green));
                                        snackbar.show();
                                    }
                                }
                            });

                        }
                    }
                });
                break;
        }
    }
}

