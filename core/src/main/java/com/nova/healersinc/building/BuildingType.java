package com.nova.healersinc.building;

public enum BuildingType {
    HERVESTER("Harvester", "Harvests herbs from nearby nodes", 100),
    DRYING_RACK("Drying Rack", "Dries fresh herbs", 150),
    STORAGE("Storage", "Stores items", 50);

    private final String displayName;
    private final String description;
    private final int cost;

    BuildingType(String displayName, String description, int cost) {
        this.displayName = displayName;
        this.description = description;
        this.cost = cost;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getDescription() {
        return description;
    }

    public int getCost() {
        return cost;
    }
}
