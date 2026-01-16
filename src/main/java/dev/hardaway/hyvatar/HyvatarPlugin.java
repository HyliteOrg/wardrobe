package dev.hardaway.hyvatar;

import com.hypixel.hytale.assetstore.AssetRegistry;
import com.hypixel.hytale.assetstore.map.DefaultAssetMap;
import com.hypixel.hytale.component.ComponentType;
import com.hypixel.hytale.logger.HytaleLogger;
import com.hypixel.hytale.protocol.packets.interface_.CustomPageLifetime;
import com.hypixel.hytale.server.core.asset.HytaleAssetStore;
import com.hypixel.hytale.server.core.modules.interaction.interaction.config.server.OpenCustomUIInteraction;
import com.hypixel.hytale.server.core.plugin.JavaPlugin;
import com.hypixel.hytale.server.core.plugin.JavaPluginInit;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import dev.hardaway.hyvatar.asset.CosmeticAsset;
import dev.hardaway.hyvatar.asset.config.GradientTextureConfig;
import dev.hardaway.hyvatar.asset.config.StaticTextureConfig;
import dev.hardaway.hyvatar.asset.config.TextureConfig;
import dev.hardaway.hyvatar.asset.config.VariantTextureConfig;
import dev.hardaway.hyvatar.command.CustomiseAvatarCommand;
import dev.hardaway.hyvatar.command.TestCommand;
import dev.hardaway.hyvatar.system.component.PlayerWardrobeComponent;
import dev.hardaway.hyvatar.ui.AvatarCustomisationPage;

import javax.annotation.Nonnull;

public class HyvatarPlugin extends JavaPlugin {

    private static final HytaleLogger LOGGER = HytaleLogger.forEnclosingClass();

    private ComponentType<EntityStore, PlayerWardrobeComponent> avatarComponent;

    public HyvatarPlugin(@Nonnull JavaPluginInit init) {
        super(init);
    }

    @Override
    protected void setup() {
        OpenCustomUIInteraction.registerCustomPageSupplier(this, AvatarCustomisationPage.class, "AvatarCustomisation", (_, _, playerRef, _) ->
                new AvatarCustomisationPage(playerRef, CustomPageLifetime.CanDismissOrCloseThroughInteraction)
        );

        this.getCommandRegistry().registerCommand(new TestCommand());
        this.getCommandRegistry().registerCommand(new CustomiseAvatarCommand());

        this.getCodecRegistry(TextureConfig.CODEC)
                .register("Gradient", GradientTextureConfig.class, GradientTextureConfig.CODEC)
                .register("Variant", VariantTextureConfig.class, VariantTextureConfig.CODEC)
                .register("Static", StaticTextureConfig.class, StaticTextureConfig.CODEC);

        AssetRegistry.register(HytaleAssetStore.builder(CosmeticAsset.class, new DefaultAssetMap<>())
                .setPath("Wardrobe/Cosmetics")
                .setCodec(CosmeticAsset.CODEC)
                .setKeyFunction(CosmeticAsset::getId)
                .build()
        );
    }
}