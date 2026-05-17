package com.example;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.util.function.IntSupplier;
import java.util.function.Supplier;

public class MmoHudBar
{
    private static final int ICON_SIZE = 18;

    private final IntSupplier maxSupplier;
    private final IntSupplier currentSupplier;
    private final Supplier<Color> colorSupplier;
    private final Supplier<Image> iconSupplier;

    private Rectangle iconBounds = new Rectangle();

    public MmoHudBar(
            IntSupplier maxSupplier,
            IntSupplier currentSupplier,
            Supplier<Color> colorSupplier,
            Supplier<Image> iconSupplier
    )
    {
        this.maxSupplier = maxSupplier;
        this.currentSupplier = currentSupplier;
        this.colorSupplier = colorSupplier;
        this.iconSupplier = iconSupplier;
    }

    public void render(Graphics2D g, int x, int y, int width, int height)
    {
        int max = Math.max(1, maxSupplier.getAsInt());
        int current = Math.max(0, Math.min(max, currentSupplier.getAsInt()));

        double ratio = current / (double) max;

        g.setColor(new Color(0, 0, 0, 220));
        g.fillRoundRect(x, y, width, height, 7, 7);

        g.setColor(new Color(80, 80, 80));
        g.setStroke(new BasicStroke(1));
        g.drawRoundRect(x, y, width, height, 7, 7);

        int fillWidth = (int) Math.round((width - 4) * ratio);

        g.setColor(colorSupplier.get());
        g.fillRoundRect(x + 2, y + 2, fillWidth, height - 4, 5, 5);

        String text = current + " / " + max;

        Font oldFont = g.getFont();
        g.setFont(oldFont.deriveFont(Font.BOLD, 12f));

        FontMetrics metrics = g.getFontMetrics();
        int textX = x + 8;
        int textY = y + ((height - metrics.getHeight()) / 2) + metrics.getAscent();

        g.setColor(Color.BLACK);
        g.drawString(text, textX + 1, textY + 1);

        g.setColor(Color.WHITE);
        g.drawString(text, textX, textY);

        Image icon = iconSupplier.get();

        if (icon != null)
        {
            int iconX = x + width - ICON_SIZE - 4;
            int iconY = y + (height - ICON_SIZE) / 2;

            g.drawImage(icon, iconX, iconY, ICON_SIZE, ICON_SIZE, null);
            iconBounds = new Rectangle(iconX, iconY, ICON_SIZE, ICON_SIZE);
        }
        else
        {
            iconBounds = new Rectangle();
        }

        g.setFont(oldFont);
    }

    public Rectangle getIconBounds()
    {
        return iconBounds;
    }
}