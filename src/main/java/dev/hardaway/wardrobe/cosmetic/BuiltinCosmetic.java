package dev.hardaway.wardrobe.cosmetic;

import com.hypixel.hytale.server.core.asset.type.model.config.ModelAttachment;
import dev.hardaway.wardrobe.api.WardrobeContext;
import dev.hardaway.wardrobe.api.cosmetic.Cosmetic;
import dev.hardaway.wardrobe.cosmetic.system.PlayerCosmetic;

import javax.annotation.Nullable;

//
// AttachableCosmetic
// - to model attachment
// Cosmetic
// - id
// - model
// - textureconfig
// - applyCosmetic(WardrobeContext context)


// wardrobe context
// - player skin
// - cosmetic map
// - base model
// - base textureconfig
public record BuiltinCosmetic(String model, String texture, @Nullable String gradientSet,
                              @Nullable String gradientId) implements Cosmetic {

    public ModelAttachment toModelAttachment() {
        return new ModelAttachment(
                model,
                texture,
                gradientSet,
                gradientId,
                1.0
        );
    }

    @Override
    public void applyCosmetic(WardrobeContext context, PlayerCosmetic playerCosmetic) {

    }
}
