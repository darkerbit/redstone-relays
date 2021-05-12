package io.github.darkerbit.redstonerelays.block.entity;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;

public class ToggleRelayBlockEntity extends AbstractRelayBlockEntity {
    public ToggleRelayBlockEntity() {
        super(BlockEntities.TOGGLE_RELAY_ENTITY);
    }

    @Override
    public void onTrigger(int num, PlayerEntity player) {
        if (num == number && player.getUuidAsString().equals(this.player) && playerInRange(player)) {
            setTriggered(!triggered);

            if (!playSounds()) return;

            float pitch = triggered ? 0.6f : 0.5f;

            world.playSound(
                    null,
                    pos,
                    SoundEvents.BLOCK_LEVER_CLICK,
                    SoundCategory.BLOCKS,
                    0.3f,
                    pitch
            );
        }
    }

    @Override
    public void onRelease(int num, PlayerEntity player) {}
}
