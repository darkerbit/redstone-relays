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

public class ToggleRelayBlockEntity extends AbstractRelayBlockEntity {
    public ToggleRelayBlockEntity(BlockPos pos, BlockState state) {
        super(BlockEntities.TOGGLE_RELAY_ENTITY, pos, state);
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
