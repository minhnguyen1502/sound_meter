package com.example.soundmeter.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.core.view.WindowInsetsControllerCompat;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Toast;

import com.example.soundmeter.databinding.ActivityLanguagesBinding;

import java.util.Locale;
import java.util.Objects;

public class LanguagesActivity extends AppCompatActivity {

    public static final String PREFS_NAME = "LanguagePrefs";
    public static final String PREF_LANGUAGE = "SelectedLanguage";
    ActivityLanguagesBinding languagesBinding;
    private String selectedLanguage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        languagesBinding = ActivityLanguagesBinding.inflate(getLayoutInflater());
        setContentView(languagesBinding.getRoot());

        languagesBinding.constraintEng.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectedLanguage = "en";
                Toast.makeText(LanguagesActivity.this, "You choose English", Toast.LENGTH_SHORT).show();
            }
        });
        languagesBinding.constraintFrench.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectedLanguage = "fr";
                Toast.makeText(LanguagesActivity.this, "You choose French", Toast.LENGTH_SHORT).show();
            }
        });
        languagesBinding.constraintGerman.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                selectedLanguage = "de";

                Toast.makeText(LanguagesActivity.this, "You choose German", Toast.LENGTH_SHORT).show();
            }
        });
        languagesBinding.constraintHindi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                selectedLanguage = "hi";

                Toast.makeText(LanguagesActivity.this, "You choose Hindi", Toast.LENGTH_SHORT).show();

            }
        });
        languagesBinding.constraintIndo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectedLanguage = "in";

                Toast.makeText(LanguagesActivity.this, "You choose Indonesia", Toast.LENGTH_SHORT).show();
            }
        });
        languagesBinding.constraintSpanish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectedLanguage = "es";

                Toast.makeText(LanguagesActivity.this, "You choose Spanish", Toast.LENGTH_SHORT).show();

            }
        });
        languagesBinding.constraintPortu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectedLanguage = "pt";
                Toast.makeText(LanguagesActivity.this, "You choose Portuguese", Toast.LENGTH_SHORT).show();

            }
        });
        languagesBinding.ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        languagesBinding.ivConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (selectedLanguage == null) {
                    Toast.makeText(LanguagesActivity.this, "choose language before change", Toast.LENGTH_SHORT).show();
                } else {
                    changeLanguage(LanguagesActivity.this,selectedLanguage);
                    saveSelectedLanguage(selectedLanguage);

                    Intent i = new Intent(LanguagesActivity.this, MainActivity.class);
                    finish();
                    startActivity(i);
                    Toast.makeText(LanguagesActivity.this, "You changed language success", Toast.LENGTH_SHORT).show();
                }
            }
        });
        getWindow().setStatusBarColor(Color.TRANSPARENT);
        getWindow().setNavigationBarColor(Color.TRANSPARENT);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);

        languagesBinding.getRoot().setPadding(languagesBinding    .getRoot().getPaddingLeft(),
                languagesBinding.getRoot().getPaddingTop() + getStatusBarHeight(),
                languagesBinding.getRoot().getPaddingRight(),
                languagesBinding.getRoot().getPaddingBottom());



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
            windowInsetsController = new WindowInsetsControllerCompat(getWindow(), languagesBinding.getRoot());
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
                        windowInsetsController1 = new WindowInsetsControllerCompat(getWindow(), languagesBinding.getRoot());
                    }
                    Objects.requireNonNull(windowInsetsController1).hide(WindowInsetsCompat.Type.navigationBars());
                }, 3000);
            }
        });
    }
    public void changeLanguage(Activity activity,String language) {
        Locale locale = new Locale(language);
        Locale.setDefault(locale);

        Resources resources = activity.getResources();
        Configuration configuration = resources.getConfiguration();
        configuration.setLocale(locale);
        resources.updateConfiguration(configuration,
                resources.getDisplayMetrics());


    }
    private void saveSelectedLanguage(String language) {
        SharedPreferences sharedPreferences = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(PREF_LANGUAGE, language);
        editor.apply();
    }
}