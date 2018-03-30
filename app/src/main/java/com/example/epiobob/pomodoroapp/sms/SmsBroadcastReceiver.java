package com.example.epiobob.pomodoroapp.sms;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.example.epiobob.pomodoroapp.Task;
import com.example.epiobob.pomodoroapp.db.SqLiteDbHelper;

/**
 * Created by piotr.gawronski2 on 26.03.2018.
 */

public class SmsBroadcastReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        String stringExtra = intent.getStringExtra(Intent.EXTRA_SUBJECT);
        if (stringExtra != null && stringExtra.equals("Bobodoro shared task")) {
            Toast.makeText(context, "very important message, YEEES", Toast.LENGTH_SHORT).show();

            SmsRegexDataGetter getter = new SmsRegexDataGetter();
            Task taskFromSms = getter.get(intent.getStringExtra(Intent.EXTRA_TEXT));

            SqLiteDbHelper dbHelper = SqLiteDbHelper.getDatabase(context);
            dbHelper.addNew(taskFromSms);
        }
    }
}
