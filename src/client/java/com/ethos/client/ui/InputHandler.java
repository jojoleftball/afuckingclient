package com.ethos.client.ui;

import com.ethos.client.hacks.HackManager;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import org.lwjgl.glfw.GLFW;

public class InputHandler {
    private static boolean prevShift = false;

    public static void register() {
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            boolean down = GLFW.glfwGetKey(client.getWindow().handle(), GLFW.GLFW_KEY_RIGHT_SHIFT) == GLFW.GLFW_PRESS;
            if (down && !prevShift) {
                MenuScreen.toggle();
            }
            prevShift = down;
        });

        // render the HUD
        @SuppressWarnings("deprecation")
        HudRenderCallback callback = (guiGraphics, tickDelta) -> renderHud(guiGraphics);
        HudRenderCallback.EVENT.register(callback);
    }

    private static void renderHud(GuiGraphics guiGraphics) {
        Minecraft client = Minecraft.getInstance();
        int y = 2;
        // include client title
        String title = "Ethos";
        y += 10; // reserve for title
        // compute total height for background
        for (com.ethos.client.hacks.Hack hack : HackManager.getHacks()) {
            if (hack.isEnabled()) {
                y += 10;
            }
        }
        // draw semi‑transparent black background
        if (y > 2) {
            guiGraphics.fill(0, 0, 80, y + 2, 0xAA000000);
        }
        // reset y to start drawing text
        y = 2;
        guiGraphics.drawString(client.font, title, 2, y, 0xFFFFC0CB);
        y += 10;
        for (com.ethos.client.hacks.Hack hack : HackManager.getHacks()) {
            if (hack.isEnabled()) {
                guiGraphics.drawString(client.font, hack.getName(), 2, y, 0xFFFFC0CB);
                y += 10;
            }
        }
    }
}