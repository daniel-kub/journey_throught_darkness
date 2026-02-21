package com.rpg.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Player extends Character implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private List<Item> inventory;
    private Weapon equippedWeapon;
    private Armor equippedArmor;
    private int gold;
    private int potionsUsed;
    private int battlesWon;
    private int battlesLost;
    private int treasuresFound;

    public Player(String name, int hp, int strength, int agility, int intelligence) {
        super(name, hp, strength, agility, intelligence);
        this.inventory = new ArrayList<>();
        this.gold = 50;
        this.potionsUsed = 0;
        this.battlesWon = 0;
        this.battlesLost = 0;
        this.treasuresFound = 0;
    }

    @Override
    public int getDamage() {
        int baseDamage = strength;
        if (equippedWeapon != null) {
            baseDamage += equippedWeapon.getDamage();
        }
        return baseDamage;
    }

    @Override
    public void takeDamage(int damage) {
        int reducedDamage = damage;
        if (equippedArmor != null) {
            reducedDamage = Math.max(1, damage - equippedArmor.getDefense());
        }
        super.takeDamage(reducedDamage);
    }

    public void addItem(Item item) {
        inventory.add(item);
    }

    public void removeItem(Item item) {
        inventory.remove(item);
    }

    public void equipWeapon(Weapon weapon) {
        if (weapon.getRequiredLevel() <= level) {
            equippedWeapon = weapon;
        }
    }

    public void equipArmor(Armor armor) {
        if (armor.getRequiredLevel() <= level) {
            equippedArmor = armor;
        }
    }

    public void usePotion(Potion potion) {
        heal(potion.getHealAmount());
        potionsUsed++;
        removeItem(potion);
    }

    public List<Item> getInventory() { return new ArrayList<>(inventory); }
    public Weapon getEquippedWeapon() { return equippedWeapon; }
    public Armor getEquippedArmor() { return equippedArmor; }
    public int getGold() { return gold; }
    public void setGold(int gold) { this.gold = gold; }
    public void addGold(int amount) { gold += amount; }
    public void spendGold(int amount) { gold = Math.max(0, gold - amount); }
    public int getPotionsUsed() { return potionsUsed; }
    public void setPotionsUsed(int potionsUsed) { this.potionsUsed = potionsUsed; }
    public int getBattlesWon() { return battlesWon; }
    public void setBattlesWon(int battlesWon) { this.battlesWon = battlesWon; }
    public void incrementBattlesWon() { battlesWon++; }
    public int getBattlesLost() { return battlesLost; }
    public void setBattlesLost(int battlesLost) { this.battlesLost = battlesLost; }
    public void incrementBattlesLost() { battlesLost++; }
    public int getTreasuresFound() { return treasuresFound; }
    public void setTreasuresFound(int treasuresFound) { this.treasuresFound = treasuresFound; }
    public void incrementTreasuresFound() { treasuresFound++; }

    @Override
    protected void onLevelUp() {
        super.onLevelUp();
    }

    public Potion findPotionInInventory() {
        for (Item item : inventory) {
            if (item instanceof Potion) {
                return (Potion) item;
            }
        }
        return null;
    }

    public Weapon findWeaponInInventory(String name) {
        for (Item item : inventory) {
            if (item instanceof Weapon && item.getName().toLowerCase().contains(name.toLowerCase())) {
                return (Weapon) item;
            }
        }
        return null;
    }

    public Armor findArmorInInventory(String name) {
        for (Item item : inventory) {
            if (item instanceof Armor && item.getName().toLowerCase().contains(name.toLowerCase())) {
                return (Armor) item;
            }
        }
        return null;
    }
}
