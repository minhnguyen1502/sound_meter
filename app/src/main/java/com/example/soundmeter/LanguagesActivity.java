package com.example.soundmeter;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.soundmeter.databinding.LanguagesBinding;

import java.util.Locale;

public class LanguagesActivity extends AppCompatActivity {

    public static final String PREFS_NAME = "LanguagePrefs";
    public static final String PREF_LANGUAGE = "SelectedLanguage";
    LanguagesBinding languagesBinding;
    private String selectedLanguage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        languagesBinding = LanguagesBinding.inflate(getLayoutInflater());
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