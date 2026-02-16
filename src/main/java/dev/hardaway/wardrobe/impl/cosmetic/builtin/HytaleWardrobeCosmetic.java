package dev.hardaway.wardrobe.impl.cosmetic.builtin;

import com.hypixel.hytale.server.core.cosmetics.CosmeticType;
import com.hypixel.hytale.server.core.cosmetics.CosmeticsModule;
import com.hypixel.hytale.server.core.cosmetics.PlayerSkinPart;
import com.hypixel.hytale.server.core.cosmetics.PlayerSkinPartTexture;
import dev.hardaway.wardrobe.api.cosmetic.WardrobeCosmetic;
import dev.hardaway.wardrobe.api.cosmetic.WardrobeContext;
import dev.hardaway.wardrobe.api.cosmetic.WardrobeCosmeticSlot;
import dev.hardaway.wardrobe.api.menu.variant.CosmeticOptionEntry;
import dev.hardaway.wardrobe.api.menu.variant.CosmeticVariantEntry;
import dev.hardaway.wardrobe.api.player.PlayerCosmetic;
import dev.hardaway.wardrobe.api.property.WardrobeProperties;
import dev.hardaway.wardrobe.api.property.WardrobeTranslationProperties;
import dev.hardaway.wardrobe.api.property.WardrobeVisibility;

import javax.annotation.Nullable;
import java.util.*;

public class HytaleWardrobeCosmetic implements WardrobeCosmetic {

    private final HytaleCosmetic delegate;
    private final String slotId;
    private final WardrobeProperties properties;
    private final Map<String, CosmeticOptionEntry> optionEntries;

    public HytaleWardrobeCosmetic(CosmeticType type, PlayerSkinPart part, String slotId) {
        this.delegate = switch (type) {
            case HAIRCUTS -> new HytaleHaircutCosmetic(type, part);
            case BODY_CHARACTERISTICS -> new HytaleBodyCharacteristicCosmetic(type, part);
            default -> new HytaleCosmetic(type, part);
        };
        this.slotId = slotId;

        this.properties = new WardrobeProperties(
                new WardrobeTranslationProperties("cosmetics." + type.name() + "." + part.getId(), null),
                WardrobeVisibility.ALWAYS,
                null
        );

        // Build option entries from variants
        if (part.getVariants() != null && !part.getVariants().isEmpty()) {
            Map<String, CosmeticOptionEntry> entries = new LinkedHashMap<>();
            for (String variantKey : part.getVariants().keySet()) {
                WardrobeProperties optionProps = new WardrobeProperties(
                        new WardrobeTranslationProperties("cosmetics." + type.name() + "." + part.getId() + "." + variantKey, null),
                        WardrobeVisibility.ALWAYS,
                        null
                );
                entries.put(variantKey, new CosmeticOptionEntry(variantKey, optionProps));
            }
            this.optionEntries = Collections.unmodifiableMap(entries);
        } else {
            this.optionEntries = Map.of();
        }
    }

    public HytaleCosmetic getDelegate() {
        return delegate;
    }

    @Override
    public String getId() {
        return delegate.getId();
    }

    @Override
    public WardrobeProperties getProperties() {
        return properties;
    }

    @Override
    public String getIconPath() {
        return null;
    }

    @Override
    public String getCosmeticSlotId() {
        return slotId;
    }

    @Override
    public Map<String, CosmeticOptionEntry> getOptionEntries() {
        return optionEntries;
    }

    @Override
    public List<CosmeticVariantEntry> getVariantEntries(@Nullable String optionId) {
        PlayerSkinPart part = delegate.getPart();
        List<CosmeticVariantEntry> entries = new ArrayList<>();

        if (!delegate.variantMap.isEmpty()) {
            // Has variants (options) — get textures for the selected option
            PlayerSkinPart.Variant variant = delegate.variantMap.get(optionId);
            if (variant == null) return List.of();

            if (variant.getTextures() != null) {
                for (Map.Entry<String, PlayerSkinPartTexture> entry : variant.getTextures().entrySet()) {
                    String textureId = entry.getKey();
                    WardrobeProperties variantProps = new WardrobeProperties(
                            new WardrobeTranslationProperties(textureId, null),
                            WardrobeVisibility.ALWAYS,
                            null
                    );
                    entries.add(new CosmeticVariantEntry(textureId, variantProps, new String[0]));
                }
            } else if (variant.getGreyscaleTexture() != null && part.getGradientSet() != null) {
                addGradientEntries(entries, part.getGradientSet());
            }
        } else {
            // No variants — textures are directly on the part
            if (part.getTextures() != null) {
                for (Map.Entry<String, PlayerSkinPartTexture> entry : part.getTextures().entrySet()) {
                    String textureId = entry.getKey();
                    WardrobeProperties variantProps = new WardrobeProperties(
                            new WardrobeTranslationProperties(textureId, null),
                            WardrobeVisibility.ALWAYS,
                            null
                    );
                    entries.add(new CosmeticVariantEntry(textureId, variantProps, new String[0]));
                }
            } else if (part.getGreyscaleTexture() != null && part.getGradientSet() != null) {
                addGradientEntries(entries, part.getGradientSet());
            }
        }

        return entries;
    }

    private void addGradientEntries(List<CosmeticVariantEntry> entries, String gradientSetId) {
        var gradientSets = CosmeticsModule.get().getRegistry().getGradientSets();
        var gradientSet = gradientSets.get(gradientSetId);
        if (gradientSet == null) return;

        for (var entry : gradientSet.getGradients().entrySet()) {
            String gradientId = entry.getKey();
            String[] colors = entry.getValue().getBaseColor();
            WardrobeProperties variantProps = new WardrobeProperties(
                    new WardrobeTranslationProperties(gradientId, null),
                    WardrobeVisibility.ALWAYS,
                    null
            );
            entries.add(new CosmeticVariantEntry(gradientId, variantProps, colors));
        }
    }

    @Override
    public void applyCosmetic(WardrobeContext context, WardrobeCosmeticSlot slot, PlayerCosmetic playerCosmetic) {
        delegate.applyCosmetic(context, slot, playerCosmetic);
    }
}
