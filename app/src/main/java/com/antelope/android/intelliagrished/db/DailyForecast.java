package com.antelope.android.intelliagrished.db;

import com.google.gson.annotations.SerializedName;

public class DailyForecast {

    //时间
    public String date;

    @SerializedName("tmp")
    public Temperature temperature;

    //天气状态
    @SerializedName("cond")
    public More more;

    //温度
    public class Temperature {

        public String max;

        public String min;

    }

    public class More {

        //天气状态，如：晴等
        @SerializedName("txt_d")
        public String info;

    }

}
