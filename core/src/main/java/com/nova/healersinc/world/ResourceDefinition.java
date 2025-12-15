package com.nova.healersinc.world;

import com.badlogic.gdx.graphics.Color;

import java.util.Properties;

public class ResourceDefinition {

    private final String id;
    private final String displayName;
    private final String category;
    private final Visual visual;
    private final Properties properties;

    public ResourceDefinition(String id, String displayName, String category, Visual visual, Properties properties) {
        this.id = id;
        this.displayName = displayName;
        this.category = category;
        this.visual = visual;
        this.properties = properties;
    }

    public String getId() {
        return id;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getCategory() {
        return category;
    }

    public Visual getVisual() {
        return visual;
    }

    public Properties getProperties() {
        return properties;
    }

    public static class Visual {
        private final Color color;
        private final String texturePath;
        private final String iconPath;

        public Visual(Color color, String texturePath, String iconPath) {

            this.color = color;
            this.texturePath = texturePath;
            this.iconPath = iconPath;
        }

        public Color getColor() {
            return color;
        }

        public String getTexturePath() {
            return texturePath;
        }

        public String getIconPath() {
            return iconPath;
        }
    }

    public static class  Properties {
        private final int stackSize;
        private final int baseValue;

        public Properties(int stackSize, int baseValue) {
            this.stackSize = stackSize;
            this.baseValue = baseValue;
        }

        public int getStackSize() {
            return stackSize;
        }

        public int getBaseValue() {
            return baseValue;
        }
    }
}
