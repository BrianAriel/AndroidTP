package com.example.proyecto;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Vibrator;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class TomarTemperatura extends AppCompatActivity {
    SensorManager sm;
    SensorEventListener listener;
    Sensor light;
    Button botonMedir;
    TextView textTemperatura;
    String temperatura;
    float medicion;
    Vibrator vibrador;

    Handler h = new Handler() {
        public void handleMessage(Message msg){
            //Toast.makeText(getApplicationContext(), "Temperatura: " + temperatura, Toast.LENGTH_LONG).show();
            textTemperatura.setText(temperatura);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tomar_temperatura);
        botonMedir = findViewById(R.id.buttonMedir);
        vibrador = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        textTemperatura = findViewById(R.id.textViewTemperatura);
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
                            vibrador.vibrate(300);//REVISAR
                            Log.i("Temp registrada: ",temperatura);
                            h.sendEmptyMessage(0);//ejecuto el handler

                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                };

                mySplash.start();//Ejecuto el thread para que tome la temperatura


            }
        });

    }
    private String convertirTemperatura(float medicion){
        float factorConversion = 40000/15;
        return String.format("%.02f",(30 + (medicion/factorConversion)));
    }



    @Override
    protected void onPause() {
        super.onPause();
        sm.unregisterListener(listener, light);
    }
}
