package com.nova.healersinc.world.resource;

/**
 * Abstract base class for harvestable resource nodes in the world.
 * A ResourceNode represents a specific instance of a resource on a tile
 * with its own yield, quality metrics, and regrowth behavior.
 *
 * @param <T> The type of resource this node contains
 */
public abstract class ResourceNode<T extends Resource> {
    protected final T type;
    protected final int maxYield;
    protected int currentYield;
    protected final float regrowthRate;

    /**
     * Creates a new resource node.
     *
     * @param type The resource type
     * @param maxYield Maximum harvestable units
     * @param regrowthRate Rate of resource regeneration per second
     */
    public ResourceNode(T type, int maxYield, float regrowthRate) {
        this.type = type;
        this.maxYield = maxYield;
        this.currentYield = maxYield;
        this.regrowthRate = regrowthRate;
    }

    /**
     * Gets the resource type for this node.
     */
    public T getType() {
        return type;
    }

    /**
     * Gets the current harvestable yield.
     */
    public int getCurrentYield() {
        return currentYield;
    }

    /**
     * Gets the maximum yield capacity.
     */
    public int getMaxYield() {
        return maxYield;
    }

    /**
     * Gets the regrowth rate.
     */
    public float getRegrowthRate() {
        return regrowthRate;
    }

    /**
     * Harvests resources from this node.
     *
     * @param amount The amount to harvest
     */
    public void harvest(int amount) {
        currentYield = Math.max(0, currentYield - amount);
    }

    /**
     * Updates the resource node, handling regrowth.
     *
     * @param deltaTime Time elapsed since last update in seconds
     */
    public void update(float deltaTime) {
        if (currentYield < maxYield) {
            currentYield += Math.max(1, (int) (regrowthRate * deltaTime));
            if (currentYield > maxYield) {
                currentYield = maxYield;
            }
        }
    }
}
