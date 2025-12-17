package com.nova.healersinc.world.resource;

/**
 * Marker interface for all harvestable resource types in the game.
 * Resources represent the type/category of harvestable materials
 * (herbs, stones, wood, etc.) but not the actual nodes in the world.
 */
public interface Resource {
    /**
     * Returns the name of this resource type.
     * For enums, this is typically the enum constant name.
     */
    String name();
    default ResourceDefinition getDefinition() {
        return ResourceRegistry.getDefinition(this);
    }
}
