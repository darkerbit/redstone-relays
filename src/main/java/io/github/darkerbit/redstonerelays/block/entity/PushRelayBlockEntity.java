package io.github.darkerbit.redstonerelays.block.entity;

import net.minecraft.entity.player.PlayerEntity;

public class PushRelayBlockEntity extends AbstractRelayBlockEntity {
    public PushRelayBlockEntity() {
        super(BlockEntities.PUSH_RELAY_ENTITY);
    }

    @Override
    public void onTrigger(int num, PlayerEntity player) {
        if (num == number && player.getUuidAsString().equals(this.player))
            setTriggered(true);
    }

    @Override
    public void onRelease(int num, PlayerEntity player) {
        if (num == number && player.getUuidAsString().equals(this.player))
            setTriggered(false);
    }
}
