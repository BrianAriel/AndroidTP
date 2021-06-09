package com.example.proyecto;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class SegundaActivity extends AppCompatActivity {

    EditText usuario, contrasenia;
    Button botonIngresar;
    TextView registrarse;
    Boolean resultadoConexion = false;
    ThreadAsyncTask task;

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
                task.execute();
            }
        });

        task = new ThreadAsyncTask();
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
            Toast.makeText(getApplicationContext(), "Existe conexion a internet", Toast.LENGTH_LONG).show();
            //Aca deberia enviar la peticion al servidor
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
}
