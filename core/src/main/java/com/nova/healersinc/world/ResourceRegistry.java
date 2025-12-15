package com.nova.healersinc.world;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.ObjectMap;

import java.util.logging.FileHandler;

public class ResourceRegistry {

    private static final String CONFIG_PATH = "resources.json";

    private static final ObjectMap<Resource, ResourceDefinition> DEFINITIONS = new ObjectMap<>();
    private static final ObjectMap<String , Resource> ID_TO_RESOURCE = new ObjectMap<>();

    private static boolean initialized = false;

    /**
     * Calling this method once during the game startup to load all resources
     */
    public static void init() {
        if (initialized) return;

        buildResourceLookup();

        FileHandle file = Gdx.files.internal(CONFIG_PATH);
        if (!file.exists()) {
            throw new RuntimeException("Resource config file not found: " + CONFIG_PATH);
        }

        Json json = new Json();
        ResourceConfig config = json.fromJson(ResourceConfig.class, file);

        for (ResourceConfig.ResourceEntry entry : config.resources) {
            ResourceDefinition definition = toDefinition(entry);
            Resource resourceKey = mapIdToResource(entry.id);
            DEFINITIONS.put(resourceKey, definition);
        }

        initialized = true;
        Gdx.app.log("ResourceRegistry", "Initialized with " + DEFINITIONS.size + " resources.");
    }

    private static void buildResourceLookup() {
        for (HerbType herb : HerbType.values()) {
            ID_TO_RESOURCE.put(herb.name().toLowerCase(), herb);
        }
    }

    private static ResourceDefinition toDefinition(ResourceConfig.ResourceEntry e) {
        Color color = Color.valueOf(e.visual.color);

        ResourceDefinition.Visual visual = new ResourceDefinition.Visual(color, e.visual.texture, e.visual.icon);

        ResourceDefinition.Properties properties = new ResourceDefinition.Properties(e.properties.stackSize, e.properties.baseValue);

        return new ResourceDefinition(
            e.id,
            e.name,
            e.category,
            visual,
            properties
        );
    }

    private static Resource mapIdToResource(String id) {
        Resource resource = ID_TO_RESOURCE.get(id);
        if (resource == null) {
            throw new IllegalArgumentException("Unknown resource id in JSON: '" + id + "'. " +
                "Make sure it matches an enum constant name (case-insensitive)."
            );
        }
        return resource;
    }

    public static ResourceDefinition getDefinition(Resource resource) {
        if (!initialized) {
            throw new IllegalStateException("ResourceRegistry not initialized yet! Call ResourceRegistry.init() once during startup.");
        }

        ResourceDefinition def = DEFINITIONS.get(resource);
        if (def == null) {
            throw new IllegalArgumentException("No resource definition found for resource: " + resource.name());
        }

        return def;
    }
}
