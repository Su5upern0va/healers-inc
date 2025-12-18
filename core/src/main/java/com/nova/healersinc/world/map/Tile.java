package com.nova.healersinc.world.map;

import com.nova.healersinc.building.Building;
import com.nova.healersinc.world.biome.BiomeType;
import com.nova.healersinc.world.herb.HerbNode;
import com.nova.healersinc.world.resource.ResourceNode;

public class Tile {
    public final int x;
    public final int y;

    public BiomeType biome;
    private ResourceNode<?> resourceNode;
    private Building building;

    public Tile(int x, int y, BiomeType biome) {
        this.x = x;
        this.y = y;
        this.biome = biome;
    }

    public BiomeType getBiome() {
        return biome;
    }

    public void setBiome(BiomeType biome) {
        this.biome = biome;
    }

    /**
     * Gets the resource node on this tile (generic).
     */
    public ResourceNode<?> getResourceNode() {
        return resourceNode;
    }

    /**
     * Sets the resource node on this tile.
     */
    public void setResourceNode(ResourceNode<?> resourceNode) {
        this.resourceNode = resourceNode;
    }

    /**
     * Checks if this tile has any resource node.
     */
    public boolean hasResourceNode() {
        return resourceNode != null;
    }

    // === BUILDING METHODS ===

    public Building getBuilding() {
        return building;
    }

    public void setBuilding(Building building) {
        this.building = building;
    }

    public boolean hasBuilding() {
        return building != null;
    }


    // === BACKWARD COMPATIBILITY METHODS ===

    /**
     * Gets the herb node if this tile contains a herb resource.
     *
     * @return HerbNode if present, null otherwise
     * @deprecated Use getResourceNode() and check type instead
     */
    @Deprecated
    public HerbNode getHerbNode() {
        return (resourceNode instanceof HerbNode) ? (HerbNode) resourceNode : null;
    }

    /**
     * Sets a herb node on this tile.
     *
     * @deprecated Use setResourceNode() instead
     */
    @Deprecated
    public void setHerbNode(HerbNode herbNode) {
        this.resourceNode = herbNode;
    }

    /**
     * Checks if this tile has a herb node.
     *
     * @deprecated Use hasResourceNode() and check type instead
     */
    @Deprecated
    public boolean isHerbNode() {
        return resourceNode instanceof HerbNode;
    }
}
