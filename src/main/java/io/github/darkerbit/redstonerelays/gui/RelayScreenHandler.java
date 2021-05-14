package io.github.darkerbit.redstonerelays.gui;

import io.github.darkerbit.redstonerelays.RedstoneRelays;
import io.github.darkerbit.redstonerelays.block.AbstractRelayBlock;
import io.github.darkerbit.redstonerelays.block.entity.AbstractRelayBlockEntity;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.screenhandler.v1.ScreenHandlerRegistry;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.screen.*;

public class RelayScreenHandler extends ScreenHandler {
    public static final ScreenHandlerType<RelayScreenHandler> RELAY_SCREEN_HANDLER
            = ScreenHandlerRegistry.registerSimple(RedstoneRelays.identifier("relay_screen"), RelayScreenHandler::new);

    private AbstractRelayBlockEntity blockEntity;
    private ScreenHandlerContext context;

    private PropertyDelegate propertyDelegate;

    // server constructor
    public RelayScreenHandler(int syncId, PlayerInventory playerInventory, AbstractRelayBlockEntity blockEntity,
                              PropertyDelegate propertyDelegate, ScreenHandlerContext context) {
        this(syncId, playerInventory, propertyDelegate, context);

        this.blockEntity = blockEntity;
    }

    // client constructor
    public RelayScreenHandler(int syncId, PlayerInventory playerInventory) {
        this(syncId, playerInventory, new ArrayPropertyDelegate(1), ScreenHandlerContext.EMPTY);
    }

    // PropertyDelegate constructor
    public RelayScreenHandler(int syncId, PlayerInventory inv, PropertyDelegate propertyDelegate, ScreenHandlerContext context) {
        super(RELAY_SCREEN_HANDLER, syncId);

        this.propertyDelegate = propertyDelegate;
        addProperties(propertyDelegate);

        this.context = context;
    }

    public int getRelayNumber() {
        return propertyDelegate.get(AbstractRelayBlockEntity.RELAY_NUMBER_PROP);
    }

    @Override
    public boolean canUse(PlayerEntity player) {
        // TODO: Add range calculation
        return context.run(
                (world, pos) -> world.getBlockState(pos).getBlock() instanceof AbstractRelayBlock,
                true
        );
    }

    @Override
    public boolean onButtonClick(PlayerEntity player, int id) {
        return blockEntity.setNumber(player, id);
    }
}
