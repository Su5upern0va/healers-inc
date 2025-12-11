package com.nova.healersinc.world;

import java.util.Random;
import java.util.List;
import java.util.ArrayList;

public class WorldGenerator {

    private final Random random;
    private static final int BIOME_CHUNK_SIZE = 4;

    public WorldGenerator(long seed) {
        this.random = new Random(seed);
    }

    public WorldMap generate(int width, int height) {
        WorldMap worldMap = new WorldMap(width, height);

        int chunksX = (int) Math.ceil(width / (float) BIOME_CHUNK_SIZE);
        int chunksY = (int) Math.ceil(height / (float) BIOME_CHUNK_SIZE);

        BiomeType[][] chunkBiomes = new BiomeType[chunksX][chunksY];

        for (int cx = 0; cx < chunksX; cx++) {
            for (int cy = 0; cy < chunksY; cy++) {
                chunkBiomes[cx][cy] = pickBiomeForChunk(cx, cy, chunkBiomes);
            }
        }

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                int cx = x / BIOME_CHUNK_SIZE;
                int cy = y / BIOME_CHUNK_SIZE;

                BiomeType biome = chunkBiomes[cx][cy];

                Tile tile = new Tile(x, y, biome);
                maybePlaceHerbNode(tile, biome);
                worldMap.setTile(x, y, tile);
            }
        }

        return worldMap;
    }

    private BiomeType pickBiomeForChunk(int cx, int cy, BiomeType[][] chunkBiomes) {
        if (cx == 0 && cy == 0) {
            return randomBiome();
        }

        List<BiomeType> neighbors = new ArrayList<>();
        if (cx > 0) {
            neighbors.add(chunkBiomes[cx - 1][cy]);
        }
        if (cy > 0) {
            neighbors.add(chunkBiomes[cx][cy - 1]);
        }

        if(!neighbors.isEmpty() && random.nextFloat() < 0.75f) {
            return neighbors.get(random.nextInt(neighbors.size()));
        } else {
            return randomBiome();
        }
    }

    private BiomeType randomBiome() {
        return random.nextFloat() < 0.6f
            ? BiomeType.SUNNY_MEADOW
            : BiomeType.SHADY_GROVE;
    }

    private void maybePlaceHerbNode(Tile tile, BiomeType biome) {
        float roll = random.nextFloat();

        switch (biome) {
            case SUNNY_MEADOW:
                //10% Chamomile, 5% Mint
                if (roll < 0.10f) {
                    tile.setResourceNode(createHerbNode(HerbType.CHAMOMILE));
                } else if (roll < 0.15f) {
                    tile.setResourceNode(createHerbNode(HerbType.MINT));
                }
                break;

            case SHADY_GROVE:
                // 12% Mint, 7% Echinaecea
                if (roll < 0.12f) {
                    tile.setResourceNode(createHerbNode(HerbType.MINT));
                } else if (roll < 0.19f) {
                    tile.setResourceNode(createHerbNode(HerbType.ECHINACEA));
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
