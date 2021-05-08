package io.github.darkerbit.redstonerelays.client;

import io.github.darkerbit.redstonerelays.RedstoneRelays;
import io.github.darkerbit.redstonerelays.network.NetworkConstants;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.options.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.network.PacketByteBuf;
import org.lwjgl.glfw.GLFW;

public final class RedstoneRelaysClient implements ClientModInitializer {
    private static final KeyBinding[] keyBindings = new KeyBinding[10];

    @Override
    public void onInitializeClient() {
        for (int i = 0; i < keyBindings.length; i++) {
            registerKeyBind(i, GLFW.GLFW_KEY_KP_0 + i); // This is a hack, the glfw scancodes aren't exactly guaranteed but it'll serve
        }

        ClientTickEvents.END_CLIENT_TICK.register(RedstoneRelaysClient::onEndClientTick);
    }

    private static void registerKeyBind(int num, int key) {
        keyBindings[num] = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key." + RedstoneRelays.MOD_ID + ".relay_" + num,
                InputUtil.Type.KEYSYM,
                key,
                "category." + RedstoneRelays.MOD_ID + ".triggers"
        ));
    }

    private static void onEndClientTick(MinecraftClient client) {
        for (int i = 0; i < keyBindings.length; i++) {
            while (keyBindings[i].wasPressed()) {
                PacketByteBuf buf = PacketByteBufs.create();
                buf.writeInt(i);

                ClientPlayNetworking.send(NetworkConstants.RELAY_TRIGGER_CHAN, buf);
            }
        }
    }
}
