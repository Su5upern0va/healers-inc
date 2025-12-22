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
import com.nova.healersinc.ui.TitleScreen;
import com.nova.healersinc.world.biome.BiomeRegistry;
import com.nova.healersinc.world.resource.ResourceRegistry;
import com.nova.healersinc.world.map.WorldGenerator;
import com.nova.healersinc.world.map.WorldMap;

public class HealersIncGame extends ApplicationAdapter {

    private enum GameState {
        TITLE,
        PLAYING
    }

    private GameState gameState = GameState.TITLE;

    private WorldMap worldMap;
    private GameCamera gameCamera;
    private MapRenderer mapRenderer;
    private GameUI gameUI;
    private TileInteractionHandler tileInteractionHandler;
    private BuildingManager buildingManager;

    private TitleScreen titleScreen;
    private InputMultiplexer inputMultiplexer;

    @Override
    public void create() {

        ResourceRegistry.init();
        BiomeRegistry.init();

        // Create title screen first
        titleScreen = new TitleScreen(new TitleScreen.Listener() {
            @Override
            public void onStartGame() {
                if (gameState == GameState.TITLE) {
                    startGame();
                }
            }
        });

        // IMPORTANT: assign to the field, not a new local variable
        inputMultiplexer = new InputMultiplexer();
        inputMultiplexer.addProcessor(titleScreen.getInputProcessor());
        Gdx.input.setInputProcessor(inputMultiplexer);
    }

    private void startGame() {
        gameState = GameState.PLAYING;

        WorldGenerator generator = new WorldGenerator(69161L);
        worldMap = generator.generate(500, 500);

        gameCamera = new GameCamera(640, 480, worldMap);
        mapRenderer = new MapRenderer();
        buildingManager = new BuildingManager(worldMap);

        gameUI = new GameUI();
        tileInteractionHandler = new TileInteractionHandler(worldMap, gameCamera, gameUI, buildingManager);

        // Rebuild input chain: Stage first, then tile interaction, then camera pan/zoom
        inputMultiplexer.clear();
        inputMultiplexer.addProcessor(gameUI.getStage());
        inputMultiplexer.addProcessor(tileInteractionHandler);
        inputMultiplexer.addProcessor(gameCamera.getInputProcessor());

        int w = Gdx.graphics.getWidth();
        int h = Gdx.graphics.getHeight();
        gameCamera.resize(w, h);
        gameUI.resize(w, h);
    }

    @Override
    public void render() {
        float delta = Gdx.graphics.getDeltaTime();

        if (gameState == GameState.TITLE) {
            titleScreen.render(delta);
            return;
        }

        gameCamera.update(delta);
        tileInteractionHandler.updateHover();

        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        gameCamera.getViewport().apply();

        buildingManager.update(delta);
        mapRenderer.render(worldMap, gameCamera.getCamera());
        gameUI.render();
    }

    @Override
    public void resize(int width, int height) {
        if (titleScreen != null) {
            titleScreen.resize(width, height);
        }
        if (gameCamera != null) {
            gameCamera.resize(width, height);
        }
        if (gameUI != null) {
            gameUI.resize(width, height);
        }
    }

    @Override
    public void dispose() {
        if (mapRenderer != null) mapRenderer.dispose();
        if (gameUI != null) gameUI.dispose();
        if (titleScreen != null) titleScreen.dispose();
    }
}
