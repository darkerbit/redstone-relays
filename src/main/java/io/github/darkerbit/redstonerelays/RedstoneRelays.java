package io.github.darkerbit.redstonerelays;

import io.github.darkerbit.redstonerelays.api.RelayTriggerCallback;
import io.github.darkerbit.redstonerelays.block.Blocks;
import io.github.darkerbit.redstonerelays.block.entity.AbstractRelayBlockEntity;
import io.github.darkerbit.redstonerelays.block.entity.BlockEntities;
import io.github.darkerbit.redstonerelays.item.Items;
import io.github.darkerbit.redstonerelays.network.RelayTriggerHandler;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerChunkEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerWorldEvents;
import net.fabricmc.fabric.api.gamerule.v1.GameRuleFactory;
import net.fabricmc.fabric.api.gamerule.v1.GameRuleRegistry;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.resource.language.I18n;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.text.component.TranslatableComponent;
import net.minecraft.util.Identifier;
import net.minecraft.world.GameRules;
import org.quiltmc.loader.api.ModContainer;
import org.quiltmc.qsl.base.api.entrypoint.ModInitializer;

public final class RedstoneRelays implements ModInitializer {
    public static final String MOD_ID = "redstonerelays";
    public static final String NAME = "Redstone Relays";

    public static final int KEY_COUNT = 10;

    public static final GameRules.Key<GameRules.IntRule> RELAY_RANGE_RULE = GameRuleRegistry.register(
            "relayRange",
            GameRules.Category.UPDATES,
            GameRuleFactory.createIntRule(32, 0, 256));

    public static Identifier identifier(String name) {
        return new Identifier(MOD_ID, name);
    }

    public static String translationKey(String category, String name) {
        return category + "." + MOD_ID + "." + name;
    }

    public static MutableText translate(String category, String name, Object... args) {
        return Text.translatable(
                translationKey(category, name),
                args
        );
    }

    public static String translateNow(String category, String name, Object... args) {
        return I18n.translate(translationKey(category, name), args);
    }

    @Override
    public void onInitialize(ModContainer mod) {
        RelayTriggerHandler.register();

        Blocks.register();
        BlockEntities.register();
        Items.register();

        ServerWorldEvents.UNLOAD.register((server, world) -> {
            RelayTriggerCallback.callbacks.clear();
        });

        ServerChunkEvents.CHUNK_UNLOAD.register((world, chunk) -> {
            for (BlockEntity blockEntity : chunk.getBlockEntities().values()) {
                if (blockEntity instanceof AbstractRelayBlockEntity relay)
                    relay.unregister();
            }
        });
    }
}
