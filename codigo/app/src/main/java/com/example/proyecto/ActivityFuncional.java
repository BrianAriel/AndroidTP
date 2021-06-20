package com.example.proyecto;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class ActivityFuncional extends AppCompatActivity {
    private static final int PERMISSION_REQUEST_CALL = 1;

    Button botonTomarTemperatura, botonHistorial, botonLlamar;
    Intent intentTomarTemperatura, intentPrevio, intentService, intentHistorial;
    String access_token, refresh_token, telEmergencia = "1134934773";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_funcional);

        intentPrevio = getIntent();
        access_token = intentPrevio.getStringExtra("access_token");
        refresh_token = intentPrevio.getStringExtra("refresh_token");

        intentService = new Intent(this, ServiceCheckToken.class);
        intentService.putExtra("refresh_token", refresh_token);

        botonTomarTemperatura = findViewById(R.id.buttonTomarTemp);
        botonHistorial = findViewById(R.id.buttonHistorial);
        botonLlamar = findViewById(R.id.buttonLlamar);

        botonTomarTemperatura.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lanzarActivityTomarTemperatura();
            }
        });

        botonHistorial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lanzarActivityHistorial();
            }
        });

        botonLlamar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                realizarLlamada();
            }
        });

        if (!isMyServiceRunning(ServiceCheckToken.class))
            startService(intentService);
    }

    private void lanzarActivityTomarTemperatura(){
        intentTomarTemperatura = new Intent (this,TomarTemperatura.class);
        this.startActivity(intentTomarTemperatura);
    }

    private void lanzarActivityHistorial(){
        intentHistorial = new Intent (this,ActivityHistorial.class);
        this.startActivity(intentHistorial);
    }

    private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    private void realizarLlamada(){
        Intent intentLlamada = new Intent(Intent.ACTION_CALL);
        intentLlamada.setData(Uri.parse("tel:" + telEmergencia));

        if(chequearPermisos()){
            startActivity(intentLlamada);
        }
    }

    private boolean chequearPermisos() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CALL_PHONE}, PERMISSION_REQUEST_CALL);
        }
        return ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED;
    }
}
