package com.nova.healersinc.item;

public enum ItemType {
    FRESH_HERB("Fresh Herb");
    DRIED_HERB("Dried Herb");

    private final String displayName;

    ItemType(String displayName) {
        this.displayName = displayName;
    }

    String getDisplayName() {
        return displayName;
    }
}
