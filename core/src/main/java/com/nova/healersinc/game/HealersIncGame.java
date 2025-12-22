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
import com.nova.healersinc.ui.LoadingScreen;
import com.nova.healersinc.ui.TitleScreen;
import com.nova.healersinc.world.biome.BiomeRegistry;
import com.nova.healersinc.world.resource.ResourceRegistry;
import com.nova.healersinc.world.map.WorldGenerator;
import com.nova.healersinc.world.map.WorldMap;

public class HealersIncGame extends ApplicationAdapter {

    private enum GameState {
        TITLE,
        LOADING,
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
    private LoadingScreen loadingScreen;
    private InputMultiplexer inputMultiplexer;

    // Loading state variables
    private boolean isLoadingStartet = false;
    private int loadingStep = 0;
    private static final int NUM_LOADING_STEPS = 5;

    @Override
    public void create() {

        ResourceRegistry.init();
        BiomeRegistry.init();

        // Create the title screen first
        titleScreen = new TitleScreen(new TitleScreen.Listener() {
            @Override
            public void onStartGame() {
                if (gameState == GameState.TITLE) {
                    transitionToLoading();
                }
            }
        });

        // IMPORTANT: assign to the field, not a new local variable
        inputMultiplexer = new InputMultiplexer();
        inputMultiplexer.addProcessor(titleScreen.getInputProcessor());
        Gdx.input.setInputProcessor(inputMultiplexer);

        int w = Gdx.graphics.getWidth();
        int h = Gdx.graphics.getHeight();
        titleScreen.resize(w, h);
    }

    private void transitionToLoading() {
        gameState = GameState.LOADING;
        loadingScreen = new LoadingScreen();

        int w = Gdx.graphics.getWidth();
        int h = Gdx.graphics.getHeight();
        loadingScreen.resize(w, h);

        isLoadingStartet = true;
        loadingStep = 0;
    }

    private void updateLoading() {
        if (!isLoadingStartet) {
            isLoadingStartet = true;
            loadingStep = 0;
        }

        // Simulate loading steps
        switch (loadingStep) {
            case 0:
                loadingScreen.setStatus("Generating world...");
                loadingScreen.setProgress(0.2f);
                WorldGenerator generator = new WorldGenerator(69161L);
                worldMap = generator.generate(500, 500);
                loadingStep++;
                break;

            case 1:
                loadingScreen.setStatus("Initializing camera... ");
                loadingScreen.setProgress(0.4f);
                gameCamera = new GameCamera(640, 480, worldMap);
                loadingStep++;
                break;

            case 2:
                loadingScreen.setStatus("Loading renderer...");
                loadingScreen.setProgress(0.6f);
                mapRenderer = new MapRenderer();
                buildingManager = new BuildingManager(worldMap);
                loadingStep++;
                break;

            case 3:
                loadingScreen.setStatus("Setting up UI...");
                loadingScreen.setProgress(0.8f);
                gameUI = new GameUI();
                tileInteractionHandler = new TileInteractionHandler(worldMap, gameCamera, gameUI, buildingManager);
                loadingStep++;
                break;

            case 4:
                loadingScreen.setStatus("Finalizing...");
                loadingScreen.setProgress(1.0f);

                // Rebuild input chain: Stage first, then tile interaction, then camera pan/zoom
                inputMultiplexer.clear();
                inputMultiplexer.addProcessor(gameUI.getStage());
                inputMultiplexer.addProcessor(tileInteractionHandler);
                inputMultiplexer.addProcessor(gameCamera.getInputProcessor());

                int w = Gdx.graphics.getWidth();
                int h = Gdx.graphics.getHeight();
                gameCamera.resize(w, h);
                gameUI.resize(w, h);

                loadingStep++;
                break;

            case 5:
                gameState = GameState.PLAYING;
                if (loadingScreen != null) {
                    loadingScreen.dispose();
                    loadingScreen = null;
                }
                break;
        }
    }

    @Override
    public void render() {
        float delta = Gdx.graphics.getDeltaTime();

        if (gameState == GameState.TITLE) {
            titleScreen.render(delta);
            return;
        }

        if (gameState == GameState.LOADING) {
            updateLoading();
            if (loadingScreen != null) {
                loadingScreen.render(delta);
            }
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
        if (loadingScreen != null) {
            loadingScreen.resize(width, height);
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
        if (loadingScreen != null) loadingScreen.dispose();
    }
}
