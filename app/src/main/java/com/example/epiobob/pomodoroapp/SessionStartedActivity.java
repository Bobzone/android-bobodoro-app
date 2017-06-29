package com.example.epiobob.pomodoroapp;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.PowerManager;
import android.os.Vibrator;
import android.support.annotation.Nullable;
import android.util.FloatMath;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

/**
 * Created by epiobob on 2017-05-21.
 */

public class SessionStartedActivity extends Activity {

    private TextView timer;
    private CountDownTimer countDownTimer;
    private Task taskContext;
    private Sensor accSensor;
    private float mAccel;
    private float mAccelCurrent;
    private float mAccelLast;
    private SensorManager sm;
    private SensorEventListener accSensorListener;
    private PowerManager.WakeLock wakeLock;
    private PowerManager pm;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD |
                WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED |
                WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);

        setContentView(R.layout.session);
        timer = (TextView) findViewById(R.id.sessionTimer);
        taskContext = (Task) getIntent().getSerializableExtra("task_context");
        startWorkTimer(25 * 1000);
        // TODO - uncomment in PROD
        //        startWorkTimer(25 * 60 * 1000);

        pm = (PowerManager) getApplicationContext().getSystemService(Context.POWER_SERVICE);

        sm = (SensorManager) this.getSystemService(SENSOR_SERVICE);
        accSensor = sm.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        mAccel = 0.00f;
        mAccelCurrent = SensorManager.GRAVITY_EARTH;
        mAccelLast = SensorManager.GRAVITY_EARTH;
        accSensorListener = new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent event) {
                if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
                    float[] mGravity = event.values.clone();
                    float x = mGravity[0];
                    float y = mGravity[1];
                    float z = mGravity[2];
                    mAccelLast = mAccelCurrent;
                    mAccelCurrent = (float) Math.sqrt(x * x + y * y + z * z);
                    float delta = mAccelCurrent - mAccelLast;
                    mAccel = mAccel * 0.9f + delta;
                    if (mAccel > 1) {
                        Log.i("SensorTest", "Device has been moved. ");

                        if (!pm.isScreenOn() && wakeLock == null) {
                            wakeLock = pm.newWakeLock(PowerManager.FULL_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP
                                    | PowerManager.ON_AFTER_RELEASE, "SessionStartedWakeLock");
                            wakeLock.acquire();
                            Log.i("SensorTest", "WakeLock acquired.");
                        }
                    }
                }
            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int accuracy) {
            }
        };
    }

    @Override
    protected void onPause() {
        super.onPause();
        releaseWakeLock();
    }

    private void releaseWakeLock() {
        if (wakeLock != null) {
            wakeLock.release();
            wakeLock = null;
            Log.i("SensorTest", "wakeLock released. ");
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        sm.registerListener(accSensorListener,
                accSensor, SensorManager.SENSOR_DELAY_NORMAL);
    }

    private void startWorkTimer(final int workLength) {
        countDownTimer = new CountDownTimer(workLength, 1000) {

            @Override
            public void onTick(long millisUntilFinished) {
                int seconds = (int) ((millisUntilFinished / 1000) % 60);
                int minutes = (int) ((millisUntilFinished / 1000) / 60);
                timer.setText(String.valueOf(minutes) + "." + String.valueOf(seconds));
            }

            @Override
            public void onFinish() {
                timer.setText("0.0");
                sm.unregisterListener(accSensorListener);
                releaseWakeLock();

                Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                Ringtone r = RingtoneManager.getRingtone(getApplicationContext(), notification);
                r.play();

                Vibrator v = (Vibrator) getApplicationContext().getSystemService(Context.VIBRATOR_SERVICE);
                v.vibrate(1000);

                showSessionFinishedAlertDialog(SessionStartedActivity.this.getCurrentFocus());
            }
        };
        countDownTimer.start();
    }

    public void backButtonPressed(View view) {
        countDownTimer.cancel();
        sm.unregisterListener(accSensorListener);
        releaseWakeLock();

        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onBackPressed() {
        backButtonPressed(this.getCurrentFocus());
        super.onBackPressed();
    }

    public void showSessionFinishedAlertDialog(View view) {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Session finished");

        String[] animals = {"Start break", "Start next session", "Mark task as complete"};
        builder.setItems(animals, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case 0: {
                        Intent intent = new Intent(SessionStartedActivity.this, BreakStartedActivity.class);
                        startActivity(intent);
                        break;
                    }
                    case 1: {
                        Intent intent = new Intent(SessionStartedActivity.this, SessionStartedActivity.class);
                        startActivity(intent);
                        break;
                    }
                    case 2: {
                        taskContext.markAsComplete();
                        onBackPressed();
                        break;
                    }
                }
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }
}
