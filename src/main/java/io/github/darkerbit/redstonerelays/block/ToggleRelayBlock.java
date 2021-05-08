package io.github.darkerbit.redstonerelays.block;

import io.github.darkerbit.redstonerelays.block.entity.ToggleRelayBlockEntity;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.world.BlockView;

public class ToggleRelayBlock extends AbstractRelayBlock {
    public ToggleRelayBlock(Settings settings) {
        super(settings);
    }

    @Override
    public BlockEntity createBlockEntity(BlockView world) {
        return new ToggleRelayBlockEntity();
    }
}
