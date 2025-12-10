package com.nova.healersinc.world;

import java.util.Random;

public class WorldGenerator {

    private final Random random;

    public WorldGenerator(long seed) {
        this.random = new Random(seed);
    }

    public WorldMap generate(int width, int height) {
        WorldMap worldMap = new WorldMap(width, height);

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {

                BiomeType biome;

                if (random.nextFloat() < 0.6f) {
                    biome = BiomeType.SUNNY_MEADOW;
                } else {
                    biome = BiomeType.SHADY_GROVE;
                }

                Tile tile = new Tile(x, y, biome);
                maybePlaceHerbNode(tile, biome);
                worldMap.setTile(x, y, tile);
            }
        }

        return worldMap;
    }

    private void maybePlaceHerbNode(Tile tile, BiomeType biome) {
        float roll = random.nextFloat();

        switch (biome) {
            case SUNNY_MEADOW:
                //10% Chamomile, 5% Mint
                if (roll < 0.10f) {
                    tile.setHerbNode(createHerbNode(HerbType.CHAMOMILE));
                } else if (roll < 0.15f) {
                    tile.setHerbNode(createHerbNode(HerbType.MINT));
                }
                break;

            case SHADY_GROVE:
                // 12% Mint, 7% Echinaecea
                if (roll < 0.12f) {
                    tile.setHerbNode(createHerbNode(HerbType.MINT));
                } else if (roll < 0.19f) {
                    tile.setHerbNode(createHerbNode(HerbType.ECHINACEA));
                }
                break;
        }
    }

    private HerbNode createHerbNode(HerbType type) {
        switch (type) {
            case MINT:
                return new HerbNode(type, 8, 1.1f, 0.12f);
            case ECHINACEA:
                return new HerbNode(type, 12, 1.2f, 0.08f);
            default: //CHAMOMILE
                return new HerbNode(type, 10, 1.0f, 0.1f);
        }
    }
}
