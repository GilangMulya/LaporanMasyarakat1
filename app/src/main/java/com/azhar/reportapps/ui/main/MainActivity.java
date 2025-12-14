package com.azhar.reportapps.ui.main;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import com.azhar.reportapps.R;
import com.azhar.reportapps.ui.history.HistoryActivity;
import com.azhar.reportapps.ui.report.ReportActivity;
import com.azhar.reportapps.utils.Constant;

import java.io.IOException;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import im.delight.android.location.SimpleLocation;

public class MainActivity extends AppCompatActivity {

    int REQ_PERMISSION = 100;
    double strCurrentLatitude;
    double strCurrentLongitude;
    String strCurrentLocation, strTitle;
    SimpleLocation simpleLocation;

    CardView cvPemadam, cvAmbulance, cvBencana, cvHistory;
    TextView tvGreeting;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setStatusBar();
        setPermission();
        setLocation();
        setInitLayout();
        setCurrentLocation();
        setGreeting(); // Tambahan Sapaan
    }

    // --- FITUR BARU: Sapaan Waktu ---
    private void setGreeting() {
        tvGreeting = findViewById(R.id.tvGreeting);
        Calendar calendar = Calendar.getInstance();
        int timeOfDay = calendar.get(Calendar.HOUR_OF_DAY);

        if (timeOfDay >= 0 && timeOfDay < 12) {
            tvGreeting.setText("Selamat Pagi, Warga!");
        } else if (timeOfDay >= 12 && timeOfDay < 15) {
            tvGreeting.setText("Selamat Siang, Warga!");
        } else if (timeOfDay >= 15 && timeOfDay < 18) {
            tvGreeting.setText("Selamat Sore, Warga!");
        } else {
            tvGreeting.setText("Selamat Malam, Warga!");
        }
    }

    private void setInitLayout() {
        cvPemadam = findViewById(R.id.cvPemadam);
        cvAmbulance = findViewById(R.id.cvAmbulance);
        cvBencana = findViewById(R.id.cvBencana);
        cvHistory = findViewById(R.id.cvHistory);

        // --- TAMBAHAN ANIMASI MUNCUL ---
        // Pastikan file res/anim/item_animation_fall_down.xml sudah ada (sama seperti di HistoryAdapter)
        Animation anim1 = AnimationUtils.loadAnimation(this, R.anim.item_animation_fall_down);
        anim1.setStartOffset(100); // Delay dikit
        cvPemadam.startAnimation(anim1);

        Animation anim2 = AnimationUtils.loadAnimation(this, R.anim.item_animation_fall_down);
        anim2.setStartOffset(200);
        cvAmbulance.startAnimation(anim2);

        Animation anim3 = AnimationUtils.loadAnimation(this, R.anim.item_animation_fall_down);
        anim3.setStartOffset(300);
        cvBencana.startAnimation(anim3);

        Animation anim4 = AnimationUtils.loadAnimation(this, R.anim.item_animation_fall_down);
        anim4.setStartOffset(400);
        cvHistory.startAnimation(anim4);
        // -----------------------------

        cvPemadam.setOnClickListener(v -> {
            strTitle = "Laporan Kebakaran";
            Intent intent = new Intent(MainActivity.this, ReportActivity.class);
            intent.putExtra(ReportActivity.DATA_TITLE, strTitle);
            startActivity(intent);
        });

        cvAmbulance.setOnClickListener(v -> {
            strTitle = "Laporan Medis";
            Intent intent = new Intent(MainActivity.this, ReportActivity.class);
            intent.putExtra(ReportActivity.DATA_TITLE, strTitle);
            startActivity(intent);
        });

        cvBencana.setOnClickListener(v -> {
            strTitle = "Laporan Bencana Alam";
            Intent intent = new Intent(MainActivity.this, ReportActivity.class);
            intent.putExtra(ReportActivity.DATA_TITLE, strTitle);
            startActivity(intent);
        });

        cvHistory.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, HistoryActivity.class);
            startActivity(intent);
        });
    }

    private void setLocation() {
        simpleLocation = new SimpleLocation(this);
        if (!simpleLocation.hasLocationEnabled()) {
            SimpleLocation.openSettings(this);
        }
        strCurrentLatitude = simpleLocation.getLatitude();
        strCurrentLongitude = simpleLocation.getLongitude();
        strCurrentLocation = strCurrentLatitude + "," + strCurrentLongitude;
    }

    private void setCurrentLocation() {
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        try {
            List<Address> addressList = geocoder.getFromLocation(strCurrentLatitude, strCurrentLongitude, 1);
            if (addressList != null && addressList.size() > 0) {
                Constant.lokasiPengaduan = addressList.get(0).getAddressLine(0);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void setPermission() {
        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{
                    Manifest.permission.ACCESS_FINE_LOCATION}, REQ_PERMISSION);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        for (int grantResult : grantResults) {
            if (grantResult == PackageManager.PERMISSION_GRANTED) {
                Intent intent = getIntent();
                finish();
                startActivity(intent);
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQ_PERMISSION && resultCode == RESULT_OK) {
        }
    }

    private void setStatusBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE |
                    View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        }

        if (Build.VERSION.SDK_INT >= 21) {
            setWindowFlag(this, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, false);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
    }

    public static void setWindowFlag(Activity activity, final int bits, boolean on) {
        Window window = activity.getWindow();
        WindowManager.LayoutParams layoutParams = window.getAttributes();
        if (on) {
            layoutParams.flags |= bits;
        } else {
            layoutParams.flags &= ~bits;
        }
        window.setAttributes(layoutParams);
    }

    // Lifecycle SimpleLocation
    @Override
    protected void onResume() {
        super.onResume();
        if (simpleLocation != null) { simpleLocation.beginUpdates(); }
    }

    @Override
    protected void onPause() {
        if (simpleLocation != null) { simpleLocation.endUpdates(); }
        super.onPause();
    }
}