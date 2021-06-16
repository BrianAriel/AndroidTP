package com.example.proyecto;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class PrimerActivity extends AppCompatActivity {

    private static final int PERMISSION_REQUEST_SEND_SMS = 1;
    Button botonEnviarMensaje, botonEnviarCodigo;
    EditText numeroTelefono, numeroCodigo;
    String telefono, codigoRecibido;
    TextView title;
    int codigo = (int) (Math.random() * 1000000);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_primer);

        botonEnviarMensaje = findViewById(R.id.buttonSMS);
        botonEnviarCodigo = findViewById(R.id.buttonCodigo);
        numeroTelefono = findViewById(R.id.editTextTelefono);
        numeroCodigo = findViewById(R.id.editTextCodigo);
        title = (TextView) findViewById(R.id.textViewTitulo);

        Typeface typeface = Typeface.createFromAsset(getAssets(), "fonts/Gayathri-Bold.ttf");
        title.setTypeface(typeface);

        botonEnviarMensaje.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(numeroTelefono.getText().toString().equals("") || numeroTelefono.getText().toString().equals(null)){
                    Toast.makeText(getApplicationContext(), "Ingrese un número de teléfono!", Toast.LENGTH_LONG).show();
                }
                else{
                    sendSMS();
                    lanzarVerificarCodigo();
                }
            }
        });

        botonEnviarCodigo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(chequearCodigo()){
                    lanzarSegundaActivity();
                }
            }
        });
    }

    private void sendSMS(){
        SmsManager smsManager = SmsManager.getDefault();

        if(!chequearPermisos()) return;
        telefono = numeroTelefono.getText().toString();
        //smsManager.sendTextMessage(telefono, null, Integer.toString(codigo), null, null);
//        numeroTelefono.setText(Integer.toString(codigo));
//        botonEnviarCodigo.setVisibility(View.VISIBLE);
    }

    private boolean chequearPermisos() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.SEND_SMS}, PERMISSION_REQUEST_SEND_SMS);
        }
        return ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS) == PackageManager.PERMISSION_GRANTED;
    }

    private boolean chequearCodigo(){
        codigoRecibido = numeroCodigo.getText().toString();
        if(codigoRecibido.equals(Integer.toString(codigo))){
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
    }

    private void lanzarVerificarCodigo(){
        Intent intent = new Intent(this, VerificarCodigo.class);
        intent.putExtra("codigo_verificacion", Integer.toString(codigo));
        this.startActivity(intent);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == PERMISSION_REQUEST_SEND_SMS) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(getApplicationContext(), "Permiso a enviar SMS otorgado", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText((getApplicationContext()), "Permiso a enviar SMS no otorgado", Toast.LENGTH_LONG).show();
            }
        }
    }
}
