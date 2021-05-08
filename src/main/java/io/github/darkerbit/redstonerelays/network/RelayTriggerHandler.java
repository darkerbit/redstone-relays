package io.github.darkerbit.redstonerelays.network;

import io.github.darkerbit.redstonerelays.RedstoneRelays;
import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class RelayTriggerHandler implements ServerPlayNetworking.PlayChannelHandler {
    private static final Logger LOGGER = LogManager.getLogger(RedstoneRelays.NAME);

    @Override
    public void receive(MinecraftServer server, ServerPlayerEntity player, ServerPlayNetworkHandler handler, PacketByteBuf buf, PacketSender responseSender) {
        int triggeredRelay = buf.readInt();

        boolean pressed = triggeredRelay > 0;

        // schedule trigger events for server thread
        server.execute(() -> {
            if (pressed) {
                RelayTriggerCallback.TRIGGER_PRESSED.invoker().trigger(triggeredRelay, player);
            } else {
                RelayTriggerCallback.TRIGGER_RELEASED.invoker().trigger(-triggeredRelay, player);
            }
        });
    }

    public static void register() {
        ServerPlayNetworking.registerGlobalReceiver(NetworkConstants.RELAY_TRIGGER_CHAN, new RelayTriggerHandler());

        RelayTriggerCallback.TRIGGER_PRESSED.register(
                (num, player) -> {
                    LOGGER.info("Player {} triggered relay {}", player.getEntityName(), num);
                }
        );

        RelayTriggerCallback.TRIGGER_RELEASED.register(
                (num, player) -> {
                    LOGGER.info("Player {} released relay {}", player.getEntityName(), num);
                }
        );
    }

    public interface RelayTriggerCallback {
        Event<RelayTriggerCallback> TRIGGER_PRESSED = EventFactory.createArrayBacked(RelayTriggerCallback.class,
                listeners -> (num, player) -> {
                    for (RelayTriggerCallback listener : listeners) {
                        listener.trigger(num, player);
                    }
                });

        Event<RelayTriggerCallback> TRIGGER_RELEASED = EventFactory.createArrayBacked(RelayTriggerCallback.class,
                listeners -> (num, player) -> {
                    for (RelayTriggerCallback listener : listeners) {
                        listener.trigger(num, player);
                    }
                });

        void trigger(int num, PlayerEntity player);
    }
}
