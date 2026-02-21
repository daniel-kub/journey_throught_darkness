package com.rpg.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public abstract class Character implements Serializable {
    private static final long serialVersionUID = 1L;
    
    protected String name;
    protected int hp;
    protected int maxHp;
    protected int strength;
    protected int agility;
    protected int intelligence;
    protected int level;
    protected int xp;
    protected int xpToNextLevel;

    public Character(String name, int hp, int strength, int agility, int intelligence) {
        this.name = name;
        this.hp = hp;
        this.maxHp = hp;
        this.strength = strength;
        this.agility = agility;
        this.intelligence = intelligence;
        this.level = 1;
        this.xp = 0;
        this.xpToNextLevel = 100;
    }

    public boolean isAlive() {
        return hp > 0;
    }

    public void takeDamage(int damage) {
        hp = Math.max(0, hp - damage);
    }

    public void heal(int amount) {
        hp = Math.min(maxHp, hp + amount);
    }

    public boolean levelUp() {
        if (xp >= xpToNextLevel) {
            level++;
            xp -= xpToNextLevel;
            xpToNextLevel = level * 100;
            return true;
        }
        return false;
    }

    public void addXp(int amount) {
        xp += amount;
        while (levelUp()) {
            onLevelUp();
        }
    }

    protected void onLevelUp() {
        maxHp += 10;
        hp = maxHp;
        strength += 2;
        agility += 2;
        intelligence += 2;
    }

    public int getDamage() {
        return strength;
    }

    public int calculateEvasionChance() {
        return agility * 2;
    }

    public int calculateTrapSave() {
        return agility + intelligence;
    }

    public int calculateDialogueSuccess() {
        return intelligence * 3 + 10;
    }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public int getHp() { return hp; }
    public void setHp(int hp) { this.hp = hp; }
    public int getMaxHp() { return maxHp; }
    public void setMaxHp(int maxHp) { this.maxHp = maxHp; }
    public int getStrength() { return strength; }
    public void setStrength(int strength) { this.strength = strength; }
    public int getAgility() { return agility; }
    public void setAgility(int agility) { this.agility = agility; }
    public int getIntelligence() { return intelligence; }
    public void setIntelligence(int intelligence) { this.intelligence = intelligence; }
    public int getLevel() { return level; }
    public void setLevel(int level) { this.level = level; }
    public int getXp() { return xp; }
    public void setXp(int xp) { this.xp = xp; }
    public int getXpToNextLevel() { return xpToNextLevel; }
    public void setXpToNextLevel(int xpToNextLevel) { this.xpToNextLevel = xpToNextLevel; }
}
