package com.nova.healersinc.interaction;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.math.Vector3;
import com.nova.healersinc.GameCamera;
import com.nova.healersinc.ui.GameUI;
import com.nova.healersinc.world.*;

/**
 * Handles tile hover detection for displaying tile information tooltips.
 * Converts screen coordinates to tile coordinates.
 */
public class TileInteractionHandler extends InputAdapter {

    private final WorldMap worldMap;
    private final GameCamera gameCamera;
    private final GameUI gameUI;
    private final Vector3 unprojected;

    public TileInteractionHandler(WorldMap worldMap, GameCamera gameCamera, GameUI gameUI) {
        this.worldMap = worldMap;
        this.gameCamera = gameCamera;
        this.gameUI = gameUI;
        this.unprojected = new Vector3();
    }

    /**
     * Called every frame from HealersIncGame.render() to update hover tooltip
     */
    public void updateHover() {
        int screenX = Gdx.input.getX();
        int screenY = Gdx.input.getY();

        // Check if mouse is over UI (Stage uses Y-up coordinates)
        float stageY = Gdx.graphics.getHeight() - screenY;
        if (gameUI.getStage().hit(screenX, stageY, true) != null) {
            gameUI.hideTooltip();
            return;
        }

        Tile tile = getTileAtScreen(screenX, screenY);
        if (tile != null) {
            String tooltipText = buildTooltipText(tile);
            gameUI.updateTooltip(tooltipText, screenX, stageY);
        } else {
            gameUI.hideTooltip();
        }
    }

    /**
     * Convert screen coordinates to tile.
     * Uses camera.unproject with the actual window dimensions since
     * the FitViewport's virtual size doesn't affect the camera's projection matrix.
     */
    private Tile getTileAtScreen(int screenX, int screenY) {
        unprojected.set(screenX, screenY, 0);

        // Use full window dimensions - the camera's projection matrix
        // already accounts for the viewport's world size and zoom
        gameCamera.getCamera().unproject(unprojected,
            0, 0,
            Gdx.graphics.getWidth(),
            Gdx.graphics.getHeight());

        int tileX = (int) Math.floor(unprojected.x / WorldMap.TILE_SIZE);
        int tileY = (int) Math.floor(unprojected.y / WorldMap.TILE_SIZE);

        if (tileX >= 0 && tileX < worldMap.getWidth()
            && tileY >= 0 && tileY < worldMap.getHeight()) {
            return worldMap.getTile(tileX, tileY);
        }

        return null;
    }

    private String buildTooltipText(Tile tile) {
        StringBuilder sb = new StringBuilder();
        sb.append("Biome: ").append(formatBiomeName(tile.getBiome()));

        if (tile.hasResourceNode()) {
            ResourceNode<?> node = tile.getResourceNode();
            sb.append("\nResource: ").append(node.getType().toString());
            sb.append("\nYield: ").append(node.getCurrentYield())
              .append("/").append(node.getMaxYield());
            sb.append("\nRegrowth: ").append(String.format("%.2f", node.getRegrowthRate()));

            if (tile.isHerbNode()) {
                HerbNode herbNode = tile.getHerbNode();
                sb.append("\nPotency: ").append(String.format("%.1f", herbNode.getPotency()));
            }
        }

        return sb.toString();
    }

    private String formatBiomeName(BiomeType biome) {
        String name = biome.name().replace('_', ' ');
        StringBuilder result = new StringBuilder();
        boolean capitalizeNext = true;
        for (char c : name.toCharArray()) {
            if (c == ' ') {
                result.append(c);
                capitalizeNext = true;
            } else if (capitalizeNext) {
                result.append(Character.toUpperCase(c));
                capitalizeNext = false;
            } else {
                result.append(Character.toLowerCase(c));
            }
        }
        return result.toString();
    }
}
