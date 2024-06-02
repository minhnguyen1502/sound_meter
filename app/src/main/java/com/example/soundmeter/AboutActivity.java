package com.example.soundmeter;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.soundmeter.databinding.AboutBinding;

public class AboutActivity extends AppCompatActivity {

    AboutBinding aboutBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        aboutBinding = AboutBinding.inflate(getLayoutInflater());
        setContentView(aboutBinding.getRoot());

        aboutBinding.ivArrowLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}