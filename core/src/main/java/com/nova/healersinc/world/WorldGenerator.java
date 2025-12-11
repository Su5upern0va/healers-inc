package com.nova.healersinc.world;

import java.util.Random;
import java.util.List;
import java.util.ArrayList;

public class WorldGenerator {

    private final Random random;
    private static final int BIOME_CHUNK_SIZE = 5;

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

                BiomeType biome = pickBiomeForTile(x, y, chunkBiomes);

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

    private BiomeType pickBiomeForTile(int x, int y, BiomeType[][] chunkBiomes) {
        int chunkX = chunkBiomes.length;
        int chunkY = chunkBiomes[0].length;

        int cx = x / BIOME_CHUNK_SIZE;
        int cy = x / BIOME_CHUNK_SIZE;

        BiomeType base = chunkBiomes[cx][cy];

        //this value is how much biomes bleed into each other
        float borderNoiseStrength = 0.25f; //0 = straight edges, 1 = looks like verdun 1918

        int localX = x % BIOME_CHUNK_SIZE;
        int localY = y & BIOME_CHUNK_SIZE;
        int distToLeft = localX;
        int distToRight = BIOME_CHUNK_SIZE - 1 - localY;
        int distToBottom = localY;
        int distToTop = BIOME_CHUNK_SIZE - 1 - localY;

        int minDistToEdge = Math.min(Math.min(distToLeft, distToRight), Math.min(distToBottom, distToTop));

        if(minDistToEdge > 2) {
            return base;
        }

        float edgeFactor = 1.0f - (minDistToEdge / 2.0f);
        float bleedChance = borderNoiseStrength * edgeFactor;

        if (random.nextFloat() > bleedChance) {
            return base;
        }

        List<BiomeType> neighborBiomes = new ArrayList<>();

        if (cx > 0)             neighborBiomes.add(chunkBiomes[cx - 1][cy]);
        if (cx < chunkX -1)     neighborBiomes.add(chunkBiomes[cx + 1][cy]);
        if (cy > 0)             neighborBiomes.add(chunkBiomes[cx][cy - 1]);
        if (cy < chunkY -1)     neighborBiomes.add(chunkBiomes[cx][cy + 1]);

        if (neighborBiomes.isEmpty()) {
            return base;
        }

        return neighborBiomes.get(random.nextInt(neighborBiomes.size()));
    }
}
