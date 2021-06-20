package com.example.proyecto;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
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
import android.widget.ImageView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;

public class TomarTemperatura extends AppCompatActivity {
    private static final int UMBRAL_AGITACION = 1600, PERMISSION_REQUEST_CALL = 1, MAX_LENGTH = 5, INTERVALO = 100;
    private static final int SLEEP_TIME = 10000, VIBRATE_TIME = 300, TEMPERATURA_BASE = 36, ESCALA = 10000;
    private static final float UMBRAL_TEMPERATURA = 37.5f, FACTOR_CONVERSION = 5000/15;
    private static final String TEL_EMERGENCIA = "120";

    SensorManager smLuz, smAcelerometro;
    SensorEventListener listenerLuz, listenerAcelerometro;
    Sensor light, acelerometro;

    Button botonMedir, botonRegresar, botonMedirOtra;
    TextView textTemperatura, textInstruc;
    ImageView imageTermometro, imageResult;
    String temperatura = "";

    float medicion, x, y, z, ultX, ultY, ultZ;
    long ultActualizacion;
    Vibrator vibrador;
    SharedPreferences sharedPref;
    SimpleDateFormat formatter;

    @SuppressLint("HandlerLeak")
    Handler h = new Handler() {
        public void handleMessage(Message msg){
            if(temperatura.length() >= MAX_LENGTH)
                textTemperatura.setText(temperatura.substring(0,5));
            else
                textTemperatura.setText(temperatura);

            mostrarResultado();
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
        botonRegresar = findViewById(R.id.buttonRegresar);
        botonMedirOtra = findViewById(R.id.buttonMedirOtra);
        textTemperatura = findViewById(R.id.textViewTemperatura);
        textInstruc = findViewById(R.id.textInstruc);
        imageTermometro = findViewById(R.id.imageTermometro);
        imageResult = findViewById(R.id.imageResult);


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
                if((tiempoActual - ultActualizacion) > INTERVALO){

                    long diferenciaTiempo = (tiempoActual - ultActualizacion);
                    ultActualizacion = tiempoActual;

                    x = event.values[0];
                    y = event.values[1];
                    z = event.values[2];

                    float velocidad = Math.abs(x + y + z - ultX - ultY - ultZ) / diferenciaTiempo * ESCALA;

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
                            sleep(SLEEP_TIME);
                            temperatura = convertirTemperatura(medicion);
                            vibrador.vibrate(VIBRATE_TIME);

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

        botonRegresar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        botonMedirOtra.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ocultarResultado();
            }
        });

    }

    //No quiero que temperatura cambie su valor cuando hago el check, en caso de que se agite cuando se registra una nueva temperatura
    private synchronized boolean chequearSiLlamo(){
        if(!temperatura.equals("") && Float.parseFloat(temperatura) > UMBRAL_TEMPERATURA){
            return true;
        }
        return false;
    }

    private void realizarLlamada(){
        Intent intentLlamada = new Intent(Intent.ACTION_CALL);
        intentLlamada.setData(Uri.parse("tel:" + TEL_EMERGENCIA));

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
        return String.valueOf(TEMPERATURA_BASE + (medicion / FACTOR_CONVERSION));
    }

    @Override
    protected void onPause() {
        super.onPause();
        smLuz.unregisterListener(listenerLuz, light);
    }

    private void mostrarResultado(){
        textInstruc.setVisibility(View.INVISIBLE);
        imageTermometro.setVisibility(View.INVISIBLE);
        botonMedir.setVisibility(View.INVISIBLE);

        imageResult.setVisibility(View.VISIBLE);
        textTemperatura.setVisibility(View.VISIBLE);
        botonMedirOtra.setVisibility(View.VISIBLE);
    }

    private void ocultarResultado(){
        textInstruc.setVisibility(View.VISIBLE);
        imageTermometro.setVisibility(View.VISIBLE);
        botonMedir.setVisibility(View.VISIBLE);

        imageResult.setVisibility(View.INVISIBLE);
        textTemperatura.setVisibility(View.INVISIBLE);
        botonMedirOtra.setVisibility(View.INVISIBLE);
    }
}
