package io.github.darkerbit.redstonerelays.block.entity;

import io.github.darkerbit.redstonerelays.block.PulseRelayBlock;
import io.github.darkerbit.redstonerelays.item.Items;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

public class PulseRelayBlockEntity extends AbstractRelayBlockEntity {
    private int level = 0;

    private int pulseLength = 2;

    public PulseRelayBlockEntity(BlockPos pos, BlockState state) {
        super(BlockEntities.PULSE_RELAY_ENTITY, pos, state);
    }

    @Override
    public void onTrigger(int num, PlayerEntity player) {
        if (num == number && player.getUuidAsString().equals(this.player) && playerInRange(player)) {
            level = 15;
            sync();

            BlockState state = getCachedState();
            if (state.getBlock() instanceof PulseRelayBlock block)
                block.trigger(world, state, pos);

            if (!playSounds())
                return;

            world.playSound(
                    null,
                    pos,
                    SoundEvents.BLOCK_LEVER_CLICK,
                    SoundCategory.BLOCKS,
                    0.3f,
                    1.3f
            );
        }
    }

    @Override
    public void onRelease(int num, PlayerEntity player) {}

    @Override
    public int getRedstoneLevel() {
        return level;
    }

    @Override
    public void readNbt(NbtCompound tag) {
        super.readNbt(tag);

        level = tag.getInt("level");
    }

    @Override
    public void writeNbt(NbtCompound tag) {
        super.writeNbt(tag);

        tag.putInt("level", level);
    }

    @Override
    protected void updateUpgrades() {
        super.updateUpgrades();

        pulseLength = 2;

        pulseLength += count(Items.PULSE_UPGRADE);
    }

    @Override
    public int getPulseLength() {
        return pulseLength;
    }

    public void step() {
        level--;

        if (!playSounds())
            return;

        world.playSound(
                null,
                pos,
                SoundEvents.BLOCK_STONE_BUTTON_CLICK_ON,
                SoundCategory.BLOCKS,
                0.1f,
                1.5f
        );
    }

    public void onComplete() {
        sync();

        if (playSounds()) {
            world.playSound(
                    null,
                    pos,
                    SoundEvents.BLOCK_LEVER_CLICK,
                    SoundCategory.BLOCKS,
                    0.3f,
                    1.1f
            );
        }
    }

    @Environment(EnvType.CLIENT)
    public int getRenderOrientation() {
        return switch (getCachedState().get(PulseRelayBlock.FACING)) {
            case NORTH, UP, DOWN -> 0;
            case EAST -> 90;
            case SOUTH -> 180;
            case WEST -> 270;
        };
    }

    @Environment(EnvType.CLIENT)
    private final Animator animator = new Animator();

    @Environment(EnvType.CLIENT)
    public Animator getAnimator() {
        return animator;
    }

    @Environment(EnvType.CLIENT)
    public static void clientTick(World world, BlockPos pos, BlockState state, PulseRelayBlockEntity blockEntity) {
        blockEntity.animator.tick();
    }

    public class Animator {
        private static final float MAX_ROTATION = 288.0f;

        private float rotation = 0.0f;
        private float lastRotation = rotation;

        private int counter = 0;

        @Environment(EnvType.CLIENT)
        private void tick() {
            lastRotation = rotation;

            if (level > 0) {
                float factor = (float) counter / (15.0f * getPulseLength());

                rotation = MathHelper.lerp(factor, 0.0f, MAX_ROTATION);

                counter++;
            }
        }

        @Environment(EnvType.CLIENT)
        public float getRotation(float tickDelta) {
            return MathHelper.lerp(tickDelta, lastRotation, rotation);
        }
    }
}
