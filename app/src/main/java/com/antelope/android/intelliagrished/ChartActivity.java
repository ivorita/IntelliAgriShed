package com.antelope.android.intelliagrished;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ChartActivity extends AppCompatActivity {

    @BindView(R.id.line)
    LineChart mLine;
    private LineChart mLineChart;
    private String[] titles = {"一月", "二月", "三月", "四月", "五月", "六月", "七月", "八月", "九月", "十月", "十一月", "十二月"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.history_chart);
        ButterKnife.bind(this);
        //mLineChart = findViewById(R.id.line);
        mLine.setDrawBorders(true);
        List<Entry> entries = new ArrayList<>();
        for (int i = 0; i < 12; i++) {
            entries.add(new Entry(i, (float) Math.random() * 80));
        }
        LineDataSet lineDataSet = new LineDataSet(entries, "温度");
        LineData lineData = new LineData(lineDataSet);
        mLine.setData(lineData);

        //X轴设置在底部，字符形式
        XAxis xAxis = mLine.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setValueFormatter(new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                return titles[(int) value];
            }
        });
        //xAxis.setLabelCount(12, true);

        Description description = new Description();
        description.setEnabled(false);
        mLine.setDescription(description);

    }
}
