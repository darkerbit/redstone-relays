package io.github.darkerbit.redstonerelays.block;

import io.github.darkerbit.redstonerelays.RedstoneRelays;
import io.github.darkerbit.redstonerelays.item.Items;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
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
