package com.rpg.io;

import com.rpg.model.*;
import java.io.*;
import java.nio.file.*;
import java.util.*;

public class JsonSerializer {
    
    public static String serializeGameState(GameState state) {
        StringBuilder sb = new StringBuilder();
        Player p = state.getPlayer();
        
        sb.append("{\n");
        sb.append("  \"player\": {\n");
        sb.append("    \"name\": \"").append(escapeJson(p.getName())).append("\",\n");
        sb.append("    \"hp\": ").append(p.getHp()).append(",\n");
        sb.append("    \"maxHp\": ").append(p.getMaxHp()).append(",\n");
        sb.append("    \"strength\": ").append(p.getStrength()).append(",\n");
        sb.append("    \"agility\": ").append(p.getAgility()).append(",\n");
        sb.append("    \"intelligence\": ").append(p.getIntelligence()).append(",\n");
        sb.append("    \"level\": ").append(p.getLevel()).append(",\n");
        sb.append("    \"xp\": ").append(p.getXp()).append(",\n");
        sb.append("    \"xpToNextLevel\": ").append(p.getXpToNextLevel()).append(",\n");
        sb.append("    \"gold\": ").append(p.getGold()).append(",\n");
        
        sb.append("    \"inventory\": [");
        List<Item> inv = p.getInventory();
        for (int i = 0; i < inv.size(); i++) {
            Item item = inv.get(i);
            sb.append("\n      {\"name\": \"").append(escapeJson(item.getName()))
              .append("\", \"type\": \"").append(item.getClass().getSimpleName())
              .append("\", \"value\": ").append(item.getValue());
            if (item instanceof Weapon) {
                sb.append(", \"damage\": ").append(((Weapon) item).getDamage())
                  .append(", \"requiredLevel\": ").append(((Weapon) item).getRequiredLevel());
            } else if (item instanceof Armor) {
                sb.append(", \"defense\": ").append(((Armor) item).getDefense())
                  .append(", \"requiredLevel\": ").append(((Armor) item).getRequiredLevel());
            } else if (item instanceof Potion) {
                sb.append(", \"healAmount\": ").append(((Potion) item).getHealAmount());
            }
            sb.append("}");
            if (i < inv.size() - 1) sb.append(",");
        }
        if (!inv.isEmpty()) sb.append("\n    ");
        sb.append("],\n");
        
        sb.append("    \"equippedWeapon\": ");
        if (p.getEquippedWeapon() != null) {
            sb.append("{\"name\": \"").append(escapeJson(p.getEquippedWeapon().getName()))
              .append("\", \"damage\": ").append(p.getEquippedWeapon().getDamage()).append("}");
        } else {
            sb.append("null");
        }
        sb.append(",\n");
        
        sb.append("    \"equippedArmor\": ");
        if (p.getEquippedArmor() != null) {
            sb.append("{\"name\": \"").append(escapeJson(p.getEquippedArmor().getName()))
              .append("\", \"defense\": ").append(p.getEquippedArmor().getDefense()).append("}");
        } else {
            sb.append("null");
        }
        sb.append("\n");
        sb.append("  },\n");
        
        sb.append("  \"currentLocation\": \"").append(escapeJson(state.getCurrentLocationName())).append("\",\n");
        sb.append("  \"stats\": {\n");
        sb.append("    \"battlesWon\": ").append(state.getBattlesWon()).append(",\n");
        sb.append("    \"battlesLost\": ").append(state.getBattlesLost()).append(",\n");
        sb.append("    \"treasuresFound\": ").append(state.getTreasuresFound()).append(",\n");
        sb.append("    \"totalTurns\": ").append(state.getTotalTurns()).append("\n");
        sb.append("  }\n");
        sb.append("}\n");
        
        return sb.toString();
    }

    private static String escapeJson(String s) {
        if (s == null) return "";
        return s.replace("\\", "\\\\")
                 .replace("\"", "\\\"")
                 .replace("\n", "\\n")
                 .replace("\r", "\\r")
                 .replace("\t", "\\t");
    }

    public static Map<String, Object> parseSimpleJson(String json) {
        Map<String, Object> result = new HashMap<>();
        return result;
    }
}
