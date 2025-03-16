/**
 * RegisterActivity.java
 * 
 * Diese Activity ermöglicht neuen Benutzern, sich für StudiClicker
 * mit Firebase Authentication zu registrieren.
 */
package com.example.studiclicker;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class RegisterActivity extends AppCompatActivity {
    private static final String TAG = "RegisterActivity";
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        // Initialisiere Firebase Auth
        mAuth = FirebaseAuth.getInstance();
        
        setContentView(R.layout.activity_register);
        
        // UI-Elemente initialisieren
        Button loginButton = findViewById(R.id.loginButton);
        Button registerButton = findViewById(R.id.registerButton);
        EditText emailInput = findViewById(R.id.EmailInput);
        EditText repeatEmailInput = findViewById(R.id.RepeatEmailInput);
        EditText passwordInput = findViewById(R.id.PasswordInput);

        // Register-Button Listener einrichten
        registerButton.setOnClickListener(v -> {
            // E-Mail und Passwort aus den Eingabefeldern extrahieren
            String email = emailInput.getText().toString().trim();
            String repeatEmail = repeatEmailInput.getText().toString().trim();
            String password = passwordInput.getText().toString().trim();

            // Validierung der Eingabefelder
            if (TextUtils.isEmpty(email)) {
                emailInput.setError("E-Mail ist erforderlich");
                return;
            }

            if (TextUtils.isEmpty(repeatEmail)) {
                repeatEmailInput.setError("E-Mail-Wiederholung ist erforderlich");
                return;
            }

            if (!email.equals(repeatEmail)) {
                repeatEmailInput.setError("E-Mail-Adressen stimmen nicht überein");
                return;
            }

            if (TextUtils.isEmpty(password)) {
                passwordInput.setError("Passwort ist erforderlich");
                return;
            }

            // Benutzer bei Firebase registrieren
            mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Registrierung erfolgreich
                                Log.d(TAG, "createUserWithEmail:success");
                                FirebaseUser user = mAuth.getCurrentUser();
                                // Zur Login-Seite wechseln
                                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                                startActivity(intent);
                            } else {
                                // Registrierung fehlgeschlagen
                                Log.w(TAG, "createUserWithEmail:failure", task.getException());
                                Toast.makeText(RegisterActivity.this, "Authentication failed.",
                                        Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        });

        // Login-Button Listener einrichten - zurück zur Login-Seite
        loginButton.setOnClickListener(v -> {
            Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
            startActivity(intent);
        });
    }
}
