package com.example.epiobob.pomodoroapp.sms;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

/**
 * Created by piotr.gawronski2 on 26.03.2018.
 */

public class SmsBroadcastReceiver extends BroadcastReceiver {

    SmsRegexDataGetter getter;

    @Override
    public void onReceive(Context context, Intent intent) {
        String stringExtra = intent.getStringExtra(Intent.EXTRA_SUBJECT);
        if (stringExtra != null && stringExtra.equals("Bobodoro shared task")) {
            Toast.makeText(context, "very important message, YEEES", Toast.LENGTH_SHORT).show();


        }
    }
}
