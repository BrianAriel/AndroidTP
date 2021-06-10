package com.example.proyecto;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONException;
import org.json.JSONObject;

public class SegundaActivityLogin extends AppCompatActivity {

    EditText usuario, contrasenia;
    Button botonIngresar;
    TextView registrarse;
    Boolean resultadoConexion = false;
    ThreadAsyncTask task;
    Intent intentLogin, intentRegistro;

    String stringUsuario = "", stringContrasenia = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_segunda_login);

        usuario = findViewById(R.id.editTextUsuario);
        contrasenia = findViewById(R.id.editTextContraseña);
        botonIngresar = findViewById(R.id.buttonIngresar);
        registrarse = findViewById(R.id.textViewRegistrar);

        botonIngresar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stringUsuario = usuario.getText().toString();
                stringContrasenia = contrasenia.getText().toString();
                if(!stringUsuario.equals("") && !stringContrasenia.equals(""))
                    task.execute();
            }
        });

        registrarse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lanzarActivityRegistro();
            }
        });

        task = new ThreadAsyncTask();
    }

    private void lanzarActivityRegistro() {
        intentRegistro = new Intent(this, SegundaActivityRegistro.class);
        this.startActivity(intentRegistro);
    }

    private class ThreadAsyncTask extends AsyncTask<Void,Void,Boolean> {

        @Override
        protected void onPreExecute() {
            //...
        }

        @Override
        protected Boolean doInBackground(Void... voids) {
            return hayConexion();
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            //...
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            resultadoConexion = aBoolean;
            enviarPeticion();
        }
    }

    private void enviarPeticion() {
        if(resultadoConexion){
            String uri = "http://so-unlam.net.ar";
            String endpoint = "/api/api/login";
            JSONObject req = new JSONObject();
            try {
                req.put("email",stringUsuario);
                req.put("password",stringContrasenia);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            intentLogin = new Intent(this, ServiceHTTPLogin.class);
            intentLogin.putExtra("uri",uri);
            intentLogin.putExtra("endpoint",endpoint);
            intentLogin.putExtra("jsonObject",req.toString());
            startService(intentLogin);
        } else {
            Toast.makeText(getApplicationContext(), "No existe conexion a internet", Toast.LENGTH_LONG).show();
        }
    }

    private boolean hayConexion () {
        return internetConectado() || redConectado();
    }

    private boolean redConectado() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopService(intentLogin);
    }
}
