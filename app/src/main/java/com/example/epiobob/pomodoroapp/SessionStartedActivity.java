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

public class SessionStartedActivity extends Activity {

    private TextView timer;
    private CountDownTimer countDownTimer;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.session);
        timer = (TextView) findViewById(R.id.sessionTimer);
        // TODO - uncomment in PROD
        startWorkTimer(25 * 1000);
//        startWorkTimer(25 * 60 * 1000);
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
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
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
                        // TODO - mark task as complete
                        break;
                    }
                }
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }
}
