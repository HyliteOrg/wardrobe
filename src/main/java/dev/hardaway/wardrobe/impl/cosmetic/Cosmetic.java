package dev.hardaway.wardrobe.impl.cosmetic;

import com.hypixel.hytale.assetstore.AssetExtraInfo;
import com.hypixel.hytale.assetstore.AssetStore;
import com.hypixel.hytale.assetstore.codec.AssetCodecMapCodec;
import com.hypixel.hytale.assetstore.map.DefaultAssetMap;
import com.hypixel.hytale.assetstore.map.JsonAssetWithMap;
import com.hypixel.hytale.codec.Codec;
import com.hypixel.hytale.codec.KeyedCodec;
import com.hypixel.hytale.codec.builder.BuilderCodec;
import com.hypixel.hytale.codec.validation.Validators;
import dev.hardaway.wardrobe.WardrobePlugin;
import dev.hardaway.wardrobe.api.cosmetic.WardrobeContext;
import dev.hardaway.wardrobe.api.cosmetic.WardrobeCosmetic;
import dev.hardaway.wardrobe.api.cosmetic.WardrobeCosmeticSlot;
import dev.hardaway.wardrobe.api.player.PlayerCosmetic;
import dev.hardaway.wardrobe.api.property.WardrobeProperties;

import javax.annotation.Nonnull;
import java.util.function.Supplier;

public abstract class Cosmetic implements WardrobeCosmetic, JsonAssetWithMap<String, DefaultAssetMap<String, Cosmetic>> {

    public static final BuilderCodec<Cosmetic> ABSTRACT_CODEC = BuilderCodec.abstractBuilder(Cosmetic.class)
//            .metadata(new UIEditorPreview(UIEditorPreview.PreviewType.MODEL)) TODO: proper model preview & icon

            .appendInherited(new KeyedCodec<>("Properties", WardrobeProperties.CODEC, true),
                    (c, value) -> c.properties = value,
                    c -> c.properties,
                    (c, p) -> c.properties = p.properties
            )
            .addValidator(Validators.nonNull())
            .add()

            .appendInherited(new KeyedCodec<>("CosmeticSlot", Codec.STRING, true),
                    (c, value) -> c.cosmeticSlotId = value,
                    c -> c.cosmeticSlotId,
                    (c, p) -> c.cosmeticSlotId = p.cosmeticSlotId
            )
            .addValidator(CosmeticSlot.VALIDATOR_CACHE.getValidator().late())
            .add()

            .appendInherited(new KeyedCodec<>("HiddenCosmeticSlots", Codec.STRING_ARRAY),
                    (c, value) -> c.hiddenCosmeticSlots = value,
                    c -> c.hiddenCosmeticSlots,
                    (c, p) -> c.hiddenCosmeticSlots = p.hiddenCosmeticSlots
            )
            .addValidator(CosmeticSlot.VALIDATOR_CACHE.getArrayValidator().late())
            .add()

            .build();

    public static final AssetCodecMapCodec<String, Cosmetic> CODEC = new AssetCodecMapCodec<>(
            Codec.STRING, (t, k) -> t.id = k, t -> t.id, (t, data) -> t.data = data, t -> t.data, true
    );

    public static final Supplier<AssetStore<String, Cosmetic, DefaultAssetMap<String, Cosmetic>>> ASSET_STORE = WardrobePlugin.createAssetStore(Cosmetic.class);

    public static DefaultAssetMap<String, Cosmetic> getAssetMap() {
        return ASSET_STORE.get().getAssetMap();
    }

    private String id;
    private AssetExtraInfo.Data data;

    private String cosmeticSlotId;
    private String[] hiddenCosmeticSlots = new String[0];
    private WardrobeProperties properties;

    protected Cosmetic() {
    }

    public Cosmetic(String id, String cosmeticSlotId, String[] hiddenCosmeticSlots, WardrobeProperties properties) {
        this.id = id;
        this.cosmeticSlotId = cosmeticSlotId;
        this.hiddenCosmeticSlots = hiddenCosmeticSlots;
        this.properties = properties;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public WardrobeProperties getProperties() {
        return properties;
    }

    @Nonnull
    public String getCosmeticSlotId() {
        return cosmeticSlotId;
    }

    public String[] getHiddenCosmeticSlotIds() {
        return hiddenCosmeticSlots;
    }

    @Override
    public void applyCosmetic(WardrobeContext context, WardrobeCosmeticSlot slot, PlayerCosmetic playerCosmetic) {
        context.hideSlots(this.getHiddenCosmeticSlotIds());
    }
}