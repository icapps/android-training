package com.worldline.nicolaldi;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.worldline.nicolaldi.view.PinProgressView;

/**
 * @author Nicola Verbeeck
 */
public class LoginActivity extends AppCompatActivity {

    private static final String SAVED_STATE_PIN = "savedPin";
    public static final String PREFERENCE_NAME = "supersecure";
    public static final String PREFERENCE_KEY_PIN = "pin";

    private String unlockPin;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login);

        setupKeyboard();

        if (savedInstanceState != null) {
            currentPinCode = savedInstanceState.getString(SAVED_STATE_PIN, "");
            updatePinProgress();
        }

        SharedPreferences preferences = getSharedPreferences(PREFERENCE_NAME, MODE_PRIVATE);
        if (preferences.contains(PREFERENCE_KEY_PIN)) {
            unlockPin = preferences.getString(PREFERENCE_KEY_PIN, "");
        } else {
            ((TextView) findViewById(R.id.login_label_hint)).setText("Please configure pincode");
        }
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putString(SAVED_STATE_PIN, currentPinCode);
    }

    private void setupKeyboard() {
        setupDigit(findViewById(R.id.login_button_0), 0);
        setupDigit(findViewById(R.id.login_button_1), 1);
        setupDigit(findViewById(R.id.login_button_2), 2);
        setupDigit(findViewById(R.id.login_button_3), 3);
        setupDigit(findViewById(R.id.login_button_4), 4);
        setupDigit(findViewById(R.id.login_button_5), 5);
        setupDigit(findViewById(R.id.login_button_6), 6);
        setupDigit(findViewById(R.id.login_button_7), 7);
        setupDigit(findViewById(R.id.login_button_8), 8);
        setupDigit(findViewById(R.id.login_button_9), 9);

        findViewById(R.id.login_button_ok).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onOkClicked();
            }
        });
        findViewById(R.id.login_button_backspace).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackspaceClicked();
            }
        });

    }

    private void setupDigit(View view, final int digit) {
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onDigitClicked(digit);
            }
        });
    }

    private String currentPinCode = "";

    private void onOkClicked() {
        if (currentPinCode.length() != 4)
            return;

        if (unlockPin == null) {
            getSharedPreferences(PREFERENCE_NAME, MODE_PRIVATE).edit()
                    .putString(PREFERENCE_KEY_PIN, currentPinCode)
                    .apply();
            unlockPin = currentPinCode;
        } else {
            if (!currentPinCode.equals(unlockPin)) {
                Toast.makeText(this, "Wrong pincode", Toast.LENGTH_SHORT).show();
                return;
            }
        }

        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);

        currentPinCode = "";
        updatePinProgress();
    }

    private void onBackspaceClicked() {
        if (currentPinCode.length() == 0)
            return;

        currentPinCode = currentPinCode.substring(0, currentPinCode.length() - 1);
        updatePinProgress();
    }

    private void onDigitClicked(int digit) {
        if (currentPinCode.length() == 4)
            return;

        currentPinCode = currentPinCode + digit;
        updatePinProgress();
    }

    private void updatePinProgress() {
        ((PinProgressView)findViewById(R.id.login_progress)).setCurrentPinLength(currentPinCode.length());
    }

}
