package io.github.darkerbit.redstonerelays.block.entity;

import io.github.darkerbit.redstonerelays.RedstoneRelays;
import io.github.darkerbit.redstonerelays.api.RelayTriggerCallback;
import io.github.darkerbit.redstonerelays.block.AbstractRelayBlock;
import io.github.darkerbit.redstonerelays.gui.RelayScreenHandler;
import io.github.darkerbit.redstonerelays.item.Items;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.Packet;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.screen.PropertyDelegate;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.text.Text;
import net.minecraft.text.component.TranslatableComponent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public abstract class AbstractRelayBlockEntity extends UpgradeableBlockEntity
        implements RelayTriggerCallback, NamedScreenHandlerFactory {

    protected boolean triggered = false;
    protected int number = 0;

    protected int range = 0;

    protected String player = "";
    protected String playerName = "";

    protected Text customName;

    private boolean registered = false;

    public static final int RELAY_NUMBER_PROP = 0;
    public static final int RELAY_RANGE_PROP = 1;
    public static final int RELAY_PULSE_PROP = 2;
    private final PropertyDelegate propertyDelegate = new PropertyDelegate() {
        @Override
        public int get(int index) {
            return switch (index) {
                case RELAY_NUMBER_PROP -> number;
                case RELAY_RANGE_PROP -> range;
                case RELAY_PULSE_PROP -> getPulseLength();
                default -> 0;
            };
        }

        @Override
        public void set(int index, int value) {}

        @Override
        public int size() {
            return 3;
        }
    };

    public AbstractRelayBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    @Override
    public void setWorld(World world) {
        super.setWorld(world);

        if (!registered && !world.isClient) {
            RelayTriggerCallback.register(this);
            updateUpgrades();

            registered = true;
        }
    }

    @Override
    public void readNbt(NbtCompound tag) {
        super.readNbt(tag);

        triggered = tag.getBoolean("triggered");
        number = tag.getInt("number");
        player = tag.getString("player");
        playerName = tag.getString("playerName");

        if (tag.contains("customName")) {
            customName = Text.Serializer.fromJson(tag.getString("customName"));
        }
    }

    @Override
    public void writeNbt(NbtCompound tag) {
        super.writeNbt(tag);

        tag.putBoolean("triggered", triggered);
        tag.putInt("number", number);
        tag.putString("player", player);
        tag.putString("playerName", playerName);

        if (customName != null)
            tag.putString("customName", Text.Serializer.toJson(customName));
    }

    @Nullable
    @Override
    public Packet<ClientPlayPacketListener> toUpdatePacket() {
        return BlockEntityUpdateS2CPacket.of(this);
    }

    @Override
    public NbtCompound toSyncedNbt() {
        NbtCompound tag = new NbtCompound();
        writeNbt(tag);
        return tag;
    }

    @Override
    public boolean copyItemDataRequiresOperator() {
        return true;
    }

    @Override
    public ScreenHandler createMenu(int syncId, PlayerInventory inv, PlayerEntity player) {
        if (!this.player.equals(player.getUuidAsString()))
            return null;

        return new RelayScreenHandler(syncId, inv, this, propertyDelegate, ScreenHandlerContext.create(world, pos));
    }

    @Override
    public Text getDisplayName() {
        return this.customName != null
                ? this.customName
                : Text.translatable(getCachedState().getBlock().getTranslationKey());
    }

    @Override
    protected void updateUpgrades() {
        range = world.getGameRules().getInt(RedstoneRelays.RELAY_RANGE_RULE);

        range += count(Items.RANGE_UPGRADE) * 8;
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

    public void setCustomName(Text customName) {
        this.customName = customName;
        markDirty();
    }

    public boolean setNumber(PlayerEntity player, int number) {
        if (this.player.equals(player.getUuidAsString()) && number != this.number) {
            this.number = number;

            markDirty();
            sync();

            return true;
        }

        return false;
    }

    protected void sync() {
        world.updateListeners(pos, getCachedState(), getCachedState(), Block.NOTIFY_LISTENERS);
    }

    public int getNumber() {
        return this.number;
    }

    public int getPulseLength() { return -1; }

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

        Vec3d playerPos = player.getPos();
        return pos.getSquaredDistance(playerPos.x, playerPos.y, playerPos.z) < range * range;
    }

    protected boolean playSounds() {
        BlockState state = getCachedState();

        if (state.getBlock() instanceof AbstractRelayBlock relayBlock)
            return relayBlock.playSounds(world, state, pos);

        return false;
    }

    protected void setTriggered(boolean triggered) {
        BlockState state = getCachedState();

        if (state.getBlock() instanceof AbstractRelayBlock relayBlock)
            relayBlock.setTriggered(world, state, pos, triggered);

        this.triggered = triggered;
        markDirty();
    }

    public int getRedstoneLevel() {
        return 15;
    }
}
