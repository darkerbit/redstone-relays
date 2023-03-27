/*
 * Copyright (c) 2023 darkerbit
 *
 * This software is provided 'as-is', without any express or implied
 * warranty. In no event will the authors be held liable for any damages
 * arising from the use of this software.
 *
 * Permission is granted to anyone to use this software for any purpose,
 * including commercial applications, and to alter it and redistribute it
 * freely, subject to the following restrictions:
 *
 * 1. The origin of this software must not be misrepresented; you must not
 *    claim that you wrote the original software. If you use this software
 *    in a product, an acknowledgment in the product documentation would be
 *    appreciated but is not required.
 * 2. Altered source versions must be plainly marked as such, and must not be
 *    misrepresented as being the original software.
 * 3. This notice may not be removed or altered from any source distribution.
 */

package io.github.darkerbit.redstonerelays.block.entity;

import io.github.darkerbit.redstonerelays.RedstoneRelays;
import io.github.darkerbit.redstonerelays.block.Blocks;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;

public final class BlockEntities {
    public static BlockEntityType<PushRelayBlockEntity> PUSH_RELAY_ENTITY;
    public static BlockEntityType<ToggleRelayBlockEntity> TOGGLE_RELAY_ENTITY;
    public static BlockEntityType<PulseRelayBlockEntity> PULSE_RELAY_ENTITY;

    public static void register() {
        PUSH_RELAY_ENTITY = Registry.register(Registries.BLOCK_ENTITY_TYPE, RedstoneRelays.identifier("push_relay"),
                FabricBlockEntityTypeBuilder.create(PushRelayBlockEntity::new, Blocks.PUSH_RELAY).build(null));

        TOGGLE_RELAY_ENTITY = Registry.register(Registries.BLOCK_ENTITY_TYPE, RedstoneRelays.identifier("toggle_relay"),
                FabricBlockEntityTypeBuilder.create(ToggleRelayBlockEntity::new, Blocks.TOGGLE_RELAY).build(null));

        PULSE_RELAY_ENTITY = Registry.register(Registries.BLOCK_ENTITY_TYPE, RedstoneRelays.identifier("pulse_relay"),
                FabricBlockEntityTypeBuilder.create(PulseRelayBlockEntity::new, Blocks.PULSE_RELAY).build(null));
    }
}
