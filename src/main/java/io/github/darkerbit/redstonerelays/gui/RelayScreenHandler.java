package io.github.darkerbit.redstonerelays.gui;

import io.github.darkerbit.redstonerelays.RedstoneRelays;
import io.github.darkerbit.redstonerelays.block.entity.AbstractRelayBlockEntity;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.screenhandler.v1.ScreenHandlerRegistry;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.screen.ArrayPropertyDelegate;
import net.minecraft.screen.PropertyDelegate;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerType;

public class RelayScreenHandler extends ScreenHandler {
    public static final ScreenHandlerType<RelayScreenHandler> RELAY_SCREEN_HANDLER
            = ScreenHandlerRegistry.registerSimple(RedstoneRelays.identifier("relay_screen"), RelayScreenHandler::new);

    private AbstractRelayBlockEntity blockEntity;

    private PropertyDelegate propertyDelegate;

    // server constructor
    public RelayScreenHandler(int syncId, PlayerInventory playerInventory, AbstractRelayBlockEntity blockEntity, PropertyDelegate propertyDelegate) {
        this(syncId, playerInventory, propertyDelegate);

        this.blockEntity = blockEntity;
    }

    // client constructor
    @Environment(EnvType.CLIENT)
    public RelayScreenHandler(int syncId, PlayerInventory playerInventory) {
        this(syncId, playerInventory, new ArrayPropertyDelegate(1));
    }

    // PropertyDelegate constructor
    public RelayScreenHandler(int syncId, PlayerInventory inv, PropertyDelegate propertyDelegate) {
        super(RELAY_SCREEN_HANDLER, syncId);

        this.propertyDelegate = propertyDelegate;
        addProperties(propertyDelegate);
    }

    public int getRelayNumber() {
        return propertyDelegate.get(AbstractRelayBlockEntity.RELAY_NUMBER_PROP);
    }

    @Override
    public boolean canUse(PlayerEntity player) {
        return true; // TODO: Add range calculation
    }
}
