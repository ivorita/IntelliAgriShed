package com.antelope.android.intelliagrished.bmob;

import cn.bmob.v3.BmobUser;

public class User extends BmobUser {
    private transient String password;
    private String name;
    private String mail;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    @Override
    public void setPassword(String password) {
        this.password = password;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }
}
