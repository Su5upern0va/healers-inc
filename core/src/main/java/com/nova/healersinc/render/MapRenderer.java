package com.nova.healersinc.render;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.nova.healersinc.building.Building;
import com.nova.healersinc.building.BuildingType;
import com.nova.healersinc.world.biome.BiomeDefinition;
import com.nova.healersinc.world.biome.BiomeRegistry;
import com.nova.healersinc.world.biome.BiomeType;
import com.nova.healersinc.world.herb.HerbType;
import com.nova.healersinc.world.map.Tile;
import com.nova.healersinc.world.map.WorldMap;
import com.nova.healersinc.world.resource.ResourceDefinition;
import com.nova.healersinc.world.resource.ResourceRegistry;

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
     * Renders a single tile (biome base + resource overlay + building overlay)
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

        // Render building overlay if present (on top of everything)
        if (tile.hasBuilding()) {
            renderBuildingOverlay(tile.getBuilding(), drawX, drawY);
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
        ResourceDefinition def = ResourceRegistry.getDefinition(herbType);
        tempColor.set(def.getVisual().getColor());
        shapeRenderer.setColor(tempColor);

        float herbSize = WorldMap.TILE_SIZE / 2f;
        float offsetX = x + WorldMap.TILE_SIZE / 4f;
        float offsetY = y + WorldMap.TILE_SIZE / 4f;

        shapeRenderer.rect(offsetX, offsetY, herbSize, herbSize);
    }

    /**
     * Renders a simple debug overlay for a building on this tile.
     */
    private void renderBuildingOverlay(Building building, float x, float y) {
        if (building == null || building.getType() == null) return;

        Color buildingColor = getBuildingColor(building.getType());
        shapeRenderer.setColor(buildingColor);

        float padding = WorldMap.TILE_SIZE * 0.15f;
        float size = WorldMap.TILE_SIZE - 2 * padding;

        shapeRenderer.rect(
            x + padding,
            y + padding,
            size,
            size
        );
    }

    /**
     * Gets the color for a specific biome type
     */
    private void getBiomeColor(BiomeType biome, Color outColor) {
        if (biome != null) {
            BiomeDefinition def = BiomeRegistry.getDefinition(biome);
            outColor.set(def.getVisual().getColor());
        } else {
            outColor.set(Color.GRAY);
        }
    }

    /**
     * Gets a debug color for a specific building type.
     */
    private Color getBuildingColor(BuildingType type) {
        switch (type) {
            case HARVESTER:
                return tempColor.set(0.95f, 0.6f, 0.2f, 1f); // bright orange
            case DRYING_RACK:
                return tempColor.set(0.6f, 0.4f, 0.2f, 1f);  // brown
            case STORAGE:
                return tempColor.set(0.5f, 0.5f, 0.5f, 1f);  // gray
            default:
                return tempColor.set(Color.MAGENTA);
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
