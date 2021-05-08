package io.github.darkerbit.redstonerelays.block.entity;

import net.minecraft.entity.player.PlayerEntity;

public class ToggleRelayBlockEntity extends AbstractRelayBlockEntity {
    public ToggleRelayBlockEntity() {
        super(BlockEntities.TOGGLE_RELAY_ENTITY);
    }

    @Override
    public void onTrigger(int num, PlayerEntity player) {
        if (num == number && player.getUuidAsString().equals(this.player))
            setTriggered(!triggered);
    }

    @Override
    public void onRelease(int num, PlayerEntity player) {}
}
