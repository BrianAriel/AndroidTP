package com.example.proyecto;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

public class SegundaActivityRegistro extends AppCompatActivity {

    EditText nombre, apellido, dni, email, password, comision, grupo;
    EditText[] vectorCampos = {nombre, apellido, dni, email, password, comision, grupo};
    Button botonRegistro;
    String[] vectorCamposObtenido;
    Boolean resultadoConexion;
    ThreadAsyncTask task;
    Intent intentRegistroUsuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_segunda_registro);

        vectorCamposObtenido = new String[7];

        vectorCampos[0] = findViewById(R.id.editTextNombre);
        vectorCampos[1] = findViewById(R.id.editTextApellido);
        vectorCampos[2] = findViewById(R.id.editTextDni);
        vectorCampos[3] = findViewById(R.id.editTextEmail);
        vectorCampos[4] = findViewById(R.id.editTextPassword);
        vectorCampos[5] = findViewById(R.id.editTextComision);
        vectorCampos[6] = findViewById(R.id.editTextGrupo);

        botonRegistro = findViewById(R.id.buttonRegistrarse);

        botonRegistro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                llenarVectorCamposObtenido();
                if(chequearCampos()){
                    task.execute();
                }
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
            String uri = "http://so-unlam.net.ar";
            String endpoint = "/api/api/register";
            JSONObject req = new JSONObject();
            try {
                req.put("env","TEST");
                req.put("name",vectorCamposObtenido[0]);
                req.put("lastname",vectorCamposObtenido[1]);
                req.put("dni",Integer.parseInt(vectorCamposObtenido[2]));
                req.put("email",vectorCamposObtenido[3]);
                req.put("password",vectorCamposObtenido[4]);
                req.put("commission",Integer.parseInt(vectorCamposObtenido[5]));
                req.put("group",Integer.parseInt(vectorCamposObtenido[6]));
                intentRegistroUsuario = new Intent(this, ServiceHTTPRegistro.class);
                intentRegistroUsuario.putExtra("uri",uri);
                intentRegistroUsuario.putExtra("endpoint",endpoint);
                intentRegistroUsuario.putExtra("jsonObject",req.toString());
                startService(intentRegistroUsuario);
            } catch (JSONException e) {
                e.printStackTrace();
            }
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

    private boolean chequearCampos(){
        int i;
        if(vectorCamposObtenido[4].length() < 8) {
            Toast.makeText(getApplicationContext(), "La password debe ser de 8 caracteres o mas", Toast.LENGTH_LONG).show();
            return false;
        }
        if(!vectorCamposObtenido[5].equals("2900") && !vectorCamposObtenido[5].equals("3900")){
            Toast.makeText(getApplicationContext(), "La comision solo puede ser 2900(Martes) o 3900(Miercoles)", Toast.LENGTH_LONG).show();
            return false;
        }
        for(i = 0; i < vectorCamposObtenido.length; i++){
            if(vectorCamposObtenido[i].equals("")) {
                Toast.makeText(getApplicationContext(), "No pueden haber campos vacios", Toast.LENGTH_LONG).show();
                return false;
            }
        }
        return true;
    }

    private void llenarVectorCamposObtenido(){
        int i;
        for(i = 0; i < vectorCampos.length; i++){
            vectorCamposObtenido[i] = vectorCampos[i].getText().toString();
        }
    }
}
