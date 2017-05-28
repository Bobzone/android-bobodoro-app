package com.example.epiobob.pomodoroapp;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.IBinder;
import android.os.PowerManager;
import android.support.annotation.Nullable;

/**
 * Created by epiobob on 2017-05-28.
 */

public class WakeUpScreenService extends Service implements SensorEventListener {

    @Override
    public void onCreate() {
        SensorManager sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        Sensor proximitySensor = sensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY);

        if (proximitySensor == null)
            stopSelf();
        else
            sensorManager
                    .registerListener(this, proximitySensor, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_PROXIMITY) {
            if (event.values[0] < event.sensor.getMaximumRange()) {
                PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
                PowerManager.WakeLock wl = pm.newWakeLock(PowerManager.SCREEN_BRIGHT_WAKE_LOCK
                        | PowerManager.ACQUIRE_CAUSES_WAKEUP, "CHESS");
                wl.acquire();
                try {
                    Thread.sleep(30 * 1000); // 30 seconds
                } catch (Exception e) {
                } finally {
                    wl.release();
                }
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}
