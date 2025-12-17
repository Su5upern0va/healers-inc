package com.nova.healersinc.world.biome;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.ObjectMap;
import com.nova.healersinc.world.herb.HerbType;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class BiomeRegistry {

    private static final String CONFIG_PATH = "biomes.json";
    private static float totalWorldGenWeight = 0f;

    private static final ObjectMap<BiomeType, BiomeDefinition> DEFINITIONS = new ObjectMap<>();
    private static final ObjectMap<String, BiomeType> ID_TO_BIOME = new ObjectMap<>();

    private static boolean initialized = false;

    /**
     * Call this once during game startup to load all biomes
     */
    public static void init() {
        if (initialized) return;

        buildBiomeLookup();

        FileHandle file = Gdx.files.internal(CONFIG_PATH);
        if (!file.exists()) {
            throw new RuntimeException("Biome config file not found: " + CONFIG_PATH);
        }

        Json json = new Json();
        BiomeConfig config = json.fromJson(BiomeConfig.class, file);

        for (BiomeConfig.BiomeEntry entry : config.biomes) {
            BiomeDefinition definition = toDefinition(entry);
            BiomeType biomeKey = mapIdToBiome(entry.id);
            DEFINITIONS.put(biomeKey, definition);

            totalWorldGenWeight += definition.getWorldGenWeight();
        }

        initialized = true;
        Gdx.app.log("BiomeRegistry", "Initialized with " + DEFINITIONS.size + " biomes.");
    }

    private static void buildBiomeLookup() {
        for (BiomeType biome : BiomeType.values()) {
            ID_TO_BIOME.put(biome.name().toLowerCase(), biome);
        }
    }

    private static BiomeDefinition toDefinition(BiomeConfig.BiomeEntry e) {
        Color color = Color.valueOf(e.visual.color);
        BiomeDefinition.Visual visual = new BiomeDefinition.Visual(color, e.visual.texture);

        // Convert resource spawns
        List<BiomeDefinition.ResourceSpawn> resourceSpawns = new ArrayList<>();
        for (BiomeConfig.ResourceSpawn spawn : e.spawnRules.resources) {
            HerbType herb = mapIdToHerb(spawn.resourceId);
            resourceSpawns.add(new BiomeDefinition.ResourceSpawn(herb, spawn.weight));
        }

        BiomeDefinition.SpawnRules spawnRules = new BiomeDefinition.SpawnRules(
            e.spawnRules.clusterCenterChance,
            resourceSpawns
        );

        return new BiomeDefinition(e.id, e.name, e.worldGenWeight, visual, spawnRules);
    }

    private static BiomeType mapIdToBiome(String id) {
        BiomeType biome = ID_TO_BIOME.get(id);
        if (biome == null) {
            throw new IllegalArgumentException("Unknown biome id in JSON: '" + id + "'. " +
                "Make sure it matches an enum constant name (case-insensitive).");
        }
        return biome;
    }

    private static HerbType mapIdToHerb(String id) {
        try {
            return HerbType.valueOf(id.toUpperCase());
        } catch (IllegalArgumentException ex) {
            throw new IllegalArgumentException("Unknown herb id in biome config: '" + id + "'");
        }
    }

    public static BiomeDefinition getDefinition(BiomeType biome) {
        if (!initialized) {
            throw new IllegalStateException("BiomeRegistry not initialized! Call BiomeRegistry.init() during startup.");
        }

        BiomeDefinition def = DEFINITIONS.get(biome);
        if (def == null) {
            throw new IllegalArgumentException("No biome definition found for: " + biome.name());
        }

        return def;
    }

    public static BiomeType pickRandomBiome(Random random) {
        if (!initialized) {
            throw new IllegalStateException("BiomeRegistry not initialized! Call BiomeRegistry.init() during startup.");
        }
        if (totalWorldGenWeight <= 0f) {
            // fallback
            return BiomeType.MILD_MEADOW;
        }

        float roll = random.nextFloat() * totalWorldGenWeight;
        float cumualtive = 0f;

        for (ObjectMap.Entry<BiomeType, BiomeDefinition> entry : DEFINITIONS) {
            float w = entry.value.getWorldGenWeight();
            if (w <= 0f) {
                continue;
            }

            cumualtive += w;
            if (roll <= cumualtive) {
                return entry.key;
            }
        }

        // in case of float rounding errors
        return DEFINITIONS.keys().next();
    }
}
