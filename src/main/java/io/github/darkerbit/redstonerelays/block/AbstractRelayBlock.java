package io.github.darkerbit.redstonerelays.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;

public abstract class AbstractRelayBlock extends Block {
    public static final BooleanProperty TRIGGERED = BooleanProperty.of("triggered");

    public AbstractRelayBlock(Settings settings) {
        super(settings);

        setDefaultState(getStateManager().getDefaultState().with(TRIGGERED, false));
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(TRIGGERED);
    }

    @Override
    public boolean emitsRedstonePower(BlockState state) {
        return true;
    }

    @Override
    public int getWeakRedstonePower(BlockState state, BlockView world, BlockPos pos, Direction direction) {
        return (Boolean) state.get(TRIGGERED) ? 15 : 0;
    }

    protected void setTriggered(World world, BlockState state, BlockPos pos, boolean triggered) {
        world.setBlockState(pos, state.with(TRIGGERED, triggered));
        world.updateNeighborsAlways(pos, state.getBlock());
    }
}
