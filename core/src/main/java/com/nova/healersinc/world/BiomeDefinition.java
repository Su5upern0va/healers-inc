package com.nova.healersinc.world;

import com.badlogic.gdx.graphics.Color;

import java.util.List;
import java.util.Random;

public class BiomeDefinition {
    private final String id;
    private final String name;
    private final Float worldGenWeight;
    private final Visual visual;
    private final SpawnRules spawnRules;

    public BiomeDefinition(String id, String name, float worldGenWeight, Visual visual, SpawnRules spawnRules) {
        this.id = id;
        this.name = name;
        this.worldGenWeight = worldGenWeight;
        this.visual = visual;
        this.spawnRules = spawnRules;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Float getWorldGenWeight() {
        return worldGenWeight;
    }

    public Visual getVisual() {
        return visual;
    }

    public SpawnRules getSpawnRules() {
        return spawnRules;
    }

    public static class Visual {
        private final Color color;
        private final String texture;

        public Visual(Color color, String texture) {
            this.color = color;
            this.texture = texture;
        }

        public Color getColor() {
            return color;
        }

        public String getTexture() {
            return texture;
        }
    }

    public static class SpawnRules {
        private final float clusterCenterChance;
        private final List<ResourceSpawn> resources;
        private final float totalWeight;

        public SpawnRules(float clusterCenterChance, List<ResourceSpawn> resources) {
            this.clusterCenterChance = clusterCenterChance;
            this.resources = resources;

            // Pre-calculate total weight for efficient random selection
            float sum = 0f;
            for (ResourceSpawn spawn : resources) {
                sum += spawn.getWeight();
            }
            this.totalWeight = sum;
        }

        public float getClusterCenterChance() {
            return clusterCenterChance;
        }

        public List<ResourceSpawn> getResources() {
            return resources;
        }

        /**
         * Picks a random herb based on weighted probabilities
         */
        public HerbType pickRandomHerb(Random random) {
            if (resources.isEmpty()) {
                return HerbType.CHAMOMILE; // fallback
            }

            float roll = random.nextFloat() * totalWeight;
            float cumulative = 0f;

            for (ResourceSpawn spawn : resources) {
                cumulative += spawn.getWeight();
                if (roll <= cumulative) {
                    return spawn.getResource();
                }
            }

            // Fallback (shouldn't happen)
            return resources.get(0).getResource();
        }
    }

    public static class ResourceSpawn {
        private final HerbType resource;
        private final float weight;

        public ResourceSpawn(HerbType resource, float weight) {
            this.resource = resource;
            this.weight = weight;
        }

        public HerbType getResource() {
            return resource;
        }

        public float getWeight() {
            return weight;
        }
    }
}
