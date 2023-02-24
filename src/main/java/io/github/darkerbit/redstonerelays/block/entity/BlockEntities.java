package io.github.darkerbit.redstonerelays.block.entity;

import io.github.darkerbit.redstonerelays.RedstoneRelays;
import io.github.darkerbit.redstonerelays.block.Blocks;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.registry.Registry;

public final class BlockEntities {
    public static BlockEntityType<PushRelayBlockEntity> PUSH_RELAY_ENTITY;
    public static BlockEntityType<ToggleRelayBlockEntity> TOGGLE_RELAY_ENTITY;
    public static BlockEntityType<PulseRelayBlockEntity> PULSE_RELAY_ENTITY;

    public static void register() {
        PUSH_RELAY_ENTITY = Registry.register(Registry.BLOCK_ENTITY_TYPE, RedstoneRelays.identifier("push_relay"),
                FabricBlockEntityTypeBuilder.create(PushRelayBlockEntity::new, Blocks.PUSH_RELAY).build(null));

        TOGGLE_RELAY_ENTITY = Registry.register(Registry.BLOCK_ENTITY_TYPE, RedstoneRelays.identifier("toggle_relay"),
                FabricBlockEntityTypeBuilder.create(ToggleRelayBlockEntity::new, Blocks.TOGGLE_RELAY).build(null));

        PULSE_RELAY_ENTITY = Registry.register(Registry.BLOCK_ENTITY_TYPE, RedstoneRelays.identifier("pulse_relay"),
                FabricBlockEntityTypeBuilder.create(PulseRelayBlockEntity::new, Blocks.PULSE_RELAY).build(null));
    }
}
