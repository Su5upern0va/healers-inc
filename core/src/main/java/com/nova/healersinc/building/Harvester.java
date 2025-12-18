package com.nova.healersinc.building;


import com.nova.healersinc.world.herb.HerbNode;
import com.nova.healersinc.world.map.Tile;
import com.nova.healersinc.world.map.WorldMap;
import com.nova.healersinc.world.resource.ResourceNode;

import java.util.ArrayList;
import java.util.List;

/**
 * A harvester building that automatically collects herbs from nearby herb nodes.
 * Scans adjacent tiles for herb nodes and harvests them over time.
 */
public class Harvester extends Building {
    private static final int HARVEST_RADIUS = 1; // Adjacent tiles only
    private static final float HARVEST_INTERVAL = 2.0f; // Harvest every two seconds
    private static final int HARVEST_AMOUNT = 1; // Amount harvested per cycle

    private final WorldMap worldMap;
    private final List<HerbNode> nearbyHerbNodes;
    private float harvestTimer;
    private int totalHarvested;

    public Harvester(Tile tile, WorldMap worldMap) {
        super(tile, BuildingType.HARVESTER);
        this.worldMap = worldMap;
        this.nearbyHerbNodes = new ArrayList<>();
        this.harvestTimer = 0.0f;
        this.totalHarvested = 0;
    }

    @Override
    public void onPlaced() {
        super.onPlaced();
        scanForHerbNodes();
    }

    @Override
    public void update(float deltaTime) {
        if (!active || nearbyHerbNodes.isEmpty()) {
            return;
        }

        harvestTimer += deltaTime;

        if (harvestTimer >= HARVEST_INTERVAL) {
            harvestTimer = 0f;
            performHarvest();
        }
    }

    /**
     * Scans nearby tiles for herb nodes within the harvest radius
     */
    private void scanForHerbNodes() {
        nearbyHerbNodes.clear();

        for (int dx = -HARVEST_RADIUS; dx <= HARVEST_RADIUS; dx++) {
            for (int dy = -HARVEST_RADIUS; dy <= HARVEST_RADIUS; dy++) {
                if (dx == 0 && dy == 0) continue;

                int checkX = tile.x + dx;
                int checkY = tile.y + dy;

                Tile nearbyTile = worldMap.getTile(checkX, checkY);
                if (nearbyTile != null && nearbyTile.hasResourceNode()) {
                    ResourceNode<?> resourceNode = nearbyTile.getResourceNode();
                    if (resourceNode instanceof HerbNode) {
                        nearbyHerbNodes.add((HerbNode) resourceNode);
                    }
                }
            }
        }
    }

    /**
     * Performs the harvest operation on all nearby herb nodes
     */
    private void performHarvest() {
        for (HerbNode herbNode : nearbyHerbNodes) {
            if (herbNode.getCurrentYield() > 0) {
                int amountToHarvest = Math.min(HARVEST_AMOUNT, herbNode.getCurrentYield());
                herbNode.harvest(amountToHarvest);
                totalHarvested += amountToHarvest;

                //TODO: Add harvested herbs to inventory/storage
                System.out.println("Harvested " + amountToHarvest + " " + herbNode.getType().name() + "(Total: " + totalHarvested + ")");
            }
        }
    }

    /**
     * Rescans for herb nodes in case the world changes. Terraform feature?
     */
    public void rescan() {
        scanForHerbNodes();
    }

    public List<HerbNode> getNerbyHerbNodes() {
        return new ArrayList<>(nearbyHerbNodes);
    }

    public int getTotalHarvested() {
        return totalHarvested;
    }

    public float getHarvestTimer() {
        return harvestTimer / HARVEST_INTERVAL;
    }
}
