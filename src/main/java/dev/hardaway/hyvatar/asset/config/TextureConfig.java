package dev.hardaway.hyvatar.asset.config;

import com.hypixel.hytale.codec.lookup.BuilderCodecMapCodec;

public interface TextureConfig {

    BuilderCodecMapCodec<TextureConfig> CODEC = new BuilderCodecMapCodec<>("Type");

}
