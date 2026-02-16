package dev.hardaway.wardrobe.impl.cosmetic.builtin;

import com.hypixel.hytale.server.core.cosmetics.CosmeticsModule;
import com.hypixel.hytale.server.core.cosmetics.PlayerSkinPart;
import dev.hardaway.wardrobe.api.cosmetic.Cosmetic;
import dev.hardaway.wardrobe.impl.cosmetic.CosmeticAsset;
import dev.hardaway.wardrobe.impl.cosmetic.CosmeticSlotAsset;

import javax.annotation.Nullable;
import java.util.*;

public class HytaleCosmeticRegistry {

    private static Map<String, List<HytaleWardrobeCosmetic>> bySlot;
    private static Map<String, HytaleWardrobeCosmetic> byId;

    private static void ensureInitialized() {
        if (bySlot != null) return;

        bySlot = new HashMap<>();
        byId = new HashMap<>();

        CosmeticSlotAsset.getAssetMap().getAssetMap().values().forEach(slot -> {
            if (slot.getHytaleCosmeticType() == null) return;

            var registry = CosmeticsModule.get().getRegistry();
            Map<String, ?> parts = registry.getByType(slot.getHytaleCosmeticType());
            if (parts == null) return;

            List<HytaleWardrobeCosmetic> slotCosmetics = new ArrayList<>();
            for (var entry : parts.entrySet()) {
                if (entry.getValue() instanceof PlayerSkinPart part) {
                    HytaleWardrobeCosmetic cosmetic = new HytaleWardrobeCosmetic(
                            slot.getHytaleCosmeticType(), part, slot.getId()
                    );
                    slotCosmetics.add(cosmetic);
                    byId.put(cosmetic.getId(), cosmetic);
                }
            }

            bySlot.put(slot.getId(), Collections.unmodifiableList(slotCosmetics));
        });
    }

    public static List<HytaleWardrobeCosmetic> getCosmeticsForSlot(String slotId) {
        ensureInitialized();
        return bySlot.getOrDefault(slotId, List.of());
    }

    @Nullable
    public static Cosmetic resolve(String id) {
        CosmeticAsset asset = CosmeticAsset.getAssetMap().getAsset(id);
        if (asset != null) return asset;
        ensureInitialized();
        return byId.get(id);
    }
}
