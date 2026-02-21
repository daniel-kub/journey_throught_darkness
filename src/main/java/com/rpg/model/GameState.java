package com.rpg.model;

import java.io.Serializable;

public class GameState implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private Player player;
    private String currentLocationName;
    private int totalTurns;
    private int battlesWon;
    private int battlesLost;
    private int treasuresFound;
    private boolean gameEnded;
    private boolean gameWon;

    public GameState(Player player, String currentLocationName) {
        this.player = player;
        this.currentLocationName = currentLocationName;
        this.totalTurns = 0;
        this.battlesWon = player.getBattlesWon();
        this.battlesLost = player.getBattlesLost();
        this.treasuresFound = player.getTreasuresFound();
        this.gameEnded = false;
        this.gameWon = false;
    }

    public Player getPlayer() { return player; }
    public void setPlayer(Player player) { this.player = player; }
    public String getCurrentLocationName() { return currentLocationName; }
    public void setCurrentLocationName(String currentLocationName) { this.currentLocationName = currentLocationName; }
    public int getTotalTurns() { return totalTurns; }
    public void setTotalTurns(int totalTurns) { this.totalTurns = totalTurns; }
    public void incrementTurns() { totalTurns++; }
    public int getBattlesWon() { return battlesWon; }
    public void setBattlesWon(int battlesWon) { this.battlesWon = battlesWon; }
    public int getBattlesLost() { return battlesLost; }
    public void setBattlesLost(int battlesLost) { this.battlesLost = battlesLost; }
    public int getTreasuresFound() { return treasuresFound; }
    public void setTreasuresFound(int treasuresFound) { this.treasuresFound = treasuresFound; }
    public boolean isGameEnded() { return gameEnded; }
    public void setGameEnded(boolean gameEnded) { this.gameEnded = gameEnded; }
    public boolean isGameWon() { return gameWon; }
    public void setGameWon(boolean gameWon) { this.gameWon = gameWon; }
}
