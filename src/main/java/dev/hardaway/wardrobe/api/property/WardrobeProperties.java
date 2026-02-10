package dev.hardaway.wardrobe.api.property;

import com.hypixel.hytale.codec.Codec;
import com.hypixel.hytale.codec.KeyedCodec;
import com.hypixel.hytale.codec.builder.BuilderCodec;
import com.hypixel.hytale.codec.schema.metadata.ui.UIDefaultCollapsedState;
import com.hypixel.hytale.codec.schema.metadata.ui.UIDisplayMode;
import com.hypixel.hytale.codec.schema.metadata.ui.UIPropertyTitle;
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
            .metadata(new UIPropertyTitle("Translation Properties")).documentation("The translation properties for this object.")
            .metadata(UIDefaultCollapsedState.UNCOLLAPSED)
            .add()

            .append(
                    new KeyedCodec<>("Visibility", WardrobeVisibility.CODEC),
                    (data, s) -> data.visibility = s,
                    data -> data.visibility
            )
            .metadata(new UIPropertyTitle("Wardrobe Visibility")).documentation("The visibility in the Wardrobe Menu. ALWAYS means that players can always see this in the Wardrobe Menu. Players without permission will see the cosmetic as locked. PERMISSION means only players with the permission defined in 'Permission Node' can see this. NEVER means this object is never visible in the Wardrobe Menu.")
            .add()

            .append(
                    new KeyedCodec<>("PermissionNode", Codec.STRING),
                    (data, s) -> data.permissionNode = s,
                    data -> data.permissionNode
            )
            .metadata(new UIPropertyTitle("Permission Node")).documentation("The permission node to bind to this object. See 'Wardrobe Visibility' above for more information.")
            .add()

            // DEPRECATED
            .append(
                    new KeyedCodec<>("Icon", Codec.STRING),
                    (data, s) -> data.icon = s,
                    data -> data.icon
            )
            .addValidator(WardrobeValidators.ICON)
            .metadata(UIDisplayMode.HIDDEN)
            .add()

            .build();

    private WardrobeTranslationProperties translationProperties;
    private @Nonnull WardrobeVisibility visibility = WardrobeVisibility.ALWAYS;
    private @Nullable String icon;
    private @Nullable String permissionNode;

    protected WardrobeProperties() {
    }

    @Deprecated(forRemoval = true)
    public WardrobeProperties(WardrobeTranslationProperties translationProperties, @Nonnull WardrobeVisibility visibility, @Nullable String icon, @Nullable String permissionNode) {
        this.translationProperties = translationProperties;
        this.visibility = visibility;
        this.permissionNode = permissionNode;
        this.icon = icon;
    }

    public WardrobeProperties(WardrobeTranslationProperties translationProperties, @Nonnull WardrobeVisibility visibility, @Nullable String permissionNode) {
        this.translationProperties = translationProperties;
        this.visibility = visibility;
        this.permissionNode = permissionNode;
    }

    public WardrobeTranslationProperties getTranslationProperties() {
        return translationProperties;
    }

    @Nonnull
    public WardrobeVisibility getWardrobeVisibility() {
        return visibility;
    }

    @Deprecated(forRemoval = true)
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
