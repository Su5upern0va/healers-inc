package com.nova.healersinc.world;

import java.util.List;

public class BiomeConfig {
    public List<BiomeEntry> biomes;

    public static class BiomeEntry {
        public String id;
        public String name;
        public Visual visual;
        public SpawnRules spawnRules;
    }

    public static class Visual {
        public String color;
        public String texture;
    }

    public static class SpawnRules {
        public float clusterCenterChance;
        public List<ResourceSpawn> resources;
    }

    public static class ResourceSpawn {
        public String resourceId;
        public float weight;
    }
}
