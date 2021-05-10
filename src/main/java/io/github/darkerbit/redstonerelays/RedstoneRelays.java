package io.github.darkerbit.redstonerelays;

import io.github.darkerbit.redstonerelays.api.ChunkUnloadListener;
import io.github.darkerbit.redstonerelays.block.Blocks;
import io.github.darkerbit.redstonerelays.block.entity.BlockEntities;
import io.github.darkerbit.redstonerelays.network.RelayTriggerHandler;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerChunkEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerWorldEvents;
import net.fabricmc.fabric.api.gamerule.v1.GameRuleFactory;
import net.fabricmc.fabric.api.gamerule.v1.GameRuleRegistry;
import net.minecraft.util.Identifier;
import net.minecraft.world.GameRules;

public final class RedstoneRelays implements ModInitializer {
    public static final String MOD_ID = "redstonerelays";
    public static final String NAME = "Redstone Relays";

    public static final int KEY_COUNT = 10;

    public static final GameRules.Key<GameRules.IntRule> RELAY_RANGE_RULE = GameRuleRegistry.register(
            "relayRange",
            GameRules.Category.UPDATES,
            GameRuleFactory.createIntRule(32, 0, 256));

    public static final Identifier RELAY_TRIGGER_CHAN = identifier("relay_trigger");

    public static Identifier identifier(String name) {
        return new Identifier(MOD_ID, name);
    }

    @Override
    public void onInitialize() {
        RelayTriggerHandler.register();

        Blocks.register();
        BlockEntities.register();

        ServerWorldEvents.UNLOAD.register(ChunkUnloadListener::impl_onWorldUnload);
        ServerChunkEvents.CHUNK_UNLOAD.register(ChunkUnloadListener::impl_onChunkUnload);
    }
}
