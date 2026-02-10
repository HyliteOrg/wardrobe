package dev.hardaway.wardrobe.api.property;

import com.hypixel.hytale.codec.codecs.EnumCodec;

public enum WardrobeVisibility {
    ALWAYS, PERMISSION, NEVER;

    public static final EnumCodec<WardrobeVisibility> CODEC = new EnumCodec<>(WardrobeVisibility.class)
            .documentKey(ALWAYS, "Players can always see this in the Wardrobe Menu. Players without permission will see the cosmetic as locked.")
            .documentKey(PERMISSION, "Only players with the permission defined in 'Permission Node' can see this.")
            .documentKey(NEVER, "This object is never visible in the Wardrobe Menu.");
}
