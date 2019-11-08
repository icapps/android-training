package com.worldline.nicolaldi;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

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

}
