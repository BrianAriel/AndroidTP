package com.example.proyecto;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class ServiceHTTPRegistrarEvento extends Service {

    public ServiceHTTPRegistrarEvento() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
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
            req.put("type_events","Registro usuario");
            req.put("description","Se registra en el servidor la ocurrencia de un login");

            DataOutputStream transmision = new DataOutputStream(con.getOutputStream());
            transmision.writeBytes(req.toString());
            transmision.flush();
            transmision.close();

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return super.onStartCommand(intent, flags, startId);
    }
}
