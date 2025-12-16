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

        // Store biomes for each tile and identify potential cluster centers
        BiomeType[][] tileBiomes = new BiomeType[width][height];
        List<int[]> clusterCenters = new ArrayList<>();

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                BiomeType biome = pickBiomeForTile(x, y, chunkBiomes);
                tileBiomes[x][y] = biome;

                // Check if this tile should be a cluster center
                if (shouldBeHerbClusterCenter(biome)) {
                    clusterCenters.add(new int[]{x, y});
                }

                // Initialize tile with its biome (no herb node yet)
                worldMap.setTile(x, y, new Tile(x, y, biome));
            }
        }

        // Now, grow the clusters from the identified centers
        growHerbClusters(worldMap, tileBiomes, clusterCenters);

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

    private boolean shouldBeHerbClusterCenter(BiomeType biome) {
        float roll = random.nextFloat();

        switch (biome) {
            case SUNNY_MEADOW:
                // 1% chance for a cluster center
                return roll < 0.01f;

            case SHADY_GROVE:
                // 1% chance for a cluster center
                return roll < 0.005f;
        }
        return false;
    }

    private void growHerbClusters(WorldMap worldMap, BiomeType[][] tileBiomes, List<int[]> clusterCenters) {
        int width = worldMap.getWidth();
        int height = worldMap.getHeight();

        for (int[] centerCoords : clusterCenters) {
            int centerX = centerCoords[0];
            int centerY = centerCoords[1];

            BiomeType biome = tileBiomes[centerX][centerY];
            HerbType herbType = getHerbTypeForBiome(biome);

            if (herbType == null) {
                continue;
            }

            // Place the center herb
            Tile centerTile = worldMap.getTile(centerX, centerY);
            if (!centerTile.hasResourceNode()) {
                centerTile.setResourceNode(createHerbNode(herbType));
            }

            // Grow 2-4 additional herbs around the center
            int herbsToGrow = 2 + random.nextInt(3); // 2, 3, or 4

            for (int i = 0; i < herbsToGrow; i++) {
                int attempts = 0;
                while (attempts < 10) {
                    int dx = random.nextInt(3) - 1; // -1, 0, 1
                    int dy = random.nextInt(3) - 1; // -1, 0, 1

                    int nx = centerX + dx;
                    int ny = centerY + dy;

                    // Check bounds
                    if (nx >= 0 && nx < width && ny >= 0 && ny < height) {
                        Tile neighborTile = worldMap.getTile(nx, ny);

                        if (!neighborTile.hasResourceNode()) {
                            neighborTile.setResourceNode(createHerbNode(herbType));
                            break;
                        }
                    }
                    attempts++;
                }
            }
        }
    }

    private HerbType getHerbTypeForBiome(BiomeType biome) {
        switch (biome) {
            case SUNNY_MEADOW:
                // 70% Chamomile, 30% Mint in sunny meadows
                return random.nextFloat() < 0.7f ? HerbType.CHAMOMILE : HerbType.MINT;
            case SHADY_GROVE:
                // 60% Mint, 40% Echinacea in shady groves
                return random.nextFloat() < 0.6f ? HerbType.MINT : HerbType.ECHINACEA;
            default:
                return HerbType.CHAMOMILE;
        }
    }

    private HerbNode createHerbNode(HerbType type) {
        switch (type) {
            case MINT:
                return new HerbNode(type, 8, 1.1f, 0.12f);
            case ECHINACEA:
                return new HerbNode(type, 12, 1.2f, 0.08f);
            default: // CHAMOMILE
                return new HerbNode(type, 10, 1.0f, 0.1f);
        }
    }

    private BiomeType pickBiomeForTile(int x, int y, BiomeType[][] chunkBiomes) {
        int chunkX = chunkBiomes.length;
        int chunkY = chunkBiomes[0].length;

        int cx = x / BIOME_CHUNK_SIZE;
        int cy = y / BIOME_CHUNK_SIZE;

        BiomeType base = chunkBiomes[cx][cy];

        // how much biomes bleed into each other
        float borderNoiseStrength = 0.25f; //0 = straight edges, 1 = looks like verdun 1918

        int localX = x % BIOME_CHUNK_SIZE;
        int localY = y % BIOME_CHUNK_SIZE;
        int distToLeft = localX;
        int distToRight = BIOME_CHUNK_SIZE - 1 - localX;
        int distToBottom = localY;
        int distToTop = BIOME_CHUNK_SIZE - 1 - localY;

        int minDistToEdge = Math.min(Math.min(distToLeft, distToRight), Math.min(distToBottom, distToTop));

        if (minDistToEdge > 2) {
            return base;
        }

        float edgeFactor = 1.0f - (minDistToEdge / 2.0f);
        float bleedChance = borderNoiseStrength * edgeFactor;

        if (random.nextFloat() > bleedChance) {
            return base;
        }

        List<BiomeType> neighborBiomes = new ArrayList<>();

        if (cx > 0) neighborBiomes.add(chunkBiomes[cx - 1][cy]);
        if (cx < chunkX - 1) neighborBiomes.add(chunkBiomes[cx + 1][cy]);
        if (cy > 0) neighborBiomes.add(chunkBiomes[cx][cy - 1]);
        if (cy < chunkY - 1) neighborBiomes.add(chunkBiomes[cx][cy + 1]);

        if (neighborBiomes.isEmpty()) {
            return base;
        }

        return neighborBiomes.get(random.nextInt(neighborBiomes.size()));
    }
}
