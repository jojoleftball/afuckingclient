package com.ethos.client.ui;

import com.ethos.client.hacks.Hack;
import com.ethos.client.hacks.HackCategory;
import com.ethos.client.hacks.HackManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.GuiGraphics;

import java.util.List;
import java.util.Map;

/**
 * A simple click-based menu screen that displays hacks grouped by category.
 * Frames are arranged horizontally across the top of the game window.
 * Clicking a module toggles it.  The screen does not pause the game.
 */
public class MenuScreen extends Screen {
    private static MenuScreen instance;

    private static final int FRAME_X_START = 20;
    private static final int FRAME_Y = 20;
    private static final int FRAME_SPACING = 6;
    private static final int FRAME_WIDTH = 100;
    private static final int HEADER_HEIGHT = 12;
    private static final int BUTTON_HEIGHT = 12;
    private static final int FRAME_PADDING = 4;

    // colors
    private static final int BG_COLOR = 0x90000000;       // semi-transparent black
    private static final int HEADER_BORDER = 0xFF66B2;    // neon pink
    private static final int TEXT_ENABLED = 0xFF66B2;     // neon pink
    private static final int TEXT_DISABLED = 0xFFFFFFFF;  // white

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
            mc.setScreen(getInstance());
            // ensure cursor is visible when menu opens
            mc.mouseHandler.releaseMouse();
        }
    }

    @Override
    public void render(GuiGraphics context, int mouseX, int mouseY, float delta) {
        // draw underlying game first
        super.render(context, mouseX, mouseY, delta);

        Map<HackCategory, List<Hack>> grouped = HackManager.getHacksByCategory();
        int x = FRAME_X_START;
        Minecraft client = Minecraft.getInstance();

        for (HackCategory category : HackCategory.values()) {
            List<Hack> hacks = grouped.get(category);
            if (hacks == null || hacks.isEmpty()) continue;

            int frameHeight = HEADER_HEIGHT + FRAME_PADDING * 2 + hacks.size() * BUTTON_HEIGHT;
            // frame background
            context.fill(x, FRAME_Y, x + FRAME_WIDTH, FRAME_Y + frameHeight, BG_COLOR);
            // header background (solid black)
            context.fill(x, FRAME_Y, x + FRAME_WIDTH, FRAME_Y + HEADER_HEIGHT, 0xFF000000);
            // border outline
            context.fill(x, FRAME_Y, x + 1, FRAME_Y + frameHeight, HEADER_BORDER); // left
            context.fill(x + FRAME_WIDTH - 1, FRAME_Y, x + FRAME_WIDTH, FRAME_Y + frameHeight, HEADER_BORDER); // right
            context.fill(x, FRAME_Y, x + FRAME_WIDTH, FRAME_Y + 1, HEADER_BORDER); // top
            // header bottom border
            context.fill(x, FRAME_Y + HEADER_HEIGHT - 1, x + FRAME_WIDTH, FRAME_Y + HEADER_HEIGHT, HEADER_BORDER);

            // header text centered
            int titleWidth = client.font.width(category.name());
            context.drawString(client.font, category.name(), x + (FRAME_WIDTH - titleWidth) / 2, FRAME_Y + 2, TEXT_DISABLED);

            // draw buttons
            int by = FRAME_Y + HEADER_HEIGHT + FRAME_PADDING;
            for (Hack hack : hacks) {
                int color = hack.isEnabled() ? TEXT_ENABLED : TEXT_DISABLED;
                boolean hovered = mouseX >= x && mouseX < x + FRAME_WIDTH && mouseY >= by && mouseY < by + BUTTON_HEIGHT;
                int bg = hovered ? HEADER_BORDER : BG_COLOR;
                // button background
                context.fill(x, by - 1, x + FRAME_WIDTH, by + BUTTON_HEIGHT - 1, bg);
                context.drawString(client.font, hack.getName(), x + FRAME_PADDING, by, color);
                by += BUTTON_HEIGHT;
            }

            x += FRAME_WIDTH + FRAME_SPACING;
        }
    }

    @Override
    public boolean mouseClicked(net.minecraft.client.input.MouseButtonEvent event, boolean consumed) {
        if (event.button() != 0) {
            return super.mouseClicked(event, consumed);
        }

        double mouseX = event.x();
        double mouseY = event.y();

        Map<HackCategory, List<Hack>> grouped = HackManager.getHacksByCategory();
        int x = FRAME_X_START;

        for (HackCategory category : HackCategory.values()) {
            List<Hack> hacks = grouped.get(category);
            if (hacks == null || hacks.isEmpty()) {
                continue;
            }
            int frameHeight = HEADER_HEIGHT + FRAME_PADDING * 2 + hacks.size() * BUTTON_HEIGHT;

            if (mouseX >= x && mouseX < x + FRAME_WIDTH && mouseY >= FRAME_Y + HEADER_HEIGHT + FRAME_PADDING && mouseY < FRAME_Y + frameHeight) {
                int index = (int)((mouseY - (FRAME_Y + HEADER_HEIGHT + FRAME_PADDING)) / BUTTON_HEIGHT);
                if (index >= 0 && index < hacks.size()) {
                    hacks.get(index).toggle();
                    return true;
                }
            }

            x += FRAME_WIDTH + FRAME_SPACING;
        }

        return super.mouseClicked(event, consumed);
    }

    @Override
    public boolean isPauseScreen() {
        // prevent the game from pausing when the menu is open
        return false;
    }
}