package com.rpg.engine;

import com.rpg.model.*;
import java.util.*;

public class EventSystem {
    private static final Random random = new Random();

    private static final String[] TRAP_DESCRIPTIONS = {
        "Stąpasz na ukrytą pułapkę z kolcami!",
        "Z podłogi wyskakuje strzała!",
        "Nagle ziemia pod Tobą ustępuje!",
        "Spadasz w dół!"
    };

    private static final String[] TREASURE_DESCRIPTIONS = {
        "Znalazłeś ukryty skarb!",
        "Odkrywasz starożytny kufer!",
        "Na ścianie widzisz błysk złota!",
        "Pod kamieniem kryje się skarb!"
    };

    private static final String[] DIALOGUE_NPC = {
        "Kupiec", "Starzec", "Wędrowiec", "Strażnik"
    };

    public static Event generateRandomEvent(Location location, Player player) {
        if (location.isSafe()) {
            return null;
        }

        List<Location.EventType> events = location.getPossibleEvents();
        if (events.isEmpty()) {
            return null;
        }

        Location.EventType eventType = events.get(random.nextInt(events.size()));

        switch (eventType) {
            case COMBAT:
                return generateCombatEvent(location.getName());
            case TRAP:
                return generateTrapEvent();
            case TREASURE:
                return generateTreasureEvent(player.getLevel());
            case DIALOGUE:
                return generateDialogueEvent();
            default:
                return null;
        }
    }

    private static Event generateCombatEvent(String locationName) {
        Enemy enemy = createRandomEnemy(locationName);
        return new CombatEvent("Napotkałeś wrogiego " + enemy.getName() + "!", enemy);
    }

    private static Enemy createRandomEnemy(String locationName) {
        String[] enemies;
        int baseHp;
        int baseStr;
        int baseAgi;
        int baseInt;
        int xpReward;
        int goldReward;

        switch (locationName.toLowerCase()) {
            case "las":
                enemies = new String[]{"Wilk", "Dziki knur", "Bandysta", "Ogr"};
                baseHp = 30 + random.nextInt(20);
                baseStr = 5 + random.nextInt(5);
                baseAgi = 5 + random.nextInt(5);
                baseInt = 2 + random.nextInt(3);
                xpReward = 20 + random.nextInt(15);
                goldReward = 10 + random.nextInt(20);
                break;
            case "jaskinia":
                enemies = new String[]{"Nietoperz", "Goblin", "Jaskiniowy troll", "Skorpion"};
                baseHp = 40 + random.nextInt(25);
                baseStr = 8 + random.nextInt(6);
                baseAgi = 6 + random.nextInt(5);
                baseInt = 3 + random.nextInt(4);
                xpReward = 30 + random.nextInt(20);
                goldReward = 20 + random.nextInt(25);
                break;
            case "krypta":
                enemies = new String[]{"Szkielet", "Zombie", "Upiór", "Wampir"};
                baseHp = 50 + random.nextInt(30);
                baseStr = 10 + random.nextInt(7);
                baseAgi = 7 + random.nextInt(6);
                baseInt = 5 + random.nextInt(5);
                xpReward = 40 + random.nextInt(25);
                goldReward = 30 + random.nextInt(30);
                break;
            case "ruiny":
                enemies = new String[]{"Czarodziej", "Smok (Młody)", "Demon"};
                baseHp = 80 + random.nextInt(40);
                baseStr = 15 + random.nextInt(10);
                baseAgi = 10 + random.nextInt(8);
                baseInt = 8 + random.nextInt(7);
                xpReward = 60 + random.nextInt(40);
                goldReward = 50 + random.nextInt(50);
                break;
            case "bagna":
                enemies = new String[]{"Żaba", "Ognik", "Topielec"};
                baseHp = 25 + random.nextInt(15);
                baseStr = 4 + random.nextInt(4);
                baseAgi = 8 + random.nextInt(5);
                baseInt = 6 + random.nextInt(4);
                xpReward = 15 + random.nextInt(10);
                goldReward = 5 + random.nextInt(15);
                break;
            default:
                enemies = new String[]{"Szczur", "Bandysta"};
                baseHp = 20 + random.nextInt(10);
                baseStr = 3 + random.nextInt(3);
                baseAgi = 4 + random.nextInt(3);
                baseInt = 2 + random.nextInt(2);
                xpReward = 10 + random.nextInt(10);
                goldReward = 5 + random.nextInt(10);
        }

        String name = enemies[random.nextInt(enemies.length)];
        return new Enemy(name, baseHp, baseStr, baseAgi, baseInt, xpReward, goldReward);
    }

    public static Enemy createBoss() {
        return new Enemy("Ciemny Władca", 150, 20, 12, 15, 100, 200);
    }

    private static Event generateTrapEvent() {
        String description = TRAP_DESCRIPTIONS[random.nextInt(TRAP_DESCRIPTIONS.length)];
        int damage = 10 + random.nextInt(15);
        int difficulty = 10 + random.nextInt(10);
        return new TrapEvent(description, damage, difficulty);
    }

    private static Event generateTreasureEvent(int playerLevel) {
        String description = TREASURE_DESCRIPTIONS[random.nextInt(TREASURE_DESCRIPTIONS.length)];
        int gold = 15 + random.nextInt(25) + playerLevel * 5;
        
        Item item = null;
        if (random.nextInt(100) < 30) {
            item = generateRandomItem(playerLevel);
        }
        
        return new TreasureEvent(description, item, gold);
    }

    private static Item generateRandomItem(int playerLevel) {
        int roll = random.nextInt(100);
        
        if (roll < 40) {
            return new Potion("Mikstura leczenia", "Przywraca 30 HP", 20, 30);
        } else if (roll < 60) {
            int dmg = 5 + playerLevel * 2 + random.nextInt(5);
            return new Weapon("Miecz", "Zwykły stalowy miecz", 50, dmg, playerLevel);
        } else if (roll < 80) {
            int def = 3 + playerLevel + random.nextInt(3);
            return new Armor("Kolczuga", "Zwykła kolczuga", 40, def, playerLevel);
        } else {
            return new Potion("Duża mikstura", "Przywraca 50 HP", 35, 50);
        }
    }

    private static Event generateDialogueEvent() {
        String npcName = DIALOGUE_NPC[random.nextInt(DIALOGUE_NPC.length)];
        String description = "Spotkałeś " + npcName + " w drodze.";
        
        Map<String, String> choices = new HashMap<>();
        choices.put("1", "Porozmawiaj");
        choices.put("2", "Ignoruj");
        
        return new DialogueEvent(description, npcName, choices, new String[]{"1", "2"});
    }
}
