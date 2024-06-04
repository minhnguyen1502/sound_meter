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

import com.example.soundmeter.R;
import com.example.soundmeter.databinding.ActivityLanguagesBinding;

import java.util.Locale;
import java.util.Objects;

public class LanguagesActivity extends AppCompatActivity {

    public static final String PREFS_NAME = "LanguagePrefs";
    public static final String PREF_LANGUAGE = "SelectedLanguage";
    ActivityLanguagesBinding languagesBinding;
    private String selectedLanguage;
    private View selectedLanguageView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        languagesBinding = ActivityLanguagesBinding.inflate(getLayoutInflater());
        setContentView(languagesBinding.getRoot());

        languagesBinding.constraintEng.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                highlightSelectedLanguage(view);
                selectedLanguage = "en";
            }
        });
        languagesBinding.constraintFrench.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                highlightSelectedLanguage(view);
                selectedLanguage = "fr";
            }
        });
        languagesBinding.constraintGerman.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                highlightSelectedLanguage(view);
                selectedLanguage = "de";

            }
        });
        languagesBinding.constraintHindi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                highlightSelectedLanguage(view);
                selectedLanguage = "hi";


            }
        });
        languagesBinding.constraintIndo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                highlightSelectedLanguage(view);
                selectedLanguage = "in";

            }
        });
        languagesBinding.constraintSpanish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                highlightSelectedLanguage(view);
                selectedLanguage = "es";


            }
        });
        languagesBinding.constraintPortu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                highlightSelectedLanguage(view);
                selectedLanguage = "pt";

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
                    Toast.makeText(LanguagesActivity.this, R.string.choose_language_before_change, Toast.LENGTH_SHORT).show();
                } else {
                    changeLanguage(LanguagesActivity.this,selectedLanguage);
                    saveSelectedLanguage(selectedLanguage);

                    Intent i = new Intent(LanguagesActivity.this, MainActivity.class);
                    finish();
                    startActivity(i);
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

    private void highlightSelectedLanguage(View view) {
        if (selectedLanguageView != null) {
            selectedLanguageView.setBackgroundResource(R.drawable.bg_languages);
        }
        view.setBackgroundResource(R.drawable.bg_selected_language);
        selectedLanguageView = view;
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

    private void saveSelectedLanguage(String languageCode) {
        SharedPreferences sharedPreferences = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(PREF_LANGUAGE, languageCode);
        editor.apply();
    }
}