package com.rpg.model;

import java.io.Serializable;

public class Enemy extends Character implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private int xpReward;
    private int goldReward;

    public Enemy(String name, int hp, int strength, int agility, int intelligence, int xpReward, int goldReward) {
        super(name, hp, strength, agility, intelligence);
        this.xpReward = xpReward;
        this.goldReward = goldReward;
    }

    public int getXpReward() { return xpReward; }
    public void setXpReward(int xpReward) { this.xpReward = xpReward; }
    public int getGoldReward() { return goldReward; }
    public void setGoldReward(int goldReward) { this.goldReward = goldReward; }
}
