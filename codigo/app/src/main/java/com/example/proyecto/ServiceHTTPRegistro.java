package com.example.proyecto;

import android.app.IntentService;
import android.content.Intent;
import android.os.Handler;
import android.widget.Toast;

import androidx.annotation.Nullable;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

public class ServiceHTTPRegistro extends IntentService {

    private static final String ETIQUETA = ServiceHTTPRegistro.class.getSimpleName();
    JSONObject req;
    Intent intentLogin;
    CheckConexion chequeaConexion = new CheckConexion(this);

    public ServiceHTTPRegistro(){
        super(ETIQUETA);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        String url = intent.getStringExtra("uri") + intent.getStringExtra("endpoint");
        try {
            URL obj = new URL(url);
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();
            con.setDoInput(true);
            con.setDoOutput(true);
            con.setRequestMethod("POST");
            con.setRequestProperty("content-type", "application/json");

            if (intent.hasExtra("jsonObject")) {
                req = new JSONObject(intent.getStringExtra("jsonObject"));
            }

            if(chequeaConexion.hayConexion()) {
                DataOutputStream transmision = new DataOutputStream(con.getOutputStream());
                transmision.writeBytes(req.toString());
                transmision.flush();
                transmision.close();

                if (con.getResponseCode() == 200) {
                    intentLogin = new Intent(this, SegundaActivityLogin.class);
                    startActivity(intentLogin);
                } else {
                    Handler mainHandler = new Handler(getMainLooper());
                    mainHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(), "El registro no fue exitoso", Toast.LENGTH_LONG).show();
                        }
                    });
                }
            } else {
                Handler mainHandler = new Handler(getMainLooper());
                mainHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(), "No existe conexion a internet", Toast.LENGTH_LONG).show();
                    }
                });
            }

            stopSelf();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
