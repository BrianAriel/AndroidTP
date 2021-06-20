package com.example.proyecto;

import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import static java.lang.Math.round;

public class MainActivity extends AppCompatActivity {

    IntentFilter intentF;
    Intent estadoBateria;
    int nivelBateria, escala;
    float porcentajeBateria;
    private static final int ESCALA = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        intentF = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        estadoBateria = getApplicationContext().registerReceiver(null, intentF);
        nivelBateria = estadoBateria.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
        escala = estadoBateria.getIntExtra(BatteryManager.EXTRA_SCALE, -1);
        porcentajeBateria = nivelBateria * ESCALA / (float)escala;
        Toast.makeText(getApplicationContext(), "Porcentaje bateria: " + round(porcentajeBateria) + "%", Toast.LENGTH_LONG).show();

        Intent intent = new Intent(this, SplashScreen.class);
        this.startActivity(intent);
    }
}
