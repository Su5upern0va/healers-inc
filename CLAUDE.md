# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

Healers Inc. is an idle factory/resource management game built with LibGDX and Java. Players discover and harvest wild herbs, process them into products, and fulfill patient orders from a clinic.

## Build Commands

```bash
# Run the game (desktop)
./gradlew lwjgl3:run          # Linux/macOS
gradlew.bat lwjgl3:run        # Windows

# Build distributable JAR (cross-platform)
./gradlew lwjgl3:jar

# Platform-specific JARs
./gradlew lwjgl3:jarWin       # Windows only
./gradlew lwjgl3:jarMac       # macOS only
./gradlew lwjgl3:jarLinux     # Linux only

# Build native executables (requires network to download JDKs)
./gradlew lwjgl3:construoLinuxX64
./gradlew lwjgl3:construoWinX64
./gradlew lwjgl3:construoMacM1
./gradlew lwjgl3:construoMacX64
```

## Architecture

### Module Structure
- `core/` - Platform-independent game logic (main codebase)
- `lwjgl3/` - Desktop launcher using LWJGL3 backend
- `assets/` - Game assets (textures, sounds, etc.)

### Core Package Structure (`com.nova.healersinc`)
- `HealersIncGame` - Main game class extending LibGDX `ApplicationAdapter`
- `world/` - World generation and map systems

### World System
- `WorldMap` - Grid-based map holding `Tile` objects (32px tile size)
- `WorldGenerator` - Procedural generation with seed-based randomness
- `Tile` - Grid cell with biome type and optional resource node
- `BiomeType` - Enum: `SUNNY_MEADOW`, `SHADY_GROVE`

### Resource System
Generic abstraction for harvestable world resources:
- `Resource` - Interface for resource types (implemented by enums)
- `ResourceNode<T>` - Abstract base class with yield, regrowth, and harvest mechanics
- `HerbType` - Enum implementing `Resource`: `CHAMOMILE`, `MINT`, `ECHINACEA`
- `HerbNode` - Extends `ResourceNode<HerbType>`, adds herb-specific `potency`

To add new resource types: create a `FooType enum implements Resource` and `FooNode extends ResourceNode<FooType>`

### Biome-Herb Distribution
- SUNNY_MEADOW: 10% Chamomile, 5% Mint
- SHADY_GROVE: 12% Mint, 7% Echinacea

## Key Configuration
- Java source compatibility: 8
- LibGDX version: 1.14.0 (defined in `gradle.properties`)
- Main class: `com.nova.healersinc.lwjgl3.Lwjgl3Launcher`
- Window default: 640x480, vsync enabled, ANGLE OpenGL emulation

## Development Notes
- Assets are in the project root `assets/` folder and automatically included via `generateAssetList` task
- The `assets.txt` file is auto-generated during build
- Gradle daemon is disabled by default to save memory