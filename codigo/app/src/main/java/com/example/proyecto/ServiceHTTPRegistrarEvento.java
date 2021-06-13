package com.example.proyecto;

import android.app.IntentService;
import android.content.Intent;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class ServiceHTTPRegistrarEvento extends IntentService {

    private static final String ETIQUETA = ServiceHTTPRegistrarEvento.class.getSimpleName();
    CheckConexion chequeaConexion = new CheckConexion(this);

    public ServiceHTTPRegistrarEvento() {
        super(ETIQUETA);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        String url = "http://so-unlam.net.ar/api/api/event";
        String access_token = "Bearer " + intent.getStringExtra("access_token");
        try {
            URL obj = new URL(url);
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();
            con.setDoInput(true);
            con.setDoOutput(true);
            con.setRequestMethod("POST");
            con.setRequestProperty("Content-Type", "application/json; utf-8");
            con.setRequestProperty("Authorization",access_token);

            JSONObject req = new JSONObject();
            req.put("env","TEST");
            req.put("type_events","Login usuario");
            req.put("description","Se registra en el servidor un login de usuario");

            if(chequeaConexion.hayConexion()) {
                DataOutputStream transmision = new DataOutputStream(con.getOutputStream());
                transmision.writeBytes(req.toString());
                transmision.flush();
                transmision.close();
            } else {
                Handler mainHandler = new Handler(getMainLooper());
                mainHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(), "No existe conexion a internet", Toast.LENGTH_LONG).show();
                    }
                });
            }
            Log.i("CODE RESPONSE",String.valueOf(con.getResponseCode()));
            stopSelf();
        } catch (MalformedURLException e) {
            e.printStackTrace();
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