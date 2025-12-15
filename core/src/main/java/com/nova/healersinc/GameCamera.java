package com.nova.healersinc;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.nova.healersinc.world.WorldMap;

import com.badlogic.gdx.InputProcessor;

public class GameCamera {
    private OrthographicCamera camera;
    private Viewport viewport;
    private InputProcessor inputProcessor;

    // Drag panning
    private float lastTouchX, lastTouchY;
    private boolean dragging = false;

    //Zoom limits
    private static final float MIN_ZOOM = 0.5f;
    private static final float MAX_ZOOM = 3.0f;

    private WorldMap worldMap;

    public GameCamera(int viewportWidth, int viewportHeight, WorldMap worldMap) {

        this.worldMap = worldMap;

        camera = new OrthographicCamera();
        viewport = new ExtendViewport(viewportWidth, viewportHeight, camera);

        float mapWidthWorld = worldMap.getWidth() * WorldMap.TILE_SIZE;
        float mapHeightWorld = worldMap.getHeight() * WorldMap.TILE_SIZE;
        camera.position.set(mapWidthWorld / 2f, mapHeightWorld / 2f, 0);
        camera.update();

        inputProcessor = createInputProcessor();
    }

    public InputProcessor getInputProcessor() {
        return inputProcessor;
    }

    public OrthographicCamera getCamera() {
        return camera;
    }

    public Viewport getViewport() {
        return viewport;
    }

    public void update(float delta) {
        handleKeyboardPan(delta);
        clampCameraToWorld();
        camera.update();
    }

    public void resize(int width, int height) {
        viewport.update(width, height);
    }

    private InputAdapter createInputProcessor() {
        return new InputAdapter() {
            @Override
            public boolean touchDown(int screenX, int screenY, int pointer, int button) {
                if (button == Input.Buttons.LEFT) {
                    lastTouchX = screenX;
                    lastTouchY = screenY;
                    dragging = true;
                    return true;
                }
                return false;
            }

            @Override
            public boolean touchDragged(int screenX, int screenY, int pointer) {
                if (!dragging) return false;

                float dx = screenX - lastTouchX;
                float dy = screenY - lastTouchY;

                camera.position.add(-dx * camera.zoom, dy *camera.zoom, 0);

                lastTouchX = screenX;
                lastTouchY = screenY;
                return true;
            }

            @Override
            public boolean touchUp(int screenX, int screenY, int pointer, int button) {
                if (button == Input.Buttons.LEFT) {
                    dragging = false;
                    return true;
                }
                return false;
            }

            @Override
            public boolean scrolled(float amountX, float amountY) {
                float zoomChange = 0.1f * amountY;
                camera.zoom = MathUtils.clamp(camera.zoom + zoomChange, MIN_ZOOM, MAX_ZOOM);
                return true;
            }
        };
    }

    private void handleKeyboardPan(float delta) {
        float panSpeed = 500f;

        if (Gdx.input.isKeyPressed(Input.Keys.A) || Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            camera.position.x -= panSpeed * delta * camera.zoom;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.D) || Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            camera.position.x += panSpeed * delta * camera.zoom;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.W) || Gdx.input.isKeyPressed(Input.Keys.UP)) {
            camera.position.y += panSpeed * delta * camera.zoom;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.S) || Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
            camera.position.y -= panSpeed * delta * camera.zoom;
        }
    }

    private void clampCameraToWorld() {
        float mapWidthWorld = worldMap.getWidth() * WorldMap.TILE_SIZE;
        float mapHeightWorld = worldMap.getHeight() * WorldMap.TILE_SIZE;

        float effectiveViewportWidth = camera.viewportWidth * camera.zoom;
        float effectiveViewportHeight = camera.viewportHeight * camera.zoom;

        float halfEffectiveViewportWidth = effectiveViewportWidth * 0.5f;
        float halfEffectiveViewportHeight = effectiveViewportHeight * 0.5f;

        float minX = halfEffectiveViewportWidth;
        float maxX = mapWidthWorld - halfEffectiveViewportWidth;
        float minY = halfEffectiveViewportHeight;
        float maxY = mapHeightWorld - halfEffectiveViewportHeight;

        // keep map centered if the map is smaller then the viewport
        if (mapWidthWorld < effectiveViewportWidth) {
            camera.position.x = mapWidthWorld / 2f;
        } else {
            camera.position.x = MathUtils.clamp(camera.position.x, minX, maxX);
        }

        if (mapHeightWorld < effectiveViewportHeight) {
            camera.position.y = mapHeightWorld / 2f;
        } else {
            camera.position.y = MathUtils.clamp(camera.position.y, minY, maxY);
        }
    }
}
