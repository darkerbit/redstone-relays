package io.github.darkerbit.redstonerelays.block.entity;

import io.github.darkerbit.redstonerelays.RedstoneRelays;
import io.github.darkerbit.redstonerelays.block.Blocks;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.util.registry.Registry;

public final class BlockEntities {
    public static BlockEntityType<PushRelayBlockEntity> PUSH_RELAY_ENTITY;
    public static BlockEntityType<ToggleRelayBlockEntity> TOGGLE_RELAY_ENTITY;

    public static void register() {
        PUSH_RELAY_ENTITY = Registry.register(Registry.BLOCK_ENTITY_TYPE, RedstoneRelays.identifier("push_relay"),
                BlockEntityType.Builder.create(PushRelayBlockEntity::new, Blocks.PUSH_RELAY).build(null));

        TOGGLE_RELAY_ENTITY = Registry.register(Registry.BLOCK_ENTITY_TYPE, RedstoneRelays.identifier("toggle_relay"),
                BlockEntityType.Builder.create(ToggleRelayBlockEntity::new, Blocks.TOGGLE_RELAY).build(null));
    }
}
