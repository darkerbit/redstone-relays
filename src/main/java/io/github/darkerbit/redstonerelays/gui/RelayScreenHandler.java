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

package io.github.darkerbit.redstonerelays.gui;

import io.github.darkerbit.redstonerelays.RedstoneRelays;
import io.github.darkerbit.redstonerelays.block.AbstractRelayBlock;
import io.github.darkerbit.redstonerelays.block.entity.AbstractRelayBlockEntity;
import io.github.darkerbit.redstonerelays.item.Items;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.feature_flags.FeatureFlags;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.screen.*;
import net.minecraft.screen.slot.Slot;

public class RelayScreenHandler extends ScreenHandler {
    public static final ScreenHandlerType<RelayScreenHandler> RELAY_SCREEN_HANDLER = new ScreenHandlerType<>(RelayScreenHandler::new, FeatureFlags.DEFAULT_SET);

    public static void register() {
        Registry.register(Registries.SCREEN_HANDLER_TYPE, RedstoneRelays.identifier("relay_screen"), RELAY_SCREEN_HANDLER);
    }

    private AbstractRelayBlockEntity blockEntity;
    private ScreenHandlerContext context;

    private PropertyDelegate propertyDelegate;

    private Inventory inventory;

    // server constructor
    public RelayScreenHandler(int syncId, PlayerInventory playerInventory, AbstractRelayBlockEntity blockEntity,
                              PropertyDelegate propertyDelegate, ScreenHandlerContext context) {
        this(syncId, playerInventory, propertyDelegate, context, blockEntity);

        this.blockEntity = blockEntity;
    }

    // client constructor
    public RelayScreenHandler(int syncId, PlayerInventory playerInventory) {
        this(syncId, playerInventory, new ArrayPropertyDelegate(3), ScreenHandlerContext.EMPTY, new SimpleInventory(4));
    }

    // Common constructor
    public RelayScreenHandler(int syncId, PlayerInventory playerInventory, PropertyDelegate propertyDelegate, ScreenHandlerContext context, Inventory inventory) {
        super(RELAY_SCREEN_HANDLER, syncId);

        this.inventory = inventory;

        this.propertyDelegate = propertyDelegate;
        addProperties(propertyDelegate);

        this.context = context;

        addSlot(new UpgradeSlot(inventory, 0, 8, 86));
        addSlot(new UpgradeSlot(inventory, 1, 8 + 18, 86));

        addSlot(new UpgradeSlot(inventory, 2, 134, 86));
        addSlot(new UpgradeSlot(inventory, 3, 134 + 18, 86));

        for (int j = 0; j < 3; j++) {
            for (int i = 0; i < 9; i++) {
                addSlot(new Slot(playerInventory, i + j * 9 + 9, 8 + i * 18, 110 + j * 18));
            }
        }

        for (int i = 0; i < 9; i++) {
            addSlot(new Slot(playerInventory, i, 8 + i * 18, 168));
        }
    }

    public int getRelayNumber() {
        return propertyDelegate.get(AbstractRelayBlockEntity.RELAY_NUMBER_PROP);
    }

    public int getRange() { return propertyDelegate.get(AbstractRelayBlockEntity.RELAY_RANGE_PROP); }

    public int getPulseLength() { return propertyDelegate.get(AbstractRelayBlockEntity.RELAY_PULSE_PROP); }

    public boolean hasPulseLength() { return getPulseLength() > 0; }

    @Override
    public boolean canUse(PlayerEntity player) {
        // TODO: Add range calculation
        return context.get(
                (world, pos) -> world.getBlockState(pos).getBlock() instanceof AbstractRelayBlock,
                true
        );
    }

    @Override
    public boolean onButtonClick(PlayerEntity player, int id) {
        return blockEntity.setNumber(player, id);
    }

    @Override
    public ItemStack quickTransfer(PlayerEntity player, int index) {
        ItemStack out = ItemStack.EMPTY;

        Slot slot = this.slots.get(index);

        if (slot != null && slot.hasStack()) {
            ItemStack originalStack = slot.getStack();

            out = originalStack.copy();

            if (index < this.inventory.size()) {
                if (!this.insertItem(originalStack, this.inventory.size(), this.slots.size(), true)) {
                    return ItemStack.EMPTY;
                }
            } else if (!this.insertItem(originalStack, 0, this.inventory.size(), false)) {
                return ItemStack.EMPTY;
            }

            if (originalStack.isEmpty()) {
                slot.setStack(ItemStack.EMPTY);
            } else {
                slot.markDirty();
            }
        }

        return out;
    }

    private static class UpgradeSlot extends Slot {
        public UpgradeSlot(Inventory inventory, int index, int x, int y) {
            super(inventory, index, x, y);
        }

        @Override
        public boolean canInsert(ItemStack stack) {
            return stack.isIn(Items.UPGRADE_TAG);
        }
    }
}
