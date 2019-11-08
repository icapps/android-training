package com.worldline.nicolaldi;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

/**
 * @author Nicola Verbeeck
 */
public class DeeplinkActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_pay);

        double totalCost = getIntent().getDoubleExtra("total_amount", 0.0);

        ((TextView) findViewById(R.id.payment_total)).setText("€ " + totalCost);

        findViewById(R.id.payment_card).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showSuccessNotification();
                setResult(Activity.RESULT_OK);
                finish();
            }
        });

        findViewById(R.id.payment_cash).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.putExtra("reason", "I ran out of change");
                setResult(Activity.RESULT_FIRST_USER, intent);
                finish();
            }
        });
    }

    private static final String NOTIFICATION_CHANNEL = "nicolaldi_payment_channel";

    private void showSuccessNotification() {

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            final NotificationChannel channel = new NotificationChannel(NOTIFICATION_CHANNEL,
                    "Payment success notifications", NotificationManager.IMPORTANCE_DEFAULT);

            NotificationManager notificationManager = ((NotificationManager)getSystemService(NOTIFICATION_SERVICE));

            notificationManager.createNotificationChannel(channel);
        }

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, NOTIFICATION_CHANNEL);

        final Notification build = builder.setSmallIcon(R.drawable.logo)
                .setContentText("Payment success!")
                .build();

        NotificationManagerCompat.from(this).notify(1, build);
    }

}
