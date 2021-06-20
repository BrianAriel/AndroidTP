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

        numeroCodigo = findViewById(R.id.editTextCodigoVerif);
        confirmBtn = findViewById(R.id.buttonConfirmCodigo);
        backBtn = findViewById(R.id.buttonBackToPhone);

        codigo = getIntent().getStringExtra("codigo_verificacion");

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
        return codigoRecibido.equals(codigo);
    }

    private void lanzarSegundaActivity(){
        Intent intent = new Intent(this, SegundaActivityLogin.class);
        this.startActivity(intent);
        finish();
    }
}
