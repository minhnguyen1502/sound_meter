package com.example.soundmeter.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.core.view.WindowInsetsControllerCompat;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Toast;

import com.example.soundmeter.R;
import com.example.soundmeter.databinding.ActivitySettingBinding;

import java.util.Objects;

public class SettingActivity extends AppCompatActivity {

    ActivitySettingBinding settingBinding;

    public static final String PREFS_NAME = "LanguagePrefs";
    public static final String PREF_LANGUAGE = "SelectedLanguage";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        settingBinding = ActivitySettingBinding.inflate(getLayoutInflater());
        setContentView(settingBinding.getRoot());

        settingBinding.ivArrowLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    settingBinding.constraintLanguage.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            startActivity(new Intent(SettingActivity.this, LanguagesActivity.class));
        }
    });

        String selectedLanguage = getSelectedLanguage();
        settingBinding.tvChangeLanguage.setText(getLanguageDisplayName(selectedLanguage));

    settingBinding.layoutAbout.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            startActivity(new Intent(SettingActivity.this, AboutActivity.class));
        }
    });
    settingBinding.layoutShare.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Toast.makeText(SettingActivity.this, "share", Toast.LENGTH_SHORT).show();
        }
    });
    settingBinding.layoutRate.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Toast.makeText(SettingActivity.this, "rate", Toast.LENGTH_SHORT).show();
        }
    });

    settingBinding.layoutPriprivacyPolicy.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Toast.makeText(SettingActivity.this, "privacy policy", Toast.LENGTH_SHORT).show();
        }
    });
        getWindow().setStatusBarColor(Color.TRANSPARENT);
        getWindow().setNavigationBarColor(Color.TRANSPARENT);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);

        settingBinding.getRoot().setPadding(settingBinding    .getRoot().getPaddingLeft(),
                settingBinding.getRoot().getPaddingTop() + getStatusBarHeight(),
                settingBinding.getRoot().getPaddingRight(),
                settingBinding.getRoot().getPaddingBottom());



        hideNavigation();
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
            windowInsetsController = new WindowInsetsControllerCompat(getWindow(), settingBinding.getRoot());
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
                        windowInsetsController1 = new WindowInsetsControllerCompat(getWindow(), settingBinding.getRoot());
                    }
                    Objects.requireNonNull(windowInsetsController1).hide(WindowInsetsCompat.Type.navigationBars());
                }, 3000);
            }
        });
    }
    private String getSelectedLanguage() {
        SharedPreferences sharedPreferences = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(PREF_LANGUAGE, "en"); // Default to English if no language is selected
    }

    private String getLanguageDisplayName(String languageCode) {
        switch (languageCode) {
            case "en":
                return getString(R.string.english);
            case "fr":
                return getString(R.string.french);
            case "de":
                return getString(R.string.german);
            case "hi":
                return getString(R.string.hindi);
            case "in":
                return getString(R.string.indonesia);
            case "es":
                return getString(R.string.spanish);
            case "pt":
                return getString(R.string.portuguese);
            default:
                return getString(R.string.english);
        }
    }
}