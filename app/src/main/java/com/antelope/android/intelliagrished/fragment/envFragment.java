package com.antelope.android.intelliagrished.fragment;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.antelope.android.intelliagrished.R;
import com.antelope.android.intelliagrished.db.DailyForecast;
import com.antelope.android.intelliagrished.db.Weather;
import com.antelope.android.intelliagrished.utils.AutoUpdateService;
import com.antelope.android.intelliagrished.utils.HttpUtil;
import com.antelope.android.intelliagrished.utils.TCPUDInfo;
import com.antelope.android.intelliagrished.utils.Utility;

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class envFragment extends Fragment {

    private static final String TAG = "envFragment";

    @BindView(R.id.city)
    TextView mCity;
    @BindView(R.id.air_temp_value)
    TextView mAirTempValue;
    @BindView(R.id.air_humidity_value)
    TextView mAirHumidityValue;
    Unbinder unbinder;
    @BindView(R.id.forecast_layout)
    LinearLayout mForecastLayout;
    @BindView(R.id.degree)
    TextView mDegree;
    @BindView(R.id.weather_info)
    TextView mWeatherInfo;
    @BindView(R.id.swipe_refresh)
    SwipeRefreshLayout mSwipeRefresh;
    @BindView(R.id.illumination)
    TextView mIllumination;
    @BindView(R.id.co2)
    TextView mCo2;
    @BindView(R.id.soil_temp_value)
    TextView mSoilTempValue;
    @BindView(R.id.soil_humidity_value)
    TextView mSoilHumidityValue;
    @BindView(R.id.salt_solubility)
    TextView mSaltSolubility;
    @BindView(R.id.pH_value)
    TextView mPHValue;

    private SharedPreferences sharedPreferences;

    private int air_tmp;
    private int air_humidity;
    private int illumination_int;
    private int co2;
    private int soil_tmp;
    private int soil_humidity;
    private double salt;
    private double ph;
    private String temp_air;
    private String humidity_air;
    private String illumination_String;
    private String co2_conc;
    private String temp_soil;
    private String humidity_soil;
    private String salt_s;
    private String pH_value;

    public static envFragment newInstance() {
        envFragment fragment = new envFragment();
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_envir, container, false);
        unbinder = ButterKnife.bind(this, view);
        mSwipeRefresh.setColorSchemeResources(R.color.c_blue);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    private void showWeatherInfo(Weather weather) {
        String cityName = weather.basic.cityName;
        String updateTime = weather.basic.update.updateTime.split(" ")[1];
        String degree = weather.now.temperature;
        String weatherInfo = weather.now.info;
        mCity.setText(cityName);
        mDegree.setText(degree);
        mWeatherInfo.setText(weatherInfo);
        mForecastLayout.removeAllViews();

        for (DailyForecast dailyForecast : weather.forecastList) {
            View view = LayoutInflater.from(getContext()).inflate(R.layout.forecast_item, mForecastLayout, false);
            TextView mDateText = view.findViewById(R.id.date_text);
            TextView mInfoText = view.findViewById(R.id.info_text);
            TextView mMaxText = view.findViewById(R.id.max_text);
            TextView mMinText = view.findViewById(R.id.min_text);

            Log.d("showWeatherInfo: ", dailyForecast.date);

            mDateText.setText(dailyForecast.date);
            mInfoText.setText(dailyForecast.more.info);

            int min = Integer.parseInt(dailyForecast.temperature.min);
            String min_tmp = getString(R.string.weather_tmp_value, min);
            mMinText.setText(min_tmp);

            int max = Integer.parseInt(dailyForecast.temperature.max);
            String max_tmp = getString(R.string.weather_tmp_value, max);
            mMaxText.setText(max_tmp);

            mForecastLayout.addView(view);
        }

        //启动定时刷新天气服务
        Intent intent = new Intent(getContext(), AutoUpdateService.class);
        getActivity().startService(intent);

    }

    /**
     * 请求城市天气信息
     */
    public void requestWeather() {

        //this.mWeatherId = weatherId;
        String weatherUrl = "http://guolin.tech/api/weather?cityid=" +
                "CN101280210" + "&key=ae28aa2c3d514239b930c3cad5187404";

        HttpUtil.sendOkHttpRequest(weatherUrl, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                //Toast.makeText(getContext(), "获取天气信息失败", Toast.LENGTH_LONG).show();
                Log.e(TAG, "onFailure: " + e.toString());
                mSwipeRefresh.setRefreshing(false);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String responseText1 = response.body().string();
                final Weather weather = Utility.handleWeatherResponse(responseText1);
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (weather != null && "ok".equals(weather.status)) {
                            SharedPreferences.Editor editor = PreferenceManager
                                    .getDefaultSharedPreferences(getContext()).edit();
                            editor.putString("weather", responseText1);
                            editor.apply();
                            //这里多添加了weatherId是为了重启软件刷新后，获得的地区不是第一次缓存的地区
                            //mWeatherId = weather.basic.weatherId;
                            //showWeatherInfo(weather);
                        } else {
                            //Toast.makeText(getContext(), "获取天气信息失败", Toast.LENGTH_LONG).show();
                            Log.e(TAG, "获取天气信息失败");
                        }
                        mSwipeRefresh.setRefreshing(false);
                    }
                });
            }
        });

    }

    @Override
    public void onResume() {
        super.onResume();

        /*mAirTempValue.setText(temp_air);
        mAirHumidityValue.setText(humidity_air);
        mIllumination.setText(illumination_String);
        mCo2.setText(co2_conc);
        mSoilTempValue.setText(temp_soil);
        mSoilHumidityValue.setText(humidity_soil);
        mSaltSolubility.setText(salt_s);
        mPHValue.setText(pH_value);*/

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getContext());
        String weatherString = prefs.getString("weather", null);

        if (weatherString != null) {
            //有缓存时直接解析天气数据
            Weather weather = Utility.handleWeatherResponse(weatherString);
            //mWeatherId = weather.basic.weatherId;
            //Log.d("WeatherId", mWeatherId);
            showWeatherInfo(weather);
        } else {
            //没有缓存时去服务器查询数据
            //mWeatherId = getIntent().getStringExtra("weather_id");
            requestWeather();
        }

        TCPUDInfo.mHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case 0x0010:
                        if (msg.arg1 > 100) {
                            msg.arg1 = 100;
                        }
                        air_tmp = msg.arg1;
                        mAirTempValue.setText(String.valueOf(msg.arg1)); // 空气环境：温度
                        Log.d(TAG, "温度 " + String.valueOf(msg.arg1));
                        Log.d(TAG, "温度更新");
                        if (msg.arg2 > 100) {
                            msg.arg2 = 100;
                        }
                        air_humidity = msg.arg2;
                        mAirHumidityValue.setText(String.valueOf(msg.arg2)); // 空气环境：湿度
                        break;
                    case 0x0011:
                        illumination_int = msg.arg1;
                        co2 = msg.arg2;
                        mIllumination.setText(String.valueOf(msg.arg1)); // 空气环境：光照
                        mCo2.setText(String.valueOf(msg.arg2)); // 空气环境：二氧化碳
                        break;
                    case 0x0012:
                        soil_tmp = msg.arg1;
                        soil_humidity = msg.arg2;
                        mSoilTempValue.setText(String.valueOf(msg.arg1)); // 土壤环境：温度
                        mSoilHumidityValue.setText(String.valueOf(msg.arg2)); // 土壤环境：湿度
                        break;
                    case 0x0013:
                        salt = (double)msg.arg1;
                        ph = (double)msg.arg2;
                        mSaltSolubility.setText(String.valueOf((double) msg.arg1 / 10)); // 土壤环境：盐溶解度
                        mPHValue.setText(String.valueOf((double) msg.arg2 / 10)); // 土壤环境：PH值
                        break;
                    default:
                        super.handleMessage(msg);
                        break;
                }
            }
        };

        mSwipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                requestWeather();
            }
        });

    }

    //存储数据，下次进入时恢复显示
    @Override
    public void onPause() {
        super.onPause();
        sharedPreferences = getActivity().getSharedPreferences("envParams", Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        //空气温度
        temp_air = String.valueOf(air_tmp);
        editor.putString("temp_air", temp_air);
        Log.d(TAG, "onPause: " + "temp_air：" + temp_air);

        //空气湿度
        humidity_air = String.valueOf(air_humidity);
        editor.putString("humidity_air", humidity_air);
        Log.d(TAG, "onPause: " + "humidity_air：" + humidity_air);

        //光照强度
        illumination_String = String.valueOf(illumination_int);
        editor.putString("illumination_String", illumination_String);
        Log.d(TAG, "onPause: " + "illumination_String：" + illumination_String);

        //CO2浓度
        co2_conc = String.valueOf(co2);
        editor.putString("CO2_concentration", co2_conc);
        Log.d(TAG, "onPause: " + "co2_conc：" + co2_conc);

        //土壤温度
        temp_soil = String.valueOf(soil_tmp);
        editor.putString("temp_soil", temp_soil);
        Log.d(TAG, "onPause: " + "temp_soil：" + temp_soil);

        //土壤湿度
        humidity_soil = String.valueOf(soil_humidity);
        editor.putString("humidity_soil", humidity_soil);
        Log.d(TAG, "onPause: " + "humidity_soil：" + humidity_soil);

        //盐溶解度
        salt_s = String.valueOf(salt);
        editor.putString("salt_solubility", salt_s);
        Log.d(TAG, "onPause: " + "salt_s：" + salt_s);

        //pH值
        pH_value = String.valueOf(ph);
        editor.putString("pH_value", pH_value);
        Log.d(TAG, "onPause: " + "pH_value：" + pH_value);

        editor.apply();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

}