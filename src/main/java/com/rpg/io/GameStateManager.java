package com.rpg.io;

import com.rpg.model.*;
import java.io.*;
import java.nio.file.*;

public class GameStateManager {
    private static final String SAVE_DIR = "saves";
    private static final String SAVE_FILE = SAVE_DIR + "/zapis.json";

    public static boolean saveGame(GameState state) {
        try {
            Files.createDirectories(Paths.get(SAVE_DIR));
            
            String json = JsonSerializer.serializeGameState(state);
            Files.writeString(Paths.get(SAVE_FILE), json);
            
            System.out.println("Gra została zapisana do: " + new File(SAVE_FILE).getAbsolutePath());
            return true;
        } catch (IOException e) {
            System.err.println("Błąd zapisu gry: " + e.getMessage());
            return false;
        }
    }

    public static GameState loadGame() {
        try {
            Path path = Paths.get(SAVE_FILE);
            if (!Files.exists(path)) {
                System.out.println("Nie znaleziono zapisanego pliku gry.");
                return null;
            }

            String json = Files.readString(path);
            return parseGameState(json);
        } catch (IOException e) {
            System.err.println("Błąd wczytywania gry: " + e.getMessage());
            return null;
        }
    }

    private static GameState parseGameState(String json) {
        try {
            String playerName = extractJsonValue(json, "name");
            int hp = Integer.parseInt(extractJsonValue(json, "hp"));
            int maxHp = Integer.parseInt(extractJsonValue(json, "maxHp"));
            int strength = Integer.parseInt(extractJsonValue(json, "strength"));
            int agility = Integer.parseInt(extractJsonValue(json, "agility"));
            int intelligence = Integer.parseInt(extractJsonValue(json, "intelligence"));
            int level = Integer.parseInt(extractJsonValue(json, "level"));
            int xp = Integer.parseInt(extractJsonValue(json, "xp"));
            int gold = Integer.parseInt(extractJsonValue(json, "gold"));
            String locationName = extractJsonValue(json, "currentLocation");

            Player player = new Player(playerName, maxHp, strength, agility, intelligence);
            player.setHp(hp);
            player.setLevel(level);
            player.setXp(xp);
            player.setGold(gold);

            int battlesWon = Integer.parseInt(extractJsonValue(json, "battlesWon"));
            int battlesLost = Integer.parseInt(extractJsonValue(json, "battlesLost"));
            int treasuresFound = Integer.parseInt(extractJsonValue(json, "treasuresFound"));
            player.setBattlesWon(battlesWon);
            player.setBattlesLost(battlesLost);
            player.setTreasuresFound(treasuresFound);

            GameState state = new GameState(player, locationName);
            state.setBattlesWon(battlesWon);
            state.setBattlesLost(battlesLost);
            state.setTreasuresFound(treasuresFound);

            System.out.println("Gra została wczytana!");
            return state;
        } catch (Exception e) {
            System.err.println("Błąd parsowania zapisu: " + e.getMessage());
            return null;
        }
    }

    private static String extractJsonValue(String json, String key) {
        String searchKey = "\"" + key + "\":";
        int index = json.indexOf(searchKey);
        if (index == -1) return "0";
        
        int valueStart = index + searchKey.length();
        while (valueStart < json.length() && java.lang.Character.isWhitespace(json.charAt(valueStart))) {
            valueStart++;
        }
        
        if (valueStart >= json.length()) return "0";
        
        if (json.charAt(valueStart) == '"') {
            int valueEnd = json.indexOf('"', valueStart + 1);
            return json.substring(valueStart + 1, valueEnd);
        } else {
            int valueEnd = valueStart;
            while (valueEnd < json.length() && (java.lang.Character.isDigit(json.charAt(valueEnd)) || json.charAt(valueEnd) == '-')) {
                valueEnd++;
            }
            return json.substring(valueStart, valueEnd).trim();
        }
    }

    public static boolean saveExists() {
        return Files.exists(Paths.get(SAVE_FILE));
    }

    public static void deleteSave() {
        try {
            Files.deleteIfExists(Paths.get(SAVE_FILE));
        } catch (IOException e) {
            System.err.println("Błąd usuwania zapisu: " + e.getMessage());
        }
    }
}
