package dev.hardaway.wardrobe.api.property;

import com.hypixel.hytale.codec.Codec;
import com.hypixel.hytale.codec.KeyedCodec;
import com.hypixel.hytale.codec.builder.BuilderCodec;
import com.hypixel.hytale.codec.schema.metadata.ui.UIEditor;
import com.hypixel.hytale.codec.schema.metadata.ui.UIPropertyTitle;
import com.hypixel.hytale.codec.validation.Validators;
import com.hypixel.hytale.server.core.Message;

import javax.annotation.Nullable;

public class WardrobeTranslationProperties {
    public static final BuilderCodec<WardrobeTranslationProperties> CODEC = BuilderCodec.builder(WardrobeTranslationProperties.class, WardrobeTranslationProperties::new)
            .append(
                    new KeyedCodec<>("Name", Codec.STRING, true),
                    (data, s) -> data.nameKey = s,
                    data -> data.nameKey
            )
            .metadata(new UIEditor(
                    new UIEditor.LocalizationKeyField(
                            "server.wardrobe.{assetId}.name"
                    )
            ))
            .metadata(new UIPropertyTitle("Name Translation")).documentation("The translation key to use for the Name.")
            .addValidator(Validators.nonEmptyString())
            .add()

            .append(
                    new KeyedCodec<>("Description", Codec.STRING),
                    (data, s) -> data.descriptionKey = s,
                    data -> data.descriptionKey
            )
            .metadata(new UIEditor(
                    new UIEditor.LocalizationKeyField(
                            "server.wardrobe.{assetId}.description"
                    )
            ))
            .metadata(new UIPropertyTitle("Description Translation")).documentation("The translation key to use for the Description.")
            .add()
            .build();

    private String nameKey;
    private @Nullable String descriptionKey;

    WardrobeTranslationProperties() {
    }

    public WardrobeTranslationProperties(String nameKey, @Nullable String descriptionKey) {
        this.nameKey = nameKey;
        this.descriptionKey = descriptionKey;
    }

    public String getNameKey() {
        return this.nameKey;
    }

    public void setNameKey(String nameKey) {
        this.nameKey = nameKey;
    }

    public Message getName() {
        return Message.translation(this.getNameKey());
    }

    @Nullable
    public String getDescriptionKey() {
        return this.descriptionKey;
    }

    public void setDescriptionKey(@Nullable String descriptionKey) {
        this.descriptionKey = descriptionKey;
    }

    public @Nullable Message getDescription() {
        return getDescriptionKey() == null ? null : Message.translation(this.getDescriptionKey());
    }
}