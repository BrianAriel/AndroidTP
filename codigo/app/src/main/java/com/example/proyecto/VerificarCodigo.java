package com.example.proyecto;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class VerificarCodigo extends AppCompatActivity {

    private String codigo, codigoRecibido;
    EditText numeroCodigo;
    Button confirmBtn, backBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verificar_codigo);

        numeroCodigo = (EditText) findViewById(R.id.editTextCodigoVerif);
        confirmBtn = (Button) findViewById(R.id.buttonConfirmCodigo);
        backBtn = (Button) findViewById(R.id.buttonBackToPhone);

        codigo = getIntent().getStringExtra("codigo_verificacion");
        Toast.makeText(getApplicationContext(), "Código de verificación: " + codigo, Toast.LENGTH_LONG).show();

        confirmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (chequearCodigo()){
                    lanzarSegundaActivity();
                }
                else {
                    Toast.makeText(getApplicationContext(), "Código inválido!", Toast.LENGTH_LONG).show();
                    numeroCodigo.setText("");
                }
            }
        });

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private boolean chequearCodigo(){
        codigoRecibido = numeroCodigo.getText().toString();
        if(codigoRecibido.equals(codigo)){
            Toast.makeText(getApplicationContext(), "El codigo es el mismo", Toast.LENGTH_LONG).show();
            return true;
        } else {
            Toast.makeText(getApplicationContext(), "El codigo no es igual", Toast.LENGTH_LONG).show();
            return false;
        }
    }

    private void lanzarSegundaActivity(){
        Intent intent = new Intent(this, SegundaActivityLogin.class);
        this.startActivity(intent);
        finish();
    }
}
