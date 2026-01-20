package dev.hardaway.wardrobe.impl.cosmetic.asset;

import com.hypixel.hytale.codec.Codec;
import com.hypixel.hytale.codec.KeyedCodec;
import com.hypixel.hytale.codec.builder.BuilderCodec;
import com.hypixel.hytale.server.core.asset.type.model.config.Model;
import com.hypixel.hytale.server.core.asset.type.model.config.ModelAsset;
import dev.hardaway.wardrobe.api.WardrobeContext;
import dev.hardaway.wardrobe.api.cosmetic.PlayerCosmetic;
import dev.hardaway.wardrobe.api.cosmetic.asset.CosmeticAsset;
import dev.hardaway.wardrobe.api.cosmetic.asset.config.TextureConfig;

import javax.annotation.Nonnull;

public class PlayerModelCosmetic extends CosmeticAsset {

    public static final BuilderCodec<PlayerModelCosmetic> CODEC = BuilderCodec.builder(PlayerModelCosmetic.class, PlayerModelCosmetic::new, CosmeticAsset.ABSTRACT_CODEC)
            .append(new KeyedCodec<>("ModelAsset", Codec.STRING, true),
                    (t, value) -> t.modelAsset = value,
                    t -> t.modelAsset
            ).add()
            .append(new KeyedCodec<>("TextureConfig", TextureConfig.CODEC, true),
                    (t, value) -> t.textureConfig = value,
                    t -> t.textureConfig
            ).add()
            .build();

    private String modelAsset;
    private TextureConfig textureConfig;

    private PlayerModelCosmetic() {
    }

    public PlayerModelCosmetic(String id, String nameKey, String group, String icon, String modelAsset, TextureConfig textureConfig) {
        super(id, nameKey, group, icon);
        this.modelAsset = modelAsset;
        this.textureConfig = textureConfig;
    }

    @Nonnull
    public String getModelAsset() {
        return modelAsset;
    }

    @Nonnull
    public TextureConfig getTextureConfig() {
        return textureConfig;
    }

    @Override
    public void applyCosmetic(WardrobeContext context, PlayerCosmetic playerCosmetic) {
        ModelAsset model = ModelAsset.getAssetMap().getAsset(this.modelAsset);
        context.setPlayerModel(Model.createUnitScaleModel(model));
        context.setPlayerTexture(this.textureConfig);
        context.setPlayerTextureVariantId(playerCosmetic.getVariantId());
        // TODO: warn if the model asset has attachments, they will be removed!
    }

}
