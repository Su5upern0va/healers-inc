package com.nova.healersinc.building;


import com.nova.healersinc.world.map.Tile;
import com.nova.healersinc.world.map.WorldMap;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Manages all buildings in the game world.
 * Handles building placement, removal, and updates
 */
public class BuildingManager {
    private final WorldMap worldMap;
    private final List<Building> buildings;
    private final Map<Tile, Building> buildingsByTile;

    public BuildingManager(WorldMap worldMap) {
        this.worldMap = worldMap;
        this.buildings = new ArrayList<>();
        this.buildingsByTile = new HashMap<>();
    }

    /**
     * Attempts to place a building on the specified tile.
     * @return true if the placement was successful, false otherwise
     */
    public boolean placeBuilding(Tile tile, BuildingType type) {
        if (!canPlaceBuilding(tile)) {
            return false;
        }

        Building building = createBuilding(tile, type);
        if (building == null) {
            return false;
        }

        buildings.add(building);
        buildingsByTile.put(tile, building);
        tile.setBuilding(building);
        building.onPlaced();

        return true;
    }

    /**
     * Removes a building from the specified tile
     */
    public boolean removeBuilding(Tile tile) {
        Building building = buildingsByTile.get(tile);
        if (building == null) {
            return false;
        }

        building.onRemoved();
        buildings.remove(building);
        buildingsByTile.remove(tile);
        tile.setBuilding(null);

        return true;
    }

    /**
     * Checks if a building can be placed on the specified tile.
     */
    public boolean canPlaceBuilding(Tile tile) {
        if (tile == null) {
            return false;
        }

        //Can't be placed on tiles that already have buildings
        if (tile.hasBuilding()) {
            return false;
        }

        // Can't place on tiles with resource nodes (for now)
        if (tile.hasResourceNode()) {
            return false;
        }

        return true;
    }

    /**
     * Creates a building instance based on type
     */
    private Building createBuilding(Tile tile, BuildingType type) {
        switch (type) {
            case HARVESTER:
                return new Harvester(tile, worldMap);
            default:
                return null;
        }
    }

    /**
     * Updates all buildings.
     */
    public void update(float deltaTime) {
        for (Building building : buildings) {
            building.update(deltaTime);
        }
    }

    /**
     * Gets all buildings of a specific type.
     */
    public List<Building> getBuildings(BuildingType type) {
        List<Building> result = new ArrayList<>();
        for (Building building : buildings) {
            if (building.getType() == type) {
                result.add(building);
            }
        }
        return result;
    }

    public List<Building> getAllBuildings() {
       return new ArrayList<>(buildings);
    }

    public int getBuildingCount() {
        return buildings.size();
    }
}
