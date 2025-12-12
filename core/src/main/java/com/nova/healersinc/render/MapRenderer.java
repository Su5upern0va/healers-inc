package com.nova.healersinc.render;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.nova.healersinc.world.HerbType;
import com.nova.healersinc.world.Tile;
import com.nova.healersinc.world.WorldMap;

public class MapRenderer {

    private final ShapeRenderer shapeRenderer;

    public MapRenderer() {
        this.shapeRenderer = new ShapeRenderer();
    }

    public void render(WorldMap worldMap, OrthographicCamera camera) {
        shapeRenderer.setProjectionMatrix(camera.combined);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);

        for (int x = 0; x < worldMap.getWidth(); x++) {
            for (int y = 0; y < worldMap.getHeight(); y++) {
                Tile tile = worldMap.getTile(x, y);

                // biome color
                switch (tile.getBiome()) {
                    case SUNNY_MEADOW:
                        shapeRenderer.setColor(0.3f, 0.8f, 0.3f, 1f);
                        break;
                    default: // SHADY_GROVE
                        shapeRenderer.setColor(0.1f, 0.4f, 0.1f ,1f);
                        break;
                }

                float drawX = x * WorldMap.TILE_SIZE;
                float drawY = y * WorldMap.TILE_SIZE;

                //base tile
                shapeRenderer.rect(drawX, drawY, WorldMap.TILE_SIZE, WorldMap.TILE_SIZE);

                //resource overlay
                if (tile.hasResourceNode()) {
                    HerbType type = tile.getHerbNode().getType();

                    switch (type) {
                        case CHAMOMILE:
                            shapeRenderer.setColor(1f, 1f, 0.8f, 1f);
                            break;
                        case MINT:
                            shapeRenderer.setColor(0.4f, 1f, 0.8f, 1f);
                            break;
                        default: // ECHINACEA
                            shapeRenderer.setColor(0.8f, 0.4f, 1f, 1f);
                            break;
                    }

                    float herbSize = WorldMap.TILE_SIZE / 2f;
                    shapeRenderer.rect(
                        drawX + WorldMap.TILE_SIZE / 4f,
                        drawY + WorldMap.TILE_SIZE /4f,
                        herbSize,
                        herbSize
                    );
                }
            }
        }

        shapeRenderer.end();
    }

    public void dispose() {
        shapeRenderer.dispose();
    }
}
