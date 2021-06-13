package com.example.proyecto;

import androidx.appcompat.app.AppCompatActivity;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class TomarTemperatura extends AppCompatActivity {
    SensorManager sm;
    SensorEventListener listener;
    Sensor light;
    Button botonMedir;
    float temperatura;
    float medicion;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tomar_temperatura);
        botonMedir = findViewById(R.id.buttonMedir);
        sm = (SensorManager) getSystemService(SENSOR_SERVICE);
        light = sm.getDefaultSensor(Sensor.TYPE_LIGHT);
        listener = new SensorEventListener() {

            @Override
            public void onAccuracyChanged(Sensor sensor, int accuracy) {

            }

            @Override
            public void onSensorChanged(SensorEvent event) {
                medicion = event.values[0];
            }
        };

        sm.registerListener(listener, light, SensorManager.SENSOR_DELAY_FASTEST);

        botonMedir.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                //aquí debe esperar 2 minutos y tomar la temperatura (luz)
                Thread mySplash = new Thread() {
                    @Override
                    public void run() {
                        super.run();
                        try {
                            Log.i("Tomando temperatura...",String.valueOf(temperatura));
                            sleep(3000);
                            temperatura = convertirTemperatura(medicion);
                            Log.i("Temp registrada: ",String.valueOf(temperatura));

                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                };
                mySplash.start();//pasan los 3 segundos


            }
        });

    }
    private float convertirTemperatura(float medicion){
        float factorConversion = 40000/15;
        float temperatura = 30 + (medicion/factorConversion);
        return temperatura;
    }
    @Override
    protected void onPause() {
        super.onPause();
        sm.unregisterListener(listener, light);
    }
}
