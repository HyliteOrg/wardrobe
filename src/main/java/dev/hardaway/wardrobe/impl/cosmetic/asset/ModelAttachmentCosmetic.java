package dev.hardaway.wardrobe.impl.cosmetic.asset;

import com.hypixel.hytale.codec.Codec;
import com.hypixel.hytale.codec.KeyedCodec;
import com.hypixel.hytale.codec.builder.BuilderCodec;
import com.hypixel.hytale.server.core.asset.type.model.config.ModelAttachment;
import dev.hardaway.wardrobe.api.WardrobeContext;
import dev.hardaway.wardrobe.api.cosmetic.PlayerCosmetic;
import dev.hardaway.wardrobe.api.cosmetic.asset.CosmeticAsset;
import dev.hardaway.wardrobe.api.cosmetic.asset.config.TextureConfig;

import javax.annotation.Nonnull;

public class ModelAttachmentCosmetic extends CosmeticAsset {

    public static final BuilderCodec<ModelAttachmentCosmetic> CODEC = BuilderCodec.builder(ModelAttachmentCosmetic.class, ModelAttachmentCosmetic::new, CosmeticAsset.ABSTRACT_CODEC)
            .append(new KeyedCodec<>("Model", Codec.STRING, true),
                    (t, value) -> t.model = value,
                    t -> t.model
            ).add()
            .append(new KeyedCodec<>("TextureConfig", TextureConfig.CODEC, true),
                    (t, value) -> t.textureConfig = value,
                    t -> t.textureConfig
            ).add()
            .build();

    private String model;
    private TextureConfig textureConfig;

    private ModelAttachmentCosmetic() {
    }

    public ModelAttachmentCosmetic(String id, String nameKey, String group, String icon, String model, TextureConfig textureConfig) {
        super(id, nameKey, group, icon);
        this.model = model;
        this.textureConfig = textureConfig;
    }

    @Nonnull
    public String getModel() {
        return model;
    }

    @Nonnull
    public TextureConfig getTextureConfig() {
        return textureConfig;
    }

    @Override
    public void applyCosmetic(WardrobeContext context, PlayerCosmetic playerCosmetic) {
        TextureConfig textureConfig = this.getTextureConfig();
        context.addAttachment(new ModelAttachment(
                this.getModel(),
                textureConfig.getTexture(playerCosmetic.getVariantId()),
                textureConfig.getGradientSet(),
                textureConfig.getGradientSet() != null ? playerCosmetic.getVariantId() : null,
                1.0
        ));
    }

}
