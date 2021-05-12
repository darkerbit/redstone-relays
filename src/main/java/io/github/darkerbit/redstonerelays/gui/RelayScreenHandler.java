package io.github.darkerbit.redstonerelays.gui;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.screen.ScreenHandler;

public class RelayScreenHandler extends ScreenHandler {
    public RelayScreenHandler(int syncId, PlayerInventory playerInventory) {
        super(null, syncId);
    }

    @Override
    public boolean canUse(PlayerEntity player) {
        return false;
    }
}
