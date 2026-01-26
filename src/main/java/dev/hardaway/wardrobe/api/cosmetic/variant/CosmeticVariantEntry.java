package dev.hardaway.wardrobe.api.cosmetic.variant;

import dev.hardaway.wardrobe.api.WardrobeTranslationProperties;

import javax.annotation.Nullable;

public record CosmeticVariantEntry(
        String id,
        WardrobeTranslationProperties translationProperties,
        @Nullable String iconPath
) {}
