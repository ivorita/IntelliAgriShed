package com.antelope.android.intelliagrished.fragment;

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
import butterknife.OnClick;
import butterknife.Unbinder;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class envFragment extends Fragment {

    private static final String TAG = "envFragment";

    @BindView(R.id.temp_value)
    TextView mTempValue;
    @BindView(R.id.humidity_value)
    TextView mHumidityValue;
    Unbinder unbinder;
    @BindView(R.id.forecast_layout)
    LinearLayout mForecastLayout;
    @BindView(R.id.degree)
    TextView mDegree;
    @BindView(R.id.weather_info)
    TextView mWeatherInfo;
    @BindView(R.id.swipe_refresh)
    SwipeRefreshLayout mSwipeRefresh;

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
            String min_tmp = getString(R.string.tmp_value, min);
            mMinText.setText(min_tmp);

            int max = Integer.parseInt(dailyForecast.temperature.max);
            String max_tmp = getString(R.string.tmp_value, max);
            mMaxText.setText(max_tmp);

            mForecastLayout.addView(view);
        }

        //启动定时刷新天气服务
        Intent intent = new Intent(getContext(),AutoUpdateService.class);
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
                super.handleMessage(msg);
                switch (msg.what) {
                    case 0x0010:
                        if (msg.arg1 > 100) {
                            msg.arg1 = 100;
                        }
                        mTempValue.setText(Integer.toString(msg.arg1) + "℃"); // 空气环境：温度
                        if (msg.arg2 > 100) {
                            msg.arg2 = 100;
                        }
                        mHumidityValue.setText(Integer.toString(msg.arg2) + "%"); // 空气环境：湿度
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

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

}