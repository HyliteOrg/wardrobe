package dev.hardaway.wardrobe.impl.cosmetic.appearance;

import com.hypixel.hytale.codec.Codec;
import com.hypixel.hytale.codec.KeyedCodec;
import com.hypixel.hytale.codec.builder.BuilderCodec;
import com.hypixel.hytale.codec.schema.metadata.ui.UIDefaultCollapsedState;
import com.hypixel.hytale.codec.schema.metadata.ui.UIPropertyTitle;
import com.hypixel.hytale.codec.validation.Validators;
import com.hypixel.hytale.server.core.asset.common.CommonAssetValidator;
import dev.hardaway.wardrobe.api.cosmetic.appearance.TextureConfig;
import dev.hardaway.wardrobe.api.property.WardrobeProperties;
import dev.hardaway.wardrobe.api.property.validator.WardrobeValidators;

import javax.annotation.Nullable;

public class VariantAppearanceEntry {
    public static final BuilderCodec<VariantAppearanceEntry> CODEC = BuilderCodec.builder(VariantAppearanceEntry.class, VariantAppearanceEntry::new)
            .append(new KeyedCodec<>("Properties", WardrobeProperties.CODEC, true),
                    (t, value) -> t.properties = value,
                    t -> t.properties
            )
            .addValidator(Validators.nonNull())
            .metadata(UIDefaultCollapsedState.UNCOLLAPSED)
            .add()

            .append(new KeyedCodec<>("Icon", Codec.STRING),
                    (t, value) -> t.icon = value,
                    t -> t.icon
            )
            .addValidator(WardrobeValidators.ICON)
            .metadata(new UIPropertyTitle("Icon")).documentation("A preview icon of the Cosmetic with this Variant applied to display in the Wardrobe Menu.")
            .add()

            .append(new KeyedCodec<>("Model", Codec.STRING, true),
                    (t, value) -> t.model = value, t -> t.model
            )
            .addValidator(CommonAssetValidator.MODEL_CHARACTER_ATTACHMENT)
            .addValidator(Validators.nonNull())
            .metadata(new UIPropertyTitle("Model")).documentation("The model to display for this appearance.")
            .add()

            .append(new KeyedCodec<>("TextureConfig", TextureConfig.CODEC, true),
                    (t, value) -> t.textureConfig = value, t -> t.textureConfig
            )
            .addValidator(Validators.nonNull())
            .metadata(new UIPropertyTitle("Texture Configuration")).documentation("The Texture Configuration for this appearance.")
            .metadata(UIDefaultCollapsedState.UNCOLLAPSED)
            .add()
            .afterDecode(asset -> {
                if (asset.getIcon() == null && asset.properties != null && asset.properties.getIcon() != null) { // DEPRECATED
                    asset.icon = asset.properties.getIcon();
                }
            })
            .build();

    protected WardrobeProperties properties;
    protected String icon;
    protected String model;
    protected float scale = 1;
    protected TextureConfig textureConfig;

    public WardrobeProperties getProperties() {
        return properties;
    }

    @Nullable
    public String getIcon() {
        return icon;
    }

    public String getModel() {
        return model;
    }

    public float getScale() {
        return scale;
    }

    public TextureConfig getTextureConfig() {
        return textureConfig;
    }
}
