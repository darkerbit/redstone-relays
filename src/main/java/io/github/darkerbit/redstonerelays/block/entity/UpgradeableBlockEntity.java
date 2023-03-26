package io.github.darkerbit.redstonerelays.block.entity;

import io.github.darkerbit.redstonerelays.item.Items;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventories;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;

public abstract class UpgradeableBlockEntity extends BlockEntity implements Inventory {
    private static final int MAX_MODULES_IN_SLOT = 1;

    private final DefaultedList<ItemStack> items = DefaultedList.ofSize(4, ItemStack.EMPTY);

    public UpgradeableBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    @Override
    public void readNbt(NbtCompound nbt) {
        super.readNbt(nbt);

        Inventories.readNbt(nbt, items);
    }

    @Override
    protected void writeNbt(NbtCompound nbt) {
        Inventories.writeNbt(nbt, items);

        super.writeNbt(nbt);
    }

    @Override
    public int size() {
        return items.size();
    }

    @Override
    public boolean isEmpty() {
        for (ItemStack stack : items) {
            if (!stack.isEmpty())
                return false;
        }

        return true;
    }

    @Override
    public ItemStack getStack(int slot) {
        return items.get(slot);
    }

    @Override
    public ItemStack removeStack(int slot, int amount) {
        return removeStack(slot);
    }

    @Override
    public ItemStack removeStack(int slot) {
        ItemStack out = Inventories.removeStack(items, slot);

        if (!out.isEmpty()) {
            markDirty();

            updateUpgrades();
        }

        return out;
    }

    @Override
    public void setStack(int slot, ItemStack stack) {
        items.set(slot, stack);

        if (stack.getCount() > MAX_MODULES_IN_SLOT)
            stack.setCount(MAX_MODULES_IN_SLOT);

        markDirty();
        updateUpgrades();
    }

    @Override
    public boolean canPlayerUse(PlayerEntity player) {
        return true;
    }

    @Override
    public int getMaxCountPerStack() {
        return MAX_MODULES_IN_SLOT;
    }

    @Override
    public boolean isValid(int slot, ItemStack stack) {
        return stack.isIn(Items.UPGRADE_TAG);
    }

    @Override
    public void clear() {
        items.clear();
    }

    protected abstract void updateUpgrades();
}
