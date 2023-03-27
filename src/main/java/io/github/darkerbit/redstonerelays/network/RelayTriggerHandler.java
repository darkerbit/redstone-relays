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

package io.github.darkerbit.redstonerelays.network;

import io.github.darkerbit.redstonerelays.RedstoneRelays;
import io.github.darkerbit.redstonerelays.api.RelayTriggerCallback;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;

public class RelayTriggerHandler implements ServerPlayNetworking.PlayChannelHandler {
    public static final Identifier RELAY_TRIGGER_CHAN = RedstoneRelays.identifier("relay_trigger");

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
        ServerPlayNetworking.registerGlobalReceiver(RELAY_TRIGGER_CHAN, new RelayTriggerHandler());
    }
}
