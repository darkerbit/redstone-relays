package io.github.darkerbit.redstonerelays.gui;

import io.github.darkerbit.redstonerelays.RedstoneRelays;
import io.github.darkerbit.redstonerelays.block.AbstractRelayBlock;
import io.github.darkerbit.redstonerelays.block.entity.AbstractRelayBlockEntity;
import net.fabricmc.fabric.api.screenhandler.v1.ScreenHandlerRegistry;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.*;
import net.minecraft.screen.slot.Slot;

public class RelayScreenHandler extends ScreenHandler {
    public static final ScreenHandlerType<RelayScreenHandler> RELAY_SCREEN_HANDLER
            = ScreenHandlerRegistry.registerSimple(RedstoneRelays.identifier("relay_screen"), RelayScreenHandler::new);

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
        this(syncId, playerInventory, new ArrayPropertyDelegate(1), ScreenHandlerContext.EMPTY, new SimpleInventory(4));
    }

    // Common constructor
    public RelayScreenHandler(int syncId, PlayerInventory playerInventory, PropertyDelegate propertyDelegate, ScreenHandlerContext context, Inventory inventory) {
        super(RELAY_SCREEN_HANDLER, syncId);

        this.inventory = inventory;

        this.propertyDelegate = propertyDelegate;
        addProperties(propertyDelegate);

        this.context = context;

        addSlot(new Slot(inventory, 0, 8, 86));
        addSlot(new Slot(inventory, 1, 8 + 18, 86));

        addSlot(new Slot(inventory, 2, 134, 86));
        addSlot(new Slot(inventory, 3, 134 + 18, 86));

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
    public ItemStack transferSlot(PlayerEntity player, int index) {
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
}
