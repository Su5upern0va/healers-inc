package com.nova.healersinc.item;

import java.util.ArrayList;
import java.util.List;

public class Inventory {
    private final int capacity;
    private final List<Item> items;

    public Inventory (int capacity) {
        this.capacity = capacity;
        this.items = new ArrayList<>();
    }

    /**
     * Attempts to add an item to the inventory.
     * @return true if successful, false if inventory is full
     */
    public boolean addItem(Item item) {
        if (getTotalItemCount() + item.getQuantity() > capacity) {
            return false;
        }
        items.add(item);
        return true;
    }

    /**
     * Removes and returns the first item in the inventory.
     */
    public Item removeItem() {
        if (items.isEmpty()) {
            return null;
        }
        return items.remove(0);
    }

    /**
     * Peeks at the first item without removing it.
     */
    public Item peekItem() {
        if (items.isEmpty()) {
            return null;
        }
        return items.get(0);
    }

    public boolean isEmpty() {
        return items.isEmpty();
    }

    public boolean isFull() {
        return getTotalItemCount() >= capacity;
    }

    public int getTotalItemCount() {
        return items.stream().mapToInt(Item::getQuantity).sum();
    }

    public int getCapacity() {
        return capacity;
    }

    public List<Item> getItems() {
        return new ArrayList<>(items);
    }

    public void clear() {
        items.clear();
    }
}
