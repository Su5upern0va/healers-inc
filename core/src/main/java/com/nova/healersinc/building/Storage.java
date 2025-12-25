package com.nova.healersinc.building;

import com.nova.healersinc.item.Inventory;
import com.nova.healersinc.item.Item;
import com.nova.healersinc.world.map.Tile;

/**
 * A storage building that can store items.
 * Acts as an endpoint for conveyor systems and a source for future crafting.
 */
public class Storage extends Building{
    public static final int DEFAULT_CAPACITY = 10;

    private final Inventory inventory;

    public Storage(Tile tile) {
        this(tile, DEFAULT_CAPACITY);
    }

    public Storage(Tile tile, int capacity) {
        super(tile, BuildingType.STORAGE);
        this.inventory = new Inventory(capacity);
    }

    @Override
    public void update(float deltaTime) {
        // building is passive. No updates required
    }

    /**
     * Attempts to store an item in this storage.
     * @return true if successful, false if storage is full
     */
    public boolean storeItem(Item item) {
        return inventory.addItem(item);
    }

    /**
     * Retrieves an item from storage.
     */
    public Item retrieveItem() {
        return inventory.removeItem();
    }

    public Inventory getInventory() {
        return inventory;
    }

    public boolean isFull() {
        return inventory.isFull();
    }

    public boolean isEmpty() {
        return inventory.isEmpty();
    }
}
