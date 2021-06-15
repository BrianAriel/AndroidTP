package com.example.proyecto;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class ActivityFuncional extends AppCompatActivity {
    Button botonTomarTemperatura;
    Intent intentTomarTemperatura, intentPrevio, intentService;
    String access_token,refresh_token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_funcional);

        intentPrevio = getIntent();
        access_token = intentPrevio.getStringExtra("access_token");
        refresh_token = intentPrevio.getStringExtra("refresh_token");

        intentService = new Intent(this,ServiceCheckToken.class);
        intentService.putExtra("refresh_token",refresh_token);

        botonTomarTemperatura = findViewById(R.id.buttonTomarTemp);

        botonTomarTemperatura.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                lanzarActivityTomarTemperatura();
            }
        });

        startService(intentService);
    }
    void lanzarActivityTomarTemperatura(){
        intentTomarTemperatura = new Intent (this,TomarTemperatura.class);
        this.startActivity(intentTomarTemperatura);
    }
}
