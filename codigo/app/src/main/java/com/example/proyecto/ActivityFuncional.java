package com.example.proyecto;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class ActivityFuncional extends AppCompatActivity {
    Button botonTomarTemperatura;
    Intent intentTomarTemperatura;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_funcional);
        botonTomarTemperatura = findViewById(R.id.buttonTomarTemp);

        botonTomarTemperatura.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                lanzarActivityTomarTemperatura();
            }
        });
    }
    void lanzarActivityTomarTemperatura(){
        intentTomarTemperatura = new Intent (this,TomarTemperatura.class);
        this.startActivity(intentTomarTemperatura);
    }
}
