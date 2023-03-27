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

package io.github.darkerbit.redstonerelays.item;

import io.github.darkerbit.redstonerelays.RedstoneRelays;
import io.github.darkerbit.redstonerelays.block.Blocks;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemGroups;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;
import org.quiltmc.qsl.item.setting.api.QuiltItemSettings;

public final class Items {
    public static final TagKey<Item> UPGRADE_TAG = TagKey.of(RegistryKeys.ITEM, RedstoneRelays.identifier("upgrade_module"));

    public static Item RANGE_UPGRADE = new Item(new QuiltItemSettings().maxCount(1));
    public static Item PULSE_UPGRADE = new Item(new QuiltItemSettings().maxCount(1));

    public static void register() {
        Registry.register(Registries.ITEM, RedstoneRelays.identifier("range_upgrade"), RANGE_UPGRADE);
        Registry.register(Registries.ITEM, RedstoneRelays.identifier("pulse_upgrade"), PULSE_UPGRADE);

        ItemGroupEvents.modifyEntriesEvent(ItemGroups.REDSTONE_BLOCKS).register(content -> {
            content.addItem(Blocks.PUSH_RELAY);
            content.addItem(Blocks.TOGGLE_RELAY);
            content.addItem(Blocks.PULSE_RELAY);

            content.addItem(RANGE_UPGRADE);
            content.addItem(PULSE_UPGRADE);
        });
    }
}
