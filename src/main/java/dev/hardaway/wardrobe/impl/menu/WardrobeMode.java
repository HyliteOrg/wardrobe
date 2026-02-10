package dev.hardaway.wardrobe.impl.menu;

import dev.hardaway.wardrobe.impl.player.PlayerWardrobeComponent;

import javax.annotation.Nullable;
import java.util.function.Consumer;

public sealed interface WardrobeMode {

    @Nullable
    default PlayerWardrobeComponent getRestoreWardrobe() {
        return null;
    }

    default void onSave(PlayerWardrobeComponent wardrobe) {
    }

    record Player() implements WardrobeMode {
    }

    record Npc(PlayerWardrobeComponent restoreWardrobe, Consumer<PlayerWardrobeComponent> saveCallback) implements WardrobeMode {

        @Override
        public PlayerWardrobeComponent getRestoreWardrobe() {
            return restoreWardrobe;
        }

        @Override
        public void onSave(PlayerWardrobeComponent wardrobe) {
            saveCallback.accept(wardrobe);
        }
    }
}
