package com.example.soundmeter;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

import com.example.soundmeter.databinding.SettingBinding;

public class SettingActivity extends AppCompatActivity {

    SettingBinding settingBinding;

    public static final String PREFS_NAME = "LanguagePrefs";
    public static final String PREF_LANGUAGE = "SelectedLanguage";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        settingBinding = SettingBinding.inflate(getLayoutInflater());
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

    settingBinding.tvAbout.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            startActivity(new Intent(SettingActivity.this, AboutActivity.class));
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