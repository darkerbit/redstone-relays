package io.github.darkerbit.redstonerelays.api;

import net.minecraft.block.entity.BlockEntity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.chunk.WorldChunk;

/**
 * Callback for when a BlockEntity is unloaded from the world.
 */
public interface ChunkUnloadListener {
    static void impl_onWorldUnload(MinecraftServer server, ServerWorld world) {
        for (BlockEntity blockEntity : world.blockEntities) {
            if (blockEntity instanceof ChunkUnloadListener)
                ((ChunkUnloadListener) blockEntity).onChunkUnload(world);
        }
    }

    static void impl_onChunkUnload(ServerWorld world, WorldChunk chunk) {
        for (BlockEntity blockEntity : chunk.getBlockEntities().values()) {
            if (blockEntity instanceof ChunkUnloadListener)
                ((ChunkUnloadListener) blockEntity).onChunkUnload(world);
        }
    }

    /**
     * Called when this BlockEntity is unloaded from the world.
     * @param world The ServerWorld the BlockEntity is being unloaded from.
     */
    void onChunkUnload(ServerWorld world);
}
