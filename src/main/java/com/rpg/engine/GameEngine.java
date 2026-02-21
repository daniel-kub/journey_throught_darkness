package com.rpg.engine;

import com.rpg.io.*;
import com.rpg.model.*;
import java.util.*;

public class GameEngine {
    private Player player;
    private Location currentLocation;
    private Map<String, Location> world;
    private boolean gameRunning;
    private boolean gameWon;
    private GameState gameState;
    private int turnCount;
    private Scanner scanner;

    public GameEngine() {
        this.scanner = new Scanner(System.in);
        this.gameRunning = false;
        this.turnCount = 0;
    }

    public void start() {
        System.out.println("\n" + "=".repeat(50));
        System.out.println("   WĘDRÓWKA PRZEZ MROKI");
        System.out.println("   (Journey through Darkness)");
        System.out.println("=".repeat(50) + "\n");

        if (GameStateManager.saveExists()) {
            System.out.println("Znaleziono zapisaną grę.");
            System.out.println("1 - Wczytaj grę");
            System.out.println("2 - Nowa gra");
            System.out.print("Wybierz: ");
            
            String choice = scanner.nextLine().trim();
            if (choice.equals("1")) {
                loadGame();
                if (gameState != null) {
                    runGameLoop();
                    return;
                }
            }
        }

        createNewGame();
        runGameLoop();
    }

    private void createNewGame() {
        GameHistory.clearHistory();
        
        System.out.println("Tworzenie nowej postaci...");
        System.out.print("Podaj imię swojego bohatera: ");
        String name = scanner.nextLine().trim();
        if (name.isEmpty()) name = "Bohater";

        System.out.println("\nRozdź punkty atrybutów (łącznie 15 punktów):");
        System.out.println("Minimalnie 3, maksymalnie 7 na atrybut.\n");

        int totalPoints = 15;
        int strength = getAttribute("Siła", totalPoints);
        totalPoints -= strength;
        int agility = getAttribute("Zwinność", totalPoints);
        totalPoints -= agility;
        int intelligence = getAttribute("Inteligencja", totalPoints);
        totalPoints -= intelligence;

        int hp = 50 + (strength * 5);

        player = new Player(name, hp, strength, agility, intelligence);
        player.addItem(new Potion("Mikstura leczenia", "Mała mikstura", 10, 20));

        world = WorldBuilder.buildWorld();
        currentLocation = WorldBuilder.getStartingLocation();

        gameState = new GameState(player, currentLocation.getName());
        
        GameHistory.logGameStart(player.getName());
        
        System.out.println("\n=== TWÓJ BOHATER ===");
        LevelSystem.printPlayerStats(player);
    }

    private int getAttribute(String attrName, int maxPoints) {
        int value;
        while (true) {
            System.out.print(attrName + " (" + maxPoints + " pkt pozostało): ");
            try {
                value = Integer.parseInt(scanner.nextLine().trim());
                if (value >= 3 && value <= 7 && value <= maxPoints) {
                    break;
                }
                System.out.println("Wartość musi być między 3 a 7 (i nie więcej niż dostępne punkty).");
            } catch (NumberFormatException e) {
                System.out.println("Podaj liczbę!");
            }
        }
        return value;
    }

    private void loadGame() {
        gameState = GameStateManager.loadGame();
        if (gameState != null) {
            player = gameState.getPlayer();
            world = WorldBuilder.buildWorld();
            currentLocation = WorldBuilder.getLocation(gameState.getCurrentLocationName());
            turnCount = gameState.getTotalTurns();
            System.out.println("Witaj z powrotem, " + player.getName() + "!");
        }
    }

    private void runGameLoop() {
        gameRunning = true;
        
        System.out.println("\nWitaj w świecie Mroków, " + player.getName() + "!");
        System.out.println("Twoim celem jest dotrzeć do Ruin i pokonać Czarnego Władcę.\n");
        
        printLocationInfo();

        while (gameRunning && player.isAlive()) {
            turnCount++;
            gameState.incrementTurns();
            
            if (!currentLocation.isSafe() && !currentLocation.isBossLocation()) {
                Event event = EventSystem.generateRandomEvent(currentLocation, player);
                if (event != null) {
                    handleEvent(event);
                }
            } else if (currentLocation.isBossLocation() && !gameWon) {
                handleBossEncounter();
            }

            if (!player.isAlive()) {
                endGame(false, "Twoja postać zginęła w walce.");
                break;
            }

            if (gameWon) {
                break;
            }

            printPrompt();
            String command = scanner.nextLine().trim().toLowerCase();
            processCommand(command);
        }

        if (!player.isAlive()) {
            endGame(false, "Twoja postać zginęła.");
        }
    }

    private void printLocationInfo() {
        System.out.println("\n" + "=".repeat(40));
        System.out.println("LOKACJA: " + currentLocation.getName().toUpperCase());
        System.out.println(currentLocation.getDescription());
        
        List<String> directions = currentLocation.getAvailableDirections();
        System.out.print("Dostępne kierunki: ");
        for (int i = 0; i < directions.size(); i++) {
            System.out.print(directions.get(i));
            if (i < directions.size() - 1) System.out.print(", ");
        }
        System.out.println("\n" + "=".repeat(40));
    }

    private void printPrompt() {
        System.out.print("\n> ");
    }

    private void handleEvent(Event event) {
        System.out.println("\n--- " + event.getDescription() + " ---");

        if (event instanceof CombatEvent) {
            handleCombat(((CombatEvent) event).getEnemy());
        } else if (event instanceof TrapEvent) {
            handleTrap((TrapEvent) event);
        } else if (event instanceof TreasureEvent) {
            handleTreasure((TreasureEvent) event);
        } else if (event instanceof DialogueEvent) {
            handleDialogue((DialogueEvent) event);
        }
    }

    private void handleCombat(Enemy enemy) {
        GameHistory.logCombatStart(enemy.getName());
        
        System.out.println("\nNapotkałeś: " + enemy.getName() + 
                          " (HP: " + enemy.getHp() + ", Siła: " + enemy.getStrength() + ")");
        
        CombatSystem.printCombatOptions();

        while (player.isAlive() && enemy.isAlive()) {
            CombatSystem.printCombatStatus(player, enemy);
            
            String action = scanner.nextLine().trim().toLowerCase();
            
            switch (action) {
                case "atak":
                    if (CombatSystem.playerAttack(player, enemy)) {
                        System.out.println("Pokonałeś " + enemy.getName() + "!");
                        int xp = enemy.getXpReward();
                        int gold = enemy.getGoldReward();
                        player.addXp(xp);
                        player.addGold(gold);
                        player.incrementBattlesWon();
                        gameState.setBattlesWon(player.getBattlesWon());
                        
                        if (LevelSystem.checkLevelUp(player)) {
                            System.out.println(LevelSystem.getLevelUpMessage());
                            GameHistory.logLevelUp(player.getLevel());
                        }
                        
                        GameHistory.logCombatEnd(true, xp, gold);
                        return;
                    }
                    
                    if (CombatSystem.enemyAttack(player, enemy)) {
                        player.incrementBattlesLost();
                        gameState.setBattlesLost(player.getBattlesLost());
                        GameHistory.logCombatEnd(false, 0, 0);
                        return;
                    }
                    break;
                    
                case "leczenie":
                    Potion potion = player.findPotionInInventory();
                    if (potion != null) {
                        player.usePotion(potion);
                        GameHistory.logItemUsed(potion.getName());
                        System.out.println("Użyłeś " + potion.getName() + ". Twoje HP: " + player.getHp());
                        
                        if (CombatSystem.enemyAttack(player, enemy)) {
                            player.incrementBattlesLost();
                            gameState.setBattlesLost(player.getBattlesLost());
                            GameHistory.logCombatEnd(false, 0, 0);
                            return;
                        }
                    } else {
                        System.out.println("Nie masz mikstury!");
                    }
                    break;
                    
                case "ucieczka":
                    if (CombatSystem.tryRun(player.getAgility(), enemy.getAgility())) {
                        GameHistory.logCombatEnd(false, 0, 0);
                        return;
                    }
                    
                    if (CombatSystem.enemyAttack(player, enemy)) {
                        player.incrementBattlesLost();
                        gameState.setBattlesLost(player.getBattlesLost());
                        GameHistory.logCombatEnd(false, 0, 0);
                        return;
                    }
                    break;
                    
                default:
                    System.out.println("Nieznana komenda. Użyj: atak, leczenie, ucieczka");
            }
        }
    }

    private void handleTrap(TrapEvent event) {
        int saveRoll = new Random().nextInt(20) + 1 + player.calculateTrapSave();
        
        System.out.println("Test zwinności: " + saveRoll + " vs " + event.getSaveDifficulty());
        
        if (saveRoll >= event.getSaveDifficulty()) {
            System.out.println("Udało Ci się uniknąć pułapki!");
            GameHistory.logTrapAvoided();
        } else {
            player.takeDamage(event.getDamage());
            System.out.println("Uderzyła pułapka! Tracisz " + event.getDamage() + " HP.");
            GameHistory.logTrapTriggered(event.getDamage());
        }
    }

    private void handleTreasure(TreasureEvent event) {
        player.addGold(event.getGold());
        player.incrementTreasuresFound();
        gameState.setTreasuresFound(player.getTreasuresFound());
        
        System.out.println("Znalazłeś " + event.getGold() + " złota!");
        
        if (event.getItem() != null) {
            player.addItem(event.getItem());
            System.out.println("oraz: " + event.getItem().getName() + " - " + event.getItem().getDescription());
            GameHistory.logTreasureFound(event.getGold(), event.getItem().getName());
        } else {
            GameHistory.logTreasureFound(event.getGold(), null);
        }
    }

    private void handleDialogue(DialogueEvent event) {
        System.out.println("\n" + event.getNpcName() + " mówi: \"Witaj, podróżniku. Czy możesz mi pomóc?\"");
        System.out.println("1 - Tak, pomogę");
        System.out.println("2 - Nie, mam ważniejsze sprawy");
        
        String choice = scanner.nextLine().trim();
        
        if (choice.equals("1")) {
            System.out.println(event.getNpcName() + " dziękuje Ci i daje drobny prezent.");
            player.addItem(new Potion("Mikstura", "Podarowana mikstura", 10, 15));
        } else {
            System.out.println("Kontynuujesz swoją podróż.");
        }
    }

    private void handleBossEncounter() {
        System.out.println("\n" + "=".repeat(50));
        System.out.println("   STARCIE Z CZARNYM WŁADCĄ!");
        System.out.println("=".repeat(50));
        
        Enemy boss = EventSystem.createBoss();
        handleCombat(boss);
        
        if (!boss.isAlive()) {
            gameWon = true;
            endGame(true, "Pokonałeś Czarnego Władcę i ocalił świat od ciemności!");
        }
    }

    private void processCommand(String command) {
        String[] parts = command.split(" ", 2);
        String action = parts[0];
        String arg = parts.length > 1 ? parts[1] : "";

        switch (action) {
            case "idź":
            case "idz":
                movePlayer(arg);
                break;
            case "pomoc":
            case "help":
                printHelp();
                break;
            case "statystyki":
            case "stats":
                LevelSystem.printPlayerStats(player);
                break;
            case "ekwipunek":
            case "inv":
            case "inventory":
                showInventory();
                break;
            case "użyj":
            case "uzyj":
            case "use":
                useItem(arg);
                break;
            case "wyposaż":
            case "wyposaz":
            case "equip":
                equipItem(arg);
                break;
            case "mapa":
            case "map":
                WorldBuilder.printMap();
                break;
            case "zapisz":
            case "save":
                saveGame();
                break;
            case "wczytaj":
            case "load":
                loadGame();
                break;
            case "wyjdź":
            case "wyjdz":
            case "exit":
                System.out.println("Dziękuję za grę!");
                gameRunning = false;
                break;
            default:
                System.out.println("Nieznana komenda. Wpisz 'pomoc' dla listy komend.");
        }
    }

    private void movePlayer(String direction) {
        if (direction.isEmpty()) {
            System.out.println("Dokąd chcesz iść?");
            return;
        }
        
        Location newLocation = currentLocation.getConnectedLocation(direction);
        
        if (newLocation != null) {
            currentLocation = newLocation;
            GameHistory.logLocationChange(currentLocation.getName());
            gameState.setCurrentLocationName(currentLocation.getName());
            printLocationInfo();
        } else {
            System.out.println("Nie możesz iść w tym kierunku.");
            System.out.print("Dostępne: ");
            for (String dir : currentLocation.getAvailableDirections()) {
                System.out.print(dir + " ");
            }
            System.out.println();
        }
    }

    private void printHelp() {
        System.out.println("\n=== DOSTĘPNE KOMENDY ===");
        System.out.println("idź <kierunek>  - Przemieszczanie (np. idź las)");
        System.out.println("pomoc           - Ta pomoc");
        System.out.println("statystyki      - Pokaż statystyki postaci");
        System.out.println("ekwipunek       - Pokaż ekwipunek");
        System.out.println("użyj <przedmiot> - Użyj mikstury");
        System.out.println("wyposaż <przedmiot> - Wyposaż broń/pancerz");
        System.out.println("mapa            - Pokaż mapę świata");
        System.out.println("zapisz          - Zapisz grę");
        System.out.println("wczytaj         - Wczytaj grę");
        System.out.println("wyjdź           - Zakończ grę");
        System.out.println("========================\n");
    }

    private void showInventory() {
        System.out.println("\n=== EKWIPUNEK ===");
        System.out.println("Złoto: " + player.getGold());
        
        List<Item> items = player.getInventory();
        if (items.isEmpty()) {
            System.out.println("Ekwipunek pusty.");
        } else {
            for (Item item : items) {
                System.out.println("- " + item.getName() + " (" + item.getDescription() + ")");
            }
        }
        
        System.out.println("\nWyposażone:");
        System.out.println("Broń: " + (player.getEquippedWeapon() != null ? 
                    player.getEquippedWeapon().getName() + " (+" + player.getEquippedWeapon().getDamage() + " dmg)" : "brak"));
        System.out.println("Pancerz: " + (player.getEquippedArmor() != null ? 
                      player.getEquippedArmor().getName() + " (+" + player.getEquippedArmor().getDefense() + " def)" : "brak"));
        System.out.println("===============\n");
    }

    private void useItem(String itemName) {
        if (itemName.isEmpty()) {
            System.out.println("Użyj którego przedmiotu?");
            return;
        }
        
        Potion potion = player.findPotionInInventory();
        if (potion != null && potion.getName().toLowerCase().contains(itemName.toLowerCase())) {
            player.usePotion(potion);
            GameHistory.logItemUsed(potion.getName());
            System.out.println("Użyłeś " + potion.getName() + ". Twoje HP: " + player.getHp() + "/" + player.getMaxHp());
        } else {
            System.out.println("Nie masz takiego przedmiotu.");
        }
    }

    private void equipItem(String itemName) {
        if (itemName.isEmpty()) {
            System.out.println("Co chcesz wyposażyć?");
            return;
        }
        
        Weapon weapon = player.findWeaponInInventory(itemName);
        if (weapon != null) {
            player.equipWeapon(weapon);
            GameHistory.logItemEquipped(weapon.getName(), "broń");
            System.out.println("Wyposażono: " + weapon.getName() + " (obrażenia: " + weapon.getDamage() + ")");
            return;
        }
        
        Armor armor = player.findArmorInInventory(itemName);
        if (armor != null) {
            player.equipArmor(armor);
            GameHistory.logItemEquipped(armor.getName(), "pancerz");
            System.out.println("Wyposażono: " + armor.getName() + " (obrona: " + armor.getDefense() + ")");
            return;
        }
        
        System.out.println("Nie masz takiego przedmiotu.");
    }

    private void saveGame() {
        if (GameStateManager.saveGame(gameState)) {
            System.out.println("Gra zapisana pomyślnie!");
        } else {
            System.out.println("Błąd zapisu.");
        }
    }

    private void endGame(boolean won, String reason) {
        gameRunning = false;
        
        System.out.println("\n" + "=".repeat(50));
        System.out.println(won ? "   ZWYCIĘSTWO!" : "   PORAŻKA");
        System.out.println("=".repeat(50));
        System.out.println(reason);
        
        System.out.println("\n=== STATYSTYKI KOŃCOWE ===");
        System.out.println("Poziom końcowy: " + player.getLevel());
        System.out.println("Walki wygrane: " + player.getBattlesWon());
        System.out.println("Walki przegrane: " + player.getBattlesLost());
        System.out.println("Znalezione skarby: " + player.getTreasuresFound());
        System.out.println("Złoto: " + player.getGold());
        System.out.println("Wykonane tury: " + turnCount);
        System.out.println("=========================\n");
        
        GameHistory.logGameEnd(won, reason);
        
        System.out.println("Historia gry zapisana w: " + GameHistory.getHistoryFilePath());
    }

    public static void main(String[] args) {
        GameEngine game = new GameEngine();
        game.start();
    }
}
