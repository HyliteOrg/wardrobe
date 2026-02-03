package dev.hardaway.wardrobe;

import com.hypixel.hytale.assetstore.event.LoadedAssetsEvent;
import com.hypixel.hytale.assetstore.map.DefaultAssetMap;
import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.server.core.event.events.entity.LivingEntityInventoryChangeEvent;
import com.hypixel.hytale.server.core.plugin.JavaPlugin;
import com.hypixel.hytale.server.core.universe.world.World;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import dev.hardaway.wardrobe.api.player.PlayerWardrobe;
import dev.hardaway.wardrobe.impl.cosmetic.Cosmetic;
import dev.hardaway.wardrobe.impl.cosmetic.CosmeticCategory;
import dev.hardaway.wardrobe.impl.cosmetic.CosmeticSlot;
import dev.hardaway.wardrobe.impl.player.PlayerWardrobeComponent;

import java.util.Objects;

public class WardrobeEvents {

    protected static void registerEvents(JavaPlugin plugin) {
        plugin.getEventRegistry().registerGlobal(LivingEntityInventoryChangeEvent.class, WardrobeEvents::onInventoryChange);
        plugin.getEventRegistry().register(LoadedAssetsEvent.class, Cosmetic.class, WardrobeEvents::onCosmeticsUpdated);
        plugin.getEventRegistry().register(LoadedAssetsEvent.class, CosmeticSlot.class, WardrobeEvents::onSlotsUpdated);
        plugin.getEventRegistry().register(LoadedAssetsEvent.class, CosmeticCategory.class, WardrobeEvents::onCategoriesUpdated);
    }

    private static void onInventoryChange(LivingEntityInventoryChangeEvent event) {
        if (!Objects.equals(event.getEntity().getInventory().getArmor(), event.getItemContainer()))
            return;

        Ref<EntityStore> ref = event.getEntity().getReference();
        World world = event.getEntity().getWorld();
        if (ref != null && world != null) {
            Store<EntityStore> store = world.getEntityStore().getStore();

            PlayerWardrobe wardrobe = store.getComponent(ref, PlayerWardrobeComponent.getComponentType());
            if (wardrobe != null) wardrobe.rebuild();
        }
    }

    private static void onCosmeticsUpdated(LoadedAssetsEvent<String, Cosmetic, DefaultAssetMap<String, Cosmetic>> event) {
        WardrobeUtil.rebuildAllWardrobes();
    }

    private static void onSlotsUpdated(LoadedAssetsEvent<String, CosmeticSlot, DefaultAssetMap<String, CosmeticSlot>> event) {
        WardrobeUtil.rebuildAllWardrobes();
    }

    private static void onCategoriesUpdated(LoadedAssetsEvent<String, CosmeticCategory, DefaultAssetMap<String, CosmeticCategory>> event) {
        WardrobeUtil.rebuildAllWardrobes();
    }
}
