package com.example.proyecto;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class PrimerActivity extends AppCompatActivity {

    private static final int PERMISSION_REQUEST_SEND_SMS = 1;
    Button botonEnviarMensaje;
    EditText numeroTelefono;
    String telefono;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_primer);

        botonEnviarMensaje = findViewById(R.id.buttonSMS);
        numeroTelefono = findViewById(R.id.editTextTelefono);

        botonEnviarMensaje.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                sendSMS();
            }
        });
    }

    protected void sendSMS(){
        SmsManager smsManager = SmsManager.getDefault();

        if(!chequearPermisos()) {
            return;
        }

        telefono = numeroTelefono.getText().toString();
        //smsManager.sendTextMessage(telefono, null, "mensaje de prueba", null, null);
        numeroTelefono.setText("Imprimo esto para que no gaste credito");
    }

    protected boolean chequearPermisos() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.SEND_SMS}, PERMISSION_REQUEST_SEND_SMS);
        }
        return ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS) == PackageManager.PERMISSION_GRANTED;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_SEND_SMS: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(getApplicationContext(), "Permiso a enviar SMS otorgado", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText((getApplicationContext()), "Permiso a enviar SMS no otorgado", Toast.LENGTH_LONG).show();
                }
            }
        }
    }
}
