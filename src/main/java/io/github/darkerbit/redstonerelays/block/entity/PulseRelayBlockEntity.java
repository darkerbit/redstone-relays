package io.github.darkerbit.redstonerelays.block.entity;

import io.github.darkerbit.redstonerelays.block.PulseRelayBlock;
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
    public void fromClientTag(NbtCompound tag) {
        super.fromClientTag(tag);

        level = tag.getInt("level");
    }

    @Override
    public NbtCompound toClientTag(NbtCompound input) {
        NbtCompound tag = super.toClientTag(input);

        tag.putInt("level", level);

        return tag;
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
    private final Animator animator = new Animator();

    @Environment(EnvType.CLIENT)
    public Animator getAnimator() {
        return animator;
    }

    @Environment(EnvType.CLIENT)
    public static void clientTick(World world, BlockPos pos, BlockState state, PulseRelayBlockEntity blockEntity) {
        blockEntity.animator.tick();
    }

    @Environment(EnvType.CLIENT)
    public class Animator {
        private static final float MAX_ROTATION = 288.0f;

        private float rotation = 0.0f;
        private float lastRotation = rotation;

        private int counter = 0;

        private void tick() {
            lastRotation = rotation;

            if (level > 0) {
                float factor = (float) counter / (15.0f * PulseRelayBlock.DELAY);

                rotation = MathHelper.lerp(factor, 0.0f, MAX_ROTATION);

                counter++;
            }
        }

        public float getRotation(float tickDelta) {
            return MathHelper.lerp(tickDelta, lastRotation, rotation);
        }
    }
}
