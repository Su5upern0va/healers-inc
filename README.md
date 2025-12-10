# 🏥 Healers Inc. (Working Title)

> A cozy idle farm-factory game about herbal medicine, built with Java + LibGDX.  
> **Status:** Early WIP – engine/framework setup only, core gameplay not implemented yet.

---

## 🌿 Overview

**Healers Inc.** (working title) is an idle factory / resource management / medical simulation game.

You will:

- Discover and harvest **wild herbs**
- Cultivate them in **greenhouses**
- Process them into **extracts and products**
- Fulfill **patient orders** from your clinic
- Research new **herbs, recipes, and buildings**
- Slowly grow your quirky, pixel-art herbal clinic empire

Right now, this repository mainly contains the **project skeleton and engine setup** (LibGDX + Java).  
Gameplay, UI, and content are still in the planning phase.

---

## 🎮 Planned Core Gameplay

High-level gameplay loop:

1. **Discover** wild herb nodes on a map
2. **Harvest** them with automated harvesters
3. **Cultivate** herbs in greenhouses for higher yield and potency
4. **Process** herbs through buildings:
   - Drying Rack → Dried Herbs  
   - Extractor → Herb Extracts  
   - Blender/Mixer → Tea Mixes / Powder Blends  
   - Formulation Station → Finished Products (teas, syrups, pills…)
5. **Transport** everything with conveyor belts, splitters, and storage
6. **Sell** finished products via a clinic dispatch to fulfill patient orders
7. **Research** new herbs, recipes, and buildings to expand your operation

Planned resources and products (first iteration):

- **Herbs:** Chamomile, Mint, Echinacea  
- **Base Ingredients:** Water, Nutrients, Syrup Base  
- **Example Products:** Calming Tea, Minty Drops, Immune Boost Pills  

---

## 🛠️ Tech Stack

- **Language:** Java  
- **Framework:** [LibGDX](https://libgdx.com/)  
- **Target Platform:** Desktop (and possibly others later)  
- **Graphics:** Minimalistic, modern pixel art

---

## 📦 Project Status

Right now this project is at a **very early stage**:

- [x] Java & LibGDX project scaffolded
- [x] Basic project structure / engine setup
- [ ] Map generation
- [ ] Herb nodes & harvesting
- [ ] Production buildings and logistics
- [ ] Orders, money, and reputation systems
- [ ] Save/load & idle mechanics
- [ ] UI, art, SFX, and polish

If you’re looking at this and there’s not much code yet: that’s expected.  
This repo currently serves as a **starting point** plus a place to track the game design and roadmap.

---

## 🚀 Running the Project

> Note: At this stage, you will likely just see a basic LibGDX window or placeholder scene.

### Prerequisites

- Java 11+ installed
- Git installed

### Build & Run

```bash
# Clone the repository
git clone https://github.com/YOUR_USERNAME/healers-inc.git
cd healers-inc

# Using Gradle wrapper (recommended)
./gradlew desktop:run      # Linux / macOS
gradlew.bat desktop:run    # Windows
```

If you haven’t set up the Gradle modules yet (e.g. `desktop`), adjust the command to match your current LibGDX setup.

---

## 🧠 High-Level Architecture (Planned)

Core planned types and systems (not all implemented yet):

- `Tile` – map grid cell
- `HerbNode` – represents a wild herb patch
- `Building` (abstract) – base for:
    - Harvester
    - Processor (Drying Rack, Extractor, Blender, Formulation Station)
    - Conveyor / Splitter
    - Storage
    - Clinic / Research buildings
- `Item` (abstract) – base for:
    - `FreshHerb`, `DriedHerb`, `HerbExtract`
    - `FinishedProduct`
    - `BaseIngredient`
- `Recipe` – input items, output item, processing time
- `Order` – requested product, quantity, reward, deadline
- `Player` – money, research points, reputation

---

## 🗺️ Roadmap (Subject to Change)

**Phase 1 – MVP (Core Loop)**
- Simple map
- Herb nodes + wild harvester
- Drying rack + extractor
- Tier 1 conveyors + splitters + storage
- Basic clinic dispatch with simple orders
- Placeholder art and UI

**Phase 2 – Expansion**
- Greenhouse and resource needs (water, nutrients)
- Blender & formulation station
- More herbs, products, and recipes
- Research lab and small tech tree
- Reputation system and more complex orders

**Phase 3 – Polish**
- Improved art and animations
- Sound effects and music
- Tutorial / onboarding
- Balancing and UX improvements

**Phase 4 – Advanced / Maybe**
- Prestige system (“new clinic location”)
- Weather / environment effects
- Special events (herb bloom, shortages, etc.)

---

## 🤝 Contributing / Following Progress

Right now this is primarily a **learning and hobby project**.

If you’re interested in:

- Java game dev
- LibGDX
- Idle / factory game design

feel free to:

- Open issues with suggestions
- Comment on design ideas
- Submit PRs once the codebase is more established

---

## 📄 License

_(Decide on a license and update this section, e.g. MIT, GPL, etc.)_

---

## 📬 Contact

- Author: **Nova**
- Project: [https://github.com/YOUR_USERNAME/healers-inc](https://github.com/YOUR_USERNAME/healers-inc)

> Thanks for checking out **Healers Inc.** – right now it’s mostly ideas and engine setup, but the clinic will open soon™.
