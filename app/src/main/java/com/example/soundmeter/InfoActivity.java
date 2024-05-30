package com.example.soundmeter;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import com.example.soundmeter.databinding.InfoBinding;

public class InfoActivity extends AppCompatActivity {

    InfoBinding infoBinding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        infoBinding = InfoBinding.inflate(getLayoutInflater());
        setContentView(infoBinding.getRoot());

        infoBinding.ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}