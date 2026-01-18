package dev.hardaway.wardrobe.asset;

import com.hypixel.hytale.assetstore.AssetExtraInfo;
import com.hypixel.hytale.assetstore.AssetStore;
import com.hypixel.hytale.assetstore.codec.AssetCodecMapCodec;
import com.hypixel.hytale.assetstore.map.DefaultAssetMap;
import com.hypixel.hytale.assetstore.map.JsonAssetWithMap;
import com.hypixel.hytale.codec.Codec;
import com.hypixel.hytale.codec.KeyedCodec;
import com.hypixel.hytale.codec.builder.BuilderCodec;
import com.hypixel.hytale.codec.validation.Validators;
import com.hypixel.hytale.server.core.asset.common.CommonAssetValidator;
import com.hypixel.hytale.server.core.cosmetics.CosmeticType;
import dev.hardaway.wardrobe.WardrobeUtil;
import dev.hardaway.wardrobe.asset.config.TextureConfig;

import javax.annotation.Nonnull;
import java.util.function.Supplier;

public class CosmeticAsset implements JsonAssetWithMap<String, DefaultAssetMap<String, CosmeticAsset>> {

    public static final BuilderCodec<CosmeticAsset> ABSTRACT_CODEC = BuilderCodec.abstractBuilder(CosmeticAsset.class)
            .append(new KeyedCodec<>("CosmeticType", Codec.STRING), // TODO: could convert into cosmetic type in here if I can preserve the case of the string (or validate it)
                    (t, value) -> t.cosmeticType = value,
                    t -> t.cosmeticType
            ).addValidator(Validators.nonEmptyString()).add()

            .append(new KeyedCodec<>("Model", Codec.STRING, true),
                    (t, value) -> t.model = value,
                    t -> t.model
            ).addValidator(CommonAssetValidator.MODEL_CHARACTER_ATTACHMENT).add()

            .append(new KeyedCodec<>("TextureConfig", TextureConfig.CODEC, true),
                    (t, value) -> t.textureConfig = value,
                    t -> t.textureConfig
            ).add()

            .append(new KeyedCodec<>("Icon", Codec.STRING),
                    (t, value) -> t.icon = value,
                    t -> t.icon
            ).add()

            .build();

    public static final BuilderCodec<CosmeticAsset> BASE_CODEC = BuilderCodec.builder(CosmeticAsset.class, CosmeticAsset::new, ABSTRACT_CODEC)
            .build();


    public static final AssetCodecMapCodec<String, CosmeticAsset> CODEC = new AssetCodecMapCodec<>(
            Codec.STRING, (t, k) -> t.id = k, t -> t.id, (t, data) -> t.data = data, t -> t.data, true
    );

    public static final Supplier<AssetStore<String, CosmeticAsset, DefaultAssetMap<String, CosmeticAsset>>> ASSET_STORE = WardrobeUtil.createAssetStore(CosmeticAsset.class);

    public static DefaultAssetMap<String, CosmeticAsset> getAssetMap() {
        return ASSET_STORE.get().getAssetMap();
    }

    protected String id;
    protected AssetExtraInfo.Data data;

    protected String cosmeticType;
    protected String model;
    protected TextureConfig textureConfig;
    protected String icon;

    @Override
    public String getId() {
        return id;
    }

    @Nonnull
    public CosmeticType getCosmeticType() {
        return CosmeticType.valueOf(cosmeticType.toUpperCase());
    }

    @Nonnull
    public String getModel() {
        return model;
    }

    @Nonnull
    public TextureConfig getTextureConfig() {
        return textureConfig;
    }

    public String getIcon() {
        return icon;
    }
}