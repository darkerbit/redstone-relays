package io.github.darkerbit.redstonerelays.item;

import io.github.darkerbit.redstonerelays.RedstoneRelays;
import io.github.darkerbit.redstonerelays.block.Blocks;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;
import org.quiltmc.qsl.item.setting.api.QuiltItemSettings;

public final class Items {
    public static final ItemGroup ITEM_GROUP = FabricItemGroup.builder(RedstoneRelays.identifier("group"))
            .icon(() -> new ItemStack(Blocks.TOGGLE_RELAY))
            .build();

    public static final TagKey<Item> UPGRADE_TAG = TagKey.of(RegistryKeys.ITEM, RedstoneRelays.identifier("upgrade_module"));

    public static Item RANGE_UPGRADE = new Item(new QuiltItemSettings().maxCount(1));
    public static Item PULSE_UPGRADE = new Item(new QuiltItemSettings().maxCount(1));

    public static void register() {
        Registry.register(Registries.ITEM, RedstoneRelays.identifier("range_upgrade"), RANGE_UPGRADE);
        Registry.register(Registries.ITEM, RedstoneRelays.identifier("pulse_upgrade"), PULSE_UPGRADE);

        ItemGroupEvents.modifyEntriesEvent(ITEM_GROUP).register(content -> {
            content.addItem(Blocks.PUSH_RELAY);
            content.addItem(Blocks.TOGGLE_RELAY);
            content.addItem(Blocks.PULSE_RELAY);

            content.addItem(RANGE_UPGRADE);
            content.addItem(PULSE_UPGRADE);
        });
    }
}
