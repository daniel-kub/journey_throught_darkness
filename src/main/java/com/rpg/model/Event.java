package com.rpg.model;

public abstract class Event {
    protected String description;

    public Event(String description) {
        this.description = description;
    }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    
    public abstract String getEventType();
}
