package com.antelope.android.intelliagrished.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.antelope.android.intelliagrished.R;
import com.antelope.android.intelliagrished.utils.DynamicLineChartManager;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.antelope.android.intelliagrished.fragment.envFragment.env_values;

public class ChartActivity extends AppCompatActivity {

    @BindView(R.id.line)
    LineChart mLine;
    private String[] titles = {"一月", "二月", "三月", "四月", "五月", "六月", "七月", "八月", "九月", "十月", "十一月", "十二月"};

    private DynamicLineChartManager dynamicLineChartManager1;
    private DynamicLineChartManager dynamicLineChartManager2;
    private List<Integer> list = new ArrayList<>(); //数据集合
    private List<String> names = new ArrayList<>(); //折线名字集合
    private List<Integer> color = new ArrayList<>();//折线颜色集合


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.history_chart);
        ButterKnife.bind(this);

        //折线名字
        names.add("温度");
        //折线颜色
        color.add(R.color.c_blue);

        dynamicLineChartManager1 = new DynamicLineChartManager(mLine, "温度", R.color.c_blue);

        dynamicLineChartManager1.setYAxis(100, 0, 10);

        mLine.setDrawBorders(true);

        dynamicLineChartManager1.setDescription("");

        //if (!env_values.isEmpty()){
            dynamicLineChartManager1.addEntry(env_values);
        //} else {
            //dynamicLineChartManager1.addEntry(0);
        //}


//        List<Entry> entries = new ArrayList<>();
//        for (int i = 0; i < 12; i++) {
//            if (!env_values.isEmpty()){
//                entries.add(new Entry(i, env_values.get(i)));
//            } else {
//                entries.add(new Entry(i,0));
//            }
//        }

//        LineDataSet lineDataSet = new LineDataSet(entries, "温度");
//        //线模式为圆滑曲线（默认折线）
//        lineDataSet.setMode(LineDataSet.Mode.CUBIC_BEZIER);
//        LineData lineData = new LineData(lineDataSet);
//        mLine.setData(lineData);

        //X轴设置在底部，字符形式
//        XAxis xAxis = mLine.getXAxis();
//        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
//        xAxis.setValueFormatter(new IAxisValueFormatter() {
//            @Override
//            public String getFormattedValue(float value, AxisBase axis) {
//                return timeList.get((int) value % timeList.size());
//            }
//        });
//        //xAxis.setLabelCount(12, true);
//
//        //设置描述内容
//        Description description = new Description();
//        description.setText("时间(s)");
//        description.setTextColor(R.color.c_blue);
//        mLine.setDescription(description);

    }
}
