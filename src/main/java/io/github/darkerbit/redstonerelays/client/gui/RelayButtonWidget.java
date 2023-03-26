package io.github.darkerbit.redstonerelays.client.gui;

import io.github.darkerbit.redstonerelays.RedstoneRelays;
import io.github.darkerbit.redstonerelays.client.RedstoneRelaysClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;

public class RelayButtonWidget extends ButtonWidget {
    private int number;
    private Text keybindName;

    public RelayButtonWidget(int x, int y, int width, int height, int number, int maxWidth, TextRenderer textRenderer, PressAction onPress) {
        super(x, y, width, height,
                Text.literal(textRenderer.trimToWidth(RedstoneRelaysClient.getKeybindName(number), maxWidth).getString()),
                onPress, null);

        this.number = number;
        this.keybindName = RedstoneRelaysClient.getKeybindName(number);
    }

    @Override
    protected MutableText getNarrationMessage() {
        return RedstoneRelays.translate("gui", "relay_screen_button_narrator", number, keybindName.getString());
    }

    public void unFocus() {
        setFocused(false);
    }
}
