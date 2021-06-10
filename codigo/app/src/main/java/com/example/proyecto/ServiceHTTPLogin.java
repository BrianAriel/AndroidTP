package com.example.proyecto;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class ServiceHTTPLogin extends Service {

    JSONObject req;

    public ServiceHTTPLogin() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent,flags,startId);
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

            DataOutputStream transmision = new DataOutputStream(con.getOutputStream());
            transmision.writeBytes(req.toString());
            transmision.flush();
            transmision.close();

            String access_token = parsearResponse(con);
            if(!access_token.equals("")) {
                Intent intentRegistro = new Intent(this, ServiceHTTPRegistrarEvento.class);
                intentRegistro.putExtra("access_token", access_token);
                startActivity(intentRegistro);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return super.onStartCommand(intent, flags, startId);
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
}
