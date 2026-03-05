package com.ethos.client.ui;

import com.ethos.client.hacks.Hack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;

import java.util.List;

/**
 * A draggable floating window for displaying hacks in a category
 */
public class FloatingWindow {
    private static final int TITLE_BAR_HEIGHT = 18;
    private static final int BUTTON_HEIGHT = 14;
    private static final int PADDING = 4;
    private static final int WIDTH = 110;

    // Modern clean color palette
    private static final int PRIMARY_COLOR = 0xFF00D4FF;      // Bright cyan
    private static final int SECONDARY_COLOR = 0xFFFF2D7A;    // Modern pink
    private static final int BG_FRAME = 0xEE0F1419;          // Darker frame background
    private static final int BG_BUTTON = 0xDD1A1E23;         // Button background
    private static final int TEXT_ENABLED = 0xFFFF2D7A;      // Pink text for enabled
    private static final int TEXT_DISABLED = 0xFF888888;     // Gray text for disabled
    private static final int HOVER_COLOR = 0x33FF2D7A;       // Subtle hover highlight
    private static final int BORDER_COLOR = 0xFF2A2D32;      // Border color

    private String categoryName;
    private List<Hack> hacks;
    private int x;
    private int y;
    private boolean isDragging = false;
    private int dragOffsetX = 0;
    private int dragOffsetY = 0;

    public FloatingWindow(String categoryName, List<Hack> hacks, int initialX, int initialY) {
        this.categoryName = categoryName;
        this.hacks = hacks;
        this.x = initialX;
        this.y = initialY;
    }

    public int getWidth() {
        return WIDTH;
    }

    public int getHeight() {
        return TITLE_BAR_HEIGHT + PADDING * 2 + hacks.size() * BUTTON_HEIGHT;
    }

    public void render(GuiGraphics context, int mouseX, int mouseY) {
        Minecraft client = Minecraft.getInstance();
        int windowHeight = getHeight();

        // Draw subtle shadow
        context.fill(x + 1, y + windowHeight, x + WIDTH + 1, y + windowHeight + 1, 0x22000000);
        context.fill(x + WIDTH, y + 1, x + WIDTH + 1, y + windowHeight, 0x22000000);

        // Draw main frame background with border
        context.fill(x, y, x + WIDTH, y + windowHeight, BG_FRAME);
        context.fill(x, y, x + WIDTH, y + 1, BORDER_COLOR); // Top border
        context.fill(x, y + windowHeight - 1, x + WIDTH, y + windowHeight, BORDER_COLOR); // Bottom border
        context.fill(x, y, x + 1, y + windowHeight, BORDER_COLOR); // Left border
        context.fill(x + WIDTH - 1, y, x + WIDTH, y + windowHeight, BORDER_COLOR); // Right border

        // Draw title bar with gradient
        drawGradientRect(context, x + 1, y + 1, x + WIDTH - 1, y + TITLE_BAR_HEIGHT - 1, PRIMARY_COLOR, SECONDARY_COLOR);

        // Draw title centered and smaller
        String title = categoryName;
        int titleWidth = client.font.width(title);
        context.drawString(
            client.font,
            title,
            x + (WIDTH - titleWidth) / 2,
            y + 5,
            0xFFFFFFFF,
            false
        );

        // Draw hack buttons with improved styling
        int hacksY = y + TITLE_BAR_HEIGHT + PADDING;
        for (int i = 0; i < hacks.size(); i++) {
            Hack hack = hacks.get(i);
            int buttonY = hacksY + i * BUTTON_HEIGHT;
            int buttonEndY = buttonY + BUTTON_HEIGHT - 2;

            // Check if button is hovered
            boolean isHovered = mouseX >= x + 2 && mouseX < x + WIDTH - 2 && mouseY >= buttonY && mouseY < buttonEndY;

            // Draw button background
            if (isHovered) {
                context.fill(x + 2, buttonY, x + WIDTH - 2, buttonEndY, HOVER_COLOR);
            } else if (hack.isEnabled()) {
                context.fill(x + 2, buttonY, x + WIDTH - 2, buttonEndY, BG_BUTTON);
            }

            // Draw status indicator (smaller)
            if (hack.isEnabled()) {
                context.fill(x + 4, buttonY + 3, x + 6, buttonY + 9, TEXT_ENABLED);
            }

            // Draw hack name
            int textColor = hack.isEnabled() ? TEXT_ENABLED : TEXT_DISABLED;
            context.drawString(
                client.font,
                hack.getName(),
                x + 12,
                buttonY + 2,
                textColor,
                false
            );
        }
    }

    /**
     * Handles mouse clicks on the window
     */
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        // Check if clicking on title bar
        if (mouseX >= x && mouseX < x + WIDTH && mouseY >= y && mouseY < y + TITLE_BAR_HEIGHT) {
            if (button == 0) { // Left click
                isDragging = true;
                dragOffsetX = (int) (mouseX - x);
                dragOffsetY = (int) (mouseY - y);
                return true;
            }
        }

        // Check if clicking on hack buttons
        int hacksY = y + TITLE_BAR_HEIGHT + PADDING;
        if (mouseX >= x + 2 && mouseX < x + WIDTH - 2 && mouseY >= hacksY && mouseY < hacksY + hacks.size() * BUTTON_HEIGHT) {
            int index = (int) ((mouseY - hacksY) / BUTTON_HEIGHT);
            if (index >= 0 && index < hacks.size()) {
                hacks.get(index).toggle();
                return true;
            }
        }

        return false;
    }

    /**
     * Handles mouse release
     */
    public void mouseReleased(double mouseX, double mouseY, int button) {
        if (button == 0) { // Left click release
            isDragging = false;
        }
    }

    /**
     * Updates window position during dragging
     */
    public void updateDrag(double mouseX, double mouseY) {
        if (isDragging) {
            x = (int) (mouseX - dragOffsetX);
            y = (int) (mouseY - dragOffsetY);
        }
    }

    /**
     * Draws a gradient rectangle
     */
    private void drawGradientRect(GuiGraphics context, int x1, int y1, int x2, int y2, int colorLeft, int colorRight) {
        int width = x2 - x1;
        for (int i = 0; i < width; i++) {
            float ratio = (float) i / width;
            int color = interpolateColor(colorLeft, colorRight, ratio);
            context.fill(x1 + i, y1, x1 + i + 1, y2, color);
        }
    }

    /**
     * Interpolates between two colors
     */
    private int interpolateColor(int colorA, int colorB, float ratio) {
        int aR = (colorA >> 16) & 0xFF;
        int aG = (colorA >> 8) & 0xFF;
        int aB = colorA & 0xFF;

        int bR = (colorB >> 16) & 0xFF;
        int bG = (colorB >> 8) & 0xFF;
        int bB = colorB & 0xFF;

        int r = (int) (aR + (bR - aR) * ratio);
        int g = (int) (aG + (bG - aG) * ratio);
        int b = (int) (aB + (bB - aB) * ratio);

        return (r << 16) | (g << 8) | b | 0xFF000000;
    }

    // Getters and setters
    public int getX() { return x; }
    public int getY() { return y; }
    public void setX(int x) { this.x = x; }
    public void setY(int y) { this.y = y; }
    public boolean isDragging() { return isDragging; }
}
