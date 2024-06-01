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
import com.github.mikephil.charting.interfaces.dataprovider.LineDataProvider;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding mainBinding;
    LineChart mChart;
    public static Typeface tf;
    private Recoder recoder;
    private SoundView soundView;
    private final int savedTime = 0;
    private final boolean isChart = false;
    private final long currentTime = 0;
    private ArrayList<Entry> yVals;

    private static final int msgWhat = 0x1001;
    private static final int refreshTime = 100;
    private float volume = 10000;
    private boolean refreshed;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainBinding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(mainBinding.getRoot());
//        World.dbCount = 140;
//        mainBinding.ivImg.refresh();


        recoder = new Recoder();
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
//                    World.setDbCount(20 * (float)(Math.log10(volume)));
//                    soundView.refresh();
                float dbCount = World.setDbCount(20 * (float) (Math.log10(volume)));
                mainBinding.tvValue.setText(String.format("%.2f", dbCount).replace(",","."));
                mainBinding.tvMax.setText(String.format("%.2f", World.MAX).replace(",","."));
                mainBinding.tvMin.setText(String.format("%.2f", World.MIN).replace(",","."));
                mainBinding.tvAvg.setText(String.format("%.2f", World.getAvg()).replace(",","."));
                soundView.refresh();
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
        soundView = (SoundView) findViewById(R.id.iv_img);
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

//        private void initChart() {
//        if(mChart!=null){
//            if (mChart.getData() != null &&
//                    mChart.getData().getDataSetCount() > 0) {
//                savedTime++;
//                isChart=true;
//            }
//        }else{
//            currentTime=new Date().getTime();
//            mChart = (LineChart) findViewById(R.id.chart);
//            mChart.setViewPortOffsets(50, 20, 5, 60);
//            // no description text
////            mChart.setDescription("");
//            // enable touch gestures
//            mChart.setTouchEnabled(true);
//            // enable scaling and dragging
//            mChart.setDragEnabled(false);
//            mChart.setScaleEnabled(true);
//            // if disabled, scaling can be done on x- and y-axis separately
//            mChart.setPinchZoom(false);
//            mChart.setDrawGridBackground(false);
//            //mChart.setMaxHighlightDistance(400);
//            XAxis x = mChart.getXAxis();
//            x.setLabelCount(8, false);
//            x.setEnabled(true);
//            x.setTypeface(tf);
//            x.setTextColor(Color.GREEN);
//            x.setPosition(XAxis.XAxisPosition.BOTTOM);
//            x.setDrawGridLines(true);
//            x.setAxisLineColor(Color.GREEN);
//            YAxis y = mChart.getAxisLeft();
//            y.setLabelCount(6, false);
//            y.setTextColor(Color.GREEN);
//            y.setTypeface(tf);
//            y.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART);
//            y.setDrawGridLines(false);
//            y.setAxisLineColor(Color.GREEN);
//            y.setAxisMinValue(0);
//            y.setAxisMaxValue(120);
//            mChart.getAxisRight().setEnabled(true);
//            yVals = new ArrayList<Entry>();
//            yVals.add(new Entry(0,0));
//            LineDataSet set1 = new LineDataSet(yVals, "DataSet 1");
//            set1.setValueTypeface(tf);
//            set1.setMode(LineDataSet.Mode.CUBIC_BEZIER);
//            set1.setCubicIntensity(0.02f);
//            set1.setDrawFilled(true);
//            set1.setDrawCircles(false);
//            set1.setCircleColor(Color.GREEN);
//            set1.setHighLightColor(Color.rgb(244, 117, 117));
//            set1.setColor(Color.GREEN);
//            set1.setFillColor(Color.GREEN);
//            set1.setFillAlpha(100);
//            set1.setDrawHorizontalHighlightIndicator(false);
//            set1.setFillFormatter(new IFillFormatter() {
//                @Override
//                public float getFillLinePosition(ILineDataSet dataSet, LineDataProvider dataProvider) {
//                    return -10;
//                }
//            });
////            set1.setFillFormatter(() {
////                @Override
////                public float getFillLinePosition(ILineDataSet dataSet, LineDataProvider dataProvider) {
////                    return -10;
////                }
////            });
//            LineData data;
//            if (mChart.getData() != null &&
//                    mChart.getData().getDataSetCount() > 0) {
//                data =  mChart.getLineData();
//                data.clearValues();
//                data.removeDataSet(0);
//                data.addDataSet(set1);
//            }else {
//                data = new LineData(set1);
//            }
//
//            data.setValueTextSize(9f);
//            data.setDrawValues(false);
//            mChart.setData(data);
//            mChart.getLegend().setEnabled(false);
//            mChart.animateXY(2000, 2000);
//            // dont forget to refresh the drawing
//            mChart.invalidate();
//            isChart=true;
//        }
//
//    }
}