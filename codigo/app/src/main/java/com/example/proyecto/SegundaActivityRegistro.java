package com.example.proyecto;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONException;
import org.json.JSONObject;

public class SegundaActivityRegistro extends AppCompatActivity {

    EditText[] vectorCampos;
    Button botonRegistro;
    String[] vectorCamposObtenido;
    Intent intentRegistroUsuario;
    private static final int MAX_DNI = 10, MIN_PASSWORD = 8;
    private static final String COM_MARTES = "2900", COM_MIERCOLES = "3900";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_segunda_registro);

        vectorCampos = new EditText[7];
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
                    enviarPeticion();
                }
            }
        });

    }

    private void enviarPeticion() {
        String uri = "http://so-unlam.net.ar";
        String endpoint = "/api/api/register";
        JSONObject req = new JSONObject();
        try {
            req.put("env", "TEST");
            req.put("name", vectorCamposObtenido[0]);
            req.put("lastname", vectorCamposObtenido[1]);
            req.put("dni", Integer.parseInt(vectorCamposObtenido[2]));
            req.put("email", vectorCamposObtenido[3]);
            req.put("password", vectorCamposObtenido[4]);
            req.put("commission", Integer.parseInt(vectorCamposObtenido[5]));
            req.put("group", Integer.parseInt(vectorCamposObtenido[6]));
            intentRegistroUsuario = new Intent(this, ServiceHTTPRegistro.class);
            intentRegistroUsuario.putExtra("uri", uri);
            intentRegistroUsuario.putExtra("endpoint", endpoint);
            intentRegistroUsuario.putExtra("jsonObject", req.toString());
            startService(intentRegistroUsuario);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private boolean chequearCampos(){
        int i;
        if(vectorCamposObtenido[2].length() > MAX_DNI) {
            Toast.makeText(getApplicationContext(), "El dni introducido es muy largo", Toast.LENGTH_LONG).show();
            return false;
        }
        if(!vectorCamposObtenido[3].contains("@")){
            Toast.makeText(getApplicationContext(), "El email debe tener formato email (incluir @)", Toast.LENGTH_LONG).show();
            return false;
        }
        if(vectorCamposObtenido[4].length() < MIN_PASSWORD) {
            Toast.makeText(getApplicationContext(), "La password debe ser de 8 caracteres o mas", Toast.LENGTH_LONG).show();
            return false;
        }
        if(!vectorCamposObtenido[5].equals(COM_MARTES) && !vectorCamposObtenido[5].equals(COM_MIERCOLES)){
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(intentRegistroUsuario != null) {
            stopService(intentRegistroUsuario);
        }
    }
}
