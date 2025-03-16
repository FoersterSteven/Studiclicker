/**
 * MainMenuActivity.java
 * 
 * Diese Activity stellt das Hauptmenü von StudiClicker dar und bietet
 * Zugang zu den wichtigsten Funktionen der App.
 */
package com.example.studiclicker;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.auth.FirebaseAuth;

public class MainMenuActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // UI-Elemente initialisieren
        Button playButton = findViewById(R.id.playButton);
        Button settingsButton = findViewById(R.id.settingsButton);
        Button logoutButton = findViewById(R.id.logoutButton);

        // Play-Button Listener einrichten - führt zum Hauptspiel
        playButton.setOnClickListener(v -> {
            Intent intent = new Intent(MainMenuActivity.this, GameActivity.class);
            startActivity(intent);
        });

        // Settings-Button Listener einrichten - öffnet Einstellungen
        settingsButton.setOnClickListener(v -> {
            Intent intent = new Intent(MainMenuActivity.this, SettingsActivity.class);
            startActivity(intent);
        });
        
        // Logout-Button Listener einrichten - meldet Benutzer ab
        logoutButton.setOnClickListener(v -> {
            // Benutzer von Firebase abmelden
            FirebaseAuth.getInstance().signOut();
            
            // Zur Login-Seite zurückkehren und Backstack löschen
            Intent intent = new Intent(MainMenuActivity.this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        });
    }
}
