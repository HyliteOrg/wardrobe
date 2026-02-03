package dev.hardaway.wardrobe.impl.command;


import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.protocol.GameMode;
import com.hypixel.hytale.server.core.command.system.CommandContext;
import com.hypixel.hytale.server.core.command.system.arguments.types.AssetArgumentType;
import com.hypixel.hytale.server.core.command.system.arguments.types.SingleArgumentType;
import com.hypixel.hytale.server.core.command.system.basecommands.AbstractPlayerCommand;
import com.hypixel.hytale.server.core.entity.entities.Player;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.universe.world.World;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import dev.hardaway.wardrobe.impl.cosmetic.Cosmetic;
import dev.hardaway.wardrobe.impl.cosmetic.CosmeticSlot;
import dev.hardaway.wardrobe.impl.menu.WardrobePage;
import dev.hardaway.wardrobe.impl.player.PlayerWardrobeComponent;

import javax.annotation.Nonnull;


public class WardrobeCommand extends AbstractPlayerCommand {

    public static final SingleArgumentType<Cosmetic> COSMETIC_ARGUMENT_TYPE = new AssetArgumentType<>("server.commands.parsing.argtype.asset.wardrobe.cosmetic.name", Cosmetic.class, "server.commands.parsing.argtype.asset.wardrobe.cosmetic.usage");
    public static final SingleArgumentType<CosmeticSlot> COSMETIC_SLOT_ARGUMENT_TYPE = new AssetArgumentType<>("server.commands.parsing.argtype.asset.wardrobe.slot.name", CosmeticSlot.class, "server.commands.parsing.argtype.asset.wardrobe.cosmetic_slot.usage");

    public WardrobeCommand() {
        super("wardrobe", "server.commands.wardrobe.description");
        this.setPermissionGroup(GameMode.Adventure);
        this.addSubCommand(new WardrobeRemoveCommand());
        this.addSubCommand(new WardrobeWearCommand());
        this.addSubCommand(new WardrobeClearCommand());
        this.addSubCommand(new WardrobeResetCommand());
    }

    @Override
    protected void execute(@Nonnull CommandContext context, @Nonnull Store<EntityStore> store, @Nonnull Ref<EntityStore> ref, @Nonnull PlayerRef playerRef, @Nonnull World world) {
        Player player = store.getComponent(ref, Player.getComponentType());

        if (player != null) {
            PlayerWardrobeComponent wardrobe = store.ensureAndGetComponent(ref, PlayerWardrobeComponent.getComponentType());
            player.getPageManager().openCustomPage(ref, store, new WardrobePage(playerRef, wardrobe, null, 0));
        }
    }
}