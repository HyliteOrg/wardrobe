package dev.hardaway.wardrobe.api.cosmetic;

import dev.hardaway.wardrobe.api.WardrobeContext;
import dev.hardaway.wardrobe.cosmetic.system.PlayerCosmetic;

import javax.annotation.Nullable;

public interface Cosmetic {

    void applyCosmetic(WardrobeContext context, PlayerCosmetic playerCosmetic);
}
