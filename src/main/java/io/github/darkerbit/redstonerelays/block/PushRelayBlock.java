package io.github.darkerbit.redstonerelays.block;

import io.github.darkerbit.redstonerelays.block.entity.PushRelayBlockEntity;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.world.BlockView;

public class PushRelayBlock extends AbstractRelayBlock {
    public PushRelayBlock(Settings settings) {
        super(settings);
    }

    @Override
    public BlockEntity createBlockEntity(BlockView world) {
        return new PushRelayBlockEntity();
    }
}
