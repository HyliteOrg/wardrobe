package dev.hardaway.wardrobe.impl.cosmetic;

import com.hypixel.hytale.assetstore.AssetExtraInfo;
import com.hypixel.hytale.assetstore.AssetKeyValidator;
import com.hypixel.hytale.assetstore.AssetStore;
import com.hypixel.hytale.assetstore.codec.AssetBuilderCodec;
import com.hypixel.hytale.assetstore.map.DefaultAssetMap;
import com.hypixel.hytale.assetstore.map.JsonAssetWithMap;
import com.hypixel.hytale.codec.Codec;
import com.hypixel.hytale.codec.KeyedCodec;
import com.hypixel.hytale.codec.schema.metadata.ui.UIDefaultCollapsedState;
import com.hypixel.hytale.codec.schema.metadata.ui.UIPropertyTitle;
import com.hypixel.hytale.codec.validation.ValidatorCache;
import com.hypixel.hytale.codec.validation.Validators;
import dev.hardaway.wardrobe.WardrobePlugin;
import dev.hardaway.wardrobe.api.menu.WardrobeCategory;
import dev.hardaway.wardrobe.api.property.WardrobeProperties;
import dev.hardaway.wardrobe.api.property.validator.WardrobeValidators;

import java.util.function.Supplier;

public class CosmeticCategoryAsset implements WardrobeCategory, JsonAssetWithMap<String, DefaultAssetMap<String, CosmeticCategoryAsset>> {

    public static final AssetBuilderCodec<String, CosmeticCategoryAsset> CODEC = AssetBuilderCodec
            .builder(CosmeticCategoryAsset.class, CosmeticCategoryAsset::new,
                    Codec.STRING,
                    (t, k) -> t.id = k,
                    (t) -> t.id,
                    (asset, data) -> asset.data = data,
                    (asset) -> asset.data
            )

            .append(new KeyedCodec<>("Properties", WardrobeProperties.CODEC, true),
                    (t, value) -> t.properties = value,
                    t -> t.properties
            )
            .metadata(new UIPropertyTitle("Wardrobe Properties")).documentation("Properties for the Cosmetic Category to display in the Wardrobe Menu.")
            .metadata(UIDefaultCollapsedState.UNCOLLAPSED)
            .add()

            .append(new KeyedCodec<>("Icon", Codec.STRING, true),
                    (t, value) -> t.icon = value,
                    t -> t.icon
            )
            .addValidator(Validators.nonNull())
            .addValidator(WardrobeValidators.ICON)
            .metadata(new UIPropertyTitle("Icon")).documentation("The icon to display on the tab button in the Wardrobe Menu.")
            .add()

            .append(new KeyedCodec<>("SelectedIcon", Codec.STRING, true),
                    (t, value) -> t.selectedIcon = value,
                    t -> t.selectedIcon
            )
            .addValidator(WardrobeValidators.ICON)
            .metadata(new UIPropertyTitle("Selected Icon")).documentation("The icon to display on the tab button in the Wardrobe Menu when the tab is selected.")
            .add()

            .append(new KeyedCodec<>("Order", Codec.INTEGER),
                    (t, value) -> t.order = value,
                    t -> t.order
            )
            .metadata(new UIPropertyTitle("Sorting Order")).documentation("The sorting order of this tab. Tabs are sorted by the sorting order with 0 at the top.")
            .add()
            .afterDecode(asset -> {
                if (asset.getIconPath() == null && asset.properties != null && asset.properties.getIcon() != null) { // DEPRECATED
                    asset.icon = asset.properties.getIcon();
                }
            })
            .build();

    public static final Supplier<AssetStore<String, CosmeticCategoryAsset, DefaultAssetMap<String, CosmeticCategoryAsset>>> ASSET_STORE = WardrobePlugin.createAssetStore(CosmeticCategoryAsset.class);
    public static final ValidatorCache<String> VALIDATOR_CACHE = new ValidatorCache(new AssetKeyValidator(CosmeticCategoryAsset.ASSET_STORE));

    public static DefaultAssetMap<String, CosmeticCategoryAsset> getAssetMap() {
        return ASSET_STORE.get().getAssetMap();
    }


    private String id;
    private AssetExtraInfo.Data data;

    private WardrobeProperties properties;

    private String icon;
    private String selectedIcon;
    private int order = -1;

    @Override
    public String getId() {
        return id;
    }

    @Override
    public WardrobeProperties getProperties() {
        return properties;
    }

    @Override
    public String getIconPath() {
        return icon;
    }

    @Override
    public String getSelectedIconPath() {
        return selectedIcon;
    }

    @Override
    public int getTabOrder() {
        return order;
    }
}
