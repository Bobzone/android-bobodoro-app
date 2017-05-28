package com.example.epiobob.pomodoroapp;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Vibrator;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by epiobob on 2017-05-21.
 */

public class SessionStartedActivity extends Activity {

    private TextView timer;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.session);
        timer = (TextView) findViewById(R.id.sessionTimer);
        startTimer();
    }

    private void startTimer() {
        CountDownTimer countDownTimer = new CountDownTimer(25 * 1000, 1000) {
            // TODO - uncomment in PROD
//        CountDownTimer countDownTimer = new CountDownTimer(25 * 60 * 1000, 1000) {

            @Override
            public void onTick(long millisUntilFinished) {
                int seconds = (int) ((millisUntilFinished / 1000) % 60);
                int minutes = (int) ((millisUntilFinished / 1000) / 60);
                timer.setText(String.valueOf(minutes) + "." + String.valueOf(seconds));
            }

            @Override
            public void onFinish() {
                timer.setText("0.0");

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

    public void taskOptionsButtonPressed(View view) {
        Toast.makeText(SessionStartedActivity.this, "Finished timer!", Toast.LENGTH_SHORT).show();
    }

    public void backButtonPressed(View view) {
        this.finish();
    }

    public void showSessionFinishedAlertDialog(View view) {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Session finished");

        String[] animals = {"Start break", "Start next session", "Mark task as complete"};
        builder.setItems(animals, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case 0: // horse
                    case 1: // cow
                    case 2: // camel
                }
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }
}
