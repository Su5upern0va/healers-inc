package com.nova.healersinc.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.nova.healersinc.world.HerbType;

import java.util.EnumMap;
import java.util.Map;

/**
 * Bottom toolbar with herb selection buttons.
 * Allows players to select an herb type to place on the map.
 */
public class HerbToolbar extends Group {

    private static final float BUTTON_WIDTH = 110f;
    private static final float BUTTON_HEIGHT = 40f;
    private static final float BUTTON_PADDING = 10f;
    private static final float TOOLBAR_MARGIN = 10f;

    private final GameUI gameUI;
    private final Map<HerbType, TextButton> herbButtons;
    private final Map<HerbType, TextButtonStyle> normalStyles;
    private final Map<HerbType, TextButtonStyle> selectedStyles;

    public HerbToolbar(GameUI gameUI, BitmapFont font) {
        this.gameUI = gameUI;
        this.herbButtons = new EnumMap<>(HerbType.class);
        this.normalStyles = new EnumMap<>(HerbType.class);
        this.selectedStyles = new EnumMap<>(HerbType.class);

        // Create buttons for each herb type
        float xOffset = 0;
        for (HerbType herbType : HerbType.values()) {
            TextButtonStyle normalStyle = createButtonStyle(font, getHerbColor(herbType), false);
            TextButtonStyle selectedStyle = createButtonStyle(font, getHerbColor(herbType), true);
            normalStyles.put(herbType, normalStyle);
            selectedStyles.put(herbType, selectedStyle);

            TextButton button = new TextButton(formatHerbName(herbType), normalStyle);
            button.setSize(BUTTON_WIDTH, BUTTON_HEIGHT);
            button.setPosition(xOffset, 0);

            button.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    gameUI.setSelectedHerbType(herbType);
                }
            });

            addActor(button);
            herbButtons.put(herbType, button);
            xOffset += BUTTON_WIDTH + BUTTON_PADDING;
        }

        layoutToolbar(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
    }

    private TextButtonStyle createButtonStyle(BitmapFont font, Color herbColor, boolean selected) {
        TextButtonStyle style = new TextButtonStyle();
        style.font = font;
        style.fontColor = Color.BLACK;

        if (selected) {
            // Brighter version for selected state with border effect
            Color bgColor = new Color(herbColor).lerp(Color.WHITE, 0.3f);
            style.up = createColorDrawable(bgColor);
            style.down = createColorDrawable(bgColor);
            style.over = createColorDrawable(bgColor);
        } else {
            // Slightly darker for normal state
            Color bgColor = new Color(herbColor).lerp(Color.GRAY, 0.2f);
            Color hoverColor = new Color(herbColor);
            style.up = createColorDrawable(bgColor);
            style.down = createColorDrawable(herbColor);
            style.over = createColorDrawable(hoverColor);
        }

        return style;
    }

    private TextureRegionDrawable createColorDrawable(Color color) {
        Pixmap pixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        pixmap.setColor(color);
        pixmap.fill();
        Texture texture = new Texture(pixmap);
        pixmap.dispose();
        return new TextureRegionDrawable(new TextureRegion(texture));
    }

    public void updateSelection(HerbType selectedType) {
        for (Map.Entry<HerbType, TextButton> entry : herbButtons.entrySet()) {
            TextButton button = entry.getValue();
            if (entry.getKey() == selectedType) {
                button.setStyle(selectedStyles.get(entry.getKey()));
            } else {
                button.setStyle(normalStyles.get(entry.getKey()));
            }
        }
    }

    public void layoutToolbar(int screenWidth, int screenHeight) {
        // Position toolbar at bottom center
        float totalWidth = HerbType.values().length * (BUTTON_WIDTH + BUTTON_PADDING) - BUTTON_PADDING;
        setPosition((screenWidth - totalWidth) / 2f, TOOLBAR_MARGIN);
    }

    /**
     * Gets the color for a specific herb type (matching MapRenderer colors)
     */
    private Color getHerbColor(HerbType herbType) {
        switch (herbType) {
            case CHAMOMILE:
                return new Color(1f, 1f, 0.8f, 1f); // pale yellow
            case MINT:
                return new Color(0.4f, 1f, 0.8f, 1f); // mint green
            case ECHINACEA:
                return new Color(0.8f, 0.4f, 1f, 1f); // purple
            default:
                return new Color(1f, 1f, 1f, 1f); // fallback white
        }
    }

    private String formatHerbName(HerbType herbType) {
        String name = herbType.name();
        return name.charAt(0) + name.substring(1).toLowerCase();
    }
}
