package com.rpg.model;

import java.io.Serializable;

public class Armor extends Item implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private int defense;
    private int requiredLevel;

    public Armor(String name, String description, int value, int defense, int requiredLevel) {
        super(name, description, value);
        this.defense = defense;
        this.requiredLevel = requiredLevel;
    }

    public int getDefense() { return defense; }
    public void setDefense(int defense) { this.defense = defense; }
    public int getRequiredLevel() { return requiredLevel; }
    public void setRequiredLevel(int requiredLevel) { this.requiredLevel = requiredLevel; }
}
