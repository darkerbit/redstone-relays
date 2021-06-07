package io.github.darkerbit.redstonerelays.block;

import io.github.darkerbit.redstonerelays.block.entity.PulseRelayBlockEntity;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.Random;

public class PulseRelayBlock extends AbstractRelayBlock {
    private static final int DELAY = 2;

    public PulseRelayBlock(Settings settings) {
        super(settings);
    }

    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new PulseRelayBlockEntity(pos, state);
    }

    public void trigger(World world, BlockState state, BlockPos pos) {
        setTriggered(world, state, pos, true);

        world.getBlockTickScheduler().schedule(pos, this, DELAY);
    }

    @Override
    public void scheduledTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
        if (world.getBlockEntity(pos) instanceof PulseRelayBlockEntity blockEntity) {
            boolean cont = blockEntity.getRedstoneLevel() > 0;

            setTriggered(world, state, pos, cont);

            if (cont) {
                blockEntity.step();
                world.getBlockTickScheduler().schedule(pos, this, DELAY);
            } else {
                blockEntity.onComplete();
            }
        }
    }
}
