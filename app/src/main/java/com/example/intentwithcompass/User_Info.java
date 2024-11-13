package com.example.intentwithcompass;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class User_Info extends AppCompatActivity {
    private TextView tv_result;
    private EditText edit_booldtype, edit_healthInfo1, edit_healthInfo2, edit_phonenum1, edit_phonenum2;
    private Button btn_save;
    private SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);

        tv_result = findViewById(R.id.textView);
        edit_booldtype = findViewById(R.id.editTextBloodType);
        edit_healthInfo1 = findViewById(R.id.editTextHealthInfo);
        edit_healthInfo2 = findViewById(R.id.editTextHealthInfo1);
        edit_phonenum1 = findViewById(R.id.editTextPhoneNum1);
        edit_phonenum2 = findViewById(R.id.editTextPhoneNum2);
        btn_save = findViewById(R.id.button);

        //getSharedPreferences("파일이름",'모드')
        //모드 => 0 (읽기,쓰기가능)
        //모드 => MODE_PRIVATE (이 앱에서만 사용가능)
        preferences = getSharedPreferences("UserInfo", 0);
        //저장했던 정보 불러오기
        getPreferences();

        //버튼 눌렀을 때
        btn_save.setOnClickListener(new View.OnClickListener(){
           @Override
           public void onClick(View view){
               //Editor를 preferences에 쓰겠다고 연결
               SharedPreferences.Editor editor = preferences.edit();
               //putString(KEY, VALUE)
               editor.putString("bloodtype", edit_booldtype.getText().toString());
               editor.putString("healthInfo1", edit_healthInfo1.getText().toString());
               editor.putString("healthInfo2", edit_healthInfo2.getText().toString());
               editor.putString("phoneNum1", edit_phonenum1.getText().toString());
               editor.putString("phoneNum2", edit_phonenum2.getText().toString());
               //commit, apply를 해주어야 저장이 됨
               editor.apply();
               //메소드 호출
               getPreferences();
           }
        });
    }

    private void getPreferences(){
        //getString
        tv_result.setText("혈핵형 : " + preferences.getString("bloodtype", "")
                + "\n 현재 건강정보 : " + preferences.getString("healthInfo1", "")
                + "\n 지병정보 : " + preferences.getString("healthInfo2", "")
                + "\n 첫 번째 긴급 전화번호 : " +preferences.getString("phoneNum2", "")
                + "\n 두 번째 긴급 전화번호 : " +preferences.getString("phoneNum1", ""));

    }
}