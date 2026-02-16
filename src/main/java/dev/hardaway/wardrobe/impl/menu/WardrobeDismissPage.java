package dev.hardaway.wardrobe.impl.menu;

import com.hypixel.hytale.codec.KeyedCodec;
import com.hypixel.hytale.codec.builder.BuilderCodec;
import com.hypixel.hytale.codec.codecs.EnumCodec;
import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.event.IEventDispatcher;
import com.hypixel.hytale.protocol.ClientCameraView;
import com.hypixel.hytale.protocol.packets.camera.SetServerCamera;
import com.hypixel.hytale.protocol.packets.interface_.CustomPageLifetime;
import com.hypixel.hytale.protocol.packets.interface_.CustomUIEventBindingType;
import com.hypixel.hytale.server.core.HytaleServer;
import com.hypixel.hytale.server.core.entity.entities.player.pages.InteractiveCustomUIPage;
import com.hypixel.hytale.server.core.ui.builder.UICommandBuilder;
import com.hypixel.hytale.server.core.ui.builder.UIEventBuilder;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import dev.hardaway.wardrobe.impl.player.PlayerWardrobeComponent;

import javax.annotation.Nonnull;

public class WardrobeDismissPage extends InteractiveCustomUIPage<WardrobeDismissPage.PageEventData> {
    private final PlayerWardrobeComponent wardrobe;
    private final WardrobeMode mode;

    public WardrobeDismissPage(@Nonnull PlayerRef playerRef, @Nonnull CustomPageLifetime lifetime, PlayerWardrobeComponent wardrobe) {
        this(playerRef, lifetime, wardrobe, new WardrobeMode.Player());
    }

    WardrobeDismissPage(@Nonnull PlayerRef playerRef, @Nonnull CustomPageLifetime lifetime, PlayerWardrobeComponent wardrobe, @Nonnull WardrobeMode mode) {
        super(playerRef, lifetime, PageEventData.CODEC);
        this.wardrobe = wardrobe;
        this.mode = mode;
    }

    @Override
    public void build(@Nonnull Ref<EntityStore> ref, @Nonnull UICommandBuilder commandBuilder, @Nonnull UIEventBuilder eventBuilder, @Nonnull Store<EntityStore> store) {
        commandBuilder.append("Wardrobe/Pages/UnsavedChanges.ui");

        eventBuilder.addEventBinding(CustomUIEventBindingType.Activating, "#Save", WardrobePage.MenuAction.Save.getEvent());
        eventBuilder.addEventBinding(CustomUIEventBindingType.Activating, "#Discard", WardrobePage.MenuAction.Discard.getEvent());
    }

    @Override
    public void handleDataEvent(@Nonnull Ref<EntityStore> ref, @Nonnull Store<EntityStore> store, @Nonnull PageEventData data) {
        super.handleDataEvent(ref, store, data);

        if (WardrobePage.MenuAction.Save.equals(data.action)) {
            PlayerWardrobeComponent current = store.getComponent(ref, PlayerWardrobeComponent.getComponentType());
            if (current != null) {
                mode.onSave(current.clone());
            }
        } else if (WardrobePage.MenuAction.Discard.equals(data.action)) {
            if (mode instanceof WardrobeMode.Player) {
                store.putComponent(ref, PlayerWardrobeComponent.getComponentType(), wardrobe);
            } else if (mode.getRestoreWardrobe() != null) {
                store.putComponent(ref, PlayerWardrobeComponent.getComponentType(), mode.getRestoreWardrobe());
            }
        }

        close();
    }

    @Override
    public void onDismiss(@Nonnull Ref<EntityStore> ref, @Nonnull Store<EntityStore> store) {
        if (mode.getRestoreWardrobe() != null) {
            store.putComponent(ref, PlayerWardrobeComponent.getComponentType(), mode.getRestoreWardrobe());
        }

        super.onDismiss(ref, store);

        IEventDispatcher<WardrobeMenuEvents.Close, WardrobeMenuEvents.Close> dispatchFor = HytaleServer.get().getEventBus().dispatchFor(WardrobeMenuEvents.Close.class);
        if (dispatchFor.hasListener()) {
            dispatchFor.dispatch(new WardrobeMenuEvents.Close(playerRef));
        }

        playerRef.getPacketHandler().writeNoCache(new SetServerCamera(ClientCameraView.FirstPerson, false, null));

        Runnable onClose = mode.getOnClose();
        if (onClose != null) {
            store.getExternalData().getWorld().execute(onClose);
        }
    }

    public static class PageEventData {
        public static final BuilderCodec<PageEventData> CODEC = BuilderCodec.builder(PageEventData.class, PageEventData::new)
                .append(new KeyedCodec<>("Action", new EnumCodec<>(WardrobePage.MenuAction.class)), (e, v) -> e.action = v, e -> e.action).add()
                .build();

        private WardrobePage.MenuAction action;
    }
}
