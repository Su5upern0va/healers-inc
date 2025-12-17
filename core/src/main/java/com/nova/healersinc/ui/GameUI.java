package com.nova.healersinc.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.nova.healersinc.building.BuildingType;
import com.nova.healersinc.interaction.TileInteractionHandler;

/**
 * Main UI controller that owns the Scene2D Stage and coordinates UI components.
 */
public class GameUI implements Disposable {
    private final Stage stage;
    private final BitmapFont font;
    private final TileTooltip tileTooltip;
    private final Skin skin;

    // Building UI
    private Table buildingMenu;
    private TextButton harvesterButton;
    private Label statusLabel;
    private TextButton selectedButton;

    // Reference to interaction handler (set after construction)
    private TileInteractionHandler interactionHandler;

    public GameUI() {
        stage = new Stage(new ScreenViewport());
        font = new BitmapFont();
        skin = new Skin(Gdx.files.internal("ui/uiskin.json"));

        tileTooltip = new TileTooltip(font);
        stage.addActor(tileTooltip);

        createBuildingsMenu();
        createStatusLabel();
    }

    private void createBuildingsMenu() {
        buildingMenu = new Table();
        buildingMenu.setFillParent(false);
        buildingMenu.top().left();
        buildingMenu.setPosition(10, Gdx.graphics.getHeight() - 10);
        buildingMenu.pad(10);

        //Tile
        Label titleLabel = new Label("Buldings", skin);
        titleLabel.setColor(Color.YELLOW);
        buildingMenu.add(titleLabel).colspan(2).padBottom(5).row();

        // Harvester button
        harvesterButton = new TextButton("Harvester ($" + BuildingType.HARVESTER.getCost() + ")", skin);
        harvesterButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                selectBuilding(BuildingType.HARVESTER, harvesterButton);
            }
        });
        buildingMenu.add(harvesterButton).width(150).padBottom(5).row();

        // Add description lable
        Label descLabel = new Label(BuildingType.HARVESTER.getDescription(), skin);
        descLabel.setFontScale(0.70f);
        descLabel.setWrap(true);
        buildingMenu.add(descLabel).width(150).padBottom(10).row();

        stage.addActor(buildingMenu);
    }

    private void createStatusLabel() {
        statusLabel = new Label("", skin);
        statusLabel.setColor(Color.CYAN);
        statusLabel.setPosition(10, 10);
        stage.addActor(statusLabel);
    }

    private void selectBuilding(BuildingType type, TextButton button) {
        if (interactionHandler != null) {
            interactionHandler.setSelectedBuildingType(type);

            // Visual Feedback
            if (selectedButton != null) {
                selectedButton.setColor(Color.WHITE);
            }
            selectedButton = button;
            selectedButton.setColor(Color.GREEN);

            statusLabel.setText("Click on a tile to place " + type.getDisplayName());
            System.out.println("Selected building: " + type.getDisplayName());
        }
    }

    public void clearBuildingSelection() {
        if (selectedButton != null) {
            selectedButton.setColor(Color.WHITE);
            selectedButton = null;
        }
        statusLabel.setText("");
    }

    public void setInteractionHandler(TileInteractionHandler handler) {
        this.interactionHandler = handler;
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
        skin.dispose();
    }
}
