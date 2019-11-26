package com.worldline.nicolaldi.util;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;
import android.util.Log;

import com.worldline.nicolaldi.service.TransactionSaverService;
import com.worldline.nicolaldi.service.TransactionSenderService;

/**
 * @author Nicola Verbeeck
 */
public class AlarmUtil {

    public static void scheduleUploadAlarms(Context context) {
        Log.d("AlarmUtil", "Scheduling alarms");

        AlarmManager alarmManager = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);

        PendingIntent intent = PendingIntent.getService(context, 0,
                new Intent(context, TransactionSenderService.class), PendingIntent.FLAG_CANCEL_CURRENT);

        long startTime = SystemClock.elapsedRealtime() + 30000L;
        alarmManager.setInexactRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, startTime, 60000L, intent);
    }

}
