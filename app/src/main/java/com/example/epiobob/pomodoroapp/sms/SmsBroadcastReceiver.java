package com.example.epiobob.pomodoroapp.sms;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;
import android.widget.Toast;

import com.example.epiobob.pomodoroapp.Constants;
import com.example.epiobob.pomodoroapp.MainActivity;
import com.example.epiobob.pomodoroapp.Task;
import com.example.epiobob.pomodoroapp.db.SqLiteDbHelper;

/**
 * Created by piotr.gawronski2 on 26.03.2018.
 */

public class SmsBroadcastReceiver extends BroadcastReceiver {
    private static final String TAG = MainActivity.class.getSimpleName();

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(TAG, "Received sms message. Application will check it. ");
        final Bundle bundle = intent.getExtras();

        if (bundle != null) {
            final Object[] pdusObj = (Object[]) bundle.get("pdus");
            StringBuilder messageBody = new StringBuilder();
            for (int i = 0; i < pdusObj.length; i++) {
                SmsMessage smsMessage = SmsMessage.createFromPdu((byte[]) pdusObj[i]);
                messageBody.append(smsMessage.getDisplayMessageBody());
            }

            String msgBodyString = messageBody.toString();
            if (msgBodyString.contains(Constants.BOBODORO_SHARED_SUBJECT)) {
                Log.i(TAG, "Received SMS message is valid bobodoro shared msg. Will read the content and write to database.");
                SmsRegexDataGetter getter = new SmsRegexDataGetter();
                Task taskFromSms = getter.get(msgBodyString);
                SqLiteDbHelper dbHelper = SqLiteDbHelper.getDatabase(context);
                dbHelper.addNew(taskFromSms);
            }
        }
    }
}
