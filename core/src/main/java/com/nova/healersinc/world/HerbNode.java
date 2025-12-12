package com.nova.healersinc.world;

/**
 * A harvestable herb node with potency characteristics.
 * Herbs are a specific type of resource with quality/potency metrics.
 */
public class HerbNode extends ResourceNode<HerbType> {
    private final float potency;

    /**
     * Creates a new herb node.
     *
     * @param type The herb type
     * @param maxYield Maximum harvestable units
     * @param potency Quality/strength of the herb (affects processing)
     * @param regrowthRate Rate of herb regeneration per second
     */
    public HerbNode(HerbType type, int maxYield, float potency, float regrowthRate) {
        super(type, maxYield, regrowthRate);
        this.potency = potency;
    }

    /**
     * Gets the potency (quality) of this herb.
     */
    public float getPotency() {
        return potency;
    }
}
