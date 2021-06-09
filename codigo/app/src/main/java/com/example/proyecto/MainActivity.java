package com.example.proyecto;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    public static final String MESSAGE_PRIMER_ACTIVITY = "com.example.proyecto.PrimerActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Intent intent = new Intent(this, PrimerActivity.class);
        intent.putExtra(MESSAGE_PRIMER_ACTIVITY, "Arrancar activity");
        this.startActivity(intent);
    }
}
