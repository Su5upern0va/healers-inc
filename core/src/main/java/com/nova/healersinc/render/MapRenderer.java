package com.nova.healersinc.render;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.nova.healersinc.world.BiomeType;
import com.nova.healersinc.world.HerbType;
import com.nova.healersinc.world.Tile;
import com.nova.healersinc.world.WorldMap;

public class MapRenderer {

    private final ShapeRenderer shapeRenderer;

    // Reusable objects to avoid garbage collection
    private final Rectangle visibleArea;
    private final Color tempColor;

    public MapRenderer() {
        this.shapeRenderer = new ShapeRenderer();
        this.visibleArea = new Rectangle();
        this.tempColor = new Color();
    }

    /**
     * Main render method - orchestrates the rendering pipeline
     */
    public void render(WorldMap worldMap, OrthographicCamera camera) {
        shapeRenderer.setProjectionMatrix(camera.combined);

        // Calculate visible tile bounds for culling
        calculateVisibleArea(camera, visibleArea);
        int startX = Math.max(0, (int) (visibleArea.x / WorldMap.TILE_SIZE));
        int endX = Math.min(worldMap.getWidth(), (int) ((visibleArea.x + visibleArea.width) / WorldMap.TILE_SIZE) + 1);
        int startY = Math.max(0, (int) (visibleArea.y / WorldMap.TILE_SIZE));
        int endY = Math.min(worldMap.getHeight(), (int) ((visibleArea.y + visibleArea.height) / WorldMap.TILE_SIZE) + 1);

        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);

        // Render only visible tiles
        for (int x = startX; x < endX; x++) {
            for (int y = startY; y < endY; y++) {
                Tile tile = worldMap.getTile(x, y);
                renderTile(tile, x, y);
            }
        }

        shapeRenderer.end();
    }

    /**
     * Renders a single tile (biome base + resource overlay)
     */
    private void renderTile(Tile tile, int x, int y) {
        float drawX = x * WorldMap.TILE_SIZE;
        float drawY = y * WorldMap.TILE_SIZE;

        // Render base biome
        renderBiomeBase(tile.getBiome(), drawX, drawY);

        // Render resource overlay if present
        if (tile.hasResourceNode()) {
            if (tile.isHerbNode()) {
                renderHerbOverlay(tile.getHerbNode().getType(), drawX, drawY);
            }
            // Future: add other resource types here (minerals, water, etc.)
        }
    }

    /**
     * Renders the base biome color for a tile
     */
    private void renderBiomeBase(BiomeType biome, float x, float y) {
        getBiomeColor(biome, tempColor);
        shapeRenderer.setColor(tempColor);
        shapeRenderer.rect(x, y, WorldMap.TILE_SIZE, WorldMap.TILE_SIZE);
    }

    /**
     * Renders herb overlay on a tile
     */
    private void renderHerbOverlay(HerbType herbType, float x, float y) {
        getHerbColor(herbType, tempColor);
        shapeRenderer.setColor(tempColor);

        float herbSize = WorldMap.TILE_SIZE / 2f;
        float offsetX = x + WorldMap.TILE_SIZE / 4f;
        float offsetY = y + WorldMap.TILE_SIZE / 4f;

        shapeRenderer.rect(offsetX, offsetY, herbSize, herbSize);
    }

    /**
     * Gets the color for a specific biome type
     */
    private void getBiomeColor(BiomeType biome, Color outColor) {
        switch (biome) {
            case SUNNY_MEADOW:
                outColor.set(0.3f, 0.8f, 0.3f, 1f);
                break;
            case SHADY_GROVE:
                outColor.set(0.1f, 0.4f, 0.1f, 1f);
                break;
            default:
                outColor.set(0.5f, 0.5f, 0.5f, 1f); // fallback gray
                break;
        }
    }

    /**
     * Gets the color for a specific herb type
     */
    private void getHerbColor(HerbType herbType, Color outColor) {
        switch (herbType) {
            case CHAMOMILE:
                outColor.set(1f, 1f, 0.8f, 1f); // pale yellow
                break;
            case MINT:
                outColor.set(0.4f, 1f, 0.8f, 1f); // mint green
                break;
            case ECHINACEA:
                outColor.set(0.8f, 0.4f, 1f, 1f); // purple
                break;
            default:
                outColor.set(1f, 1f, 1f, 1f); // fallback white
                break;
        }
    }

    /**
     * Calculates the visible area in world coordinates based on camera
     * This enables frustum culling to only render visible tiles
     */
    private void calculateVisibleArea(OrthographicCamera camera, Rectangle outRect) {
        float halfWidth = camera.viewportWidth * camera.zoom * 0.5f;
        float halfHeight = camera.viewportHeight * camera.zoom * 0.5f;

        outRect.x = camera.position.x - halfWidth;
        outRect.y = camera.position.y - halfHeight;
        outRect.width = camera.viewportWidth * camera.zoom;
        outRect.height = camera.viewportHeight * camera.zoom;
    }

    /**
     * Disposes of rendering resources
     */
    public void dispose() {
        shapeRenderer.dispose();
    }
}
