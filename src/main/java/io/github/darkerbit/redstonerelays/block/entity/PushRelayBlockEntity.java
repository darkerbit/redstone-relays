package io.github.darkerbit.redstonerelays.block.entity;

import io.github.darkerbit.redstonerelays.RedstoneRelays;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;

public class PushRelayBlockEntity extends AbstractRelayBlockEntity {
    public PushRelayBlockEntity() {
        super(BlockEntities.PUSH_RELAY_ENTITY);
    }

    @Override
    public void onTrigger(int num, PlayerEntity player) {
        if (!triggered && num == number && player.getUuidAsString().equals(this.player) && playerInRange(player)) {
            setTriggered(true);

            if (!playSounds()) return;

            world.playSound(
                    null,
                    pos,
                    SoundEvents.BLOCK_STONE_BUTTON_CLICK_ON,
                    SoundCategory.BLOCKS,
                    0.3f,
                    1.2f
            );
        }
    }

    @Override
    public void onRelease(int num, PlayerEntity player) {
        if (triggered && num == number && player.getUuidAsString().equals(this.player)) {
            setTriggered(false);

            if (!playSounds()) return;

            world.playSound(
                    null,
                    pos,
                    SoundEvents.BLOCK_STONE_BUTTON_CLICK_OFF,
                    SoundCategory.BLOCKS,
                    0.3f,
                    1.0f
            );
        }
    }
}
