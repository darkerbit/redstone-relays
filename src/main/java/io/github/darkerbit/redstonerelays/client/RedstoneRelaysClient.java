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

package io.github.darkerbit.redstonerelays.client;

import com.mojang.blaze3d.platform.InputUtil;
import io.github.darkerbit.redstonerelays.RedstoneRelays;
import io.github.darkerbit.redstonerelays.block.entity.BlockEntities;
import io.github.darkerbit.redstonerelays.client.gui.RelayScreen;
import io.github.darkerbit.redstonerelays.client.render.PulseRelayBlockEntityRenderer;
import io.github.darkerbit.redstonerelays.client.render.RelayBlockEntityRenderer;
import io.github.darkerbit.redstonerelays.gui.RelayScreenHandler;
import io.github.darkerbit.redstonerelays.network.RelayTriggerHandler;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.client.screenhandler.v1.ScreenRegistry;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBind;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactories;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.text.Text;
import org.lwjgl.glfw.GLFW;
import org.quiltmc.loader.api.ModContainer;
import org.quiltmc.qsl.base.api.entrypoint.client.ClientModInitializer;

@Environment(EnvType.CLIENT)
public final class RedstoneRelaysClient implements ClientModInitializer {
    private static final KeyBind[] keyBindings = new KeyBind[RedstoneRelays.KEY_COUNT];
    private static final boolean[] pressed = new boolean[keyBindings.length];

    @Override
    public void onInitializeClient(ModContainer mod) {
        for (int i = 0; i < keyBindings.length; i++) {
            registerKeyBind(i, GLFW.GLFW_KEY_KP_0 + i);
        }

        ClientTickEvents.END_CLIENT_TICK.register(RedstoneRelaysClient::onEndClientTick);

        ScreenRegistry.register(RelayScreenHandler.RELAY_SCREEN_HANDLER, RelayScreen::new);

        PulseRelayBlockEntityRenderer.register();

        BlockEntityRendererFactories.register(BlockEntities.PUSH_RELAY_ENTITY, RelayBlockEntityRenderer::create);
        BlockEntityRendererFactories.register(BlockEntities.TOGGLE_RELAY_ENTITY, RelayBlockEntityRenderer::create);
        BlockEntityRendererFactories.register(BlockEntities.PULSE_RELAY_ENTITY, PulseRelayBlockEntityRenderer::new);
    }

    public static Text getKeybindName(int number) {
        return keyBindings[number].getKeyName();
    }

    private static void registerKeyBind(int num, int key) {
        keyBindings[num] = KeyBindingHelper.registerKeyBinding(new KeyBind(
                RedstoneRelays.translationKey("key", "relay_" + num),
                InputUtil.Type.KEYSYM,
                key,
                RedstoneRelays.translationKey("category", "triggers")
        ));
    }

    private static void onEndClientTick(MinecraftClient client) {
        for (int i = 0; i < keyBindings.length; i++) {
            PacketByteBuf buf = PacketByteBufs.create();

            if (keyBindings[i].isPressed() && !pressed[i]) { // pressed
                buf.writeInt(i);

                ClientPlayNetworking.send(RelayTriggerHandler.RELAY_TRIGGER_CHAN, buf);
            } else if (!keyBindings[i].isPressed() && pressed[i]) { // released
                buf.writeInt((-i) - 1); // offset by -1 because -0 = 0

                ClientPlayNetworking.send(RelayTriggerHandler.RELAY_TRIGGER_CHAN, buf);
            }

            pressed[i] = keyBindings[i].isPressed();
        }
    }
}
