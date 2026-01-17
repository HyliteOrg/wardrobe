package dev.hardaway.hyvatar;

import com.hypixel.hytale.assetstore.AssetRegistry;
import com.hypixel.hytale.assetstore.map.DefaultAssetMap;
import com.hypixel.hytale.codec.lookup.Priority;
import com.hypixel.hytale.component.ComponentType;
import com.hypixel.hytale.protocol.packets.interface_.CustomPageLifetime;
import com.hypixel.hytale.server.core.asset.HytaleAssetStore;
import com.hypixel.hytale.server.core.asset.type.model.config.ModelAsset;
import com.hypixel.hytale.server.core.modules.interaction.interaction.config.server.OpenCustomUIInteraction;
import com.hypixel.hytale.server.core.plugin.JavaPlugin;
import com.hypixel.hytale.server.core.plugin.JavaPluginInit;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import dev.hardaway.hyvatar.asset.BodyCharacteristicCosmeticAsset;
import dev.hardaway.hyvatar.asset.CosmeticAsset;
import dev.hardaway.hyvatar.asset.config.DefaultTextureConfig;
import dev.hardaway.hyvatar.asset.config.GradientTextureConfig;
import dev.hardaway.hyvatar.asset.config.TextureConfig;
import dev.hardaway.hyvatar.asset.config.VariantTextureConfig;
import dev.hardaway.hyvatar.command.CustomiseAvatarCommand;
import dev.hardaway.hyvatar.command.TestCommand;
import dev.hardaway.hyvatar.cosmetic.PlayerWardrobeComponent;
import dev.hardaway.hyvatar.cosmetic.PlayerWardrobeSystem;
import dev.hardaway.hyvatar.cosmetic.SetupPlayerWardrobeSystem;
import dev.hardaway.hyvatar.ui.AvatarCustomisationPage;

import javax.annotation.Nonnull;

public class HyvatarPlugin extends JavaPlugin {

    private static HyvatarPlugin instance;
    private ComponentType<EntityStore, PlayerWardrobeComponent> playerWardrobeComponentType;

    public HyvatarPlugin(@Nonnull JavaPluginInit init) {
        super(init);
        instance = this;
    }

    public static HyvatarPlugin get() {
        return instance;
    }

    @Override
    protected void setup() {
        this.getCodecRegistry(TextureConfig.CODEC)
                .register(Priority.DEFAULT, "Default", DefaultTextureConfig.class, DefaultTextureConfig.CODEC)
                .register("Gradient", GradientTextureConfig.class, GradientTextureConfig.CODEC)
                .register("Variant", VariantTextureConfig.class, VariantTextureConfig.CODEC);

        this.getCodecRegistry(CosmeticAsset.CODEC)
                .register(Priority.DEFAULT, "Default", CosmeticAsset.class, CosmeticAsset.BASE_CODEC);

        AssetRegistry.register(HytaleAssetStore.builder(CosmeticAsset.class, new DefaultAssetMap<>())
                .setPath("Wardrobe/Cosmetics")
                .setCodec(CosmeticAsset.CODEC)
                .setKeyFunction(CosmeticAsset::getId)
                .loadsAfter(ModelAsset.class)
                .build()
        );

        this.playerWardrobeComponentType = this.getEntityStoreRegistry().registerComponent(
                PlayerWardrobeComponent.class,
                PlayerWardrobeComponent::new
        );

        this.getEntityStoreRegistry().registerSystem(new SetupPlayerWardrobeSystem(this.playerWardrobeComponentType));
        this.getEntityStoreRegistry().registerSystem(new PlayerWardrobeSystem(this.playerWardrobeComponentType));

        OpenCustomUIInteraction.registerCustomPageSupplier(this, AvatarCustomisationPage.class, "AvatarCustomisation", (_, _, playerRef, _) ->
                new AvatarCustomisationPage(playerRef, CustomPageLifetime.CanDismissOrCloseThroughInteraction)
        );

        this.getCommandRegistry().registerCommand(new TestCommand(this.playerWardrobeComponentType));
        this.getCommandRegistry().registerCommand(new CustomiseAvatarCommand());

    }
}