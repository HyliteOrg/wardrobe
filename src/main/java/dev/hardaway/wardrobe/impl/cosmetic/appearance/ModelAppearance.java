package dev.hardaway.wardrobe.impl.cosmetic.appearance;

import com.hypixel.hytale.codec.Codec;
import com.hypixel.hytale.codec.KeyedCodec;
import com.hypixel.hytale.codec.builder.BuilderCodec;
import com.hypixel.hytale.codec.schema.metadata.ui.UIDefaultCollapsedState;
import com.hypixel.hytale.codec.schema.metadata.ui.UIPropertyTitle;
import com.hypixel.hytale.codec.validation.Validators;
import dev.hardaway.wardrobe.api.cosmetic.appearance.Appearance;
import dev.hardaway.wardrobe.api.cosmetic.appearance.TextureConfig;
import dev.hardaway.wardrobe.api.property.validator.WardrobeValidators;

import javax.annotation.Nullable;

public class ModelAppearance implements Appearance {

    public static final BuilderCodec<ModelAppearance> CODEC = BuilderCodec.builder(ModelAppearance.class, ModelAppearance::new)
            .append(new KeyedCodec<>("Model", Codec.STRING, true),
                    (a, value) -> a.model = value,
                    a -> a.model
            )
            .addValidator(Validators.nonNull())
            .metadata(new UIPropertyTitle("Model")).documentation("The model to display for this appearance.")
            .add()

            .append(new KeyedCodec<>("Scale", Codec.DOUBLE),
                    (a, d) -> a.scale = d.floatValue(),
                    (a) -> (double) a.scale
            )
            .metadata(new UIPropertyTitle("Player Model Scale")).documentation("The scale to use for the Player Model. Only applies to PlayerModel Cosmetics.")
            .add()

            .append(new KeyedCodec<>("TextureConfig", TextureConfig.CODEC),
                    (a, value) -> a.textureConfig = value,
                    a -> a.textureConfig
            )
            .addValidator(Validators.nonNull())
            .metadata(new UIPropertyTitle("Appearance")).documentation("The Texture Configuration for this appearance.")
            .metadata(UIDefaultCollapsedState.UNCOLLAPSED)
            .add()

            .build();

    private String model;
    private float scale = 1;
    private TextureConfig textureConfig;

    @Override
    public String getModel(String variantId) {
        return model;
    }

    @Override
    public float getScale(String variantId) {
        return scale;
    }

    @Override
    public TextureConfig getTextureConfig(@Nullable String variantId) {
        return textureConfig;
    }

    @Override
    public String[] collectVariants() {
        return new String[0];
    }
}
