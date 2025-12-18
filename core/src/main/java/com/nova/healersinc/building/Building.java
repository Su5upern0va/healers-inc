package com.nova.healersinc.building;

import com.nova.healersinc.world.map.Tile;

/**
 * Abstract base class for all buildings in the game.
 * Buildings are placed on tiles and perform various functions
 */
public abstract class Building {
   protected final Tile tile ;
   protected final BuildingType type;
   protected boolean active;

   public Building(Tile tile, BuildingType type) {
       this.tile = tile;
       this.type = type;
       this.active = true;
   }

    /**
     * Updates the building logic.
     * @param deltaTime Tile elapsed since the last update in seconds
     */
    public abstract void update(float deltaTime);

    /**
     * Called when the building is placed.
     */
    public void onPlaced() {
        // Override in subclasses if needed e.g., animation triggers
    }

    /**
     * Called, when the building is removed.
     */
    public void onRemoved() {
        // Override in subclasses if needed e.g., animation triggers
    }

    public Tile getTile() {
        return tile;
    }

    public BuildingType getType() {
        return type;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}
