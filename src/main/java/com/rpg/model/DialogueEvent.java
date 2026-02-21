package com.rpg.model;

import java.util.List;
import java.util.Map;

public class DialogueEvent extends Event {
    private String npcName;
    private Map<String, String> choices;
    private String[] choiceKeys;

    public DialogueEvent(String description, String npcName, Map<String, String> choices, String[] choiceKeys) {
        super(description);
        this.npcName = npcName;
        this.choices = choices;
        this.choiceKeys = choiceKeys;
    }

    public String getNpcName() { return npcName; }
    public void setNpcName(String npcName) { this.npcName = npcName; }
    public Map<String, String> getChoices() { return choices; }
    public String[] getChoiceKeys() { return choiceKeys; }
    
    @Override
    public String getEventType() { return "DIALOGUE"; }
}
