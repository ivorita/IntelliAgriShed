package com.antelope.android.intelliagrished.activity;

import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.antelope.android.intelliagrished.R;
import com.antelope.android.intelliagrished.utils.DynamicLineChartManager;
import com.antelope.android.intelliagrished.utils.TCPUDInfo;
import com.github.mikephil.charting.charts.LineChart;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ChartActivity extends AppCompatActivity {

    @BindView(R.id.line)
    LineChart mLine;
    @BindView(R.id.line_1)
    LineChart mLine1;

    private int air_tmp;

    private DynamicLineChartManager dynamicLineChartManager_air;
    private DynamicLineChartManager dynamicLineChartManager_soil;
    private List<Integer> list_air = new ArrayList<>(); //数据集合
    private List<String> names_air = new ArrayList<>(); //折线名字集合
    private List<Integer> color_air = new ArrayList<>();//折线颜色集合

    private List<Integer> list_soil = new ArrayList<>(); //数据集合
    private List<String> names_soil = new ArrayList<>(); //折线名字集合
    private List<Integer> color_soil = new ArrayList<>();//折线颜色集合


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.history_chart);
        ButterKnife.bind(this);

        chartAirInit();

        chartSoilInit();

    }

    /**
     * 空气环境Chart
     */
    private void chartAirInit() {
        //折线名字
        names_air.add("空气温度");
        names_air.add("空气湿度");
        names_air.add("光照强度");
        names_air.add("CO₂浓度");
        //折线颜色
        color_air.add(getResources().getColor(R.color.air_temp,getTheme()));
        color_air.add(getResources().getColor(R.color.green,getTheme()));
        color_air.add(getResources().getColor(R.color.light,getTheme()));
        color_air.add(getResources().getColor(R.color.co_2,getTheme()));

        mLine.setDrawBorders(true);

        dynamicLineChartManager_air = new DynamicLineChartManager(mLine, names_air, color_air);
        dynamicLineChartManager_air.setDescription();
        dynamicLineChartManager_air.setYAxis(600, 0, 10);
    }

    /**
     * 湿度环境Chart
     */
    private void chartSoilInit() {
        //折线名字
        names_soil.add("土壤温度");
        names_soil.add("土壤湿度");
        names_soil.add("盐溶解度");
        names_soil.add("酸碱度");
        //折线颜色
        color_soil.add(getResources().getColor(R.color.soil_temp,getTheme()));
        color_soil.add(getResources().getColor(R.color.soil_humi,getTheme()));
        color_soil.add(getResources().getColor(R.color.salt,getTheme()));
        color_soil.add(getResources().getColor(R.color.ph,getTheme()));

        mLine1.setDrawBorders(true);

        dynamicLineChartManager_soil = new DynamicLineChartManager(mLine1, names_soil, color_soil);
        dynamicLineChartManager_soil.setDescription();
        dynamicLineChartManager_soil.setYAxis(80, 0, 10);
    }

    @Override
    protected void onResume() {
        super.onResume();

        TCPUDInfo.mHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case 0x0010:
                        if (msg.arg1 > 100) {
                            msg.arg1 = 100;
                        }
                        air_tmp = msg.arg1;
                        list_air.add(air_tmp);
                        list_air.add(msg.arg2);
                        if (msg.arg2 > 100) {
                            msg.arg2 = 100;
                        }
                        break;
                    case 0x0011:
                        list_air.add(msg.arg1);
                        list_air.add(msg.arg2);
                        dynamicLineChartManager_air.addEntry(list_air);
                        list_air.clear();
                        break;
                    case 0x0012:
                        Log.d("ChartActivity", "土壤温度：" + msg.arg1);
                        list_soil.add(msg.arg1);
                        list_soil.add(msg.arg2);
                        break;
                    case 0x0013:
                        list_soil.add(msg.arg1 / 10);
                        list_soil.add(msg.arg2 / 10);
                        dynamicLineChartManager_soil.addEntry(list_soil);
                        list_soil.clear();
                        break;
                    default:
                        super.handleMessage(msg);
                        break;
                }
            }
        };
    }
}
