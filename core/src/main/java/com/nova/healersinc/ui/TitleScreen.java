package com.nova.healersinc.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

public class TitleScreen {

    public interface Listener {
        void onStartGame();
    }

    private static final float VIRTUAL_WIDTH = 1280;
    private static final float VIRTUAL_HEIGHT = 720;

    private final Listener listener;

    private final OrthographicCamera camera;
    private final Viewport viewport;
    private final SpriteBatch batch;
    private final ShapeRenderer shapes;
    private final BitmapFont font;

    private final InputAdapter inputAdapter;

    public TitleScreen(Listener listener) {
        this.listener = listener;

        camera = new OrthographicCamera();
        viewport = new FitViewport(VIRTUAL_WIDTH, VIRTUAL_HEIGHT, camera);
        viewport.apply(true);
        camera.position.set(VIRTUAL_WIDTH / 2f, VIRTUAL_HEIGHT / 2f, 0);
        camera.update();

        batch = new SpriteBatch();
        shapes = new ShapeRenderer();
        font = new BitmapFont(); //TODO: Muss noch ne font raussuchen die geil ist...

        inputAdapter = new InputAdapter() {
            @Override
            public boolean keyDown(int keycode) {
                if (listener != null) {
                    listener.onStartGame();
                }
                return true;
            }

            @Override
            public boolean touchDown(int screenX, int screenY, int pointer, int button) {
                if (listener != null) {
                    listener.onStartGame();
                }
                return true;
            }
        };
    }

    public InputAdapter getInputProcessor() {
        return inputAdapter;
    }

    public void resize(int width, int height) {
        viewport.update(width, height, true);
    }

    public void render(float delta) {
        //blackground Clear
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        viewport.apply();
        camera.update();

        // Draw a simple rounded-rect style "card" in the center
        shapes.setProjectionMatrix(camera.combined);
        shapes.begin(ShapeRenderer.ShapeType.Filled);

        // Full background gradient approximation using two rects
        shapes.setColor(0.69f, 0.93f, 0.86f, 1f); //light teal top
        shapes.rect(0, 0, VIRTUAL_WIDTH / 2f, VIRTUAL_HEIGHT / 2f);

        shapes.setColor(0.93f, 1f, 1f, 1f); // almost white bottom
        shapes.rect(0, 0, VIRTUAL_WIDTH, VIRTUAL_HEIGHT / 2f);

        // Logo placeholder
        float cardW = 220f;
        float cardH = 220f;
        float cardX = (VIRTUAL_WIDTH - cardW) / 2f;
        float cardY = (VIRTUAL_HEIGHT - cardH) / 2f;


        shapes.setColor(0.7f, 0.95f, 0.9f, 1f);
        shapes.rect(cardX, cardY, cardW, cardH);

        shapes.setColor(0.3f, 0.8f, 0.7f, 1f);
        // vertical bar of a cross
        shapes.rect(cardX + cardW / 2f - 20f, cardY + 40f, 40f, cardH - 80f);
        //horizontal bar of a cross
        shapes.rect(cardX + 40f, cardY + cardH / 2f - 20f, cardW - 80f, 40f);

        shapes.end();

        // Text
        batch.setProjectionMatrix(camera.combined);
        batch.begin();

        font.setColor(Color.DARK_GRAY);
        font.getData().setScale(1.3f);
        font.draw(
          batch,
          "Health Inc.",
          0,
          cardY - 20f,
            VIRTUAL_WIDTH,
            Align.center,
            false
        );

        font.setColor(new Color(0.1f, 0.4f, 0.35f, 1f));
        font.getData().setScale(0.9f);
        font.draw(
          batch,
          "Press any key to contune...",
          0,
          40f,
          VIRTUAL_WIDTH,
          Align.center,
          false
        );

        batch.end();
    }

    public void dispose() {
        shapes.dispose();
        batch.dispose();
        font.dispose();
    }
}
