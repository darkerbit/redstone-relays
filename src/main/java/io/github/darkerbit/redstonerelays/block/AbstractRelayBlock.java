package io.github.darkerbit.redstonerelays.block;

import io.github.darkerbit.redstonerelays.block.entity.AbstractRelayBlockEntity;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;

public abstract class AbstractRelayBlock extends HorizontalFacingBlock implements BlockEntityProvider {
    public static final BooleanProperty TRIGGERED = BooleanProperty.of("triggered");

    private static final VoxelShape SHAPE = Block.createCuboidShape(0, 0, 0, 16, 6, 16);

    public AbstractRelayBlock(Settings settings) {
        super(settings);

        setDefaultState(getStateManager().getDefaultState()
                .with(TRIGGERED, false).with(Properties.HORIZONTAL_FACING, Direction.NORTH));
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
        builder.add(Properties.HORIZONTAL_FACING);
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

    public boolean playSounds(BlockView world, BlockState state, BlockPos pos) {
        return !(world.getBlockState(pos.down()).getMaterial() == Material.WOOL);
    }

    @Override
    public boolean emitsRedstonePower(BlockState state) {
        return true;
    }

    @Override
    public int getWeakRedstonePower(BlockState state, BlockView world, BlockPos pos, Direction direction) {
        return state.get(TRIGGERED) && direction != Direction.DOWN ? 15 : 0;
    }

    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        return this.getDefaultState().with(Properties.HORIZONTAL_FACING, ctx.getPlayerFacing().getOpposite());
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return SHAPE;
    }

    public void setTriggered(World world, BlockState state, BlockPos pos, boolean triggered) {
        world.setBlockState(pos, state.with(TRIGGERED, triggered));
        world.updateNeighborsAlways(pos, state.getBlock());
    }
}