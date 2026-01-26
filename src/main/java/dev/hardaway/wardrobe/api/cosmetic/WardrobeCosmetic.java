package dev.hardaway.wardrobe.api.cosmetic;

import dev.hardaway.wardrobe.api.WardrobeTranslatable;
import dev.hardaway.wardrobe.api.cosmetic.variant.CosmeticColorEntry;
import dev.hardaway.wardrobe.api.cosmetic.variant.CosmeticVariantEntry;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Map;

public interface WardrobeCosmetic extends Cosmetic, WardrobeTranslatable {

    WardrobeVisibility getWardrobeVisibility();

    String[] getRequiredCosmeticIds();

    @Nullable
    String getIconPath();

    Map<String, CosmeticVariantEntry> getVariantEntries();

    List<CosmeticColorEntry> getColorEntries(@Nullable String variantId);
}
