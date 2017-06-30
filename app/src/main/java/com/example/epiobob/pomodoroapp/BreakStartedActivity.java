package com.example.epiobob.pomodoroapp;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
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

public class BreakStartedActivity extends Activity {

    private TextView timer;
    private CountDownTimer countDownTimer;
    private int countDownTimerLeft;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.session);
        timer = (TextView) findViewById(R.id.sessionTimer);
        // TODO - uncomment in PROD#2
        startBreakTimer(20 * 1000);
//        startWorkTimer(25 * 5 * 1000);
    }

    private void startBreakTimer(final int breakLength) {
        countDownTimerLeft = breakLength;
        countDownTimer = new CountDownTimer(breakLength, 1000) {

            @Override
            public void onTick(long millisUntilFinished) {
                countDownTimerLeft -= 1000;
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

                showSessionFinishedAlertDialog(BreakStartedActivity.this.getCurrentFocus());
            }
        };
        countDownTimer.start();
    }

    public void backButtonPressed(View view) {
        countDownTimer.cancel();
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onBackPressed() {
        backButtonPressed(this.getCurrentFocus());
        super.onBackPressed();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        countDownTimer.cancel();
        outState.putInt("timerBase", countDownTimerLeft);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        countDownTimer.cancel();
        startBreakTimer(savedInstanceState.getInt("timerBase"));
    }

    public void showSessionFinishedAlertDialog(View view) {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Break finished");

        String[] animals = {"Start session", "Start another break"};
        builder.setItems(animals, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case 0: {
                        Intent intent = new Intent(BreakStartedActivity.this, SessionStartedActivity.class);
                        startActivity(intent);
                        break;
                    }
                    case 1: {
                        Intent intent = new Intent(BreakStartedActivity.this, BreakStartedActivity.class);
                        startActivity(intent);
                        break;
                    }
                }
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }
}

