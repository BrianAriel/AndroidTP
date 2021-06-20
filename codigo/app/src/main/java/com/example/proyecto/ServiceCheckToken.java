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
    private static final int INITIAL_DELAY = 0, PERIOD = 30;

    public ServiceCheckToken () {
        super(ETIQUETA);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        intentRefresh = new Intent(this,ActivityRefresh.class);
        intentRefresh.putExtra("refresh_token",intent.getStringExtra("refresh_token"));
        intentRefresh.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        scheduler = Executors.newSingleThreadScheduledExecutor();

        scheduler.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                iniciarActivity();
            }
        },INITIAL_DELAY,PERIOD, TimeUnit.MINUTES);
    }

    private void iniciarActivity() {
        startActivity(intentRefresh);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
