package com.rpg.engine;

import com.rpg.model.Player;

public class LevelSystem {
    
    public static boolean checkLevelUp(Player player) {
        while (player.getXp() >= player.getXpToNextLevel()) {
            player.setXp(player.getXp() - player.getXpToNextLevel());
            player.setLevel(player.getLevel() + 1);
            player.setXpToNextLevel(player.getLevel() * 100);
            
            applyLevelUpBenefits(player);
            return true;
        }
        return false;
    }

    private static void applyLevelUpBenefits(Player player) {
        player.setMaxHp(player.getMaxHp() + 10);
        player.setHp(player.getMaxHp());
        player.setStrength(player.getStrength() + 2);
        player.setAgility(player.getAgility() + 2);
        player.setIntelligence(player.getIntelligence() + 2);
    }

    public static String getLevelUpMessage() {
        return "AWANS! Twoja postać osiągnęła wyższy poziom!\n" +
               "Odnowiono HP, zwiększono Siłę, Zwinność i Inteligencję!";
    }

    public static void printPlayerStats(Player player) {
        System.out.println("\n=== STATYSTYKI POSTACI ===");
        System.out.println("Imię: " + player.getName());
        System.out.println("Poziom: " + player.getLevel());
        System.out.println("Doświadczenie: " + player.getXp() + "/" + player.getXpToNextLevel());
        System.out.println("HP: " + player.getHp() + "/" + player.getMaxHp());
        System.out.println("Siła: " + player.getStrength());
        System.out.println("Zwinność: " + player.getAgility());
        System.out.println("Inteligencja: " + player.getIntelligence());
        System.out.println("Złoto: " + player.getGold());
        System.out.println("Obrażenia: " + player.getDamage());
        System.out.println("==========================\n");
    }
}
