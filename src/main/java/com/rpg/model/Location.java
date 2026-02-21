package com.rpg.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Location implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private String name;
    private String description;
    private Map<String, Location> connectedLocations;
    private List<EventType> possibleEvents;
    private boolean isSafe;
    private boolean isBossLocation;

    public enum EventType {
        COMBAT, TRAP, TREASURE, DIALOGUE
    }

    public Location(String name, String description, boolean isSafe) {
        this.name = name;
        this.description = description;
        this.connectedLocations = new HashMap<>();
        this.possibleEvents = new ArrayList<>();
        this.isSafe = isSafe;
        this.isBossLocation = false;
    }

    public void addConnection(String direction, Location location) {
        connectedLocations.put(direction.toLowerCase(), location);
    }

    public Location getConnectedLocation(String direction) {
        return connectedLocations.get(direction.toLowerCase());
    }

    public List<String> getAvailableDirections() {
        return new ArrayList<>(connectedLocations.keySet());
    }

    public void addPossibleEvent(EventType event) {
        possibleEvents.add(event);
    }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public Map<String, Location> getConnectedLocations() { return connectedLocations; }
    public List<EventType> getPossibleEvents() { return new ArrayList<>(possibleEvents); }
    public boolean isSafe() { return isSafe; }
    public void setSafe(boolean safe) { isSafe = safe; }
    public boolean isBossLocation() { return isBossLocation; }
    public void setBossLocation(boolean bossLocation) { isBossLocation = bossLocation; }
}
