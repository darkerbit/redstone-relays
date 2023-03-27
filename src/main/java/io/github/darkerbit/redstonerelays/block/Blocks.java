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

package io.github.darkerbit.redstonerelays.block;

import io.github.darkerbit.redstonerelays.RedstoneRelays;
import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import org.quiltmc.qsl.block.extensions.api.QuiltBlockSettings;
import org.quiltmc.qsl.item.setting.api.QuiltItemSettings;

public final class Blocks {
    public static final Block PUSH_RELAY = new PushRelayBlock(QuiltBlockSettings.copyOf(net.minecraft.block.Blocks.DAYLIGHT_DETECTOR));
    public static final Block TOGGLE_RELAY = new ToggleRelayBlock(QuiltBlockSettings.copyOf(net.minecraft.block.Blocks.DAYLIGHT_DETECTOR));
    public static final Block PULSE_RELAY = new PulseRelayBlock(QuiltBlockSettings.copyOf(net.minecraft.block.Blocks.DAYLIGHT_DETECTOR));

    public static void register() {
        registerBlockItem("push_relay", PUSH_RELAY);
        registerBlockItem("toggle_relay", TOGGLE_RELAY);
        registerBlockItem("pulse_relay", PULSE_RELAY);
    }

    private static void registerBlock(String name, Block block) {
        Registry.register(Registries.BLOCK, RedstoneRelays.identifier(name), block);
    }

    private static void registerBlockItem(String name, Block block) {
        registerBlock(name, block);

        Registry.register(Registries.ITEM, RedstoneRelays.identifier(name), new BlockItem(block, new QuiltItemSettings()));
    }
}
