package com.antelope.android.intelliagrished.db;

import com.google.gson.annotations.SerializedName;

public class Basic {

    //城市名，如：浈江
    @SerializedName("city")
    public String cityName;

    //天气id，如：CN101280210
    @SerializedName("id")
    public String weatherId;

    public Update update;

    //更新时间
    public class Update {

        @SerializedName("utc")
        public String updateTime;
    }
}
