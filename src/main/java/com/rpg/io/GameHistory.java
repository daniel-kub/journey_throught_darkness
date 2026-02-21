package com.rpg.io;

import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class GameHistory {
    private static final String HISTORY_FILE = "historia.txt";
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public static void logEvent(String event) {
        try (FileWriter fw = new FileWriter(HISTORY_FILE, true);
             BufferedWriter bw = new BufferedWriter(fw)) {
            String timestamp = LocalDateTime.now().format(formatter);
            bw.write("[" + timestamp + "] " + event);
            bw.newLine();
        } catch (IOException e) {
            System.err.println("Błąd zapisu historii: " + e.getMessage());
        }
    }

    public static void logGameStart(String playerName) {
        logEvent("=== ROZPOCZĘCIE GRY ===");
        logEvent("Gracz: " + playerName);
    }

    public static void logLocationChange(String locationName) {
        logEvent("Przemieszczenie do: " + locationName);
    }

    public static void logCombatStart(String enemyName) {
        logEvent("Walka z: " + enemyName);
    }

    public static void logCombatEnd(boolean playerWon, int xpGained, int goldGained) {
        if (playerWon) {
            logEvent("Walka wygrana! Zdobyto " + xpGained + " XP i " + goldGained + " złota.");
        } else {
            logEvent("Walka przegrana!");
        }
    }

    public static void logTreasureFound(int gold, String itemName) {
        if (itemName != null) {
            logEvent("Znaleziono skarb: " + gold + " złota i przedmiot: " + itemName);
        } else {
            logEvent("Znaleziono skarb: " + gold + " złota");
        }
    }

    public static void logTrapTriggered(int damage) {
        logEvent("Uderzyła pułapka! Utrata " + damage + " HP");
    }

    public static void logTrapAvoided() {
        logEvent("Udało się uniknąć pułapki!");
    }

    public static void logLevelUp(int newLevel) {
        logEvent("AWANS! Osiągnięto poziom " + newLevel);
    }

    public static void logGameEnd(boolean gameWon, String reason) {
        logEvent("=== ZAKOŃCZENIE GRY ===");
        logEvent("Wynik: " + (gameWon ? "ZWYCIĘSTWO" : "PORAŻKA"));
        logEvent("Przyczyna: " + reason);
    }

    public static void logItemUsed(String itemName) {
        logEvent("Użyto przedmiotu: " + itemName);
    }

    public static void logItemEquipped(String itemName, String slot) {
        logEvent("Wyposażono " + slot + ": " + itemName);
    }

    public static void clearHistory() {
        try (FileWriter fw = new FileWriter(HISTORY_FILE, false)) {
        } catch (IOException e) {
            System.err.println("Błąd czyszczenia historii: " + e.getMessage());
        }
    }

    public static String getHistoryFilePath() {
        return new File(HISTORY_FILE).getAbsolutePath();
    }
}
