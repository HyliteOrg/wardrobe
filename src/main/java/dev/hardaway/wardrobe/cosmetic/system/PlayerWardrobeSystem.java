package dev.hardaway.wardrobe.cosmetic.system;

import com.hypixel.hytale.assetstore.map.DefaultAssetMap;
import com.hypixel.hytale.component.ArchetypeChunk;
import com.hypixel.hytale.component.CommandBuffer;
import com.hypixel.hytale.component.ComponentType;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.component.query.Query;
import com.hypixel.hytale.component.system.tick.EntityTickingSystem;
import com.hypixel.hytale.server.core.asset.type.model.config.Model;
import com.hypixel.hytale.server.core.asset.type.model.config.ModelAttachment;
import com.hypixel.hytale.server.core.cosmetics.*;
import com.hypixel.hytale.server.core.modules.entity.component.ModelComponent;
import com.hypixel.hytale.server.core.modules.entity.player.PlayerSkinComponent;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import dev.hardaway.wardrobe.cosmetic.BuiltinCosmetic;
import dev.hardaway.wardrobe.cosmetic.asset.CosmeticAsset;
import dev.hardaway.wardrobe.cosmetic.asset.category.CosmeticGroup;
import dev.hardaway.wardrobe.cosmetic.asset.config.TextureConfig;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

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

        PlayerSkinComponent playerSkinComponent = chunk.getComponent(i, PlayerSkinComponent.getComponentType());
        CosmeticsModule cosmeticsModule = CosmeticsModule.get();

        List<ModelAttachment> attachmentList = new ArrayList<>();
        PlayerSkin skin = PlayerWardrobeSystem.skinFromProtocol(playerSkinComponent.getPlayerSkin());

        BuiltinCosmetic builtinBodyCharacteristic = PlayerWardrobeSystem.createBuiltinCosmetic(CosmeticType.BODY_CHARACTERISTICS, skin);
        String baseModelName = builtinBodyCharacteristic.model();
        String baseModelTexture = builtinBodyCharacteristic.texture();
        String baseModelGradientSet = builtinBodyCharacteristic.gradientSet();
        String baseModelGradientId = builtinBodyCharacteristic.gradientId();

        DefaultAssetMap<String, CosmeticGroup> cosmeticGroups = CosmeticGroup.getAssetMap();
        for (CosmeticGroup group : cosmeticGroups.getAssetMap().values()) {
            if (group.getCosmeticType() == CosmeticType.BODY_CHARACTERISTICS) {
                PlayerCosmetic bodyCharacteristic = wardrobeComponent.getCosmetic(group);
                if (bodyCharacteristic != null) {
                    CosmeticAsset cosmetic = CosmeticAsset.getAssetMap().getAsset(bodyCharacteristic.getId());
                    if (cosmetic != null) {
                        TextureConfig textureConfig = cosmetic.getTextureConfig();
                        baseModelName = cosmetic.getModel();
                        baseModelTexture = textureConfig.getTexture(bodyCharacteristic.getVariantId());
                        baseModelGradientSet = textureConfig.getGradientSet();
                        baseModelGradientId = textureConfig.getGradientSet() != null ? bodyCharacteristic.getVariantId() : null;
                    }
                }
                continue;
            }

            PlayerCosmetic cosmeticData = wardrobeComponent.getCosmetic(group);
            if (cosmeticData != null) {
                // TODO: more warnings when this is null
                CosmeticAsset cosmetic = CosmeticAsset.getAssetMap().getAsset(cosmeticData.getId());
                if (cosmetic != null) {
                    TextureConfig textureConfig = cosmetic.getTextureConfig();
                    attachmentList.add(new ModelAttachment(
                            cosmetic.getModel(),
                            textureConfig.getTexture(cosmeticData.getVariantId()),
                            textureConfig.getGradientSet(),
                            textureConfig.getGradientSet() != null ? cosmeticData.getVariantId() : null,
                            1.0
                    ));
                }
            } else if (group.getCosmeticType() != null) {
                BuiltinCosmetic builtinCosmetic = PlayerWardrobeSystem.createBuiltinCosmetic(group.getCosmeticType(), skin);
                if (builtinCosmetic != null) {
                    attachmentList.add(builtinCosmetic.toModelAttachment());
                }
            }
        }

        Model baseModel = cosmeticsModule.createModel(playerSkinComponent.getPlayerSkin());
        Model model = new Model(
                baseModel.getModelAssetId(),
                baseModel.getScale(), // TODO: allow scale change
                baseModel.getRandomAttachmentIds(),
                attachmentList.toArray(ModelAttachment[]::new), // Skin attachments
                baseModel.getBoundingBox(),
                baseModelName, // Model
                baseModelTexture, // Skin texture
                baseModelGradientSet, // Skin gradient set
                baseModelGradientId, // Skin gradient id
                baseModel.getEyeHeight(),
                baseModel.getCrouchOffset(),
                baseModel.getAnimationSetMap(),
                baseModel.getCamera(),
                baseModel.getLight(),
                baseModel.getParticles(),
                baseModel.getTrails(),
                baseModel.getPhysicsValues(),
                baseModel.getDetailBoxes(),
                baseModel.getPhobia(),
                baseModel.getPhobiaModelAssetId()
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
