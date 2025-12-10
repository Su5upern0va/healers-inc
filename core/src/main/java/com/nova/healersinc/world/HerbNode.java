package com.nova.healersinc.world;

public class HerbNode {
    private final HerbType type;
    private final int maxYield;
    private int currentYield;
    private final float potency;
    private final float regrowthRate;

    public HerbNode(HerbType type, int maxYield, float potency, float regrowthRate) {
        this.type = type;
        this.maxYield = maxYield;
        this.currentYield = maxYield;
        this.potency = potency;
        this.regrowthRate = regrowthRate;
    }

    public HerbType getType() {
        return type;
    }

    public int getCurrentYield() {
        return currentYield;
    }

    public void harvest(int amount) {
        currentYield = Math.max(0, currentYield - amount);
    }

    public void update(float deltaTime) {
        if (currentYield < maxYield) {
            currentYield += Math.max(1, (int) (regrowthRate * deltaTime));
            if (currentYield > maxYield) {
                currentYield = maxYield;
            }
        }
    }
}
