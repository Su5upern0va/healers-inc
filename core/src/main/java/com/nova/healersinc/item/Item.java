package com.nova.healersinc.item;

import com.nova.healersinc.world.herb.HerbType;

public class Item {
    private final ItemType type;
    private final HerbType herbType;
    private final int quantity;

    public Item(ItemType type, int quantity) {
        this(type, null, quantity);
    }

    public Item(ItemType type, HerbType herbType, int quantity) {
        this.type = type;
        this.herbType = herbType;
        this.quantity = quantity;
    }

    public ItemType getType() {
        return type;
    }

    public HerbType getHerbType() {
        return herbType;
    }

    public int getQuantity() {
        return quantity;
    }

    public Item withQuantity() {
        return new Item(type, herbType, quantity);
    }

    @Override
    public String toString() {
        if (herbType != null) {
            return quantity + "x " + type.displayName() + " (" + herbType.displayName() + ")";
        }
        return quantity + "x " + type.displayName();
    }
}
