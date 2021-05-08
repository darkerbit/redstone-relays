package io.github.darkerbit.redstonerelays.network;

import io.github.darkerbit.redstonerelays.api.RelayTriggerCallback;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;

public class RelayTriggerHandler implements ServerPlayNetworking.PlayChannelHandler {
    @Override
    public void receive(MinecraftServer server, ServerPlayerEntity player, ServerPlayNetworkHandler handler, PacketByteBuf buf, PacketSender responseSender) {
        int triggeredRelay = buf.readInt();

        boolean pressed = triggeredRelay >= 0;

        // schedule trigger events for server thread
        server.execute(() -> {
            if (pressed) {
                RelayTriggerCallback.impl_trigger(triggeredRelay, player);
            } else {
                RelayTriggerCallback.impl_release((-triggeredRelay) - 1, player);
            }
        });
    }

    public static void register() {
        ServerPlayNetworking.registerGlobalReceiver(NetworkConstants.RELAY_TRIGGER_CHAN, new RelayTriggerHandler());
    }
}
