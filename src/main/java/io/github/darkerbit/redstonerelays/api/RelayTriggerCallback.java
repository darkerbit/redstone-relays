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

package io.github.darkerbit.redstonerelays.api;

import net.minecraft.entity.player.PlayerEntity;

import java.util.ArrayList;
import java.util.List;

/**
 * A callback triggered on trigger or release of a relay button.
 */
public interface RelayTriggerCallback {
    List<RelayTriggerCallback> callbacks = new ArrayList<>();

    /**
     * Registers a new callback.
     * @param callback The callback to register
     */
    static void register(RelayTriggerCallback callback) {
        callbacks.add(callback);
    }

    /**
     * Removes a callback.
     * @param callback The callback to remove
     */
    static void unregister(RelayTriggerCallback callback) {
        callbacks.remove(callback);
    }

    static void impl_trigger(int num, PlayerEntity player) {
        for (RelayTriggerCallback callback : callbacks) {
            callback.onTrigger(num, player);
        }
    }

    static void impl_release(int num, PlayerEntity player) {
        for (RelayTriggerCallback callback : callbacks) {
            callback.onRelease(num, player);
        }
    }

    /**
     * Called on trigger of a relay.
     * @param num The number of the triggered relay
     * @param player The player who triggered the relay
     */
    void onTrigger(int num, PlayerEntity player);

    /**
     * Called on release of a relay.
     * @param num The number of the triggered relay
     * @param player The player who triggered the relay
     */
    void onRelease(int num, PlayerEntity player);
}
