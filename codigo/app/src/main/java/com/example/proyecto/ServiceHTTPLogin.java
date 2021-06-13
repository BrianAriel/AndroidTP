package com.example.proyecto;

import android.app.IntentService;
import android.content.Intent;
import android.os.Handler;
import android.widget.Toast;

import androidx.annotation.Nullable;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class ServiceHTTPLogin extends IntentService {

    private static final String ETIQUETA = ServiceHTTPLogin.class.getSimpleName();
    JSONObject req;
    Intent intentRegistroEvento, intentFuncional;
    CheckConexion chequeaConexion = new CheckConexion(this);

    public ServiceHTTPLogin (){
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
            con.setRequestProperty("Content-Type", "application/json; utf-8");

            if (intent.hasExtra("jsonObject")) {
                req = new JSONObject(intent.getStringExtra("jsonObject"));
            }

            if(chequeaConexion.hayConexion()) {
                DataOutputStream transmision = new DataOutputStream(con.getOutputStream());
                transmision.writeBytes(req.toString());
                transmision.flush();
                transmision.close();

                String access_token = parsearResponse(con);
                if (!access_token.equals("")) {
                    intentRegistroEvento = new Intent(this, ServiceHTTPRegistrarEvento.class);
                    intentRegistroEvento.putExtra("access_token", access_token);
                    intentFuncional = new Intent(this, ActivityFuncional.class);
                    intentFuncional.putExtra("access_token",access_token);
                    startService(intentRegistroEvento);
                    startActivity(intentFuncional);
                } else {
                    Handler mainHandler = new Handler(getMainLooper());
                    mainHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(), "El login no fue exitoso", Toast.LENGTH_LONG).show();
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

    private String parsearResponse(HttpURLConnection con) {
        try {
            if(con.getResponseCode() == 200){
                BufferedReader br = new BufferedReader(new InputStreamReader((con.getInputStream())));
                StringBuilder sb = new StringBuilder();
                String output;
                br.readLine();
                output = br.readLine();
                sb.append(output);
                br.close();
                return sb.toString();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }



    @Override
    public void onDestroy() {
        super.onDestroy();
        if(intentRegistroEvento != null)
            stopService(intentRegistroEvento);
    }
}
