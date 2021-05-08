package io.github.darkerbit.redstonerelays.api;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.entity.player.PlayerEntity;

/**
 * A callback triggered on press or release of a Redstone Relays trigger button.
 */
public interface RelayTriggerCallback {
    /**
     * Event triggered on press of a trigger button.
     */
    Event<RelayTriggerCallback> TRIGGER_PRESSED = EventFactory.createArrayBacked(RelayTriggerCallback.class,
            listeners -> (num, player) -> {
                for (RelayTriggerCallback listener : listeners) {
                    listener.trigger(num, player);
                }
            });

    /**
     * Event triggered on release of a trigger button.
     */
    Event<RelayTriggerCallback> TRIGGER_RELEASED = EventFactory.createArrayBacked(RelayTriggerCallback.class,
            listeners -> (num, player) -> {
                for (RelayTriggerCallback listener : listeners) {
                    listener.trigger(num, player);
                }
            });

    /**
     * Callback triggered on press or release of a trigger button.
     * @param num The number of the trigger that was pressed or released
     * @param player The player who pressed or released the trigger
     */
    void trigger(int num, PlayerEntity player);
}
