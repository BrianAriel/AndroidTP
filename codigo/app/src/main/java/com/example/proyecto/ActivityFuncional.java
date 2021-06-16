package com.example.proyecto;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class ActivityFuncional extends AppCompatActivity {
    Button botonTomarTemperatura, botonHistorial;
    Intent intentTomarTemperatura, intentPrevio, intentService, intentHistorial;
    String access_token, refresh_token;

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
}
