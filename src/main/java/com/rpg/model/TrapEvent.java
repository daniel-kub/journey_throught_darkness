package com.rpg.model;

public class TrapEvent extends Event {
    private int damage;
    private int saveDifficulty;

    public TrapEvent(String description, int damage, int saveDifficulty) {
        super(description);
        this.damage = damage;
        this.saveDifficulty = saveDifficulty;
    }

    public int getDamage() { return damage; }
    public void setDamage(int damage) { this.damage = damage; }
    public int getSaveDifficulty() { return saveDifficulty; }
    public void setSaveDifficulty(int saveDifficulty) { this.saveDifficulty = saveDifficulty; }
    
    @Override
    public String getEventType() { return "TRAP"; }
}
