package dev.hardaway.wardrobe.api.cosmetic;

import dev.hardaway.wardrobe.api.cosmetic.apperance.CosmeticAppearance;

public interface AppearanceCosmetic extends WardrobeCosmetic {

    CosmeticAppearance getAppearance();
}
