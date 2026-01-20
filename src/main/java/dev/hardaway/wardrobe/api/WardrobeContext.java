package dev.hardaway.wardrobe.api;

import com.hypixel.hytale.server.core.asset.type.model.config.Model;
import com.hypixel.hytale.server.core.asset.type.model.config.ModelAttachment;
import com.hypixel.hytale.server.core.cosmetics.PlayerSkin;
import dev.hardaway.wardrobe.api.component.PlayerWardrobeComponent;
import dev.hardaway.wardrobe.api.cosmetic.asset.config.TextureConfig;

import javax.annotation.Nullable;
import java.util.Collections;
import java.util.List;

public class WardrobeContext {

    private final PlayerSkin skin;
    private final PlayerWardrobeComponent component;
    private final List<ModelAttachment> attachments;
    private Model playerModel;
    private TextureConfig playerTexture;
    private String playerTextureVariantId;

    public WardrobeContext(PlayerSkin skin, PlayerWardrobeComponent component, List<ModelAttachment> attachments, Model playerModel, TextureConfig texture, @Nullable String playerTextureVariantId) {
        this.skin = skin;
        this.component = component;
        this.attachments = attachments;
        this.playerModel = playerModel;
        this.playerTexture = texture;
        this.playerTextureVariantId = playerTextureVariantId;
    }

    public PlayerSkin getSkin() {
        return skin;
    }

    public PlayerWardrobeComponent getComponent() {
        return component;
    }

    public List<ModelAttachment> getAttachments() {
        return Collections.unmodifiableList(this.attachments);
    }

    public boolean addAttachment(ModelAttachment attachment) {
        return this.attachments.add(attachment);
    }

    public Model getPlayerModel() {
        return playerModel;
    }

    public void setPlayerModel(Model playerModel) {
        this.playerModel = playerModel;
    }

    public TextureConfig getPlayerTexture() {
        return playerTexture;
    }

    public void setPlayerTexture(TextureConfig playerTexture) {
        this.playerTexture = playerTexture;
    }

    public String getPlayerTextureVariantId() {
        return playerTextureVariantId;
    }

    public void setPlayerTextureVariantId(String playerTextureVariantId) {
        this.playerTextureVariantId = playerTextureVariantId;
    }
}
