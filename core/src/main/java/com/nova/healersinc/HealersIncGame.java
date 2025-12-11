package com.nova.healersinc;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.nova.healersinc.world.*;

/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */
public class HealersIncGame extends ApplicationAdapter {

    private ShapeRenderer shapeRenderer;
    private WorldMap worldMap;
    private GameCamera gameCamera;

    @Override
    public void create() {
        shapeRenderer = new ShapeRenderer();

        WorldGenerator generator = new WorldGenerator(69161L);
        worldMap = generator.generate(500, 500);
    }

    @Override
    public void render() {
        float delta = Gdx.graphics.getDeltaTime();
        gameCamera.update(delta);

        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        shapeRenderer.setProjectionMatrix(gameCamera.getCamera().combined);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);

        for (int x = 0; x < worldMap.getWidth(); x++) {
            for (int y = 0; y < worldMap.getHeight(); y++) {
                Tile tile = worldMap.getTile(x, y);

                //biome color
                switch (tile.getBiome()) {
                    case SUNNY_MEADOW:
                        shapeRenderer.setColor(0.3f, 0.8f, 0.3f, 1f);
                        break;
                    default: //SHADY_GROVE
                        shapeRenderer.setColor(0.1f, 0.4f, 0.1f, 1f);
                        break;
                }

                float drawX = x * WorldMap.TILE_SIZE;
                float drawY = y * WorldMap.TILE_SIZE;
                shapeRenderer.rect(drawX, drawY, WorldMap.TILE_SIZE, WorldMap.TILE_SIZE);

                //herb overlay
                if (tile.isHerbNode()) {
                    HerbType type = tile.getHerbNode().getType();

                    switch (type) {
                        case CHAMOMILE:
                            shapeRenderer.setColor(1f, 1f, 0.8f, 1f); //pale
                            break;
                        case MINT:
                            shapeRenderer.setColor(0.4f, 1f, 0.8f, 1f);
                            break;
                        default: //ECHINACEA
                            shapeRenderer.setColor(0.8f, 0.4f, 1f, 1f); //purple-ish?
                            break;
                    }

                    float herbSize = WorldMap.TILE_SIZE / 2f;
                    shapeRenderer.rect(
                        drawX + WorldMap.TILE_SIZE / 4f,
                        drawY + WorldMap.TILE_SIZE / 4f,
                        herbSize,
                        herbSize
                    );
                }
            }
        }

        shapeRenderer.end();
    }

    @Override
    public void dispose() {
        shapeRenderer.dispose();
    }
}
