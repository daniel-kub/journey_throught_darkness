package com.rpg.engine;

import com.rpg.model.*;
import java.util.Random;

public class CombatSystem {
    private static final Random random = new Random();

    public static final int ATTACK = 1;
    public static final int HEAL = 2;
    public static final int RUN = 3;

    public static boolean playerAttack(Player player, Enemy enemy) {
        int damage = calculateDamage(player.getDamage());
        
        if (rollCriticalHit(player.getAgility())) {
            damage *= 2;
            System.out.println("Krytyczne uderzenie!");
        }
        
        enemy.takeDamage(damage);
        System.out.println("Zadajesz " + damage + " obrażeń wrogowi " + enemy.getName() + 
                          " (HP: " + enemy.getHp() + "/" + enemy.getMaxHp() + ")");
        
        return enemy.getHp() <= 0;
    }

    public static boolean enemyAttack(Player player, Enemy enemy) {
        if (tryEvade(player.getAgility())) {
            System.out.println("Uniknąłeś ataku!");
            return false;
        }
        
        int damage = calculateDamage(enemy.getDamage());
        player.takeDamage(damage);
        System.out.println(enemy.getName() + " zadaje Ci " + damage + " obrażeń!" +
                          " (HP: " + player.getHp() + "/" + player.getMaxHp() + ")");
        
        return player.getHp() <= 0;
    }

    public static boolean tryRun(int playerAgility, int enemyAgility) {
        int escapeChance = playerAgility * 3 + 20;
        int roll = random.nextInt(100);
        
        if (roll < escapeChance) {
            System.out.println("Udało Ci się uciec!");
            return true;
        } else {
            System.out.println("Nie udało Ci się uciec!");
            return false;
        }
    }

    private static int calculateDamage(int baseDamage) {
        int variance = random.nextInt(5) - 2;
        return Math.max(1, baseDamage + variance);
    }

    private static boolean rollCriticalHit(int agility) {
        return random.nextInt(100) < agility;
    }

    private static boolean tryEvade(int agility) {
        return random.nextInt(100) < agility * 2;
    }

    public static void printCombatStatus(Player player, Enemy enemy) {
        System.out.println("\n=== WALKA ===");
        System.out.println(player.getName() + ": " + player.getHp() + "/" + player.getMaxHp() + " HP");
        System.out.println(enemy.getName() + ": " + enemy.getHp() + "/" + enemy.getMaxHp() + " HP");
        System.out.println("=============\n");
    }

    public static void printCombatOptions() {
        System.out.println("Dostępne akcje:");
        System.out.println("  atak   - zaatakuj przeciwnika");
        System.out.println("  leczenie - użyj mikstury (jeśli masz)");
        System.out.println("  ucieczka - spróbuj uciec z walki");
    }
}
