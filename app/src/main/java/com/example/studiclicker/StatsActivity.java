/**
 * StatsActivity.java
 * 
 * Diese Activity zeigt dem Spieler detaillierte Statistiken über seinen
 * aktuellen Spielfortschritt und Upgrades an.
 */
package com.example.studiclicker;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class StatsActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stats);

        // Holen der Spieldaten aus dem Intent
        int studicoinCounter = getIntent().getIntExtra("studicoinCounter", 0);
        int clickValue = getIntent().getIntExtra("clickValue", 1);

        // Gesamtes passives Einkommen abrufen
        int totalPassiveIncome = getIntent().getIntExtra("totalPassiveIncome", 0);

        // Individuelle passive Einkommensstufen abrufen
        int passiveIncome1 = getIntent().getIntExtra("passiveIncome1", 0);
        int passiveIncome2 = getIntent().getIntExtra("passiveIncome2", 0);
        int passiveIncome3 = getIntent().getIntExtra("passiveIncome3", 0);
        int passiveIncome4 = getIntent().getIntExtra("passiveIncome4", 0);
        int passiveIncome5 = getIntent().getIntExtra("passiveIncome5", 0);

        // Einkommen pro Level für jedes Upgrade (sollte mit UpgradeActivity übereinstimmen)
        final int incomePerLevel1 = 1;  // 1 Coin pro Sekunde
        final int incomePerLevel2 = 4;  // 4 Coins pro Sekunde
        final int incomePerLevel3 = 10; // 10 Coins pro Sekunde
        final int incomePerLevel4 = 25; // 25 Coins pro Sekunde
        final int incomePerLevel5 = 75; // 75 Coins pro Sekunde

        // Statistik-Text formatieren und anzeigen
        TextView statsText = findViewById(R.id.statsText);
        statsText.setText(
                "Gesammelte Studicoins: " + studicoinCounter +
                        "\n\nClick-Wert: " + clickValue + " Coins pro Klick" +
                        "\nPassives Einkommen gesamt: " + totalPassiveIncome + " Coins pro Sekunde" +
                        "\n\nClick-Level: " + clickValue +
                        "\n\nPassive Upgrades:" +
                        "\nGrünes P: Level " + passiveIncome1 + " (" + (passiveIncome1 * incomePerLevel1) + " Coins/s)" +
                        "\nLerngruppe: Level " + passiveIncome2 + " (" + (passiveIncome2 * incomePerLevel2) + " Coins/s)" +
                        "\nNotizen von früheren Semestern: Level " + passiveIncome3 + " (" + (passiveIncome3 * incomePerLevel3) + " Coins/s)" +
                        "\nPrivater Tutor: Level " + passiveIncome4 + " (" + (passiveIncome4 * incomePerLevel4) + " Coins/s)" +
                        "\nVideotutorial: Level " + passiveIncome5 + " (" + (passiveIncome5 * incomePerLevel5) + " Coins/s)"
        );

        // Return-Button Listener einrichten - zurück zur vorherigen Activity
        Button backButton = findViewById(R.id.returnButton);
        backButton.setOnClickListener(v -> {
            finish(); // Schließt diese Activity und kehrt zur vorherigen zurück
        });
    }
}
