package com.example.soundmeter;

import androidx.appcompat.app.AppCompatActivity;

import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.soundmeter.databinding.LanguagesBinding;

import java.util.Locale;

public class LanguagesActivity extends AppCompatActivity {

    LanguagesBinding languagesBinding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        languagesBinding = LanguagesBinding.inflate(getLayoutInflater());
        setContentView(languagesBinding.getRoot());

        languagesBinding.constraintEng.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                changeLanguage("en");
                Toast.makeText(LanguagesActivity.this, "You changed language to English", Toast.LENGTH_SHORT).show();
            }
        });
        languagesBinding.constraintFrench.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeLanguage("fr");

                Toast.makeText(LanguagesActivity.this, "You changed language to French", Toast.LENGTH_SHORT).show();
            }
        });
        languagesBinding.constraintGerman.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeLanguage("de");

                Toast.makeText(LanguagesActivity.this, "You changed language to German", Toast.LENGTH_SHORT).show();
            }
        });
        languagesBinding.constraintHindi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeLanguage("hi");

                Toast.makeText(LanguagesActivity.this, "You changed language to Hindi", Toast.LENGTH_SHORT).show();

            }
        });
        languagesBinding.constraintIndo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeLanguage("in");

                Toast.makeText(LanguagesActivity.this, "You changed language to Indonesia", Toast.LENGTH_SHORT).show();
            }
        });
        languagesBinding.constraintSpanish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeLanguage("es");
                Toast.makeText(LanguagesActivity.this, "You changed language to Spanish", Toast.LENGTH_SHORT).show();

            }
        });
        languagesBinding.constraintPortu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeLanguage("pt");

                Toast.makeText(LanguagesActivity.this, "You changed language to Portuguese", Toast.LENGTH_SHORT).show();

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
                Toast.makeText(LanguagesActivity.this, "You changed language success", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void changeLanguage(String language){
        Locale locale = new Locale(language);
        Locale.setDefault(locale);
        Configuration configuration = new Configuration();
        configuration.locale=locale;
        getApplication().getResources().updateConfiguration(configuration,
                getApplication().getResources().getDisplayMetrics());

        languagesBinding.tvLanguage.setText(getString(R.string.language));
        languagesBinding.tvEng.setText(getString(R.string.english));
        languagesBinding.tvDe.setText(getString(R.string.german));
        languagesBinding.tvEs.setText(getString(R.string.spanish));
        languagesBinding.tvHi.setText(getString(R.string.hindi));
        languagesBinding.tvPt.setText(getString(R.string.portuguese));
        languagesBinding.tvFr.setText(getString(R.string.french));
        languagesBinding.tvIn.setText(getString(R.string.indonesia));


    }
}