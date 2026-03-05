package com.ethos.client.ui;

import com.ethos.client.hacks.Hack;
import com.ethos.client.hacks.HackCategory;
import com.ethos.client.hacks.HackManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.GuiGraphics;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * A modern menu screen with draggable floating windows for each category.
 * Currently displays the MOVEMENT category. More categories can be added later.
 */
public class MenuScreen extends Screen {
    private static MenuScreen instance;

    private List<FloatingWindow> windows = new ArrayList<>();
    private FloatingWindow draggingWindow = null;

    private static final int PRIMARY_COLOR = 0xFF00D9FF; // Cyan

    protected MenuScreen() {
        super(net.minecraft.network.chat.Component.literal("Ethos Menu"));
    }

    public static MenuScreen getInstance() {
        if (instance == null) {
            instance = new MenuScreen();
        }
        return instance;
    }

    /**
     * Toggle the menu on or off.
     */
    public static void toggle() {
        Minecraft mc = Minecraft.getInstance();
        if (mc.screen instanceof MenuScreen) {
            mc.setScreen(null);
        } else {
            instance = new MenuScreen(); // Create fresh instance each time
            mc.setScreen(instance);
            mc.mouseHandler.releaseMouse();
        }
    }

    @Override
    public void init() {
        super.init();
        windows.clear();

        // Initialize floating windows for categories with hacks
        // Currently only showing MOVEMENT category (ScaffoldWalk)
        Map<HackCategory, List<Hack>> grouped = HackManager.getHacksByCategory();

        int windowCount = 0;
        for (HackCategory category : HackCategory.values()) {
            List<Hack> hacks = grouped.get(category);
            if (hacks == null || hacks.isEmpty()) continue;
            
            // For now, only show MOVEMENT category
            if (category != HackCategory.MOVEMENT) continue;

            // Create floating window for this category
            int startX = 15 + (windowCount * 120); // Adjusted for new width
            int startY = 15;
            FloatingWindow window = new FloatingWindow(category.name(), hacks, startX, startY);
            windows.add(window);
            windowCount++;
        }
    }

    @Override
    public void render(GuiGraphics context, int mouseX, int mouseY, float delta) {
        // Render dark transparent background overlay
        context.fill(0, 0, this.width, this.height, 0x88000000);

        // Render title
        String title = "» ETHOS CLIENT «";
        Minecraft client = Minecraft.getInstance();
        context.drawString(client.font, title, 20, 5, PRIMARY_COLOR, true);

        // Render all floating windows
        for (FloatingWindow window : windows) {
            window.render(context, mouseX, mouseY);
        }

        // Update dragging if needed
        if (draggingWindow != null) {
            draggingWindow.updateDrag(mouseX, mouseY);
        }
    }

    @Override
    public boolean mouseClicked(net.minecraft.client.input.MouseButtonEvent event, boolean consumed) {
        double mouseX = event.x();
        double mouseY = event.y();
        int button = event.button();

        // Check windows in reverse order (top windows first)
        for (int i = windows.size() - 1; i >= 0; i--) {
            FloatingWindow window = windows.get(i);
            if (window.mouseClicked(mouseX, mouseY, button)) {
                // Move clicked window to front
                windows.remove(i);
                windows.add(window);
                if (window.isDragging()) {
                    draggingWindow = window;
                }
                return true;
            }
        }

        return super.mouseClicked(event, consumed);
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double horizontalAmount, double verticalAmount) {
        return super.mouseScrolled(mouseX, mouseY, horizontalAmount, verticalAmount);
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }
}