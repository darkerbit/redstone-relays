package io.github.darkerbit.redstonerelays.gui;

import io.github.darkerbit.redstonerelays.RedstoneRelays;
import io.github.darkerbit.redstonerelays.block.entity.AbstractRelayBlockEntity;
import net.fabricmc.fabric.api.screenhandler.v1.ScreenHandlerRegistry;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.screen.PropertyDelegate;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerType;

public class RelayScreenHandler extends ScreenHandler {
    public static final ScreenHandlerType<RelayScreenHandler> RELAY_SCREEN_HANDLER
            = ScreenHandlerRegistry.registerSimple(RedstoneRelays.identifier("relay_screen"), RelayScreenHandler::new);

    private AbstractRelayBlockEntity blockEntity;

    // server constructor
    public RelayScreenHandler(int syncId, PlayerInventory playerInventory, AbstractRelayBlockEntity blockEntity, PropertyDelegate propertyDelegate) {
        this(syncId, playerInventory);

        this.blockEntity = blockEntity;

        addProperties(propertyDelegate);
    }

    // client constructor
    public RelayScreenHandler(int syncId, PlayerInventory playerInventory) {
        super(RELAY_SCREEN_HANDLER, syncId);
    }

    @Override
    public boolean canUse(PlayerEntity player) {
        return true; // TODO: Add range calculation
    }
}
