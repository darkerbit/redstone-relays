package io.github.darkerbit.redstonerelays.block;

import io.github.darkerbit.redstonerelays.block.entity.ToggleRelayBlockEntity;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.util.math.BlockPos;

public class ToggleRelayBlock extends AbstractRelayBlock {
    public ToggleRelayBlock(Settings settings) {
        super(settings);
    }

    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new ToggleRelayBlockEntity(pos, state);
    }
}
