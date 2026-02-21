package com.rpg.model;

import java.io.Serializable;

public class Potion extends Item implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private int healAmount;

    public Potion(String name, String description, int value, int healAmount) {
        super(name, description, value);
        this.healAmount = healAmount;
    }

    public int getHealAmount() { return healAmount; }
    public void setHealAmount(int healAmount) { this.healAmount = healAmount; }
}
