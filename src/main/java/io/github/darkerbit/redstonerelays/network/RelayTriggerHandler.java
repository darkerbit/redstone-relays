package io.github.darkerbit.redstonerelays.network;

import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;

public class RelayTriggerHandler implements ServerPlayNetworking.PlayChannelHandler {
    @Override
    public void receive(MinecraftServer server, ServerPlayerEntity player, ServerPlayNetworkHandler handler, PacketByteBuf buf, PacketSender responseSender) {
    }

    public static void register() {
        ServerPlayNetworking.registerGlobalReceiver(NetworkConstants.RELAY_TRIGGER_CHAN, new RelayTriggerHandler());
    }
}
