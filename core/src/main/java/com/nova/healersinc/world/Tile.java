package com.nova.healersinc.world;

public class Tile {
    public final int x;
    public final int y;

    private BiomeType biome;
    private HerbNode herbNode; //null when not a herb node

    public Tile(int x, int y, BiomeType biome) {
        this.x = x;
        this.y = y;
        this.biome = biome;
    }

    public BiomeType getBiome() {
        return biome;
    }

    public void setBiome(BiomeType biome) {
        this.biome = biome;
    }

    public HerbNode getHerbNode() {
        return herbNode;
    }

    public void setHerbNode(HerbNode herbNode) {
        this.herbNode = herbNode;
    }

    public boolean isHerbNode() {
        return herbNode != null;
    }
}
