package com.nova.healersinc;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.nova.healersinc.render.MapRenderer;
import com.nova.healersinc.world.WorldGenerator;
import com.nova.healersinc.world.WorldMap;

/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */
public class HealersIncGame extends ApplicationAdapter {

    private WorldMap worldMap;
    private GameCamera gameCamera;
    private MapRenderer mapRenderer;

    @Override
    public void create() {
        WorldGenerator generator = new WorldGenerator(69161L);
        worldMap = generator.generate(500, 500);

        gameCamera = new GameCamera(640, 480, worldMap);
        mapRenderer = new MapRenderer();
    }

    @Override
    public void render() {
        float delta = Gdx.graphics.getDeltaTime();
        gameCamera.update(delta);

        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        mapRenderer.render(worldMap, gameCamera.getCamera());
    }

    @Override
    public void resize(int width, int height) {
        gameCamera.resize(width, height);
    }

    @Override
    public void dispose() {
        mapRenderer.dispose();
    }
}
