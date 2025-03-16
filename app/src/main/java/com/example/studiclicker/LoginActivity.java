/**
 * LoginActivity.java
 * 
 * Diese Activity ermöglicht die Authentifizierung von Benutzern
 * über Firebase Authentication für StudiClicker.
 */
package com.example.studiclicker;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.AuthResult;
import android.util.Log;
import android.widget.Toast;
import com.google.android.gms.tasks.Task;

public class LoginActivity extends AppCompatActivity {
    // Firebase Authentication Instanz
    private FirebaseAuth mAuth;
    private static final String TAG = "LoginActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        
        // Initialisiere Firebase Auth
        mAuth = FirebaseAuth.getInstance();
        
        // UI-Elemente initialisieren
        Button LoginButton = findViewById(R.id.LoginButton);
        Button registerButton = findViewById(R.id.registerButton);
        EditText emailInput = findViewById(R.id.EmailInput);
        EditText passwordInput = findViewById(R.id.PasswordInput);

        // Login-Button Listener einrichten
        LoginButton.setOnClickListener(v -> {
            // E-Mail und Passwort aus den Eingabefeldern extrahieren
            String email = emailInput.getText().toString().trim();
            String password = passwordInput.getText().toString().trim();

            // Validiere E-Mail und Passwort
            if (TextUtils.isEmpty(email)) {
                emailInput.setError("E-Mail ist erforderlich");
                return;
            }

            if (TextUtils.isEmpty(password)) {
                passwordInput.setError("Passwort ist erforderlich");
                return;
            }

            // Mit Firebase anmelden
            mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Anmeldung erfolgreich
                                Log.d(TAG, "signInWithEmail:success");
                                FirebaseUser user = mAuth.getCurrentUser();
                                // Zum Hauptmenü wechseln
                                Intent intent = new Intent(LoginActivity.this, MainMenuActivity.class);
                                startActivity(intent);
                            } else {
                                // Anmeldung fehlgeschlagen
                                Log.w(TAG, "signInWithEmail:failure", task.getException());
                                Toast.makeText(LoginActivity.this, "Authentication failed.",
                                        Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        });

        // Register-Button Listener einrichten
        registerButton.setOnClickListener(v -> {
            // Zur Registrierungsseite wechseln
            Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
            startActivity(intent);
        });
    }

    /**
     * Überprüft beim Starten der Activity, ob ein Benutzer bereits angemeldet ist
     */
    @Override
    public void onStart() {
        super.onStart();
        // Prüfe, ob Benutzer bereits angemeldet ist
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            // Benutzer ist bereits angemeldet, wechsle zum Hauptmenü
            Intent intent = new Intent(LoginActivity.this, MainMenuActivity.class);
            startActivity(intent);
        }
    }
}
