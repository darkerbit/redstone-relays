package io.github.darkerbit.redstonerelays.block;

import io.github.darkerbit.redstonerelays.block.entity.AbstractRelayBlockEntity;
import net.minecraft.block.Block;
import net.minecraft.block.BlockEntityProvider;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;

public abstract class AbstractRelayBlock extends Block implements BlockEntityProvider {
    public static final BooleanProperty TRIGGERED = BooleanProperty.of("triggered");

    public AbstractRelayBlock(Settings settings) {
        super(settings);

        setDefaultState(getStateManager().getDefaultState().with(TRIGGERED, false));
    }

    @Override
    public void onPlaced(World world, BlockPos pos, BlockState state, LivingEntity placer, ItemStack itemStack) {
        if (placer instanceof PlayerEntity) {
            BlockEntity blockEntity = world.getBlockEntity(pos);

            if (blockEntity instanceof AbstractRelayBlockEntity)
                ((AbstractRelayBlockEntity) blockEntity).setPlayer((PlayerEntity) placer);
        }
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(TRIGGERED);
    }

    @Override
    public void onStateReplaced(BlockState state, World world, BlockPos pos, BlockState newState, boolean moved) {
        if (!state.isOf(newState.getBlock())) {
            BlockEntity blockEntity = world.getBlockEntity(pos);

            if (blockEntity instanceof AbstractRelayBlockEntity) {
                ((AbstractRelayBlockEntity) blockEntity).unregister();
            }
        }

        super.onStateReplaced(state, world, pos, newState, moved);
    }



    @Override
    public boolean emitsRedstonePower(BlockState state) {
        return true;
    }

    @Override
    public int getWeakRedstonePower(BlockState state, BlockView world, BlockPos pos, Direction direction) {
        return (Boolean) state.get(TRIGGERED) ? 15 : 0;
    }

    public void setTriggered(World world, BlockState state, BlockPos pos, boolean triggered) {
        world.setBlockState(pos, state.with(TRIGGERED, triggered));
        world.updateNeighborsAlways(pos, state.getBlock());
    }
}
