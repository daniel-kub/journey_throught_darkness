package com.rpg.model;

public class TreasureEvent extends Event {
    private Item item;
    private int gold;

    public TreasureEvent(String description, Item item, int gold) {
        super(description);
        this.item = item;
        this.gold = gold;
    }

    public Item getItem() { return item; }
    public void setItem(Item item) { this.item = item; }
    public int getGold() { return gold; }
    public void setGold(int gold) { this.gold = gold; }
    
    @Override
    public String getEventType() { return "TREASURE"; }
}
