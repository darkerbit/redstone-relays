package io.github.darkerbit.redstonerelays.block;

import io.github.darkerbit.redstonerelays.RedstoneRelays;
import io.github.darkerbit.redstonerelays.api.RelayTriggerCallback;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemGroup;
import net.minecraft.util.registry.Registry;

public final class Blocks {
    public static final Block PUSH_RELAY = new PushRelayBlock(FabricBlockSettings.copyOf(net.minecraft.block.Blocks.DAYLIGHT_DETECTOR));
    public static final Block TOGGLE_RELAY = new ToggleRelayBlock(FabricBlockSettings.copyOf(net.minecraft.block.Blocks.DAYLIGHT_DETECTOR));

    public static void register() {
        registerBlockItem("push_relay", PUSH_RELAY);
        registerBlockItem("toggle_relay", TOGGLE_RELAY);
    }

    private static void registerBlock(String name, Block block) {
        Registry.register(Registry.BLOCK, RedstoneRelays.identifier(name), block);
    }

    private static void registerBlockItem(String name, Block block) {
        registerBlock(name, block);

        Registry.register(Registry.ITEM, RedstoneRelays.identifier(name), new BlockItem(block, new FabricItemSettings().group(ItemGroup.REDSTONE)));
    }
}
