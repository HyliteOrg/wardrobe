package dev.hardaway.wardrobe.api.cosmetic;

import com.hypixel.hytale.codec.Codec;
import com.hypixel.hytale.codec.KeyedCodec;
import com.hypixel.hytale.codec.builder.BuilderCodec;

import javax.annotation.Nullable;

public final class PlayerCosmetic {

    public static final BuilderCodec<PlayerCosmetic> CODEC = BuilderCodec.builder(PlayerCosmetic.class, PlayerCosmetic::new)
            .append(new KeyedCodec<>("Id", Codec.STRING, true), (t, value) -> t.id = value, t -> t.id).add()
            .append(new KeyedCodec<>("Variant", Codec.STRING), (t, value) -> t.variantId = value, t -> t.variantId).add()
            .build();

    private String id;
    @Nullable
    private String variantId;

    public PlayerCosmetic() {
    }

    public PlayerCosmetic(String id) {
        this(id, null);
    }

    public PlayerCosmetic(String id, @Nullable String variantId) {
        this.id = id;
        this.variantId = variantId;
    }

    public String getId() {
        return id;
    }

    @Nullable
    public String getVariantId() {
        return variantId;
    }
}
