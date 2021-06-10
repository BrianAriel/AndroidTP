package com.example.proyecto;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class SegundaActivityRegistro extends AppCompatActivity {

    EditText nombre, apellido, dni, email, password, comision, grupo;
    EditText[] vectorCampos = {nombre, apellido, dni, email, password, comision, grupo};
    Button botonRegistro;
    String[] vectorCamposObtenido;

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
                    Toast.makeText(getApplicationContext(), "Todo en orden", Toast.LENGTH_LONG).show();
                }
            }
        });
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
