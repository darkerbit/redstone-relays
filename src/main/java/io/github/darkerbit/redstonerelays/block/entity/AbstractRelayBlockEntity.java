package io.github.darkerbit.redstonerelays.block.entity;

import io.github.darkerbit.redstonerelays.RedstoneRelays;
import io.github.darkerbit.redstonerelays.api.ChunkUnloadListener;
import io.github.darkerbit.redstonerelays.api.RelayTriggerCallback;
import io.github.darkerbit.redstonerelays.block.AbstractRelayBlock;
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerFactory;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public abstract class AbstractRelayBlockEntity extends BlockEntity
        implements RelayTriggerCallback, ChunkUnloadListener, ExtendedScreenHandlerFactory {

    protected boolean triggered = false;
    protected int number = 0;

    protected String player = "";
    protected String playerName = "";

    private boolean registered = false;

    public AbstractRelayBlockEntity(BlockEntityType<?> type) {
        super(type);
    }

    @Override
    public void setLocation(World world, BlockPos pos) {
        super.setLocation(world, pos);

        if (!registered && !world.isClient) {
            RelayTriggerCallback.register(this);

            registered = true;
        }
    }

    @Override
    public void fromTag(BlockState state, CompoundTag tag) {
        super.fromTag(state, tag);

        triggered = tag.getBoolean("triggered");
        number = tag.getInt("number");
        player = tag.getString("player");
        playerName = tag.getString("playerName");
    }

    @Override
    public CompoundTag toTag(CompoundTag tag) {
        super.toTag(tag);

        tag.putBoolean("triggered", triggered);
        tag.putInt("number", number);
        tag.putString("player", player);
        tag.putString("playerName", playerName);

        return tag;
    }

    @Override
    public void onChunkUnload(ServerWorld world) {
        unregister();
    }

    @Override
    public void writeScreenOpeningData(ServerPlayerEntity player, PacketByteBuf buf) {
        buf.writeInt(number);
    }

    @Override
    public ScreenHandler createMenu(int syncId, PlayerInventory inv, PlayerEntity player) {
        return null;
    }

    @Override
    public Text getDisplayName() {
        return new TranslatableText(getCachedState().getBlock().getTranslationKey());
    }

    // Runs on the logical server only
    public void onUse(PlayerEntity player) {
        if (this.player.isEmpty())
            setPlayer(player);

        if (!this.player.equals(player.getUuidAsString())) {
            player.sendMessage(RedstoneRelays.translate("error", "wrong_player", this.playerName), true);
            return;
        }

        player.openHandledScreen(this);
    }

    public void setPlayer(PlayerEntity player) {
        this.player = player.getUuidAsString();
        this.playerName = player.getEntityName();

        markDirty();
    }

    public void unregister() {
        if (!registered) return;

        RelayTriggerCallback.unregister(this);
        registered = false;
    }

    protected boolean playerInRange(PlayerEntity player) {
        // check if the player is in the same dimension
        if (player.world.getRegistryKey() != world.getRegistryKey())
            return false;

        int range = world.getGameRules().getInt(RedstoneRelays.RELAY_RANGE_RULE);

        return pos.getSquaredDistance(player.getPos(), false) < range * range;
    }

    protected boolean playSounds() {
        BlockState state = getCachedState();
        Block block = state.getBlock();

        if (block instanceof AbstractRelayBlock)
            return ((AbstractRelayBlock) block).playSounds(world, state, pos);

        return false;
    }

    protected void setTriggered(boolean triggered) {
        BlockState state = getCachedState();
        Block block = state.getBlock();

        if (block instanceof AbstractRelayBlock)
            ((AbstractRelayBlock) block).setTriggered(world, state, pos, triggered);

        this.triggered = triggered;
        markDirty();
    }
}
