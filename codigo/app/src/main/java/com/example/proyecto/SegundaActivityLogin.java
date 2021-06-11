package com.example.proyecto;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONException;
import org.json.JSONObject;

public class SegundaActivityLogin extends AppCompatActivity {

    EditText usuario, contrasenia;
    Button botonIngresar;
    TextView registrarse;
    Intent intentServiceLogin, intentRegistro;
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
                if (!stringUsuario.equals("") && !stringContrasenia.equals(""))
                    enviarPeticion();
            }
        });

        registrarse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lanzarActivityRegistro();
            }
        });

    }

    private void lanzarActivityRegistro() {
        intentRegistro = new Intent(this, SegundaActivityRegistro.class);
        this.startActivity(intentRegistro);
    }


    private void enviarPeticion() {
        String uri = "http://so-unlam.net.ar";
        String endpoint = "/api/api/login";
        JSONObject req = new JSONObject();
        try {
            req.put("email", stringUsuario);
            req.put("password", stringContrasenia);
            intentServiceLogin = new Intent(this, ServiceHTTPLogin.class);
            intentServiceLogin.putExtra("uri", uri);
            intentServiceLogin.putExtra("endpoint", endpoint);
            intentServiceLogin.putExtra("jsonObject", req.toString());
            startService(intentServiceLogin);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(intentServiceLogin != null)
            stopService(intentServiceLogin);
    }
}
