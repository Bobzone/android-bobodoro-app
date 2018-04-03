package com.example.epiobob.pomodoroapp.sms;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.example.epiobob.pomodoroapp.Constants;
import com.example.epiobob.pomodoroapp.Task;
import com.example.epiobob.pomodoroapp.db.SqLiteDbHelper;

/**
 * Created by piotr.gawronski2 on 26.03.2018.
 */

public class SmsBroadcastReceiver extends BroadcastReceiver {


    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO: probably have to read PDUs from SMS here?
        String extraText = intent.getStringExtra(Intent.EXTRA_TEXT);
        if (extraText != null && extraText.contains(Constants.BOBODORO_SHARED_SUBJECT)) {
            Toast.makeText(context, "very important message, YEEES", Toast.LENGTH_SHORT).show();

            SmsRegexDataGetter getter = new SmsRegexDataGetter();
            Task taskFromSms = getter.get(extraText);

            SqLiteDbHelper dbHelper = SqLiteDbHelper.getDatabase(context);
            dbHelper.addNew(taskFromSms);
        }
    }
}
