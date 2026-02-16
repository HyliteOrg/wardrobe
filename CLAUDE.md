# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

Wardrobe is a Hytale server plugin (Java) that provides a customizable cosmetics system. Creators define cosmetics as JSON assets in Asset Packs; players equip them via an in-game Wardrobe Menu or `/wardrobe` command. No tests exist in this project.

## Build Commands

```bash
./gradlew build          # Build and assemble
./gradlew jar            # Assemble JAR only
./gradlew clean build    # Clean rebuild
```

Requires Java 25 and a local Hytale installation (auto-detected by platform). Override with `-PhytaleHome=/path/to/hytale`.

Run configuration for IntelliJ IDEA is auto-generated (`HytaleServer`) via `./gradlew idea`.

## Architecture

### Two-Layer Design

- **API** (`dev.hardaway.wardrobe.api`) — Interfaces and contracts: `Cosmetic`, `WardrobeCosmetic`, `AppearanceCosmetic`, `PlayerWardrobe`, `Appearance`, `TextureConfig`, `WardrobeTab`, `WardrobeCategory`
- **Implementation** (`dev.hardaway.wardrobe.impl`) — Concrete types, ECS components, menu UI, commands

### Key Subsystems

- **Cosmetics** (`impl/cosmetic/`) — `CosmeticAsset` base class with subtypes `ModelAttachmentCosmetic` and `PlayerModelCosmetic`. Categories (`CosmeticCategoryAsset`) and slots (`CosmeticSlotAsset`) are also asset-driven.
- **Appearances** (`impl/cosmetic/appearance/`) — `ModelAppearance` (single model) and `VariantAppearance` (option-based model switching). Supports armor-aware and cosmetic-aware conditional appearances.
- **Textures** (`impl/cosmetic/texture/`) — `StaticTextureConfig`, `GradientTextureConfig` (gradient set colors), `VariantTextureConfig` (multiple texture choices).
- **Player Component** (`impl/player/`) — `PlayerWardrobeComponent` is an ECS component on the entity store. `PlayerWardrobeSystems` contains tick, entity-added, wardrobe-changed, and armor-visibility-changed systems. `CosmeticSaveData` handles persistence.
- **Menu** (`impl/menu/`) — `WardrobePage` is the main UI (registered as a custom page via `OpenCustomUIInteraction`). `WardrobeMenu`/`WardrobeDismissPage` handle navigation.
- **Commands** (`impl/command/`) — `/wardrobe` with subcommands: wear, remove, clear, reset.
- **Built-in cosmetics** (`impl/cosmetic/builtin/`) — Wrappers around Hytale's native cosmetics (`HytaleCosmetic`, `HytaleHaircutCosmetic`, `HytaleBodyCharacteristicCosmetic`).

### Plugin Entry Point

`WardrobePlugin` extends `JavaPlugin`. In `setup()` it:
1. Registers codec types for `TextureConfig`, `CosmeticAsset`, and `Appearance` via a priority-based codec registry
2. Registers asset stores for categories, slots, and cosmetics (with load-order dependencies)
3. Registers the `PlayerWardrobeComponent` and ECS systems
4. Registers event listeners and commands

### Asset Resources

JSON asset definitions live under `src/main/resources/Server/Wardrobe/` (Categories, Slots, Cosmetics). Items in `Server/Item/`, translations in `Server/Languages/`, common assets in `Common/`.

### Key Patterns

- **Codec-based serialization**: All assets use Hytale's `BuilderCodec` system for JSON deserialization with typed polymorphism (e.g., `TextureConfig` dispatches to Static/Gradient/Variant by `Type` field).
- **Asset store with load ordering**: Asset stores declare `loadsAfter` dependencies (e.g., cosmetics load after slots and model assets).
- **Property validators**: API validators (`api/property/validator/`) provide asset editor metadata for live editing support.
- **Singleton plugin access**: `WardrobePlugin.get()` static accessor.

## Code Style

- `.editorconfig`: UTF-8, LF line endings, 2-space indent (applies to `.ui` files)
- Java source follows standard Java conventions
