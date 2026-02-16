package dev.hardaway.wardrobe.impl.menu;

import dev.hardaway.wardrobe.impl.player.PlayerWardrobeComponent;

import javax.annotation.Nullable;
import java.util.function.BiConsumer;

public sealed interface WardrobeMode {

    @Nullable
    default PlayerWardrobeComponent getRestoreWardrobe() {
        return null;
    }

    default void onSave(PlayerWardrobeComponent wardrobe) {
    }

    @Nullable
    default String getNpcName() {
        return null;
    }

    default void setNpcName(String name) {
    }

    record Player() implements WardrobeMode {
    }

    final class Npc implements WardrobeMode {
        private final PlayerWardrobeComponent restoreWardrobe;
        private final BiConsumer<PlayerWardrobeComponent, String> saveCallback;
        private String name;

        public Npc(PlayerWardrobeComponent restoreWardrobe, BiConsumer<PlayerWardrobeComponent, String> saveCallback) {
            this(restoreWardrobe, saveCallback, null);
        }

        public Npc(PlayerWardrobeComponent restoreWardrobe, BiConsumer<PlayerWardrobeComponent, String> saveCallback, @Nullable String name) {
            this.restoreWardrobe = restoreWardrobe;
            this.saveCallback = saveCallback;
            this.name = name;
        }

        @Override
        public PlayerWardrobeComponent getRestoreWardrobe() {
            return restoreWardrobe;
        }

        @Override
        public void onSave(PlayerWardrobeComponent wardrobe) {
            saveCallback.accept(wardrobe, name);
        }

        @Override
        @Nullable
        public String getNpcName() {
            return name;
        }

        @Override
        public void setNpcName(String name) {
            this.name = name;
        }
    }
}
