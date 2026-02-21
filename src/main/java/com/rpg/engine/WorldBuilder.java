package com.rpg.engine;

import com.rpg.model.*;
import java.util.*;

public class WorldBuilder {
    private static Map<String, Location> world;

    public static Map<String, Location> buildWorld() {
        world = new HashMap<>();

        Location village = new Location("Wioska", 
            "Spokojna wioska na skraju lasu. Mieszkańcy żyją tu bezpiecznie.", true);
        village.addPossibleEvent(Location.EventType.DIALOGUE);
        world.put("wioska", village);

        Location forest = new Location("Las",
            "Gęsty, ciemny las pełen niebezpieczeństw. Ścieżki giną w mroku.", false);
        forest.addPossibleEvent(Location.EventType.COMBAT);
        forest.addPossibleEvent(Location.EventType.TRAP);
        forest.addPossibleEvent(Location.EventType.TREASURE);
        forest.addPossibleEvent(Location.EventType.DIALOGUE);
        world.put("las", forest);

        Location cave = new Location("Jaskinia",
            "Mroczna jaskinia, z której dochodzą dziwne dźwięki. Czy coś tam mieszka?", false);
        cave.addPossibleEvent(Location.EventType.COMBAT);
        cave.addPossibleEvent(Location.EventType.TRAP);
        cave.addPossibleEvent(Location.EventType.TREASURE);
        world.put("jaskinia", cave);

        Location crypt = new Location("Krypta",
            "Starożytna krypta pełna nieumarłych. Powietrze przesiąknięte złem.", false);
        crypt.addPossibleEvent(Location.EventType.COMBAT);
        crypt.addPossibleEvent(Location.EventType.TRAP);
        crypt.addPossibleEvent(Location.EventType.TREASURE);
        world.put("krypta", crypt);

        Location ruins = new Location("Ruiny",
            "Ruiny starożytnej świątyni. Tu według legendy ukrywa się Cień.", false);
        ruins.addPossibleEvent(Location.EventType.COMBAT);
        ruins.addPossibleEvent(Location.EventType.TREASURE);
        ruins.setBossLocation(true);
        world.put("ruiny", ruins);

        Location swamp = new Location("Bagna",
            "Mroczne bagna, gdzie łatwo zgubić drogę. Niebezpieczne i zdradliwe.", false);
        swamp.addPossibleEvent(Location.EventType.TRAP);
        swamp.addPossibleEvent(Location.EventType.COMBAT);
        swamp.addPossibleEvent(Location.EventType.DIALOGUE);
        world.put("bagna", swamp);

        village.addConnection("las", forest);
        village.addConnection("bagna", swamp);

        forest.addConnection("wioska", village);
        forest.addConnection("jaskinia", cave);
        forest.addConnection("krypta", crypt);
        forest.addConnection("bagna", swamp);

        cave.addConnection("las", forest);
        cave.addConnection("ruiny", ruins);

        crypt.addConnection("las", forest);

        ruins.addConnection("jaskinia", cave);

        swamp.addConnection("wioska", village);
        swamp.addConnection("las", forest);

        return world;
    }

    public static Location getLocation(String name) {
        if (world == null) {
            buildWorld();
        }
        return world.get(name.toLowerCase());
    }

    public static Location getStartingLocation() {
        if (world == null) {
            buildWorld();
        }
        return world.get("wioska");
    }

    public static void printMap() {
        System.out.println("\n=== MAPA ŚWIATA ===");
        System.out.println("Wioska <---> Las <---> Jaskinia");
        System.out.println("   |           |           |");
        System.out.println(" Bagna         Krypta      Ruiny (BOSS)");
        System.out.println("=====================\n");
    }
}
