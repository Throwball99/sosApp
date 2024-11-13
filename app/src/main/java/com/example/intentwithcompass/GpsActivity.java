package com.example.intentwithcompass;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import org.osmdroid.api.IMapController;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;

public class GpsActivity extends AppCompatActivity {

    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1; // 위치 권한 요청 코드
    private boolean locationPermissionGranted; // 위치 권한 부여 여부

    TextView mylocation; // 현재 위치를 표시할 TextView
    MapView mapView; // 지도 뷰
    IMapController mapController; // 지도 컨트롤러

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gps);

        mylocation = findViewById(R.id.textView); // TextView 초기화
        mapView = findViewById(R.id.mapview); // MapView 초기화
        mapView.setTileSource(TileSourceFactory.MAPNIK); // 지도 타일 소스 설정
        mapView.setMultiTouchControls(true); // 멀티터치 제어 활성화

        mapController = mapView.getController(); // MapController 초기화
        mapController.setZoom(15.0); // 지도 줌 레벨 설정

        Button button_mylocation = findViewById(R.id.Button); // 위치 확인 버튼 초기화
        button_mylocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getLocationPermission(); // 위치 권한 확인 및 요청
                if (locationPermissionGranted) {
                    currentMyLocation(); // 권한이 부여되면 현재 위치 확인
                }
            }
        });
    }

    private void getLocationPermission() {
        if (ContextCompat.checkSelfPermission(this.getApplicationContext(),
                Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            locationPermissionGranted = true; // 권한이 이미 부여된 경우
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION); // 권한 요청
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        locationPermissionGranted = false; // 초기값 false
        switch (requestCode) {
            case PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    locationPermissionGranted = true; // 권한이 부여된 경우
                }
            }
        }
        if (locationPermissionGranted) {
            currentMyLocation(); // 권한이 부여되면 현재 위치 확인
        }
    }

    private void currentMyLocation() {
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE); // LocationManager 초기화

        GPSListener gpsListener = new GPSListener(); // GPSListener 초기화
        long minTime = 10000; // 최소 시간 간격
        float minDistance = 0; // 최소 거리 간격

        try {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return; // 권한이 부여되지 않은 경우 종료
            }
            locationManager.requestSingleUpdate(
                    LocationManager.GPS_PROVIDER,
                    gpsListener,
                    null); // GPS_PROVIDER를 사용한 위치 요청

            locationManager.requestSingleUpdate(
                    LocationManager.NETWORK_PROVIDER,
                    gpsListener,
                    null); // NETWORK_PROVIDER를 사용한 위치 요청

        } catch (SecurityException ex) {
            ex.printStackTrace(); // 예외 처리
        }

        Toast.makeText(getApplicationContext(), "위치 확인 시작", Toast.LENGTH_SHORT).show(); // 위치 확인 시작 토스트 메시지
    }

    private void updateMapLocation(double latitude, double longitude) {
        GeoPoint startPoint = new GeoPoint(latitude, longitude); // 새로운 위치 설정
        mapController.setCenter(startPoint); // 지도의 중심 설정

        Marker startMarker = new Marker(mapView); // 새로운 마커 생성
        startMarker.setPosition(startPoint); // 마커 위치 설정
        startMarker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM); // 마커 앵커 설정
        mapView.getOverlays().add(startMarker); // 마커 추가
        mapView.invalidate(); // 지도 새로 고침
    }

    private class GPSListener implements LocationListener {
        public void onLocationChanged(Location location) {
            Double latitude = location.getLatitude(); // 위도 가져오기
            Double longitude = location.getLongitude(); // 경도 가져오기

            String msg = "Latitude : " + latitude + "\nLongitude : " + longitude;
            Log.i("GPSListener", msg); // 로그 메시지 출력

            mylocation.setText("내 위치 : " + latitude + ", " + longitude); // 위치 텍스트 업데이트
            updateMapLocation(latitude, longitude); // 지도 위치 업데이트
            Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show(); // 토스트 메시지 표시
        }
    }
}
