package com.nova.healersinc.world.map;

public class WorldMap {

    public static final int TILE_SIZE = 32;

    private final int width;
    private final int height;
    private final Tile[][] tiles;

    public WorldMap(int width, int height) {
        this.width = width;
        this.height = height;
        this.tiles = new Tile[width][height];
    }

    public void setTile(int x, int y, Tile tile) {
        tiles[x][y] = tile;
    }

    public Tile getTile(int x, int y) {
        return tiles[x][y];
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }
}
