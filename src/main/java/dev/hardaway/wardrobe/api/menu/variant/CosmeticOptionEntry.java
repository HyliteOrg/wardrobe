package dev.hardaway.wardrobe.api.menu.variant;

import dev.hardaway.wardrobe.api.property.WardrobeTranslationProperties;

import javax.annotation.Nullable;

public record CosmeticOptionEntry(
        String id,
        WardrobeTranslationProperties translationProperties,
        @Nullable String iconPath
) {
}
