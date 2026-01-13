package com.priyanshu.resumegenerator;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.material.slider.Slider;

import java.util.concurrent.TimeUnit;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import okhttp3.OkHttpClient;
import yuku.ambilwarna.AmbilWarnaDialog;

public class MainActivity extends AppCompatActivity {

    private TextView tvGps, tvResume, tvFontSize;
    private Slider sliderFontSize;
    private Button btnRegenerate, btnFontColorPicker, btnBgColorPicker;

    private FusedLocationProviderClient fusedLocationClient;
    private Retrofit retrofit;
    private ApiService apiService;

    private static final int LOCATION_PERMISSION_REQUEST = 100;

    private float currentFontSize = 16f;
    private int currentFontColor = Color.BLACK;
    private int currentBgColor = Color.WHITE;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initViews();
        setupRetrofit();
        setupLocation();
        setupControls();

        loadResume("Priyanshu");
    }

    private void initViews() {
        tvGps = findViewById(R.id.tvGps);
        tvResume = findViewById(R.id.tvResume);
        tvFontSize = findViewById(R.id.tvFontSize);

        sliderFontSize = findViewById(R.id.sliderFontSize);

        btnRegenerate = findViewById(R.id.btnRegenerate);
        btnFontColorPicker = findViewById(R.id.btnFontColorPicker);
        btnBgColorPicker = findViewById(R.id.btnBgColorPicker);

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
    }

    private void setupRetrofit() {

        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS)
                .build();

        retrofit = new Retrofit.Builder()
                .baseUrl("https://expressjs-api-resume-random.onrender.com/")
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        apiService = retrofit.create(ApiService.class);
    }

    private void setupLocation() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(
                    this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    LOCATION_PERMISSION_REQUEST
            );

        } else {
            getCurrentLocation();
        }
    }

    private void getCurrentLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) return;

        fusedLocationClient.getLastLocation().addOnSuccessListener(location -> {
            if (location != null) {
                tvGps.setText(String.format("Lat: %.4f\nLon: %.4f",
                        location.getLatitude(), location.getLongitude()));
            } else {
                tvGps.setText("GPS: Not Available");
            }
        });
    }

    private void setupControls() {

        // Font size slider
        sliderFontSize.addOnChangeListener((slider, value, fromUser) -> {
            currentFontSize = value;
            tvFontSize.setText(String.format("Font Size: %.0f", value));
            tvResume.setTextSize(value);
        });

        // Font color picker dialog
        btnFontColorPicker.setOnClickListener(v -> openFontColorDialog());

        // Background color picker dialog
        btnBgColorPicker.setOnClickListener(v -> openBgColorDialog());

        // Regenerate resume
        btnRegenerate.setOnClickListener(v -> loadResume("Priyanshu"));
    }

    private void openFontColorDialog() {
        AmbilWarnaDialog dialog = new AmbilWarnaDialog(this, currentFontColor,
                new AmbilWarnaDialog.OnAmbilWarnaListener() {
                    @Override
                    public void onOk(AmbilWarnaDialog dialog, int color) {
                        currentFontColor = color;
                        tvResume.setTextColor(currentFontColor);
                        Toast.makeText(MainActivity.this, "Font Color Selected", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onCancel(AmbilWarnaDialog dialog) {
                        Toast.makeText(MainActivity.this, "Cancelled", Toast.LENGTH_SHORT).show();
                    }
                });
        dialog.show();
    }

    private void openBgColorDialog() {
        AmbilWarnaDialog dialog = new AmbilWarnaDialog(this, currentBgColor,
                new AmbilWarnaDialog.OnAmbilWarnaListener() {
                    @Override
                    public void onOk(AmbilWarnaDialog dialog, int color) {
                        currentBgColor = color;
                        tvResume.setBackgroundColor(currentBgColor);
                        Toast.makeText(MainActivity.this, "Background Color Selected", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onCancel(AmbilWarnaDialog dialog) {
                        Toast.makeText(MainActivity.this, "Cancelled", Toast.LENGTH_SHORT).show();
                    }
                });
        dialog.show();
    }

    private void loadResume(String name) {
        tvResume.setText("Loading your resume...");

        Call<Resume> call = apiService.getResume(name);
        call.enqueue(new Callback<Resume>() {
            @Override
            public void onResponse(Call<Resume> call, Response<Resume> response) {
                if (response.isSuccessful() && response.body() != null) {

                    Resume resume = response.body();

                    StringBuilder sb = new StringBuilder();
                    sb.append("👤 NAME: ").append(resume.name.toUpperCase()).append("\n\n");

                    sb.append("💻 SKILLS:\n");
                    if (resume.skills != null) {
                        for (String skill : resume.skills) {
                            sb.append("• ").append(skill).append("\n");
                        }
                    }

                    sb.append("\n🚀 PROJECTS:\n");
                    if (resume.projects != null) {
                        for (Project project : resume.projects) {
                            sb.append("• ").append(project.toString()).append("\n");
                        }
                    }

                    tvResume.setText(sb.toString());

                } else {
                    tvResume.setText("⚠️ API Error - Check internet connection");
                }

                // Apply user selected styling
                tvResume.setTextSize(currentFontSize);
                tvResume.setTextColor(currentFontColor);
                tvResume.setBackgroundColor(currentBgColor);
            }

            @Override
            public void onFailure(Call<Resume> call, Throwable t) {
                tvResume.setText("❌ Network Error\n" + t.getMessage());
                Log.e("ResumeApp", "API Failure", t);
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == LOCATION_PERMISSION_REQUEST
                && grantResults.length > 0
                && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            getCurrentLocation();
        } else {
            tvGps.setText("GPS: Permission Denied");
        }
    }
}
