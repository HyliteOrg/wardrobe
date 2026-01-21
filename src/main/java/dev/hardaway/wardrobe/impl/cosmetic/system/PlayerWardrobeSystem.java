package dev.hardaway.wardrobe.impl.cosmetic.system;

import com.hypixel.hytale.assetstore.map.DefaultAssetMap;
import com.hypixel.hytale.component.ArchetypeChunk;
import com.hypixel.hytale.component.CommandBuffer;
import com.hypixel.hytale.component.ComponentType;
import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.component.query.Query;
import com.hypixel.hytale.component.system.tick.EntityTickingSystem;
import com.hypixel.hytale.server.core.asset.type.model.config.Model;
import com.hypixel.hytale.server.core.asset.type.model.config.ModelAttachment;
import com.hypixel.hytale.server.core.cosmetics.CosmeticRegistry;
import com.hypixel.hytale.server.core.cosmetics.CosmeticType;
import com.hypixel.hytale.server.core.cosmetics.CosmeticsModule;
import com.hypixel.hytale.server.core.cosmetics.PlayerSkin;
import com.hypixel.hytale.server.core.cosmetics.PlayerSkinPart;
import com.hypixel.hytale.server.core.entity.entities.Player;
import com.hypixel.hytale.server.core.modules.entity.component.ModelComponent;
import com.hypixel.hytale.server.core.modules.entity.player.PlayerSkinComponent;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import dev.hardaway.wardrobe.api.WardrobeContext;
import dev.hardaway.wardrobe.api.component.PlayerWardrobeComponent;
import dev.hardaway.wardrobe.api.cosmetic.PlayerCosmetic;
import dev.hardaway.wardrobe.api.cosmetic.asset.CosmeticAsset;
import dev.hardaway.wardrobe.api.cosmetic.asset.CosmeticGroup;
import dev.hardaway.wardrobe.api.cosmetic.asset.config.TextureConfig;
import dev.hardaway.wardrobe.impl.cosmetic.BuiltinCosmetic;
import dev.hardaway.wardrobe.impl.cosmetic.asset.config.GradientTextureConfig;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;

public class PlayerWardrobeSystem extends EntityTickingSystem<EntityStore> {
    private final ComponentType<EntityStore, PlayerWardrobeComponent> wardrobeComponentType;

    public PlayerWardrobeSystem(ComponentType<EntityStore, PlayerWardrobeComponent> wardrobeComponentType) {
        this.wardrobeComponentType = wardrobeComponentType;
    }

    @Override
    public void tick(float v, int i, @Nonnull ArchetypeChunk<EntityStore> chunk, @Nonnull Store<EntityStore> store, @Nonnull CommandBuffer<EntityStore> commandBuffer) {
        PlayerWardrobeComponent wardrobeComponent = chunk.getComponent(i, this.wardrobeComponentType);
        if (!wardrobeComponent.consumeDirty()) // TODO: use events instead of ticking
            return;

        if (wardrobeComponent.getCosmetics().isEmpty()) {
            Ref<EntityStore> ref = chunk.getReferenceTo(i);
            commandBuffer.tryRemoveComponent(ref, this.wardrobeComponentType);
            return;
        }

        Player player = chunk.getComponent(i, Player.getComponentType());
        com.hypixel.hytale.protocol.PlayerSkin playerSkin = chunk.getComponent(i, PlayerSkinComponent.getComponentType()).getPlayerSkin();
        Model playerModel = CosmeticsModule.get().createModel(playerSkin);
        PlayerSkin skin = PlayerWardrobeSystem.skinFromProtocol(playerSkin);
        WardrobeContext context = new WardrobeContext(
                player,
                skin,
                wardrobeComponent,
                new ArrayList<>(),
                playerModel,
                new GradientTextureConfig(
                        playerModel.getGradientSet(),
                        playerModel.getTexture()
                ),
                playerModel.getGradientId()
        );

        DefaultAssetMap<String, CosmeticGroup> cosmeticGroups = CosmeticGroup.getAssetMap();
        for (CosmeticGroup group : cosmeticGroups.getAssetMap().values()) {
            PlayerCosmetic cosmeticData = wardrobeComponent.getCosmetic(group);
            if (cosmeticData != null) {
                CosmeticAsset cosmetic = CosmeticAsset.getAssetMap().getAsset(cosmeticData.getId());
                if (cosmetic != null) {
                    cosmetic.applyCosmetic(context, group, cosmeticData);
                }
            } else if (group.getCosmeticType() != null) {
//                BuiltinCosmetic builtinCosmetic = PlayerWardrobeSystem.createBuiltinCosmetic(group.getCosmeticType(), skin);
//                if (builtinCosmetic != null) {
//                    context.addAttachment(builtinCosmetic.toModelAttachment());
//                }
            }
        }

        Model contextModel = context.getPlayerModel();
        TextureConfig textureConfig = context.getPlayerTexture();
        Model model = new Model(
                "Wardrobe_Player",
                contextModel.getScale(),
                contextModel.getRandomAttachmentIds(),
                context.getAttachments().toArray(ModelAttachment[]::new), // Skin attachments, TODO: merge from model asset
                contextModel.getBoundingBox(),
                contextModel.getModel(), // Model
                textureConfig.getTexture(context.getPlayerTextureVariantId()), // Skin texture
                textureConfig.getGradientSet(), // Skin gradient set
                context.getPlayerTextureVariantId(), // Skin gradient id
                contextModel.getEyeHeight(),
                contextModel.getCrouchOffset(),
                contextModel.getAnimationSetMap(),
                contextModel.getCamera(),
                contextModel.getLight(),
                contextModel.getParticles(),
                contextModel.getTrails(),
                contextModel.getPhysicsValues(),
                contextModel.getDetailBoxes(),
                contextModel.getPhobia(),
                contextModel.getPhobiaModelAssetId()
        );
        chunk.setComponent(i, ModelComponent.getComponentType(), new ModelComponent(model));
    }

    @Nullable
    @Override
    public Query<EntityStore> getQuery() {
        return this.wardrobeComponentType;
    }


    public static PlayerSkin skinFromProtocol(com.hypixel.hytale.protocol.PlayerSkin protocolPlayerSkin) {
        return new PlayerSkin(protocolPlayerSkin.bodyCharacteristic, protocolPlayerSkin.underwear, protocolPlayerSkin.face, protocolPlayerSkin.ears, protocolPlayerSkin.mouth, protocolPlayerSkin.eyes, protocolPlayerSkin.facialHair, protocolPlayerSkin.haircut, protocolPlayerSkin.eyebrows, protocolPlayerSkin.pants, protocolPlayerSkin.overpants, protocolPlayerSkin.undertop, protocolPlayerSkin.overtop, protocolPlayerSkin.shoes, protocolPlayerSkin.headAccessory, protocolPlayerSkin.faceAccessory, protocolPlayerSkin.earAccessory, protocolPlayerSkin.skinFeature, protocolPlayerSkin.gloves, protocolPlayerSkin.cape);
    }

    public static BuiltinCosmetic createBuiltinCosmetic(CosmeticType type, PlayerSkin skin) {
        PlayerSkin.PlayerSkinPartId skinPartId = switch (type) {
            case EMOTES, GRADIENT_SETS, EYE_COLORS, SKIN_TONES -> null;
            case BODY_CHARACTERISTICS -> skin.getBodyCharacteristic();
            case UNDERWEAR -> skin.getUnderwear();
            case EYEBROWS -> skin.getEyebrows();
            case EYES -> skin.getEyes();
            case FACIAL_HAIR -> skin.getFacialHair();
            case PANTS -> skin.getPants();
            case OVERPANTS -> skin.getOverpants();
            case UNDERTOPS -> skin.getUndertop();
            case OVERTOPS -> skin.getOvertop();
            case HAIRCUTS -> skin.getHaircut();
            case SHOES -> skin.getShoes();
            case HEAD_ACCESSORY -> skin.getHeadAccessory();
            case FACE_ACCESSORY -> skin.getFaceAccessory();
            case EAR_ACCESSORY -> skin.getEarAccessory();
            case GLOVES -> skin.getGloves();
            case CAPES -> skin.getCape();
            case SKIN_FEATURES -> skin.getSkinFeature();
            case EARS -> new PlayerSkin.PlayerSkinPartId(skin.getEars(), skin.getBodyCharacteristic().textureId, null);
            case FACE -> new PlayerSkin.PlayerSkinPartId(skin.getFace(), skin.getBodyCharacteristic().textureId, null);
            case MOUTHS ->
                    new PlayerSkin.PlayerSkinPartId(skin.getMouth(), skin.getBodyCharacteristic().textureId, null);
            case null -> null;
        };

        if (skinPartId == null)
            return null;

        CosmeticRegistry cosmeticRegistry = CosmeticsModule.get().getRegistry();
        PlayerSkinPart skinPart = (PlayerSkinPart) cosmeticRegistry.getByType(type).get(skinPartId.assetId);

        String model;
        String texture;
        @Nullable String gradientSet = null;
        @Nullable String gradientId = null;

        if (skinPart.getVariants() != null && skinPartId.variantId != null) {
            PlayerSkinPart.Variant variant = skinPart.getVariants().get(skinPartId.variantId);
            model = variant.getModel();

            if (variant.getTextures() != null) {
                texture = variant.getTextures().get(skinPartId.textureId).getTexture();
            } else {
                texture = variant.getGreyscaleTexture();
                gradientSet = skinPart.getGradientSet();
                gradientId = skinPartId.getTextureId();
            }
        } else {
            model = skinPart.getModel();
            if (skinPart.getTextures() != null) {
                texture = skinPart.getTextures().get(skinPartId.textureId).getTexture();
            } else {
                texture = skinPart.getGreyscaleTexture();
                gradientSet = skinPart.getGradientSet();
                gradientId = skinPartId.getTextureId();
            }
        }

        return new BuiltinCosmetic(
                model,
                texture,
                gradientSet,
                gradientId
        );
    }
}
