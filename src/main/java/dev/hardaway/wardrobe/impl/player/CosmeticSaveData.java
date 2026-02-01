package dev.hardaway.wardrobe.impl.player;

import com.hypixel.hytale.codec.Codec;
import com.hypixel.hytale.codec.KeyedCodec;
import com.hypixel.hytale.codec.builder.BuilderCodec;
import dev.hardaway.wardrobe.api.player.PlayerCosmetic;

import javax.annotation.Nullable;

// TODO: redo this this sucks
public final class CosmeticSaveData implements PlayerCosmetic {

    public static final BuilderCodec<CosmeticSaveData> CODEC = BuilderCodec.builder(CosmeticSaveData.class, CosmeticSaveData::new)
            .append(new KeyedCodec<>("Id", Codec.STRING, true), (t, value) -> t.id = value, t -> t.id).add()
            .append(new KeyedCodec<>("Option", Codec.STRING), (t, value) -> t.optionId = value, t -> t.optionId).add()
            .append(new KeyedCodec<>("Variant", Codec.STRING), (t, value) -> t.variantId = value, t -> t.variantId).add()
            .build();

    private String id;
    @Nullable
    private String optionId;
    @Nullable
    private String variantId;

    public CosmeticSaveData() {
    }

    public CosmeticSaveData(String id) {
        this(id, null, null);
    }

    public CosmeticSaveData(String id, @Nullable String optionId, @Nullable String variantId) {
        this.id = id;
        this.optionId = optionId;
        this.variantId = variantId;
    }

    public String getCosmeticId() {
        return id;
    }

    public String getOptionId() {
        return optionId;
    }

    public String getVariantId() {
        return variantId;
    }

}
