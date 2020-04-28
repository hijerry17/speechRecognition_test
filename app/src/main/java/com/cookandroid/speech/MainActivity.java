package com.cookandroid.speech;

import android.Manifest;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    Button btn, btn_2, btn_3;
    int PERMISSION;
    String[] example_k = new String[3];
    String[] example_e = new String[3];
    int count;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (Build.VERSION.SDK_INT >= 23) {
            // 퍼미션 체크
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.INTERNET,
                    Manifest.permission.RECORD_AUDIO}, PERMISSION);
        }

        createKorData();
        createEngData();

            count = -1;

            btn = findViewById(R.id.btn);
            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if(count<2) {
                        count++;
                    }
                    Intent intent = new Intent(getApplicationContext(), CustomDialog.class);
                    intent.putExtra("kor", example_k[count]);
                    intent.putExtra("eng", example_e[count]);

                    startActivity(intent);
                }
            });

        btn_2 = findViewById(R.id.btn_2);
        btn_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(getApplicationContext(), SpeechActivity.class);
                for (int i=0; i<2; i++) {
                    intent.putExtra("count", 2);
                    intent.putExtra("kor_"+i, example_k[i]);
                    intent.putExtra("eng_"+i, example_e[i]);
                }
                startActivity(intent);
            }
        });

        btn_3 = findViewById(R.id.btn_3);
        btn_3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(getApplicationContext(), SpeechActivity.class);
                for (int i=0; i<3; i++) {
                    intent.putExtra("count", 3);
                    intent.putExtra("kor_"+i, example_k[i]);
                    intent.putExtra("eng_"+i, example_e[i]);
                }
                startActivity(intent);
            }
        });

    }

    public void createKorData() {
        example_k[0] = "안녕하세요.";
        example_k[1] = "오랜만입니다.";
        example_k[2] = "배가 고프네요.";
    }

    public void createEngData() {
        example_e[0] = "Hi.";
        example_e[1] = "Long time no see";
        example_e[2] = "I'm hungry.";

    }

}



