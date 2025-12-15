package com.nova.healersinc.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Disposable;

/**
 * Hover tooltip showing tile information.
 * Displays biome name, resource type, yield, potency, and regrowth rate.
 */
public class TileTooltip extends Actor implements Disposable {

    private static final float PADDING = 8f;
    private static final float OFFSET_X = 15f;
    private static final float OFFSET_Y = 15f;

    private final BitmapFont font;
    private final GlyphLayout layout;
    private final ShapeRenderer shapeRenderer;

    private String text;
    private boolean showing;

    public TileTooltip(BitmapFont font) {
        this.font = font;
        this.layout = new GlyphLayout();
        this.shapeRenderer = new ShapeRenderer();
        this.showing = false;
    }

    public void show(String text, float screenX, float screenY) {
        this.text = text;
        this.showing = true;

        layout.setText(font, text);

        float tooltipWidth = layout.width + PADDING * 2;
        float tooltipHeight = layout.height + PADDING * 2;

        // Position tooltip near cursor with offset
        float x = screenX + OFFSET_X;
        float y = screenY + OFFSET_Y;

        // Adjust if tooltip would go off-screen
        int screenWidth = Gdx.graphics.getWidth();
        int screenHeight = Gdx.graphics.getHeight();

        if (x + tooltipWidth > screenWidth) {
            x = screenX - tooltipWidth - OFFSET_X;
        }
        if (y + tooltipHeight > screenHeight) {
            y = screenY - tooltipHeight - OFFSET_Y;
        }
        if (x < 0) {
            x = 0;
        }
        if (y < 0) {
            y = 0;
        }

        setPosition(x, y);
        setSize(tooltipWidth, tooltipHeight);
    }

    public void hide() {
        this.showing = false;
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        if (!showing || text == null) return;

        // Draw background
        batch.end();
        shapeRenderer.setProjectionMatrix(batch.getProjectionMatrix());
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(0.1f, 0.1f, 0.1f, 0.9f);
        shapeRenderer.rect(getX(), getY(), getWidth(), getHeight());
        shapeRenderer.end();

        // Draw border
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        shapeRenderer.setColor(0.4f, 0.4f, 0.4f, 1f);
        shapeRenderer.rect(getX(), getY(), getWidth(), getHeight());
        shapeRenderer.end();
        batch.begin();

        // Draw text
        font.setColor(Color.WHITE);
        font.draw(batch, text, getX() + PADDING, getY() + getHeight() - PADDING);
    }

    @Override
    public void dispose() {
        shapeRenderer.dispose();
    }
}
