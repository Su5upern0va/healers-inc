package com.nova.healersinc.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.GL20;
import com.nova.healersinc.building.BuildingManager;
import com.nova.healersinc.camera.GameCamera;
import com.nova.healersinc.interaction.TileInteractionHandler;
import com.nova.healersinc.render.MapRenderer;
import com.nova.healersinc.ui.GameUI;
import com.nova.healersinc.world.biome.BiomeRegistry;
import com.nova.healersinc.world.resource.ResourceRegistry;
import com.nova.healersinc.world.map.WorldGenerator;
import com.nova.healersinc.world.map.WorldMap;

/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */
public class HealersIncGame extends ApplicationAdapter {

    private WorldMap worldMap;
    private GameCamera gameCamera;
    private MapRenderer mapRenderer;
    private GameUI gameUI;
    private TileInteractionHandler tileInteractionHandler;
    private BuildingManager buildingManager;

    @Override
    public void create() {

        ResourceRegistry.init();
        BiomeRegistry.init();
        WorldGenerator generator = new WorldGenerator(69161L);
        worldMap = generator.generate(500, 500);

        gameCamera = new GameCamera(640, 480, worldMap);
        mapRenderer = new MapRenderer();
        buildingManager = new BuildingManager(worldMap);

        // Initialize UI layer
        gameUI = new GameUI();

        // Initialize tile interaction handler
        tileInteractionHandler = new TileInteractionHandler(worldMap, gameCamera, gameUI, buildingManager);

        // Setup input multiplexer: Stage first, then tile interaction, then camera pan/zoom
        InputMultiplexer multiplexer = new InputMultiplexer();
        multiplexer.addProcessor(gameUI.getStage());
        multiplexer.addProcessor(tileInteractionHandler);
        multiplexer.addProcessor(gameCamera.getInputProcessor());
        Gdx.input.setInputProcessor(multiplexer);
    }

    @Override
    public void render() {
        float delta = Gdx.graphics.getDeltaTime();
        gameCamera.update(delta);

        // Update tile hover detection
        tileInteractionHandler.updateHover();

        // Clear full screen (black letterbox areas)
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // Apply viewport to set up correct glViewport with letterboxing
        gameCamera.getViewport().apply();

        // Update buildings
        buildingManager.update(delta);

        // Render world
        mapRenderer.render(worldMap, gameCamera.getCamera());

        // Render UI on top (uses its own ScreenViewport)
        gameUI.render();
    }

    @Override
    public void resize(int width, int height) {
        gameCamera.resize(width, height);
        gameUI.resize(width, height);
    }

    @Override
    public void dispose() {
        mapRenderer.dispose();
        gameUI.dispose();
    }
}
