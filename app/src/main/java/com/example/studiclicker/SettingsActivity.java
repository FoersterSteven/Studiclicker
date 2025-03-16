/**
 * SettingsActivity.java
 * 
 * Diese Activity ermöglicht Spielern, Einstellungen vorzunehmen
 * und ihren Spielfortschritt zurückzusetzen.
 */
package com.example.studiclicker;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

public class SettingsActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        // UI-Elemente initialisieren
        Button resetButton = findViewById(R.id.resetButton);
        Button themeButton = findViewById(R.id.themeButton);
        Button backButton = findViewById(R.id.backButton);

        // Reset-Button Listener einrichten - setzt Spielfortschritt zurück
        resetButton.setOnClickListener(v -> {
            // Spiel-Daten in SharedPreferences zurücksetzen
            getSharedPreferences("game_prefs", MODE_PRIVATE)
                    .edit()
                    .clear()
                    .apply();

            // Zurück zum Hauptmenü mit Flags, um Activity-Stack zu leeren
            Intent intent = new Intent(SettingsActivity.this, MainMenuActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        });

        // Theme-Button Listener einrichten - für zukünftige Implementierung
        themeButton.setOnClickListener(v -> {
            // TODO: Theme-Wechsel implementieren
            // Hier könnte man ein Theme wechseln lassen
        });

        // Back-Button Listener einrichten - zurück zum Hauptmenü
        backButton.setOnClickListener(v -> {
            Intent intent = new Intent(SettingsActivity.this, MainMenuActivity.class);
            startActivity(intent);
        });
    }
}
