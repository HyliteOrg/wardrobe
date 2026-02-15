package dev.hardaway.wardrobe.impl.cosmetic.appearance;

import com.hypixel.hytale.codec.KeyedCodec;
import com.hypixel.hytale.codec.builder.BuilderCodec;
import com.hypixel.hytale.codec.codecs.map.MapCodec;
import com.hypixel.hytale.codec.schema.metadata.ui.UIDefaultCollapsedState;
import com.hypixel.hytale.codec.schema.metadata.ui.UIPropertyTitle;
import dev.hardaway.wardrobe.api.cosmetic.appearance.Appearance;
import dev.hardaway.wardrobe.api.cosmetic.appearance.TextureConfig;

import java.util.LinkedHashMap;
import java.util.Map;

public class VariantAppearance implements Appearance {

    public static final BuilderCodec<VariantAppearance> CODEC = BuilderCodec.builder(VariantAppearance.class, VariantAppearance::new)
            .append(new KeyedCodec<>("Variants", new MapCodec<>(VariantAppearanceEntry.CODEC, LinkedHashMap::new), true),
                    (a, value) -> a.variants = value, a -> a.variants
            )
            .metadata(new UIPropertyTitle("Variants")).documentation("The available variants of this appearance.")
            .metadata(UIDefaultCollapsedState.UNCOLLAPSED)
            .add().build();

    private Map<String, VariantAppearanceEntry> variants;

    public Map<String, ? extends VariantAppearanceEntry> getVariants() {
        return variants;
    }

    @Override
    public String getModel(String variantId) {
        VariantAppearanceEntry entry = getVariants().get(variantId);
        return entry == null ? null : entry.getModel();
    }

    @Override
    public float getScale(String variantId) {
        VariantAppearanceEntry entry = getVariants().get(variantId);
        return entry == null ? 1.0F : entry.getScale();
    }

    @Override
    public TextureConfig getTextureConfig(String variantId) {
        VariantAppearanceEntry entry = getVariants().get(variantId);
        return entry == null ? null : entry.getTextureConfig();
    }

    @Override
    public String[] collectVariants() {
        return this.getVariants() == null ? new String[0] : this.getVariants().keySet().toArray(String[]::new);
    }
}
