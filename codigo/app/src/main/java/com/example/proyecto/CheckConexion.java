package com.example.proyecto;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class CheckConexion {

    Context contexto;

    public CheckConexion (Context mContext){
        contexto = mContext;
    }

    boolean hayConexion() {
        return internetConectado() || redConectado();
    }

    private boolean redConectado() {
        ConnectivityManager connectivityManager = (ConnectivityManager) contexto.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    private boolean internetConectado() {
        try {
            String command = "ping -c 1 google.com";
            return (Runtime.getRuntime().exec(command).waitFor() == 0);
        } catch (Exception e) {
            return false;
        }
    }
}
