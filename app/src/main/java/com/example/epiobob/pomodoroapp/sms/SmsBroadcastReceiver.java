package com.example.epiobob.pomodoroapp.sms;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

/**
 * Created by piotr.gawronski2 on 26.03.2018.
 */

public class SmsBroadcastReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Toast.makeText(context, "very important message, YEEES", Toast.LENGTH_SHORT).show();
    }
}
