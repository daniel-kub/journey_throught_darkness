package com.rpg.model;

import java.io.Serializable;

public class Weapon extends Item implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private int damage;
    private int requiredLevel;

    public Weapon(String name, String description, int value, int damage, int requiredLevel) {
        super(name, description, value);
        this.damage = damage;
        this.requiredLevel = requiredLevel;
    }

    public int getDamage() { return damage; }
    public void setDamage(int damage) { this.damage = damage; }
    public int getRequiredLevel() { return requiredLevel; }
    public void setRequiredLevel(int requiredLevel) { this.requiredLevel = requiredLevel; }
}
