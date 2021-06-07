package io.github.darkerbit.redstonerelays.block.entity;

import io.github.darkerbit.redstonerelays.block.PulseRelayBlock;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;

public class PulseRelayBlockEntity extends AbstractRelayBlockEntity {
    private int level = 0;

    public PulseRelayBlockEntity(BlockPos pos, BlockState state) {
        super(BlockEntities.PULSE_RELAY_ENTITY, pos, state);
    }

    @Override
    public void onTrigger(int num, PlayerEntity player) {
        level = 15;

        BlockState state = getCachedState();
        if (state.getBlock() instanceof PulseRelayBlock block)
            block.trigger(world, state, pos);
    }

    @Override
    public void onRelease(int num, PlayerEntity player) {}

    @Override
    public int getRedstoneLevel() {
        return level;
    }

    public void step() {
        level--;
    }

    public void onComplete() {}
}
