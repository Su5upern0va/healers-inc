package com.nova.healersinc.building;

import com.nova.healersinc.item.Inventory;
import com.nova.healersinc.item.Item;
import com.nova.healersinc.item.ItemType;
import com.nova.healersinc.world.map.Tile;

/**
 * A drying rack that processes fresh herbs into dried herbs.
 * Takes fresh herbs as input and produces dried herbs after a processing time.
 */
public class DryingRack extends Building{
    public static final float DRYING_TIME = 5.0f;
    public static final int INPUT_CAPACITY = 10;
    public static final int OUTPUT_CAPACITY = 10;

    private final Inventory inputInventory;
    private final Inventory outputInventory;
    private Item currentProcessing;
    private float processingTimer;

    public DryingRack(Tile tile) {
        super(tile, BuildingType.DRYING_RACK);
        this.inputInventory = new Inventory(INPUT_CAPACITY);
        this.outputInventory = new Inventory(OUTPUT_CAPACITY);
        this.currentProcessing = null;
        this.processingTimer = 0.0f;
    }

    @Override
    public void update(float deltaTime) {
        if (!active) return;

        // if currently processes an item, update timer
        if (currentProcessing != null) {
            processingTimer += deltaTime;

            if (processingTimer >= DRYING_TIME) {
                finishProcessing();
            }
        } else {
            startProcessing();
        }
    }

    /**
     * Starts processing the next item from input inventory.
     */
    private void startProcessing() {
        if(inputInventory.isEmpty() || outputInventory.isFull()) return;

        Item freshHerb = inputInventory.removeItem();
        if (freshHerb != null && freshHerb.getType() == ItemType.FRESH_HERB) {
            currentProcessing = freshHerb;
            processingTimer = 0.0f;
            System.out.println("Stated drying " + freshHerb);
        }
    }

    /**
    * Finishes processing and moves the dried herb to output.
    */
    private void finishProcessing() {
        if (currentProcessing == null) return;

        Item driedHerb = new Item(
            ItemType.DRIED_HERB,
            currentProcessing.getHerbType(),
            currentProcessing.getQuantity()
        );

        if (outputInventory.addItem(driedHerb)) {
            System.out.println("Finished drying " + currentProcessing);
            currentProcessing = null;
            processingTimer = 0.0f;
        }
    }

    /**
     * Adds a fresh herb to the input inventory.
     * @return true if successful, false if input is full
     */
    public boolean addInput(Item item) {
        if (item.getType() != ItemType.FRESH_HERB) return false;
        return inputInventory.addItem(item);
    }

    /**
     * Retrieves a dried herb from the output inventory.
     */
    public Item retrieveOutput() {
        return outputInventory.removeItem();
    }

    public boolean hasOutput() {
        return !outputInventory.isEmpty();
    }

    public boolean canAcceptInput() {
        return !inputInventory.isFull();
    }

    public float getProcessingProgress() {
        if (currentProcessing == null) return 0.0f;
        return Math.min(1.0f, processingTimer / DRYING_TIME);
    }

    public Item getCurrentProcessingItem() {
        return currentProcessing;
    }

    public Inventory getInputInventory() {
        return inputInventory;
    }

    public Inventory getOutputInventory() {
        return outputInventory;
    }
}
