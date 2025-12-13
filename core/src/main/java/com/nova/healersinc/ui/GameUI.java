package com.nova.healersinc.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.nova.healersinc.world.HerbType;

/**
 * Main UI controller that owns the Scene2D Stage and coordinates UI components.
 */
public class GameUI implements Disposable {
    private final Stage stage;
    private final BitmapFont font;
    private final HerbToolbar herbToolbar;
    private final TileTooltip tileTooltip;

    private HerbType selectedHerbType;

    public GameUI() {
        stage = new Stage(new ScreenViewport());
        font = new BitmapFont();

        herbToolbar = new HerbToolbar(this, font);
        tileTooltip = new TileTooltip(font);

        stage.addActor(herbToolbar);
        stage.addActor(tileTooltip);
    }

    public Stage getStage() {
        return stage;
    }

    public HerbType getSelectedHerbType() {
        return selectedHerbType;
    }

    public void setSelectedHerbType(HerbType type) {
        this.selectedHerbType = type;
        herbToolbar.updateSelection(type);
    }

    public void updateTooltip(String text, float screenX, float screenY) {
        tileTooltip.show(text, screenX, screenY);
    }

    public void hideTooltip() {
        tileTooltip.hide();
    }

    public void render() {
        stage.act(Gdx.graphics.getDeltaTime());
        stage.draw();
    }

    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
        herbToolbar.layoutToolbar(width, height);
    }

    @Override
    public void dispose() {
        stage.dispose();
        font.dispose();
        tileTooltip.dispose();
    }
}
