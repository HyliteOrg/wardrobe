package dev.hardaway.wardrobe.impl.cosmetic.builtin;

import com.hypixel.hytale.server.core.cosmetics.PlayerSkin;
import dev.hardaway.wardrobe.api.player.PlayerCosmetic;

import javax.annotation.Nullable;

public class HytalePlayerCosmetic implements PlayerCosmetic {

    private final String cosmeticId;
    private final String optionId;
    private final String variantId;

    public HytalePlayerCosmetic(PlayerSkin.PlayerSkinPartId partId) {
        this.cosmeticId = partId.getAssetId();
        this.optionId = partId.getVariantId();
        this.variantId = partId.getTextureId();
    }

    @Override
    public String getCosmeticId() {
        return cosmeticId;
    }

    @Nullable
    public String getOptionId() {
        return optionId;
    }

    @Override
    public String getVariantId() {
        return variantId;
    }
}
