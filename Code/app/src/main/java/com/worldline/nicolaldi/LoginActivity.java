package com.worldline.nicolaldi;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.worldline.nicolaldi.databinding.ActivityLoginBinding;
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

        ActivityLoginBinding binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setupKeyboard(binding);

        if (savedInstanceState != null) {
            currentPinCode = savedInstanceState.getString(SAVED_STATE_PIN, "");
            updatePinProgress();
        }

        SharedPreferences preferences = getSharedPreferences(PREFERENCE_NAME, MODE_PRIVATE);
        if (preferences.contains(PREFERENCE_KEY_PIN)) {
            unlockPin = preferences.getString(PREFERENCE_KEY_PIN, "");
            binding.setSignatureIsAvailable(true);
        } else {
            binding.setLoginHintText("Please configure pincode");
        }

        binding.buttonSignature.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, SignatureActivity.class));
            }
        });
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putString(SAVED_STATE_PIN, currentPinCode);
    }

    private void setupKeyboard(ActivityLoginBinding binding) {
        setupDigit(binding.loginButton0, 0);
        setupDigit(binding.loginButton1, 1);
        setupDigit(binding.loginButton2, 2);
        setupDigit(binding.loginButton3, 3);
        setupDigit(binding.loginButton4, 4);
        setupDigit(binding.loginButton5, 5);
        setupDigit(binding.loginButton6, 6);
        setupDigit(binding.loginButton7, 7);
        setupDigit(binding.loginButton8, 8);
        setupDigit(binding.loginButton9, 9);

        binding.loginButtonOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onOkClicked();
            }
        });
        binding.loginButtonBackspace.setOnClickListener(new View.OnClickListener() {
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
        ((PinProgressView) findViewById(R.id.login_progress)).setCurrentPinLength(currentPinCode.length());
    }

}
