package com.example.proyecto;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Vibrator;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;

public class TomarTemperatura extends AppCompatActivity {
    private static final int UMBRAL_AGITACION = 1600;
    private static final int PERMISSION_REQUEST_CALL = 1;

    SensorManager smLuz, smAcelerometro;
    SensorEventListener listenerLuz, listenerAcelerometro;
    Sensor light, acelerometro;

    Button botonMedir;
    TextView textTemperatura;
    String temperatura = "";

    float medicion, x, y, z, ultX, ultY, ultZ;
    long ultActualizacion;
    Vibrator vibrador;
    SharedPreferences sharedPref;
    SimpleDateFormat formatter;

    Handler h = new Handler() {
        public void handleMessage(Message msg){
            textTemperatura.setText(temperatura.substring(0,5));
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tomar_temperatura);

        formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm");

        sharedPref = getApplicationContext().getSharedPreferences(getString(R.string.archivo_preferences), Context.MODE_PRIVATE);

        vibrador = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        botonMedir = findViewById(R.id.buttonMedir);
        textTemperatura = findViewById(R.id.textViewTemperatura);

        smLuz = (SensorManager) getSystemService(SENSOR_SERVICE);
        light = smLuz.getDefaultSensor(Sensor.TYPE_LIGHT);

        smAcelerometro = (SensorManager) getSystemService(SENSOR_SERVICE);
        acelerometro = smAcelerometro.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        listenerLuz = new SensorEventListener() {

            @Override
            public void onSensorChanged(SensorEvent event) {
                medicion = event.values[0];
            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int accuracy) {
                //...
            }
        };
        smLuz.registerListener(listenerLuz, light, SensorManager.SENSOR_DELAY_GAME);

        listenerAcelerometro = new SensorEventListener() {

            @Override
            public void onSensorChanged(SensorEvent event) {
                long tiempoActual = System.currentTimeMillis();
                if((tiempoActual - ultActualizacion) > 100){

                    long diferenciaTiempo = (tiempoActual - ultActualizacion);
                    ultActualizacion = tiempoActual;

                    x = event.values[0];
                    y = event.values[1];
                    z = event.values[2];

                    float velocidad = Math.abs(x + y + z - ultX - ultY - ultZ) / diferenciaTiempo * 10000;

                    if (velocidad > UMBRAL_AGITACION) {
                        if(chequearSiLlamo()){
                            realizarLlamada();
                        }
                    }
                    ultX = x;
                    ultY = y;
                    ultZ = z;
                }
            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int accuracy) {
                //...
            }
        };
        smAcelerometro.registerListener(listenerAcelerometro, acelerometro, SensorManager.SENSOR_DELAY_GAME);

        botonMedir.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                //aquí debe esperar 2 minutos y tomar la temperatura (luz)

                Thread mySplash = new Thread() {
                    @Override
                    public void run() {
                        super.run();
                        try {
                            sleep(3000);
                            temperatura = convertirTemperatura(medicion);
                            vibrador.vibrate(300);

                            h.sendEmptyMessage(0);//ejecuto el handler
                            guardarMedicion(temperatura,new Date(System.currentTimeMillis()));

                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                };

                mySplash.start();//Ejecuto el thread para que tome la temperatura

            }
        });

    }

    //No quiero que temperatura cambie su valor cuando hago el check, en caso de que se agite cuando se registra una nueva temperatura
    private synchronized boolean chequearSiLlamo(){
        if(!temperatura.equals("") && Float.parseFloat(temperatura) > 37.5f){
            return true;
        }
        return false;
    }

    private void realizarLlamada(){
        Intent intentLlamada = new Intent(Intent.ACTION_CALL);
        intentLlamada.setData(Uri.parse("tel:1134934773"));

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

    private synchronized void guardarMedicion(String temp, Date date){
        String fecha = formatter.format(date);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(fecha,"Fecha y hora: " + fecha + " Temperatura: " + temp);
        editor.apply();
    }

    private String convertirTemperatura(float medicion){
        float factorConversion = 5000/15;
        return String.valueOf(36 + (medicion / factorConversion));
    }

    @Override
    protected void onPause() {
        super.onPause();
        smLuz.unregisterListener(listenerLuz, light);
    }
}
