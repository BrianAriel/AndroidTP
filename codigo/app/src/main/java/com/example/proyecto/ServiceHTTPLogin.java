package com.example.proyecto;

import android.app.IntentService;
import android.content.Intent;
import android.os.Handler;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.view.ContentInfoCompat;

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
    CheckConexion chequeaConexion;
    String access_token, refresh_token;
    String[] parsedResponse;
    private static final int STATUS_SUCCESS = 200;

    public ServiceHTTPLogin (){
        super(ETIQUETA);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        String url = intent.getStringExtra("uri") + intent.getStringExtra("endpoint");
        chequeaConexion = new CheckConexion(this);
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

                parsedResponse = parsearResponse(con);
                access_token = parsedResponse[0];
                refresh_token = parsedResponse[1];

                if (!access_token.equals("")) {
                    intentRegistroEvento = new Intent(this, ServiceHTTPRegistrarEvento.class);
                    intentRegistroEvento.putExtra("access_token", access_token);

                    intentFuncional = new Intent(this, ActivityFuncional.class);
                    intentFuncional.putExtra("access_token",access_token);
                    intentFuncional.putExtra("refresh_token",refresh_token);

                    intentFuncional.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

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
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
    }

    private String[] parsearResponse(HttpURLConnection con) {
        String[] arrayReturn = new String[2];
        try {
            if(con.getResponseCode() == STATUS_SUCCESS){
                BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
                StringBuffer response = new StringBuffer();
                String inputLine;

                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();

                JSONObject jsonResponse = new JSONObject(response.toString());
                arrayReturn[0] = jsonResponse.getString("token");
                arrayReturn[1] = jsonResponse.getString("token_refresh");
                
                return arrayReturn;
            }
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
        arrayReturn[0] = "";
        arrayReturn[1] = "";
        return arrayReturn;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(intentRegistroEvento != null)
            stopService(intentRegistroEvento);
    }
}
