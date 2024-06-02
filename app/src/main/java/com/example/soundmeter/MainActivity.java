package com.example.soundmeter;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Toast;

import com.example.soundmeter.databinding.ActivityMainBinding;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IFillFormatter;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.interfaces.dataprovider.LineDataProvider;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

import java.io.File;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding mainBinding;
    LineChart mChart;
    private Recoder recoder;
    private SoundView soundView;
    private final int savedTime = 0;
    private final long currentTime = 0;
    private ArrayList<Entry> yVals;

    private static final int msgWhat = 0x1001;
    private static final int refreshTime = 100;
    private float volume = 10000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainBinding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(mainBinding.getRoot());

        recoder = new Recoder();
        initChart();

        mainBinding.imgSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this, SettingActivity.class);
                startActivity(i);
            }
        });
        mainBinding.btnReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                reset();
                Toast.makeText(MainActivity.this, "you reseted", Toast.LENGTH_SHORT).show();
            }
        });

        mainBinding.btnInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, InfoActivity.class));
            }
        });
    }

    private void reset() {
        World.reset();
        mainBinding.tvValue.setText(String.format("%.2f", World.dbCount).replace(",","."));
        mainBinding.tvMax.setText(String.format("%.2f", World.MAX).replace(",","."));
        mainBinding.tvMin.setText(String.format("%.2f", World.MIN).replace(",","."));
        mainBinding.tvAvg.setText(String.format("%.2f", World.getAvg()).replace(",","."));
        mChart.clear();
        initChart();
        startListen();
    }

    private final Handler handler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            if (this.hasMessages(msgWhat)) {
                return;
            }
            volume = recoder.getMax();
            if (volume > 0 && volume < 10000) {
                float dbCount = World.setDbCount(20 * (float) (Math.log10(volume)));
                mainBinding.tvValue.setText(String.format("%.2f", dbCount).replace(",","."));
                mainBinding.tvMax.setText(String.format("%.2f", World.MAX).replace(",","."));
                mainBinding.tvMin.setText(String.format("%.2f", World.MIN).replace(",","."));
                mainBinding.tvAvg.setText(String.format("%.2f", World.getAvg()).replace(",","."));
                soundView.refresh();
                addEntry(dbCount);
            }
            startListen();
        }
    };

    private void startListen() {
        handler.sendEmptyMessageDelayed(msgWhat, refreshTime);
    }

    public void startRecode(File mFile) {
        try {
            recoder.setmFile(mFile);
            if (recoder.startRecoding()) {
                startListen();
            } else {
                Toast.makeText(this, "Failed recoding", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            Toast.makeText(this, "Something wrong", Toast.LENGTH_SHORT).show();
            e.getStackTrace();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        soundView = findViewById(R.id.iv_img);
        File file = FileUtil.createFile("temp.amr");
        if (file != null) {
            startRecode(file);
        } else {
            Toast.makeText(this, "Create file failed", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        recoder.deleteRecoding();
        handler.hasMessages(msgWhat);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        recoder.deleteRecoding();
        handler.hasMessages(msgWhat);
    }

    private void initChart() {
        mChart = findViewById(R.id.chart);
        mChart.setViewPortOffsets(50, 20, 5, 60);
        mChart.setTouchEnabled(true);
        mChart.setDragEnabled(true);
        mChart.setScaleEnabled(true);
        mChart.setPinchZoom(true);
        mChart.setDrawGridBackground(false);  // Không có nền cho khung lưới
        mChart.getDescription().setEnabled(false);

        // Thiết lập trục X
        XAxis x = mChart.getXAxis();
        x.setEnabled(true);
        x.setPosition(XAxis.XAxisPosition.BOTTOM);
        x.setDrawGridLines(true);  // Hiển thị đường lưới dọc
        x.setGranularity(120f);  // Đặt khoảng cách giữa các nhãn là 10 đơn vị
        x.setLabelCount(4, true); // Đảm bảo có nhãn mỗi 10 đơn vị
        x.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                return String.format("%.0f s", value);
            }
        });

        // Thiết lập trục Y
//        YAxis y = mChart.getAxisLeft();
//        y.setLabelCount(8, true);  // Đảm bảo có 8 nhãn từ 0 đến 140 với khoảng cách 20 đơn vị
//        y.setTextColor(R.color.green);
//        y.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART);
//        y.setDrawGridLines(true);  // Hiển thị đường lưới ngang
//        y.setAxisLineColor(R.color.green);
//        y.setAxisMinimum(0);
//        y.setAxisMaximum(160);

        YAxis y = mChart.getAxisLeft();
        y.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART);
                y.setLabelCount(8, true);  // Đảm bảo có 8 nhãn từ 0 đến 140 với khoảng cách 20 đơn vị

        y.setDrawGridLines(true);  // Hiển thị đường lưới ngang
        y.setDrawLabels(false);  // Ẩn giá trị trên trục Y
        y.setAxisLineColor(R.color.green);
        y.setAxisMinimum(0);
        y.setAxisMaximum(160);
        y.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                return String.format("%.0f dB", value);
            }
        });

        mChart.getAxisRight().setEnabled(false);

        // Thiết lập dữ liệu cho biểu đồ
        yVals = new ArrayList<>();
        LineDataSet set1 = new LineDataSet(yVals, "Sound Level");
        set1.setMode(LineDataSet.Mode.CUBIC_BEZIER);
        set1.setCubicIntensity(0.2f);
        set1.setDrawFilled(true);
        set1.setDrawCircles(false);
        set1.setCircleColor(R.color.green);
        set1.setHighLightColor(Color.rgb(244, 117, 117));
        set1.setColor(R.color.green);
        set1.setFillColor(R.color.green);
        set1.setFillAlpha(100);
        set1.setDrawHorizontalHighlightIndicator(false);
        set1.setFillFormatter(new IFillFormatter() {
            @Override
            public float getFillLinePosition(ILineDataSet dataSet, LineDataProvider dataProvider) {
                return -10;
            }
        });

        LineData data = new LineData(set1);
        data.setValueTextSize(9f);
        data.setDrawValues(false);

        mChart.setData(data);
        mChart.getLegend().setEnabled(false);
        mChart.invalidate();
    }

    private void addEntry(float dbCount) {
        LineData data = mChart.getData();
        if (data != null) {
            ILineDataSet set = data.getDataSetByIndex(0);
            if (set == null) {
                set = createSet();
                data.addDataSet(set);
            }
            data.addEntry(new Entry(set.getEntryCount(), dbCount), 0);
            data.notifyDataChanged();

            mChart.notifyDataSetChanged();
            mChart.setVisibleXRangeMaximum(50);
            mChart.moveViewToX(data.getEntryCount());
        }
    }

    private LineDataSet createSet() {
        LineDataSet set = new LineDataSet(null, "Sound Level");
        set.setMode(LineDataSet.Mode.CUBIC_BEZIER);
        set.setCubicIntensity(0.2f);
        set.setDrawFilled(true);
        set.setDrawCircles(false);
        set.setCircleColor(Color.GREEN);
        set.setHighLightColor(Color.rgb(244, 117, 117));
        set.setColor(Color.GREEN);
        set.setFillColor(Color.GREEN);
        set.setFillAlpha(100);
        set.setDrawHorizontalHighlightIndicator(false);
        set.setFillFormatter(new IFillFormatter() {
            @Override
            public float getFillLinePosition(ILineDataSet dataSet, LineDataProvider dataProvider) {
                return -10;
            }
        });
        return set;
    }
}
