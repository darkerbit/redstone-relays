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

    static void trigger(int num, PlayerEntity player) {
        for (RelayTriggerCallback callback : callbacks) {
            callback.onTrigger(num, player);
        }
    }

    static void release(int num, PlayerEntity player) {
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
