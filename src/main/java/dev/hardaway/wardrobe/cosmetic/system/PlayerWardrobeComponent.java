package dev.hardaway.wardrobe.cosmetic.system;

import com.hypixel.hytale.codec.KeyedCodec;
import com.hypixel.hytale.codec.builder.BuilderCodec;
import com.hypixel.hytale.codec.codecs.map.MapCodec;
import com.hypixel.hytale.component.Component;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import dev.hardaway.wardrobe.cosmetic.asset.category.CosmeticGroup;

import javax.annotation.Nullable;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class PlayerWardrobeComponent implements Component<EntityStore> {

    public static final BuilderCodec<PlayerWardrobeComponent> CODEC = BuilderCodec.builder(PlayerWardrobeComponent.class, PlayerWardrobeComponent::new)
            .append(new KeyedCodec<>("Cosmetics", new MapCodec<>(PlayerCosmetic.CODEC, HashMap::new, false), true), (t, value) -> t.cosmetics = value, t -> t.cosmetics).add()
            .build();

    private Map<String, PlayerCosmetic> cosmetics;
    protected boolean dirty;

    public PlayerWardrobeComponent() {
        this(new HashMap<>());
    }

    protected PlayerWardrobeComponent(Map<String, PlayerCosmetic> cosmetics) {
        this.cosmetics = cosmetics;
        this.dirty = true;
    }

    public boolean consumeDirty() {
        boolean dirty = this.dirty;
        this.dirty = false;
        return dirty;
    }

    public Map<String, PlayerCosmetic> getCosmetics() {
        return Collections.unmodifiableMap(this.cosmetics);
    }

    @Nullable
    public PlayerCosmetic getCosmetic(CosmeticGroup group) {
        return cosmetics.get(group.getId());
    }

    public void setCosmetic(CosmeticGroup group, @Nullable PlayerCosmetic cosmetic) {
        this.cosmetics.compute(group.getId(), (_, _) -> cosmetic);
        this.dirty = true;
    }

    @Nullable
    @Override
    public Component<EntityStore> clone() {
        return new PlayerWardrobeComponent(this.cosmetics);
    }
}
