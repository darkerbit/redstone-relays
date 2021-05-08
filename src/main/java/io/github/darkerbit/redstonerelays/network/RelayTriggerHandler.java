package io.github.darkerbit.redstonerelays.network;

import io.github.darkerbit.redstonerelays.RedstoneRelays;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class RelayTriggerHandler implements ServerPlayNetworking.PlayChannelHandler {
    private static final Logger LOGGER = LogManager.getLogger(RedstoneRelays.NAME);

    @Override
    public void receive(MinecraftServer server, ServerPlayerEntity player, ServerPlayNetworkHandler handler, PacketByteBuf buf, PacketSender responseSender) {
        int triggeredRelay = buf.readInt();

        LOGGER.info("Triggered relay {}", triggeredRelay);
    }

    public static void register() {
        ServerPlayNetworking.registerGlobalReceiver(NetworkConstants.RELAY_TRIGGER_CHAN, new RelayTriggerHandler());
    }
}
