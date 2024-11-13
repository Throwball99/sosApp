package com.example.intentwithcompass;

import android.content.Context;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import java.util.HashMap;
import java.util.Map;
import androidx.appcompat.app.AppCompatActivity;

public class MossActitvity extends AppCompatActivity {
    private CameraManager cameraManager;
    private String cameraId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_moss_actitvity);

        // 카메라 매니저 설정
        cameraManager = (CameraManager) getSystemService(Context.CAMERA_SERVICE);
        try {
            cameraId = cameraManager.getCameraIdList()[0];
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }

        // UI 요소 설정
        EditText editText = findViewById(R.id.editText);
        TextView textView = findViewById(R.id.textView);
        Button button = findViewById(R.id.button);

        // 버튼 클릭 리스너 설정
        button.setOnClickListener(v -> {
            String inputText = editText.getText().toString();
            String morseCode = MorseCode.getMorseCode(inputText);
            textView.setText("변환한 모스부호 : " + morseCode);
            flashMorseCode(morseCode);
            Toast.makeText(getApplicationContext(), morseCode, Toast.LENGTH_LONG).show();
        });
    }

    private void flashMorseCode(String morseCode) {
        Handler handler = new Handler(Looper.getMainLooper());
        long delay = 0;

        for (char c : morseCode.toCharArray()) {
            if (c == '.') {
                handler.postDelayed(() -> toggleFlash(true), delay);
                delay += 200;
                handler.postDelayed(() -> toggleFlash(false), delay);
                delay += 200;
            } else if (c == '-') {
                handler.postDelayed(() -> toggleFlash(true), delay);
                delay += 600;
                handler.postDelayed(() -> toggleFlash(false), delay);
                delay += 200;
            } else if (c == ' ') {
                delay += 600;
            }
        }
    }

    private void toggleFlash(boolean state) {
        try {
            cameraManager.setTorchMode(cameraId, state);
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }

    // 모스 부호 변환 클래스
    private static class MorseCode {
        private static final Map<Character, String> morseCodeMap = new HashMap<>();

        static {
            morseCodeMap.put('A', ".-");
            morseCodeMap.put('B', "-...");
            morseCodeMap.put('C', "-.-.");
            morseCodeMap.put('D', "-..");
            morseCodeMap.put('E', ".");
            morseCodeMap.put('F', "..-.");
            morseCodeMap.put('G', "--.");
            morseCodeMap.put('H', "....");
            morseCodeMap.put('I', "..");
            morseCodeMap.put('J', ".---");
            morseCodeMap.put('K', "-.-");
            morseCodeMap.put('L', ".-..");
            morseCodeMap.put('M', "--");
            morseCodeMap.put('N', "-.");
            morseCodeMap.put('O', "---");
            morseCodeMap.put('P', ".--.");
            morseCodeMap.put('Q', "--.-");
            morseCodeMap.put('R', ".-.");
            morseCodeMap.put('S', "...");
            morseCodeMap.put('T', "-");
            morseCodeMap.put('U', "..-");
            morseCodeMap.put('V', "...-");
            morseCodeMap.put('W', ".--");
            morseCodeMap.put('X', "-..-");
            morseCodeMap.put('Y', "-.--");
            morseCodeMap.put('Z', "--..");
            morseCodeMap.put('1', ".----");
            morseCodeMap.put('2', "..---");
            morseCodeMap.put('3', "...--");
            morseCodeMap.put('4', "....-");
            morseCodeMap.put('5', ".....");
            morseCodeMap.put('6', "-....");
            morseCodeMap.put('7', "--...");
            morseCodeMap.put('8', "---..");
            morseCodeMap.put('9', "----.");
            morseCodeMap.put('0', "-----");
        }

        public static String getMorseCode(String input) {
            StringBuilder morseCode = new StringBuilder();
            for (char c : input.toUpperCase().toCharArray()) {
                if (morseCodeMap.containsKey(c)) {
                    morseCode.append(morseCodeMap.get(c)).append(" ");
                } else {
                    morseCode.append(" ");
                }
            }
            return morseCode.toString();
        }
    }
}