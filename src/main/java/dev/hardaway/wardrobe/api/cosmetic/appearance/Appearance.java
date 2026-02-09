package dev.hardaway.wardrobe.api.cosmetic.appearance;

import com.hypixel.hytale.codec.lookup.BuilderCodecMapCodec;

import javax.annotation.Nullable;

public interface Appearance {

    BuilderCodecMapCodec<Appearance> CODEC = new BuilderCodecMapCodec<>("Type", true);

    String getModel(@Nullable String variantId);

    TextureConfig getTextureConfig(@Nullable String variantId);

    String[] collectVariants();

    float getScale(@Nullable String variantId);
}
