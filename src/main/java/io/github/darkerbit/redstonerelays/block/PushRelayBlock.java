package io.github.darkerbit.redstonerelays.block;

import io.github.darkerbit.redstonerelays.block.entity.PushRelayBlockEntity;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.util.math.BlockPos;

public class PushRelayBlock extends AbstractRelayBlock {
    public PushRelayBlock(Settings settings) {
        super(settings);
    }

    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new PushRelayBlockEntity(pos, state);
    }
}
