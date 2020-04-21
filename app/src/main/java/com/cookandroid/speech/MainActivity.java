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
    Intent i;
    SpeechRecognizer mRecognizer;
    Button btn;
    TextView tv1, tv2, speech;
    int PERMISSION;
    String[] example = new String[5];
    int count, rightCount;
    String recText;
    String lyrics;
    int[] wrong = new int[10];
    int wrong_num;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btn = findViewById(R.id.btn); // 누르면 시작
        tv1 = findViewById(R.id.tv1); // 예시 문장
        tv2 = findViewById(R.id.tv2); // 인식된 문장
        speech = findViewById(R.id.speech);

        if (Build.VERSION.SDK_INT >= 23) {
            // 퍼미션 체크
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.INTERNET,
                    Manifest.permission.RECORD_AUDIO}, PERMISSION);
        }

            i = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
            i.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE, getPackageName());
            i.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "ko-KR");

            createData();
            count = -1;

            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    wrong_num=0;
                    rightCount=0;
                    if(count<4) {
                        count++;
                    }
                        tv1.setText(example[count]);
                        mRecognizer = SpeechRecognizer.createSpeechRecognizer(getApplicationContext());
                        mRecognizer.setRecognitionListener(listener);
                        mRecognizer.startListening(i);


                }
            });

    }

    private final RecognitionListener listener = new RecognitionListener() {
        @Override
        public void onReadyForSpeech(Bundle params) {

            Toast.makeText(getApplicationContext(), "음성인식을 시작합니다.", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onBeginningOfSpeech() {

        }

        @Override
        public void onRmsChanged(float rmsdB) {

        }

        @Override
        public void onBufferReceived(byte[] buffer) {
        }

        @Override
        public void onEndOfSpeech() {

        }

        @Override
        public void onError(int error) {
            String message;

            switch (error) {
                case SpeechRecognizer.ERROR_AUDIO:
                    message = "오디오 에러";
                    break;
                case SpeechRecognizer.ERROR_CLIENT:
                    message = "클라이언트 에러";
                    break;
                case SpeechRecognizer.ERROR_INSUFFICIENT_PERMISSIONS:
                    message = "퍼미션 없음";
                    break;
                case SpeechRecognizer.ERROR_NETWORK:
                    message = "네트워크 에러";
                    break;
                case SpeechRecognizer.ERROR_NETWORK_TIMEOUT:
                    message = "네트웍 타임아웃";
                    break;
                case SpeechRecognizer.ERROR_NO_MATCH:
                    message = "찾을 수 없음";
                    break;
                case SpeechRecognizer.ERROR_RECOGNIZER_BUSY:
                    message = "RECOGNIZER가 바쁨";
                    break;
                case SpeechRecognizer.ERROR_SERVER:
                    message = "서버가 이상함";
                    break;
                case SpeechRecognizer.ERROR_SPEECH_TIMEOUT:
                    message = "말하는 시간초과";
                    break;
                default:
                    message = "알 수 없는 오류임";
                    break;
            }

            Toast.makeText(getApplicationContext(), "에러가 발생하였습니다. : " + message, Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onResults(Bundle results) {
            // 말을 하면 ArrayList에 단어를 넣고 textView에 단어를 이어줍니다.
            ArrayList<String> matches =
                    results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
            String recText_org = matches.get(0); //인식된 음성정보
            recText = recText_org.replace(" ", ""); //인식된 음성정보 공백제거

            String lyrics_org = tv1.getText().toString(); //가사 정보
            lyrics = lyrics_org.replace(" ", ""); //가사정보 공백제거
            lyrics = lyrics.replace(".", ""); //가사정보 온점제거
            lyrics = lyrics.replace("!",""); //가사정보 느낌표제거

            SpannableStringBuilder sb = new SpannableStringBuilder(recText_org);

            int length = (recText.length()>lyrics.length())?recText.length():lyrics.length();
            for (int i = 0; i < length; i++) {
                try {
                    if ((recText.charAt(i)) == (lyrics.charAt(i))) {  //음성정보와 가사와 비교해서 가사 정확도 점수측정
                        rightCount++;
                    } else {
                        wrong[wrong_num] = i;
                        wrong_num++;
                    }
                } catch (Exception e) {
                    break;
                }
            }

            for(int j=0; j<wrong_num; j++) {
                for(int i=0; i<recText_org.length(); i++) {
                    try {
                        if((recText_org.charAt(i)) == (recText.charAt(wrong[j]))) {
                            ForegroundColorSpan span = new ForegroundColorSpan(Color.parseColor("#FFFA5252"));
                            sb.setSpan(span, i, i+1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                        }
                    } catch (Exception e) {
                        break;
                    }
                }

            }

            if(rightCount > recText.length()-3) {
                tv2.setText(sb);
                btn.setEnabled(true);

            } else {
                btn.setEnabled(false);
                Toast.makeText(getApplicationContext(), "다시 말해주세요.", Toast.LENGTH_SHORT).show();
                rightCount=0;
                mRecognizer.startListening(i);
            }

        }

        @Override
        public void onPartialResults(Bundle partialResults) {
        }

        @Override
        public void onEvent(int eventType, Bundle params) {

        }
    };

    public void createData() {
        example[0] = "안녕하세요.";
        example[1] = "음성 인식 테스트입니다.";
        example[2] = "생각보다 복잡해지네요.";
        example[3] = "이거 안되면 나 치킨 시킬거다.";
        example[4] = "배고파서 아님 속상해서임.";
    }

}



