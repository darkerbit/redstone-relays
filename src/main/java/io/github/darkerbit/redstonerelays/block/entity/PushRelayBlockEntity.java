/*
 * Copyright (c) 2023 darkerbit
 *
 * This software is provided 'as-is', without any express or implied
 * warranty. In no event will the authors be held liable for any damages
 * arising from the use of this software.
 *
 * Permission is granted to anyone to use this software for any purpose,
 * including commercial applications, and to alter it and redistribute it
 * freely, subject to the following restrictions:
 *
 * 1. The origin of this software must not be misrepresented; you must not
 *    claim that you wrote the original software. If you use this software
 *    in a product, an acknowledgment in the product documentation would be
 *    appreciated but is not required.
 * 2. Altered source versions must be plainly marked as such, and must not be
 *    misrepresented as being the original software.
 * 3. This notice may not be removed or altered from any source distribution.
 */

package io.github.darkerbit.redstonerelays.block.entity;

import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.BlockPos;

public class PushRelayBlockEntity extends AbstractRelayBlockEntity {
    public PushRelayBlockEntity(BlockPos pos, BlockState state) {
        super(BlockEntities.PUSH_RELAY_ENTITY, pos, state);
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
