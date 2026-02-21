package com.rpg.model;

public class CombatEvent extends Event {
    private Enemy enemy;

    public CombatEvent(String description, Enemy enemy) {
        super(description);
        this.enemy = enemy;
    }

    public Enemy getEnemy() { return enemy; }
    public void setEnemy(Enemy enemy) { this.enemy = enemy; }
    
    @Override
    public String getEventType() { return "COMBAT"; }
}
