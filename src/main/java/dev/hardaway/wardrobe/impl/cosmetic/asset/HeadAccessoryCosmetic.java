package dev.hardaway.wardrobe.impl.cosmetic.asset;

import com.hypixel.hytale.codec.Codec;
import com.hypixel.hytale.codec.KeyedCodec;
import com.hypixel.hytale.codec.builder.BuilderCodec;
import com.hypixel.hytale.codec.codecs.EnumCodec;
import com.hypixel.hytale.protocol.ItemArmorSlot;
import com.hypixel.hytale.server.core.asset.type.item.config.ItemArmor;
import com.hypixel.hytale.server.core.asset.type.model.config.ModelAttachment;
import com.hypixel.hytale.server.core.entity.entities.Player;
import com.hypixel.hytale.server.core.inventory.ItemStack;
import com.hypixel.hytale.server.core.inventory.container.ItemContainer;
import dev.hardaway.wardrobe.api.WardrobeContext;
import dev.hardaway.wardrobe.api.cosmetic.PlayerCosmetic;
import dev.hardaway.wardrobe.api.cosmetic.asset.CosmeticAsset;
import dev.hardaway.wardrobe.api.cosmetic.asset.CosmeticGroup;
import dev.hardaway.wardrobe.api.cosmetic.asset.config.TextureConfig;

import javax.annotation.Nullable;
import java.util.Objects;

// Style
// Cover, Half, Default
public class HeadAccessoryCosmetic extends ModelAttachmentCosmetic {

    public static final BuilderCodec<HeadAccessoryCosmetic> CODEC = BuilderCodec.builder(HeadAccessoryCosmetic.class, HeadAccessoryCosmetic::new, ModelAttachmentCosmetic.CODEC)
            .append(new KeyedCodec<>("HatStyle", new EnumCodec<>(HatStyle.class)),
                    (t, value) -> t.hatStyle = value,
                    t -> t.hatStyle
            ).add()
            .build();

    private HatStyle hatStyle = HatStyle.DEFAULT;

    protected HeadAccessoryCosmetic() {
    }

    public HeadAccessoryCosmetic(String id, String nameKey, String group, String icon, @Nullable String permissionNode, String model, TextureConfig textureConfig, HatStyle hatStyle) {
        super(id, nameKey, group, icon, permissionNode, model, textureConfig);
        this.hatStyle = hatStyle;
    }

    public HatStyle getHatStyle() {
        return hatStyle;
    }

    public enum HatStyle {
        DEFAULT,
        HALF,
        COVERING
    }
}
