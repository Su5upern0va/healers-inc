package com.nova.healersinc.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

/**
 * Main UI controller that owns the Scene2D Stage and coordinates UI components.
 */
public class GameUI implements Disposable {
    private final Stage stage;
    private final BitmapFont font;
    private final TileTooltip tileTooltip;

    public GameUI() {
        stage = new Stage(new ScreenViewport());
        font = new BitmapFont();

        tileTooltip = new TileTooltip(font);
        stage.addActor(tileTooltip);
    }

    public Stage getStage() {
        return stage;
    }

    public void updateTooltip(String text, float screenX, float screenY) {
        tileTooltip.show(text, screenX, screenY);
    }

    public void hideTooltip() {
        tileTooltip.hide();
    }

    public void render() {
        // Apply UI viewport to reset glViewport to full screen
        stage.getViewport().apply();
        stage.act(Gdx.graphics.getDeltaTime());
        stage.draw();
    }

    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }

    @Override
    public void dispose() {
        stage.dispose();
        font.dispose();
        tileTooltip.dispose();
    }
}
