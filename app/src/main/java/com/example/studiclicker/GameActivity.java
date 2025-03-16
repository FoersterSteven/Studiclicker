/**
 * GameActivity.java
 * 
 * Diese Activity bildet das Hauptspiel-Interface von StudiClicker, wo der Spieler
 * Studicoin sammeln kann durch Klicken und passives Einkommen.
 */
package com.example.studiclicker;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class GameActivity extends AppCompatActivity {
    // Spielstand-Variablen
    private int studicoinCounter;           // Aktuelle Anzahl der Studicoin
    private int clickValue;                 // Wert pro Klick
    private TextView counterText;           // TextView für den Studicoin-Zähler
    private int totalPassiveIncome;         // Gesamtes passives Einkommen pro Sekunde

    // Individuelle passive Einkommensquellen
    private int passiveIncome1;             // Grünes P
    private int passiveIncome2;             // Lerngruppe
    private int passiveIncome3;             // Notizen von früheren Semestern
    private int passiveIncome4;             // Privater Tutor
    private int passiveIncome5;             // Videotutorial

    // Handler und Runnable für periodisches passives Einkommen
    private Handler handler = new Handler();
    private Runnable passiveIncomeRunnable = new Runnable() {
        @Override
        public void run() {
            // Füge passives Einkommen zum Studicoin-Counter hinzu
            studicoinCounter += totalPassiveIncome;
            updateCounterText();
            saveGameData();                 // Spieldaten jede Sekunde speichern
            handler.postDelayed(this, 1000); // Wiederhole alle 1000ms (1 Sekunde)
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        // UI-Elemente initialisieren
        counterText = findViewById(R.id.counterText);
        ImageView clickImage = findViewById(R.id.clickButton);
        Button upgradeButton = findViewById(R.id.upgradeButton);
        Button statsButton = findViewById(R.id.statsButton);
        Button menuButton = findViewById(R.id.menuButton);

        // Gespeicherte Spieldaten laden
        loadGameData();
        updateCounterText();

        // Passives Einkommen starten, nachdem Daten geladen wurden
        handler.post(passiveIncomeRunnable);

        // Click-Button Listener einrichten
        clickImage.setOnClickListener(v -> {
            studicoinCounter += clickValue;
            updateCounterText();
            saveGameData();
        });

        // Upgrade-Button Listener einrichten
        upgradeButton.setOnClickListener(v -> {
            Intent intent = new Intent(GameActivity.this, UpgradeActivity.class);
            intent.putExtra("studicoinCounter", studicoinCounter);
            startActivityForResult(intent, 1);
        });

        // Stats-Button Listener einrichten
        statsButton.setOnClickListener(v -> {
            Intent intent = new Intent(GameActivity.this, StatsActivity.class);
            // Übergebe alle relevanten Spieldaten an StatsActivity
            intent.putExtra("studicoinCounter", studicoinCounter);
            intent.putExtra("clickValue", clickValue);
            intent.putExtra("totalPassiveIncome", totalPassiveIncome);
            intent.putExtra("passiveIncome1", passiveIncome1);
            intent.putExtra("passiveIncome2", passiveIncome2);
            intent.putExtra("passiveIncome3", passiveIncome3);
            intent.putExtra("passiveIncome4", passiveIncome4);
            intent.putExtra("passiveIncome5", passiveIncome5);
            startActivity(intent);
        });

        // Menu-Button Listener einrichten
        menuButton.setOnClickListener(v -> {
            Intent intent = new Intent(GameActivity.this, MainMenuActivity.class);
            startActivity(intent);
        });
    }

    /**
     * Verarbeitet das Ergebnis von UpgradeActivity
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK && data != null) {
            // Aktualisiere Click-Wert und Coin-Anzahl
            clickValue = data.getIntExtra("clickValue", clickValue);
            studicoinCounter = data.getIntExtra("studicoinCounter", studicoinCounter);

            // Hole gesamtes passives Einkommen
            totalPassiveIncome = data.getIntExtra("totalPassiveIncome", totalPassiveIncome);

            // Hole individuelle passive Einkommensstufen
            passiveIncome1 = data.getIntExtra("passiveIncome1", passiveIncome1);
            passiveIncome2 = data.getIntExtra("passiveIncome2", passiveIncome2);
            passiveIncome3 = data.getIntExtra("passiveIncome3", passiveIncome3);
            passiveIncome4 = data.getIntExtra("passiveIncome4", passiveIncome4);
            passiveIncome5 = data.getIntExtra("passiveIncome5", passiveIncome5);

            updateCounterText();
            saveGameData();
        }
    }

    /**
     * Speichert alle Spieldaten in SharedPreferences
     */
    private void saveGameData() {
        SharedPreferences prefs = getSharedPreferences("game_prefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();

        // Grundlegende Daten speichern
        editor.putInt("clickValue", clickValue);
        editor.putInt("studicoinCounter", studicoinCounter);

        // Gesamtes passives Einkommen speichern
        editor.putInt("totalPassiveIncome", totalPassiveIncome);

        // Individuelle passive Einkommensstufen speichern
        editor.putInt("passiveIncome1", passiveIncome1);
        editor.putInt("passiveIncome2", passiveIncome2);
        editor.putInt("passiveIncome3", passiveIncome3);
        editor.putInt("passiveIncome4", passiveIncome4);
        editor.putInt("passiveIncome5", passiveIncome5);

        editor.apply();
    }

    /**
     * Lädt alle Spieldaten aus SharedPreferences
     */
    private void loadGameData() {
        SharedPreferences prefs = getSharedPreferences("game_prefs", MODE_PRIVATE);

        // Grundlegende Daten laden
        clickValue = prefs.getInt("clickValue", 1);
        studicoinCounter = prefs.getInt("studicoinCounter", 0);

        // Gesamtes passives Einkommen laden
        totalPassiveIncome = prefs.getInt("totalPassiveIncome", 0);

        // Individuelle passive Einkommensstufen laden
        passiveIncome1 = prefs.getInt("passiveIncome1", 0);
        passiveIncome2 = prefs.getInt("passiveIncome2", 0);
        passiveIncome3 = prefs.getInt("passiveIncome3", 0);
        passiveIncome4 = prefs.getInt("passiveIncome4", 0);
        passiveIncome5 = prefs.getInt("passiveIncome5", 0);
    }

    /**
     * Aktualisiert den Studicoin-Zähler in der UI
     */
    private void updateCounterText() {
        counterText.setText("Studicoin: " + studicoinCounter);
    }

    /**
     * Speichert den Spielstand, wenn die App pausiert wird
     */
    @Override
    protected void onPause() {
        super.onPause();
        saveGameData();
    }
}
