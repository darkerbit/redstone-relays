package io.github.darkerbit.redstonerelays.item;

import io.github.darkerbit.redstonerelays.RedstoneRelays;
import io.github.darkerbit.redstonerelays.block.Blocks;
import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
import net.fabricmc.fabric.api.tag.TagRegistry;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.tag.Tag;

public final class Items {
    public static final ItemGroup ITEM_GROUP = FabricItemGroupBuilder.build(
            RedstoneRelays.identifier("group"),
            () -> new ItemStack(Blocks.TOGGLE_RELAY));

    public static final Tag<Item> UPGRADE_TAG = TagRegistry.item(RedstoneRelays.identifier("upgrade_module"));

    public static void register() {}
}