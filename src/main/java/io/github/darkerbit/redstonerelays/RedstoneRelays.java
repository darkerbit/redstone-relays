package io.github.darkerbit.redstonerelays;

import io.github.darkerbit.redstonerelays.block.Blocks;
import io.github.darkerbit.redstonerelays.network.RelayTriggerHandler;
import net.fabricmc.api.ModInitializer;
import net.minecraft.util.Identifier;

public final class RedstoneRelays implements ModInitializer {
    public static final String MOD_ID = "redstonerelays";
    public static final String NAME = "Redstone Relays";

    public static final int KEY_COUNT = 10;

    public static Identifier identifier(String name) {
        return new Identifier(MOD_ID, name);
    }

    @Override
    public void onInitialize() {
        RelayTriggerHandler.register();

        Blocks.register();
    }
}
