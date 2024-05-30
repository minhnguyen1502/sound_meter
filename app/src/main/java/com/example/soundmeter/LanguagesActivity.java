package com.example.soundmeter;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.soundmeter.databinding.LanguagesBinding;

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
                Toast.makeText(LanguagesActivity.this, "You changed language to English", Toast.LENGTH_SHORT).show();
            }
        });
        languagesBinding.constraintFrench.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(LanguagesActivity.this, "You changed language to French", Toast.LENGTH_SHORT).show();
            }
        });
        languagesBinding.constraintGerman.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(LanguagesActivity.this, "You changed language to German", Toast.LENGTH_SHORT).show();
            }
        });
        languagesBinding.constraintHindi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(LanguagesActivity.this, "You changed language to Hindi", Toast.LENGTH_SHORT).show();

            }
        });
        languagesBinding.constraintIndo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(LanguagesActivity.this, "You changed language to Indonesia", Toast.LENGTH_SHORT).show();
            }
        });
        languagesBinding.constraintSpanish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(LanguagesActivity.this, "You changed language to Spanish", Toast.LENGTH_SHORT).show();

            }
        });
        languagesBinding.constraintPortu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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
}