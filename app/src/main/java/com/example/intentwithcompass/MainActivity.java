package com.example.intentwithcompass;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import org.osmdroid.config.Configuration;
import org.osmdroid.wms.BuildConfig;

import java.io.File;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // OsmDroid 캐시 설정
        Configuration.getInstance().setUserAgentValue(BuildConfig.APPLICATION_ID);
        File osmDroidBasePath = new File(getCacheDir(), "osmdroid");
        File osmDroidTileCache = new File(osmDroidBasePath, "tile");
        Configuration.getInstance().setOsmdroidBasePath(osmDroidBasePath);
        Configuration.getInstance().setOsmdroidTileCache(osmDroidTileCache);
        setContentView(R.layout.activity_main);

        Button button = findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 화면전환
                Intent intent = new Intent(MainActivity.this, CompassActivity.class);
                startActivity(intent);
            }
        });

        Button button2 = findViewById(R.id.button2);
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 화면전환
                Intent intent = new Intent(MainActivity.this, MossActitvity.class);
                startActivity(intent);
            }
        });

        Button button3 = findViewById(R.id.button3);
        button3.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                // 화면전환
                Intent intent = new Intent(MainActivity.this, GpsActivity.class);
                startActivity(intent);
            }
        });

        Button button4 = findViewById(R.id.button4);
        button4.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                // 화면전환
                Intent intent = new Intent(MainActivity.this, User_Info.class);
                startActivity(intent);
            }
        });

        TextView txt = findViewById(R.id.textView2);
        String[] randomTxT = getResources().getStringArray(R.array.randomText);
        Random random = new Random();
        int n = random.nextInt(randomTxT.length);
        txt.setText(randomTxT[n]);
    }
}
