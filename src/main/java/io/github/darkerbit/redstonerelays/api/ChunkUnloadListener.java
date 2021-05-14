package io.github.darkerbit.redstonerelays.api;

import net.minecraft.block.entity.BlockEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.chunk.WorldChunk;

/**
 * Callback for when a BlockEntity is unloaded from the world.
 */
public interface ChunkUnloadListener {
    static void impl_onChunkUnload(ServerWorld world, WorldChunk chunk) {
        for (BlockEntity blockEntity : chunk.getBlockEntities().values()) {
            if (blockEntity instanceof ChunkUnloadListener listener)
                listener.onChunkUnload(world);
        }
    }

    /**
     * Called when this BlockEntity is unloaded from the world due to a chunk unload.
     * @param world The ServerWorld the BlockEntity is being unloaded from.
     */
    void onChunkUnload(ServerWorld world);
}
