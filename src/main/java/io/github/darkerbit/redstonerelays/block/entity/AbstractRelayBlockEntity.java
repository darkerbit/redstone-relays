package io.github.darkerbit.redstonerelays.block.entity;

import io.github.darkerbit.redstonerelays.api.ChunkUnloadListener;
import io.github.darkerbit.redstonerelays.api.RelayTriggerCallback;
import io.github.darkerbit.redstonerelays.block.AbstractRelayBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.world.ServerWorld;

public abstract class AbstractRelayBlockEntity extends BlockEntity implements RelayTriggerCallback, ChunkUnloadListener {
    protected boolean triggered = false;
    protected int number = 0;

    protected String player = "";

    private boolean registered = true;

    public AbstractRelayBlockEntity(BlockEntityType<?> type) {
        super(type);

        RelayTriggerCallback.register(this);
    }

    @Override
    public void fromTag(BlockState state, CompoundTag tag) {
        super.fromTag(state, tag);

        triggered = tag.getBoolean("triggered");
        number = tag.getInt("number");
        player = tag.getString("player");
    }

    @Override
    public CompoundTag toTag(CompoundTag tag) {
        super.toTag(tag);

        tag.putBoolean("triggered", triggered);
        tag.putInt("number", number);
        tag.putString("player", player);

        return tag;
    }

    @Override
    public void onChunkUnload(ServerWorld world) {
        unregister();
    }

    public void setPlayer(PlayerEntity player) {
        this.player = player.getUuidAsString();
    }

    public void unregister() {
        if (!registered) return;

        RelayTriggerCallback.unregister(this);
        registered = false;
    }

    protected void setTriggered(boolean triggered) {
        BlockState state = world.getBlockState(pos);
        Block block = state.getBlock();

        if (block instanceof AbstractRelayBlock)
            ((AbstractRelayBlock) block).setTriggered(world, state, pos, triggered);

        this.triggered = triggered;
        markDirty();
    }
}
