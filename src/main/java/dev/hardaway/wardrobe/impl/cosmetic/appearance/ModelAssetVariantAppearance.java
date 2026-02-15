package dev.hardaway.wardrobe.impl.cosmetic.appearance;

import com.hypixel.hytale.codec.KeyedCodec;
import com.hypixel.hytale.codec.builder.BuilderCodec;
import com.hypixel.hytale.codec.codecs.map.MapCodec;
import com.hypixel.hytale.codec.schema.metadata.ui.UIDefaultCollapsedState;
import com.hypixel.hytale.codec.schema.metadata.ui.UIPropertyTitle;

import java.util.LinkedHashMap;
import java.util.Map;

public class ModelAssetVariantAppearance extends VariantAppearance {
    public static final BuilderCodec<ModelAssetVariantAppearance> CODEC = BuilderCodec.builder(ModelAssetVariantAppearance.class, ModelAssetVariantAppearance::new)
            .append(new KeyedCodec<>("Variants", new MapCodec<>(ModelAssetVariantAppearanceEntry.CODEC, LinkedHashMap::new), true),
                    (a, value) -> a.variants = value, a -> a.variants
            )
            .metadata(new UIPropertyTitle("Variants")).documentation("The available variants of this appearance.")
            .metadata(UIDefaultCollapsedState.UNCOLLAPSED)
            .add().build();

    private Map<String, ModelAssetVariantAppearanceEntry> variants;

    public Map<String, ? extends VariantAppearanceEntry> getVariants() {
        return variants;
    }
}
