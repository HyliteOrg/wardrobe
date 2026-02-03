package dev.hardaway.wardrobe.api.property;

import com.hypixel.hytale.codec.Codec;
import com.hypixel.hytale.codec.KeyedCodec;
import com.hypixel.hytale.codec.builder.BuilderCodec;
import com.hypixel.hytale.codec.schema.metadata.ui.UIEditor;
import com.hypixel.hytale.codec.schema.metadata.ui.UIRebuildCaches;
import com.hypixel.hytale.codec.validation.Validators;
import com.hypixel.hytale.server.core.permissions.PermissionsModule;
import dev.hardaway.wardrobe.api.property.validator.WardrobeValidators;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.UUID;

public class WardrobeProperties {

    public static final BuilderCodec<WardrobeProperties> CODEC = BuilderCodec.builder(WardrobeProperties.class, WardrobeProperties::new)
            .append(
                    new KeyedCodec<>("Translation", WardrobeTranslationProperties.CODEC, true),
                    (data, s) -> data.translationProperties = s,
                    data -> data.translationProperties
            )
            .addValidator(Validators.nonNull())
            .add()

            .append(
                    new KeyedCodec<>("Visibility", WardrobeVisibility.CODEC),
                    (data, s) -> data.visibility = s,
                    data -> data.visibility
            )
            .documentation("Enum used to specify when the element will be visible in menus.")
            .add()

            .append(
                    new KeyedCodec<>("Icon", Codec.STRING),
                    (data, s) -> data.icon = s,
                    data -> data.icon
            )
            .addValidator(WardrobeValidators.ICON)
            .metadata(new UIEditor(new UIEditor.Icon(
                    "Icons/ModelsGenerated/{assetId}.png", 64, 64
            )))
            .metadata(new UIRebuildCaches(UIRebuildCaches.ClientCache.ITEM_ICONS))
            .add()

//            .append(
//                    new KeyedCodec<>("IconProperties", AssetIconProperties.CODEC),
//                    (p, i) -> p.iconProperties = i,
//                    (item) -> item.iconProperties
//            )
//            .metadata(UIDisplayMode.HIDDEN)
//            .add() // TODO: proper asset editor icon

            .append(
                    new KeyedCodec<>("PermissionNode", Codec.STRING),
                    (data, s) -> data.permissionNode = s,
                    data -> data.permissionNode
            ).add()

            .build();

    private WardrobeTranslationProperties translationProperties;
    private @Nonnull WardrobeVisibility visibility = WardrobeVisibility.ALWAYS;
    private @Nullable String icon;
    private @Nullable String permissionNode;

    protected WardrobeProperties() {
    }

    public WardrobeProperties(WardrobeTranslationProperties translationProperties, @Nonnull WardrobeVisibility visibility, @Nullable String icon, @Nullable String permissionNode) {
        this.translationProperties = translationProperties;
        this.visibility = visibility;
        this.icon = icon;
        this.permissionNode = permissionNode;
    }

    public WardrobeTranslationProperties getTranslationProperties() {
        return translationProperties;
    }

    @Nonnull
    public WardrobeVisibility getWardrobeVisibility() {
        return visibility;
    }

    @Nullable
    public String getIcon() {
        return icon;
    }

    @Nullable
    public String getPermissionNode() {
        return permissionNode;
    }

    public boolean hasPermission(@Nonnull UUID uuid) {
        String permissionNode = this.getPermissionNode();
        if (permissionNode == null) return true;
        return PermissionsModule.get().hasPermission(uuid, permissionNode);
    }
}
