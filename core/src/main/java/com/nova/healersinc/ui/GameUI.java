package com.nova.healersinc.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

/**
 * Main UI controller that owns the Scene2D Stage and coordinates UI components.
 * Currently only provides a tooltip and a very simple debug status label.
 */
public class GameUI implements Disposable {
    private final Stage stage;
    private final BitmapFont font;
    private final TileTooltip tileTooltip;

    // Debug label that shows current building mode
    private final Label debugStatusLabel;

    public GameUI() {
        stage = new Stage(new ScreenViewport());
        font = new BitmapFont();

        tileTooltip = new TileTooltip(font);
        stage.addActor(tileTooltip);

        // Simple label using default style (no Skin)
        Label.LabelStyle labelStyle = new Label.LabelStyle();
        labelStyle.font = font;
        debugStatusLabel = new Label("", labelStyle);
        debugStatusLabel.setPosition(10, 10);
        stage.addActor(debugStatusLabel);
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

    /**
     * Shows current building mode on screen (e.g. "Harvester placement ON" / "OFF").
     */
    public void setDebugStatusText(String text) {
        debugStatusLabel.setText(text == null ? "" : text);
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
