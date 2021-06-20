package com.example.proyecto;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Map;

public class ActivityHistorial extends AppCompatActivity {

    SharedPreferences sharedPref;
    ListView lista;
    Button botonRegresar;
    ArrayList<String> listaMediciones;
    ArrayAdapter<String> arrayAdapter;
    Intent intentFuncional;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_historial);

        intentFuncional = new Intent(this,ActivityFuncional.class);

        sharedPref = getApplicationContext().getSharedPreferences(getString(R.string.archivo_preferences), Context.MODE_PRIVATE);
        listaMediciones = new ArrayList<>();
        llenarLista();

        lista = findViewById(R.id.listaMediciones);
        botonRegresar = findViewById(R.id.buttonRegresar);

        arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, listaMediciones);

        botonRegresar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        lista.setAdapter(arrayAdapter);
    }

    private synchronized void llenarLista(){
        Map<String,?> keys = sharedPref.getAll();

        for (Map.Entry<String,?> entry : keys.entrySet()){
            listaMediciones.add(entry.getValue().toString());
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
