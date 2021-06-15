package com.example.proyecto;

import android.app.IntentService;
import android.content.Intent;

import androidx.annotation.Nullable;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class ServiceCheckToken extends IntentService {

    private static final String ETIQUETA = ServiceCheckToken.class.getSimpleName();
    ScheduledExecutorService scheduler;
    Intent intentRefresh;

    public ServiceCheckToken () {
        super(ETIQUETA);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        intentRefresh = new Intent(this,ActivityRefresh.class);
        intentRefresh.putExtra("access_token",intent.getStringExtra("access_token"));

        scheduler = Executors.newSingleThreadScheduledExecutor();

        scheduler.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                iniciarActivity();
            }
        },5,10, TimeUnit.SECONDS);
    }

    private void iniciarActivity() {
        startActivity(intentRefresh);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}