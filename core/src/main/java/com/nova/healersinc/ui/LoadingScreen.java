package com.nova.healersinc.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

public class LoadingScreen {

    private Stage stage;
    private ShapeRenderer shapeRenderer;
    private Label loadingLabel;
    private Label statusLabel;

    private float progress = 0f;
    private String currentTask = "Initializing...";

    private static final Color BACKGROUND_COLOR = new Color(0.1f, 0.15f, 0.2f, 1f);
    private static final Color BAR_BACKGROUND = new Color(0.2f, 0.25f, 0.3f, 1f);
    private static final Color BAR_FILL = new Color(0.3f, 0.7f, 0.5f, 1f);
    private static final float BAR_WIDTH = 400f;
    private static final float BAR_HEIGHT = 30f;

    public LoadingScreen() {
        stage = new Stage();
        shapeRenderer = new ShapeRenderer();

        // Create a simple skin with a default font
        Skin skin = new Skin();
        BitmapFont font = new BitmapFont();
        font.getData().setScale(1.5f);
        skin.add("default", font);

        Label.LabelStyle labelStyle = new Label.LabelStyle();
        labelStyle.font = font;
        labelStyle.fontColor = Color.WHITE;
        skin.add("default", labelStyle);

        // Creating labels
        loadingLabel = new Label("Loading...", skin);
        loadingLabel.setFontScale(2f);

        statusLabel = new Label(currentTask, skin);
        statusLabel.setFontScale(1f);

        stage.addActor(loadingLabel);
        stage.addActor(statusLabel);
    }

    public void setProgress(float progress) {
        this.progress = Math.max(0f, Math.min(1f, progress));
    }

    public void setStatus(String status) {
        this.currentTask = status;
        if (statusLabel != null) statusLabel.setText(status);
    }

    public float getProgress() {
        return progress;
    }

    public boolean isComplete() {
        return progress >= 1f;
    }

    public void render(float delta) {
        // Clear screen
        Gdx.gl.glClearColor(BACKGROUND_COLOR.r, BACKGROUND_COLOR.g, BACKGROUND_COLOR.b, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        float centerX = Gdx.graphics.getWidth() / 2f;
        float centerY = Gdx.graphics.getHeight() / 2f;

        // position labels
        loadingLabel.setPosition(
            centerX - loadingLabel.getWidth() / 2f,
            centerY
        );

        statusLabel.setText(currentTask);
        statusLabel.setPosition(
            centerX - statusLabel.getWidth() / 2f,
            centerY - 80
        );

        // Draw progress bar
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);

        // Background bar
        shapeRenderer.setColor(BAR_BACKGROUND);
        shapeRenderer.rect(
            centerX - BAR_WIDTH / 2f,
            centerY - BAR_HEIGHT /2f,
            BAR_WIDTH,
            BAR_HEIGHT
        );

        // Progress fill
        shapeRenderer.setColor(BAR_FILL);
        shapeRenderer.rect(
            centerX - BAR_WIDTH / 2f,
            centerY - BAR_HEIGHT / 2f,
            BAR_WIDTH,
            BAR_HEIGHT
        );

        shapeRenderer.end();

        // Draw stage
        stage.act(delta);
        stage.draw();
    }

    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }

    public void dispose() {
        stage.dispose();
        shapeRenderer.dispose();
    }
}
