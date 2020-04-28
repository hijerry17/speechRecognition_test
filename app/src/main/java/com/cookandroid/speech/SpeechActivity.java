package com.cookandroid.speech;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.support.v7.app.AppCompatActivity;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class SpeechActivity extends AppCompatActivity {
    TextView tv_wrong;
    TextView[] textview_kor = new TextView[3];
    TextView[] textview_eng = new TextView[3];

    String[] kor = new String[3];
    String[] eng = new String[3];
    int count; // case 개수 (intent로 받아옴)
    Intent i;
    SpeechRecognizer mRecognizer;
    String recText;
    int[] wrong = new int[20];
    int wrong_num;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.speech);

        textview_kor[0] = findViewById(R.id.tv1_kor);
        textview_eng[0] = findViewById(R.id.tv1_eng);
        textview_kor[1] = findViewById(R.id.tv2_kor);
        textview_eng[1] = findViewById(R.id.tv2_eng);
        textview_kor[2] = findViewById(R.id.tv3_kor);
        textview_eng[2] = findViewById(R.id.tv3_eng);
        tv_wrong = findViewById(R.id.tv_wrong);

        Intent intent = getIntent();
        count = intent.getExtras().getInt("count");

        if(count == 2) {
            for(int i=0; i<2; i++) {
                kor[i] = intent.getExtras().getString("kor_"+i);
                eng[i] = intent.getExtras().getString("eng_"+i);

                textview_kor[i].setText(kor[i]);
                textview_eng[i].setText(eng[i]);
            }
            // 텍스트뷰 숨기기
            textview_kor[2].setVisibility(View.GONE);
            textview_eng[2].setVisibility(View.GONE);
        } else {
            for(int i=0; i<3; i++) {
                kor[i] = intent.getExtras().getString("kor_"+i);
                eng[i] = intent.getExtras().getString("eng_"+i);
                textview_kor[i].setText(kor[i]);
                textview_eng[i].setText(eng[i]);
            }
        }

        i = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        i.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE, getPackageName());
        i.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "ko-KR");

        wrong_num=0;

        mRecognizer = SpeechRecognizer.createSpeechRecognizer(getApplicationContext());
        mRecognizer.setRecognitionListener(listener);
        mRecognizer.startListening(i);

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

            // 배열로 선언
            String[] lyrics_org = new String[count];
            String[] lyrics = new String[count];
            int[] rightCount = new int[count];
            int max = 0;
            int max_num = 0;

            SpannableStringBuilder sb = new SpannableStringBuilder(recText_org);

            // 문장 개수 따라 검사
            for(int i=0; i< count; i++) {
                rightCount[i] = 0;

                lyrics_org[i] = kor[i]; //가사 정보
                lyrics[i] = lyrics_org[i].replace(" ", ""); //가사정보 공백제거
                lyrics[i] = lyrics[i].replace(".", ""); //가사정보 온점제거
                lyrics[i] = lyrics[i].replace("!", ""); //가사정보 느낌표제거

                int length = (recText.length() > lyrics[i].length()) ? recText.length() : lyrics[i].length();
                for (int j = 0; j < length; j++) {
                    try {
                        if ((recText.charAt(j)) == (lyrics[i].charAt(j))) {  //음성정보와 가사와 비교
                            rightCount[i]++; // 맞은 개수 체크
                        } else {
                            continue;
                        }
                    } catch (Exception e) {
                        continue;

                    }
                }

                // 맞은 개수가 가장 많은 문장 찾기
                if(rightCount[i] > max) {
                    max = rightCount[i];
                    // 그 문장의 번호를 저장
                    max_num = i;
                }

            }

            // 해당 문장과 발음한 문장 비교
                int length = (recText.length()>lyrics[max_num].length())?recText.length():lyrics[max_num].length();
                for (int j = 0; j < length; j++) {
                    try {
                        if ((recText.charAt(j)) == (lyrics[max_num].charAt(j))) {  //음성정보와 가사와 비교
                            continue;
                        } else {
                            wrong[wrong_num] = j; // 틀린 부분 저장
                            wrong_num++;
                        }
                    } catch (Exception e) {
                        wrong[wrong_num] = j; // 틀린 부분 저장
                        wrong_num++;
                    }
                }

                for(int j=0; j<wrong_num; j++) {
                    for(int k=0; k<recText_org.length(); k++) {
                        try {
                            if((recText_org.charAt(k)) == (recText.charAt(wrong[j]))) { // 기존 예시에서 틀린 부분 찾기
                                // 틀린 부분 색깔 바꾸기
                                ForegroundColorSpan span = new ForegroundColorSpan(Color.parseColor("#FFFA5252"));
                                sb.setSpan(span, k, k+1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                            }
                        } catch (Exception e) {
                            break;
                        }
                    }

                }

                // 해당되는 문장 색 바꾸기
            textview_kor[max_num].setTextColor(Color.parseColor("#FF52B4FA"));
            textview_eng[max_num].setTextColor(Color.parseColor("#FF52B4FA"));
            tv_wrong.setText(sb);


        }

        @Override
        public void onPartialResults(Bundle partialResults) {
        }

        @Override
        public void onEvent(int eventType, Bundle params) {

        }
    };

}
