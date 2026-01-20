package dev.hardaway.wardrobe.cosmetic.asset;

import com.hypixel.hytale.server.core.asset.type.model.config.Model;
import dev.hardaway.wardrobe.api.WardrobeContext;
import dev.hardaway.wardrobe.cosmetic.asset.config.TextureConfig;
import dev.hardaway.wardrobe.cosmetic.system.PlayerCosmetic;

// reimpl types
// player model only has "model" which goes to a modelasset

public class PlayerModelCosmetic extends CosmeticAsset{

    @Override
    public void applyCosmetic(WardrobeContext context, PlayerCosmetic playerCosmetic) {
        TextureConfig textureConfig = this.getTextureConfig();
//        context.setPlayerModel(Model.createUnitScaleModel());
//        con
//        context.addAttachment(new ModelAttachment(
//                this.getModel(),
//                textureConfig.getTexture(playerCosmetic.getVariantId()),
//                textureConfig.getGradientSet(),
//                textureConfig.getGradientSet() != null ? playerCosmetic.getVariantId() : null,
//                1.0
//        ));
    }
}
