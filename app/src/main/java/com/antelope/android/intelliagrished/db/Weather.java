package com.antelope.android.intelliagrished.db;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Weather {

    //基本信息
    public Basic basic;

    //连接状态
    public String status;

    //现在状态
    public Now now;

    //天气预报
    @SerializedName("daily_forecast")
    public List<DailyForecast> forecastList;

}
