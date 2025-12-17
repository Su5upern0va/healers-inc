package com.nova.healersinc.world.resource;

import java.util.List;

public class ResourceConfig {
    public List<ResourceEntry> resources;

    public static class ResourceEntry {
        public String id;
        public String name;
        public String category;
        public Visual visual;
        public Properties properties;
    }

    public static class Properties {
        public int stackSize;
        public int baseValue;
    }

    public static class Visual {
        public String color;
        public String texture;
        public String icon;
    }
}
