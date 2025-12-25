package com.nova.healersinc.building;

import com.nova.healersinc.item.Item;
import com.nova.healersinc.world.map.Tile;
import com.nova.healersinc.world.map.WorldMap;

/**
 * A conveyor belt that transports items between buildings.
 * Moves items from one building to an adjacent building in a specified direction.
 */
public class Conveyor extends Building{
   private static final float TRANSPORT_INTERVAL = 1.0f; // seconds

    public enum Direction {
        NORTH(0, 1),
        EAST(1, 0),
        SOUTH(0, -1),
        WEST(-1, 0);

        public final int dx;
        public final int dy;

        Direction(int dx, int dy) {
            this.dx = dx;
            this.dy = dy;
        }

        public Direction rotate() {
            return values()[(ordinal() + 1) % values().length];
        }
    }

    private final WorldMap worldMap;
    private Direction direction;
    private Item heldItem;
    private float transportTimer;

    public Conveyor(WorldMap worldMap, Tile tile) {
        this(tile, worldMap, Direction.EAST);
    }

    public Conveyor(Tile tile, WorldMap worldMap, Direction direction) {
        super(tile, BuildingType.CONVEYOR);
        this.worldMap = worldMap;
        this.direction = direction;
        this.transportTimer = 0.0f;
    }

    @Override
    public void update(float deltaTime) {
        if (!active) return;

        transportTimer += deltaTime;

        if (transportTimer >= TRANSPORT_INTERVAL) {
            transportTimer = 0f;

            // Try to transport item
            if (heldItem != null) {
                tryMoveItem();
            }

            // Try to pull item from adjacent building
            if (heldItem == null) {
                tryPullItem();
            }
        }
    }

    /**
     * Attempts to move the held item to the next building in the direction.
     */
    private void tryMoveItem() {
        Tile targetTile = worldMap.getTile(
            tile.x + direction.dx,
            tile.y + direction.dy
        );

        if (targetTile != null && !targetTile.hasBuilding()) return;

        Building targetBuilding = targetTile.getBuilding();

        //Try to deliver to different building types
        if (targetBuilding instanceof Storage) {
            Storage storage = (Storage) targetBuilding;
            if (storage.storeItem(heldItem)) {
                heldItem = null;
            }
        } else if (targetBuilding instanceof DryingRack) {
            DryingRack dryingRack = (DryingRack) targetBuilding;
            if (dryingRack.addInput(heldItem)) {
                heldItem = null;
            }
        } else if (targetBuilding instanceof Conveyor) {
            Conveyor conveyor = (Conveyor) targetBuilding;
            if (conveyor.acceptItem(heldItem)) {
                heldItem = null;
            }
        }
    }

    /**
     * Attempts to pull an item from the previous building (opposite direction).
     */
    private void tryPullItem() {
        Tile sourceTile = worldMap.getTile(
            tile.x - direction.dx,
            tile.y - direction.dy
        );

        if(sourceTile == null || !sourceTile.hasBuilding()) return;

        Building sourceBuilding = sourceTile.getBuilding();

        // try to pull from different building types
        if (sourceBuilding instanceof Harvester) {
            // skip for now cause they have to be updated
        } else if (sourceBuilding instanceof DryingRack) {
            DryingRack dryingRack = (DryingRack) sourceBuilding;
            if (dryingRack.hasOutput()) {
                heldItem = dryingRack.retrieveOutput();
            }
        } else if (sourceBuilding instanceof Storage) {
            Storage storage = (Storage) sourceBuilding;
            if (!storage.isEmpty()) {
                heldItem = storage.retrieveItem();
            }
        }
    }

    /**
     * Accepts an item from another conveyor or building.
     * @return true if the item was accepted, false if already holding an item
     */
    public boolean acceptItem(Item item) {
        if (heldItem != null) return false;

        heldItem = item;
        return true;
    }

    /**
     * Rotates the conveyor direction clockwise.
     */
    public void rotate() {
        direction = direction.rotate();
    }

    public Direction getDirection() {
        return direction;
    }

    public void setDirection(Direction direction) {
        this.direction = direction;
    }

    public Item getHeldItem() {
        return heldItem;
    }

    public boolean isHoldingItem() {
        return heldItem != null;
    }
}
