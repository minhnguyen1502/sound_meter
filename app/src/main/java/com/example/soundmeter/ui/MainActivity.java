package com.example.soundmeter.ui;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Vibrator;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.core.view.WindowInsetsControllerCompat;

import com.example.soundmeter.R;
import com.example.soundmeter.databinding.ActivityMainBinding;
import com.example.soundmeter.utils.FileUtil;
import com.example.soundmeter.utils.Recoder;
import com.example.soundmeter.utils.SoundView;
import com.example.soundmeter.utils.World;
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
import java.util.Locale;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding mainBinding;
    LineChart mChart;
    public static final String PREFS_NAME = "LanguagePrefs";
    public static final String PREF_LANGUAGE = "SelectedLanguage";
    private Recoder recoder;
    private SoundView soundView;
    private ArrayList<Entry> yVals;
    private static final int msgWhat = 0x1001;
    private static final int refreshTime = 100;
    private float volume = 10000;
    public static Typeface typeface;
    private boolean isPaused = false;

//    MediaPlayer mediaPlayer;
    Vibrator vibrator;
    boolean bl_sound, bl_vibration;
    public static final String PREFS_VIBRATION = "VibrationPrefs";
    public static final String PREF_VIBRATION_SWITCH = "VibrationSwitch";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        String selectedLanguage = getSelectedLanguage();
        Locale locale = new Locale(selectedLanguage);
        Locale.setDefault(locale);
        Configuration configuration = getResources().getConfiguration();
        configuration.setLocale(locale);
        getResources().updateConfiguration(configuration, getBaseContext().getResources().getDisplayMetrics());

        mainBinding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(mainBinding.getRoot());

        getWindow().setStatusBarColor(Color.TRANSPARENT);
        getWindow().setNavigationBarColor(Color.TRANSPARENT);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);

        mainBinding.getRoot().setPadding(mainBinding.getRoot().getPaddingLeft(),
                mainBinding.getRoot().getPaddingTop() + getStatusBarHeight(), mainBinding.getRoot().getPaddingRight(),
                mainBinding.getRoot().getPaddingBottom());

//        mediaPlayer = MediaPlayer.create(this, R.raw.ic_pause);  // Ensure ic_pause is an audio file in res/raw/
        vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);

        SharedPreferences prefs = getSharedPreferences(PREFS_VIBRATION, MODE_PRIVATE);
        bl_vibration = prefs.getBoolean(PREF_VIBRATION_SWITCH, false);

        typeface = ResourcesCompat.getFont(this, R.font.source_sans_3_light_300);

        recoder = new Recoder();

        initChart();

        mainBinding.imgSetting.setOnClickListener(view -> {
            Intent i = new Intent(MainActivity.this, SettingActivity.class);
            startActivity(i);
        });
        mainBinding.btnReset.setOnClickListener(view -> {
            if (!isPaused) {
                reset();
            }
        });
        mainBinding.ivPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                vibrator.vibrate(1000);
                isPaused = !isPaused;
                if (isPaused) {
                    pauseMeasurement();
                } else {
                    resumeMeasurement();
                }
            }
        });
        mainBinding.btnInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, InfoActivity.class));
            }
        });

        hideNavigation();
    }
//    public void SoundButton(View view){
//        if (bl_sound){
//            mediaPlayer.start();
//        }else {
//
//        }
//        if (bl_vibration){
//            vibrator.vibrate(30);
//        }
//    }
    private void pauseMeasurement() {
        handler.removeMessages(msgWhat);
        Toast.makeText(this, " Paused", Toast.LENGTH_SHORT).show();
    }
    private void resumeMeasurement() {
        startListen();
        Toast.makeText(this, " Resumed", Toast.LENGTH_SHORT).show();
    }
    private String getSelectedLanguage() {
        SharedPreferences sharedPreferences = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(PREF_LANGUAGE, "en");
    }
    private int getStatusBarHeight() {
        int result = 0;
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }
    public void hideNavigation() {
        WindowInsetsControllerCompat windowInsetsController;
        if (Build.VERSION.SDK_INT >= 30) {
            windowInsetsController = ViewCompat.getWindowInsetsController(getWindow().getDecorView());
        } else {
            windowInsetsController = new WindowInsetsControllerCompat(getWindow(), mainBinding.getRoot());
        }
        if (windowInsetsController == null) {
            return;
        }
        windowInsetsController.setSystemBarsBehavior(WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE);
        windowInsetsController.hide(WindowInsetsCompat.Type.navigationBars());
        getWindow().getDecorView().setOnSystemUiVisibilityChangeListener(i -> {
            if (i == 0) {
                new Handler().postDelayed(() -> {
                    WindowInsetsControllerCompat windowInsetsController1;
                    if (Build.VERSION.SDK_INT >= 30) {
                        windowInsetsController1 = ViewCompat.getWindowInsetsController(getWindow().getDecorView());
                    } else {
                        windowInsetsController1 = new WindowInsetsControllerCompat(getWindow(), mainBinding.getRoot());
                    }
                    Objects.requireNonNull(windowInsetsController1).hide(WindowInsetsCompat.Type.navigationBars());
                }, 3000);
            }
        });
    }
    private void reset() {
        World.reset();
        mainBinding.tvValue.setText(String.format("%.1f", World.dbCount).replace(",", "."));
        mainBinding.tvMax.setText(String.format("%.1f dB", World.MAX).replace(",", "."));
        mainBinding.tvMin.setText(String.format("%.1f dB", World.MIN).replace(",", "."));
        mainBinding.tvAvg.setText(String.format("%.1f dB", World.getAvg()).replace(",", "."));
        mChart.clear();
        initChart();
        startListen();
    }
    @SuppressLint("HandlerLeak")
    private final Handler handler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            if (isPaused || this.hasMessages(msgWhat)) {
                return;
            }
            volume = recoder.getMax();
            if (volume > 0 && volume < 10000) {
                float dbCount = World.setDbCount(20 * (float) (Math.log10(volume)));
                mainBinding.tvValue.setText(String.format("%.1f", dbCount).replace(",", "."));
                mainBinding.tvMax.setText(String.format("%.1f dB", World.MAX).replace(",", "."));
                mainBinding.tvMin.setText(String.format("%.1f dB", World.MIN).replace(",", "."));
                mainBinding.tvAvg.setText(String.format("%.1f dB", World.getAvg()).replace(",", "."));
                soundView.refresh();
                updateData(dbCount);
            }
            startListen();
        }
    };
    private void startListen() {
        if (!isPaused) {
            handler.sendEmptyMessageDelayed(msgWhat, refreshTime);
        }
    }
    public void startRecode(File mFile) {
        try {
            recoder.setmFile(mFile);
            if (recoder.startRecoding()) {
                startListen();
            } else {
                World.dbCount = 0;
                mainBinding.ivImg.refresh();
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

        SharedPreferences getVibration = getSharedPreferences("vibration_switch",MODE_PRIVATE);
        bl_vibration = getVibration.getBoolean("VibrationSwitch", false);

        soundView = findViewById(R.id.iv_img);
        File file = FileUtil.createFile("sound_meter.amr", this);
        startRecode(file);
    }
    @Override
    protected void onPause() {
        super.onPause();
        recoder.deleteRecoding();
        handler.hasMessages(msgWhat);
    }
    @Override
    protected void onDestroy() {
        recoder.deleteRecoding();
        handler.hasMessages(msgWhat);
        super.onDestroy();

    }
    public int dpToPx(float dp, Context context) {
        return Math.round(dp * context.getResources().getDisplayMetrics().density);
    }
    private void initChart() {

        mChart = findViewById(R.id.chart);
        mChart.setViewPortOffsets(dpToPx(38, this), dpToPx(10, this), 0, dpToPx(18, this));
        mChart.setTouchEnabled(false);
        mChart.setDragEnabled(true);
        mChart.setScaleEnabled(false);
        mChart.setPinchZoom(false);
        mChart.setDrawGridBackground(false);
        mChart.getDescription().setEnabled(false);
        // Thiết lập trục X
        XAxis x = mChart.getXAxis();
        x.setEnabled(true);
        x.setTypeface(typeface);
        x.setTextSize(10f);
        x.setPosition(XAxis.XAxisPosition.BOTTOM);
        x.setDrawGridLines(true);
        x.setTextColor(Color.WHITE);
//        x.setLabelCount(4, true);
        x.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                return String.format("%.0f s", value);
            }
        });

//      Thiết lập trục Y
        YAxis y = mChart.getAxisLeft();
        y.setLabelCount(6, false);
        y.setTextColor(Color.WHITE);
        y.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART);
        y.setDrawGridLines(false);
        y.setDrawGridLines(true);
        x.setTypeface(typeface);
        x.setTextSize(10f);
        y.setAxisMinimum(0);
        y.setAxisMaximum(140);
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
        set1.setMode(LineDataSet.Mode.LINEAR);
        set1.setCubicIntensity(0.2f);
        set1.setDrawFilled(true);
        set1.setDrawCircles(false);
        set1.setHighlightEnabled(false);
        set1.setColor(Color.rgb(0, 255, 255));
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
    private void updateData(float dbCount) {
        LineData data = mChart.getData();
        if (data != null) {
            ILineDataSet set = data.getDataSetByIndex(0);
            if (set == null) {
                set = createSet();
                data.addDataSet(set);
            }
            data.addEntry(new Entry(set.getEntryCount() * 0.1f, dbCount), 0);
            data.notifyDataChanged();

            mChart.notifyDataSetChanged();
            mChart.moveViewToX(data.getEntryCount());
        }
    }
    private LineDataSet createSet() {
        LineDataSet set = new LineDataSet(null, "Sound Level");
        set.setMode(LineDataSet.Mode.CUBIC_BEZIER);
        set.setCubicIntensity(0.2f);
        set.setDrawFilled(true);
        set.setDrawCircles(false);
        set.setHighlightEnabled(false);
        set.setDrawHorizontalHighlightIndicator(false);
        set.setFillFormatter(new IFillFormatter() {
            @Override
            public float getFillLinePosition(ILineDataSet dataSet, LineDataProvider dataProvider) {
                return -10;
            }
        });
        return set;
    }
    @Override
    public void onBackPressed() {
        showQuitDialog();
    }
    private void showQuitDialog() {
        new AlertDialog.Builder(this)
                .setMessage(R.string.quit)
                .setCancelable(false)
                .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        finishAffinity();
                    }
                })
                .setNegativeButton(R.string.no, null)
                .show();
    }
}
