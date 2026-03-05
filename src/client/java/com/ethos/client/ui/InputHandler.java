package com.ethos.client.ui;

import com.ethos.client.hacks.HackManager;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
// import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderContext;
// import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderEvents;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import org.lwjgl.glfw.GLFW;

import java.util.ArrayList;
import java.util.List;

public class InputHandler {
    private static boolean prevShift = false;

    // Modern clean color palette
    private static final int PRIMARY_COLOR = 0xFF00D4FF;      // Bright cyan
    private static final int SECONDARY_COLOR = 0xFFFF2D7A;    // Modern pink
    private static final int BG_COLOR = 0xEE0F1419;          // Dark background
    private static final int BORDER_COLOR = 0xFF2A2D32;      // Border color
    private static final int TEXT_ENABLED = 0xFFFF2D7A;      // Pink text
    private static final int TEXT_TITLE = 0xFFFFFFFF;        // White

    public static void register() {
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            // Handle menu toggle
            boolean down = GLFW.glfwGetKey(client.getWindow().handle(), GLFW.GLFW_KEY_RIGHT_SHIFT) == GLFW.GLFW_PRESS;
            if (down && !prevShift) {
                MenuScreen.toggle();
            }
            prevShift = down;

            // Handle hack keybinds
            handleHackKeybinds(client);
        });

        // Render the enhanced HUD
        @SuppressWarnings("deprecation")
        HudRenderCallback callback = (guiGraphics, tickDelta) -> renderEnhancedHud(guiGraphics);
        registerHudCallback(callback);

        // Render ESP in world
        // WorldRenderEvents.AFTER_ENTITIES.register(InputHandler::renderESP);
    }

    @SuppressWarnings("deprecation")
    private static void registerHudCallback(HudRenderCallback callback) {
        HudRenderCallback.EVENT.register(callback);
    }

    /**
     * Renders an enhanced and visually appealing HUD
     */
    private static void renderEnhancedHud(GuiGraphics guiGraphics) {
        Minecraft client = Minecraft.getInstance();
        
        // Get enabled hacks
        List<com.ethos.client.hacks.Hack> enabledHacks = new ArrayList<>();
        for (com.ethos.client.hacks.Hack hack : HackManager.getHacks()) {
            if (hack.isEnabled()) {
                enabledHacks.add(hack);
            }
        }

        if (enabledHacks.isEmpty()) {
            return; // Don't render HUD if no hacks are enabled
        }

        // HUD positioning - more compact
        int hudX = 6;
        int hudY = 6;
        int hudWidth = 120;
        int lineHeight = 12;
        int headerHeight = 18;
        int totalHeight = headerHeight + enabledHacks.size() * lineHeight + 6;

        // Draw HUD background with clean styling
        drawHudBackground(guiGraphics, hudX, hudY, hudWidth, totalHeight);

        // Draw header
        drawHudHeader(guiGraphics, client, hudX, hudY, hudWidth);

        // Draw enabled hacks
        int contentY = hudY + headerHeight + 3;
        for (com.ethos.client.hacks.Hack hack : enabledHacks) {
            drawHackEntry(guiGraphics, client, hudX, contentY, hudWidth, hack);
            contentY += lineHeight;
        }
    }

    /**
     * Draws the HUD background with clean styling
     */
    private static void drawHudBackground(GuiGraphics guiGraphics, int x, int y, int width, int height) {
        // Draw subtle shadow
        guiGraphics.fill(x + 1, y + height, x + width + 1, y + height + 1, 0x22000000);
        guiGraphics.fill(x + width, y + 1, x + width + 1, y + height, 0x22000000);

        // Draw main background
        guiGraphics.fill(x, y, x + width, y + height, BG_COLOR);

        // Draw clean borders
        guiGraphics.fill(x, y, x + width, y + 1, BORDER_COLOR); // Top
        guiGraphics.fill(x, y + height - 1, x + width, y + height, BORDER_COLOR); // Bottom
        guiGraphics.fill(x, y, x + 1, y + height, BORDER_COLOR); // Left
        guiGraphics.fill(x + width - 1, y, x + width, y + height, BORDER_COLOR); // Right
    }

    /**
     * Draws the HUD header with title
     */
    private static void drawHudHeader(GuiGraphics guiGraphics, Minecraft client, int x, int y, int width) {
        // Draw header background with gradient
        drawGradientRect(guiGraphics, x + 1, y + 1, x + width - 1, y + 16, PRIMARY_COLOR, SECONDARY_COLOR);

        // Draw title
        String title = "⚡ HACKS";
        int titleX = x + 4;
        int titleY = y + 5;
        guiGraphics.drawString(client.font, title, titleX, titleY, TEXT_TITLE, false);
    }

    /**
     * Draws a single hack entry in the HUD
     */
    private static void drawHackEntry(GuiGraphics guiGraphics, Minecraft client, int x, int y, int width, com.ethos.client.hacks.Hack hack) {
        // Draw status indicator (smaller dot)
        guiGraphics.fill(x + 4, y + 2, x + 6, y + 8, TEXT_ENABLED);

        // Draw hack name
        String hackName = hack.getName();
        guiGraphics.drawString(client.font, hackName, x + 10, y + 1, TEXT_ENABLED, false);
    }

    /**
     * Draws a gradient rectangle
     */
    private static void drawGradientRect(GuiGraphics guiGraphics, int x1, int y1, int x2, int y2, int colorLeft, int colorRight) {
        int width = x2 - x1;
        for (int i = 0; i < width; i++) {
            float ratio = (float) i / width;
            int color = interpolateColor(colorLeft, colorRight, ratio);
            guiGraphics.fill(x1 + i, y1, x1 + i + 1, y2, color);
        }
    }

    /**
     * Handles keybinds for hacks
     */
    private static void handleHackKeybinds(Minecraft client) {
        for (var hack : HackManager.getHacks()) {
            int keybind = hack.getKeybind();
            if (keybind != -1) {
                boolean pressed = GLFW.glfwGetKey(client.getWindow().handle(), keybind) == GLFW.GLFW_PRESS;
                if (pressed) {
                    hack.toggle();
                    // Add a small delay to prevent rapid toggling
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                }
            }
        }
    }

    /**
     * Interpolates between two colors
     */
    private static int interpolateColor(int color1, int color2, float ratio) {
        int a1 = (color1 >> 24) & 0xFF;
        int r1 = (color1 >> 16) & 0xFF;
        int g1 = (color1 >> 8) & 0xFF;
        int b1 = color1 & 0xFF;

        int a2 = (color2 >> 24) & 0xFF;
        int r2 = (color2 >> 16) & 0xFF;
        int g2 = (color2 >> 8) & 0xFF;
        int b2 = color2 & 0xFF;

        int a = (int) (a1 + (a2 - a1) * ratio);
        int r = (int) (r1 + (r2 - r1) * ratio);
        int g = (int) (g1 + (g2 - g1) * ratio);
        int b = (int) (b1 + (b2 - b1) * ratio);

        return (a << 24) | (r << 16) | (g << 8) | b;
    }

    /**
     * Renders ESP in the world
     */
    private static void renderESP(/*WorldRenderContext context*/) {
        Minecraft client = Minecraft.getInstance();
        if (client.player == null || client.level == null) return;

        // ESP rendering temporarily disabled due to API changes
        /*
        // Render MobESP
        HackManager.getHack("MobESP").ifPresent(hack -> {
            if (hack.isEnabled()) {
                // ((com.ethos.client.hacks.MobESP) hack).render(context.matrixStack(), context.consumers(), client);
            }
        });

        // Render PlayerESP
        HackManager.getHack("PlayerESP").ifPresent(hack -> {
            if (hack.isEnabled()) {
                // ((com.ethos.client.hacks.PlayerESP) hack).render(context.matrixStack(), context.consumers(), client);
            }
        });
        */
    }
}