package com.example.studiclicker;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

/**
 * UpgradeActivity - Verwaltet die Upgrade-Funktionen der StudiClicker App
 * 
 * Diese Aktivität ermöglicht:
 * - Das Upgraden des Click-Werts (mehr Coins pro Klick)
 * - Den Kauf von passiven Einkommensquellen (automatische Coins pro Sekunde)
 * - Die Speicherung und Verwaltung aller Upgrade-Daten
 */
public class UpgradeActivity extends AppCompatActivity {
    // Spielvariablen
    private int studicoinCounter;        // Aktuelle Anzahl an Studicoins
    private int clickValue;              // Aktueller Wert pro Klick
    private double upgradeCost;          // Kosten für das nächste Click-Upgrade
    private int totalPassiveIncome;      // Gesamtes passives Einkommen pro Sekunde
    
    // UI-Elemente
    private TextView errorText;          // Textfeld für Fehlermeldungen
    private Button upgradeButton;        // Button für Click-Upgrades
    private Button returnButton;         // Button zur Rückkehr zum Hauptbildschirm
    private Button passiveUpgradeButton1;
    private Button passiveUpgradeButton2;
    private Button passiveUpgradeButton3;
    private Button passiveUpgradeButton4;
    private Button passiveUpgradeButton5;

    // Passive Einkommensebenen für jeden Upgrade-Typ
    private int passiveIncome1;          // Level des "Grünes P" Upgrades
    private int passiveIncome2;          // Level des "Lerngruppe" Upgrades
    private int passiveIncome3;          // Level des "Notizen" Upgrades
    private int passiveIncome4;          // Level des "Tutor" Upgrades
    private int passiveIncome5;          // Level des "Videotutorial" Upgrades

    // Aktuelle Kosten für jedes passive Upgrade
    private double passiveUpgradeCost1;  // Kosten für nächstes "Grünes P" Upgrade
    private double passiveUpgradeCost2;  // Kosten für nächstes "Lerngruppe" Upgrade
    private double passiveUpgradeCost3;  // Kosten für nächstes "Notizen" Upgrade
    private double passiveUpgradeCost4;  // Kosten für nächstes "Tutor" Upgrade
    private double passiveUpgradeCost5;  // Kosten für nächstes "Videotutorial" Upgrade

    // Konstanten für passives Einkommen pro Level jedes Upgrade-Typs
    private final int incomePerLevel1 = 1;   // 1 Coin pro Sekunde für "Grünes P"
    private final int incomePerLevel2 = 4;   // 4 Coins pro Sekunde für "Lerngruppe"
    private final int incomePerLevel3 = 10;  // 10 Coins pro Sekunde für "Notizen"
    private final int incomePerLevel4 = 25;  // 25 Coins pro Sekunde für "Tutor"
    private final int incomePerLevel5 = 75;  // 75 Coins pro Sekunde für "Videotutorial"

    // Skalierungsfaktoren für die Upgrade-Kosten
    // Höhere Faktoren bedeuten schneller steigende Kosten
    private final double scalingFactor1 = 1.15;  // Grünes P
    private final double scalingFactor2 = 1.18;  // Lerngruppe
    private final double scalingFactor3 = 1.21;  // Notizen
    private final double scalingFactor4 = 1.25;  // Tutor
    private final double scalingFactor5 = 1.30;  // Videotutorial

    // Handler für zeitgesteuerte Aktionen (z.B. Fehlermeldungen ausblenden)
    private Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upgrade);

        // Laden der gespeicherten Spieldaten
        loadGameData();
        
        // Initialisieren der UI-Elemente
        initializeUIElements();
        
        // Aktualisieren der Button-Texte mit den aktuellen Werten
        updateButtonTexts();

        // Click-Upgrade Button Listener
        setupClickUpgradeButton();
        
        // Passive Upgrade Button Listeners
        setupPassiveUpgradeButtons();
        
        // Return Button Listener für die Rückkehr zum Hauptbildschirm
        setupReturnButton();
    }

    /**
     * Lädt alle gespeicherten Spieldaten aus den SharedPreferences
     */
    private void loadGameData() {
        SharedPreferences prefs = getSharedPreferences("game_prefs", MODE_PRIVATE);
        
        // Lade Click-Upgrade Daten
        clickValue = prefs.getInt("clickValue", 1);
        upgradeCost = prefs.getFloat("upgradeCost", (float) (10 * Math.pow(1.15, clickValue - 1)));

        // Lade passive Einkommenslevels
        passiveIncome1 = prefs.getInt("passiveIncome1", 0);
        passiveIncome2 = prefs.getInt("passiveIncome2", 0);
        passiveIncome3 = prefs.getInt("passiveIncome3", 0);
        passiveIncome4 = prefs.getInt("passiveIncome4", 0);
        passiveIncome5 = prefs.getInt("passiveIncome5", 0);

        // Berechne aktuelle Kosten für passive Upgrades basierend auf dem Level
        passiveUpgradeCost1 = prefs.getFloat("passiveUpgradeCost1", (float) (50 * Math.pow(scalingFactor1, passiveIncome1)));
        passiveUpgradeCost2 = prefs.getFloat("passiveUpgradeCost2", (float) (250 * Math.pow(scalingFactor2, passiveIncome2)));
        passiveUpgradeCost3 = prefs.getFloat("passiveUpgradeCost3", (float) (1000 * Math.pow(scalingFactor3, passiveIncome3)));
        passiveUpgradeCost4 = prefs.getFloat("passiveUpgradeCost4", (float) (5000 * Math.pow(scalingFactor4, passiveIncome4)));
        passiveUpgradeCost5 = prefs.getFloat("passiveUpgradeCost5", (float) (20000 * Math.pow(scalingFactor5, passiveIncome5)));

        // Berechne Gesamteinkommen pro Sekunde
        calculateTotalPassiveIncome();
        
        // Hole aktuelle Studicoins von der Intent
        studicoinCounter = getIntent().getIntExtra("studicoinCounter", 0);
    }

    /**
     * Initialisiert alle UI-Elemente durch Referenzierung aus dem Layout
     */
    private void initializeUIElements() {
        errorText = findViewById(R.id.errorText);
        upgradeButton = findViewById(R.id.upgradeButton);
        returnButton = findViewById(R.id.returnButton);
        passiveUpgradeButton1 = findViewById(R.id.passiveUpgradeButton1);
        passiveUpgradeButton2 = findViewById(R.id.passiveUpgradeButton2);
        passiveUpgradeButton3 = findViewById(R.id.passiveUpgradeButton3);
        passiveUpgradeButton4 = findViewById(R.id.passiveUpgradeButton4);
        passiveUpgradeButton5 = findViewById(R.id.passiveUpgradeButton5);
    }

    /**
     * Berechnet das Gesamteinkommen pro Sekunde aus allen passiven Quellen
     */
    private void calculateTotalPassiveIncome() {
        totalPassiveIncome = (passiveIncome1 * incomePerLevel1) +
                (passiveIncome2 * incomePerLevel2) +
                (passiveIncome3 * incomePerLevel3) +
                (passiveIncome4 * incomePerLevel4) +
                (passiveIncome5 * incomePerLevel5);
    }

    /**
     * Richtet den Click-Upgrade Button mit dem entsprechenden Click-Listener ein
     */
    private void setupClickUpgradeButton() {
        upgradeButton.setOnClickListener(v -> {
            if (studicoinCounter >= upgradeCost) {
                // Kaufe das Upgrade
                studicoinCounter -= upgradeCost;
                clickValue++;

                // Berechne neue Upgrade-Kosten mit exponentieller Formel
                upgradeCost = 10 * Math.pow(1.15, clickValue - 1);
                
                // Aktualisiere UI und speichere Daten
                updateButtonTexts();
                saveUpgradeData();
            } else {
                // Zeige Fehlermeldung, wenn nicht genug Coins vorhanden sind
                showErrorMessage("Nicht genug Studicoins für Click-Upgrade!");
            }
        });
    }

    /**
     * Richtet alle passiven Upgrade Buttons mit Click-Listenern ein
     */
    private void setupPassiveUpgradeButtons() {
        // Grünes P Upgrade Button
        passiveUpgradeButton1.setOnClickListener(v -> {
            if (studicoinCounter >= passiveUpgradeCost1) {
                studicoinCounter -= passiveUpgradeCost1;
                passiveIncome1++;

                // Berechne neue Kosten für das nächste Upgrade
                passiveUpgradeCost1 = 50 * Math.pow(scalingFactor1, passiveIncome1);

                // Aktualisiere Gesamteinkommen
                calculateTotalPassiveIncome();
                updateButtonTexts();
                saveUpgradeData();
            } else {
                showErrorMessage("Nicht genug Studicoins für Grünes P Upgrade!");
            }
        });

        // Lerngruppe Upgrade Button
        passiveUpgradeButton2.setOnClickListener(v -> {
            if (studicoinCounter >= passiveUpgradeCost2) {
                studicoinCounter -= passiveUpgradeCost2;
                passiveIncome2++;

                // Berechne neue Kosten für das nächste Upgrade
                passiveUpgradeCost2 = 250 * Math.pow(scalingFactor2, passiveIncome2);

                // Aktualisiere Gesamteinkommen
                calculateTotalPassiveIncome();
                updateButtonTexts();
                saveUpgradeData();
            } else {
                showErrorMessage("Nicht genug Studicoins für Lerngruppe Upgrade!");
            }
        });

        // Notizen Upgrade Button
        passiveUpgradeButton3.setOnClickListener(v -> {
            if (studicoinCounter >= passiveUpgradeCost3) {
                studicoinCounter -= passiveUpgradeCost3;
                passiveIncome3++;

                // Berechne neue Kosten für das nächste Upgrade
                passiveUpgradeCost3 = 1000 * Math.pow(scalingFactor3, passiveIncome3);

                // Aktualisiere Gesamteinkommen
                calculateTotalPassiveIncome();
                updateButtonTexts();
                saveUpgradeData();
            } else {
                showErrorMessage("Nicht genug Studicoins für Notizen Upgrade!");
            }
        });

        // Tutor Upgrade Button
        passiveUpgradeButton4.setOnClickListener(v -> {
            if (studicoinCounter >= passiveUpgradeCost4) {
                studicoinCounter -= passiveUpgradeCost4;
                passiveIncome4++;

                // Berechne neue Kosten für das nächste Upgrade
                passiveUpgradeCost4 = 5000 * Math.pow(scalingFactor4, passiveIncome4);

                // Aktualisiere Gesamteinkommen
                calculateTotalPassiveIncome();
                updateButtonTexts();
                saveUpgradeData();
            } else {
                showErrorMessage("Nicht genug Studicoins für Tutor Upgrade!");
            }
        });

        // Videotutorial Upgrade Button
        passiveUpgradeButton5.setOnClickListener(v -> {
            if (studicoinCounter >= passiveUpgradeCost5) {
                studicoinCounter -= passiveUpgradeCost5;
                passiveIncome5++;

                // Berechne neue Kosten für das nächste Upgrade
                passiveUpgradeCost5 = 20000 * Math.pow(scalingFactor5, passiveIncome5);

                // Aktualisiere Gesamteinkommen
                calculateTotalPassiveIncome();
                updateButtonTexts();
                saveUpgradeData();
            } else {
                showErrorMessage("Nicht genug Studicoins für Video Tutorial Upgrade!");
            }
        });
    }

    /**
     * Richtet den Return Button ein, der zur MainActivity zurückkehrt
     * und alle relevanten Daten über Intent übergibt
     */
    private void setupReturnButton() {
        returnButton.setOnClickListener(v -> {
            Intent resultIntent = new Intent();
            
            // Übergebe alle relevanten Daten zurück an die MainActivity
            resultIntent.putExtra("clickValue", clickValue);
            resultIntent.putExtra("studicoinCounter", studicoinCounter);
            resultIntent.putExtra("totalPassiveIncome", totalPassiveIncome);

            // Übergebe auch die einzelnen passiven Einkommenswerte
            resultIntent.putExtra("passiveIncome1", passiveIncome1);
            resultIntent.putExtra("passiveIncome2", passiveIncome2);
            resultIntent.putExtra("passiveIncome3", passiveIncome3);
            resultIntent.putExtra("passiveIncome4", passiveIncome4);
            resultIntent.putExtra("passiveIncome5", passiveIncome5);

            setResult(Activity.RESULT_OK, resultIntent);
            finish();
        });
    }

    /**
     * Speichert alle Spieldaten in SharedPreferences
     */
    private void saveUpgradeData() {
        SharedPreferences prefs = getSharedPreferences("game_prefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();

        // Speichere Click-Upgrade Daten
        editor.putInt("clickValue", clickValue);
        editor.putFloat("upgradeCost", (float) upgradeCost);

        // Speichere alle passiven Einkommenslevels
        editor.putInt("passiveIncome1", passiveIncome1);
        editor.putInt("passiveIncome2", passiveIncome2);
        editor.putInt("passiveIncome3", passiveIncome3);
        editor.putInt("passiveIncome4", passiveIncome4);
        editor.putInt("passiveIncome5", passiveIncome5);

        // Speichere alle passiven Upgrade-Kosten
        editor.putFloat("passiveUpgradeCost1", (float) passiveUpgradeCost1);
        editor.putFloat("passiveUpgradeCost2", (float) passiveUpgradeCost2);
        editor.putFloat("passiveUpgradeCost3", (float) passiveUpgradeCost3);
        editor.putFloat("passiveUpgradeCost4", (float) passiveUpgradeCost4);
        editor.putFloat("passiveUpgradeCost5", (float) passiveUpgradeCost5);

        // Speichere Gesamteinkommen für einfacheren Zugriff
        editor.putInt("totalPassiveIncome", totalPassiveIncome);

        editor.apply();
    }

    /**
     * Aktualisiert die Button-Texte mit den aktuellen Werten und Kosten
     */
    private void updateButtonTexts() {
        upgradeButton.setText("Chat GPT Upgrade für " + (int) upgradeCost + " Coins");

        passiveUpgradeButton1.setText("Grünes P (Lvl " + passiveIncome1 + "): " +
                (int) passiveUpgradeCost1 + " Coins | +" + incomePerLevel1 + "/s");

        passiveUpgradeButton2.setText("Lerngruppe (Lvl " + passiveIncome2 + "): " +
                (int) passiveUpgradeCost2 + " Coins | +" + incomePerLevel2 + "/s");

        passiveUpgradeButton3.setText("Notizen von früheren Semestern (Lvl " + passiveIncome3 + "): " +
                (int) passiveUpgradeCost3 + " Coins | +" + incomePerLevel3 + "/s");

        passiveUpgradeButton4.setText("Privater Tutor (Lvl " + passiveIncome4 + "): " +
                (int) passiveUpgradeCost4 + " Coins | +" + incomePerLevel4 + "/s");

        passiveUpgradeButton5.setText("Videotutorial mit Indischem Akzent (Lvl " + passiveIncome5 + "): " +
                (int) passiveUpgradeCost5 + " Coins | +" + incomePerLevel5 + "/s");
    }

    /**
     * Zeigt eine Fehlermeldung an und blendet sie nach 5 Sekunden automatisch aus
     * 
     * @param message Die anzuzeigende Fehlermeldung
     */
    private void showErrorMessage(String message) {
        errorText.setText(message);
        errorText.setVisibility(View.VISIBLE);

        // Blende Meldung nach 5 Sekunden aus
        handler.removeCallbacksAndMessages(null); // Entferne vorherige Timer
        handler.postDelayed(() -> {
            errorText.setVisibility(View.INVISIBLE);
        }, 5000); // 5 Sekunden = 5000 Millisekunden
    }
}
