package com.antelope.android.intelliagrished.db;

import com.google.gson.annotations.SerializedName;

public class Now {

    //当前温度
    @SerializedName("tmp")
    public String temperature;

    //当前天气状态
    @SerializedName("cond_txt")
    public String info;

}
