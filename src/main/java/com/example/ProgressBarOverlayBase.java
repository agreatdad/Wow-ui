package com.example;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import net.runelite.client.ui.overlay.Overlay;
import net.runelite.client.ui.overlay.OverlayLayer;
import net.runelite.client.ui.overlay.OverlayPosition;

public abstract class ProgressBarOverlayBase extends Overlay
{
    private static final int SEGMENTS = 10;

    protected final ExampleConfig config;

    protected ProgressBarOverlayBase(ExampleConfig config)
    {
        this.config = config;

        setPosition(OverlayPosition.TOP_LEFT);
        setLayer(OverlayLayer.ABOVE_WIDGETS);
        setMovable(true);
        setSnappable(true);
        setResizable(false);
    }

    protected abstract boolean isEnabled();

    protected abstract int getBarWidth();

    protected abstract int getBarHeight();

    protected abstract Color getBarColor();

    protected abstract String getIconText();

    protected abstract ProgressData getProgressData();

    @Override
    public Dimension render(Graphics2D graphics)
    {
        if (!config.showProgressBars() || !isEnabled())
        {
            return null;
        }

        ProgressData data = getProgressData();

        if (data == null)
        {
            return null;
        }

        int width = getBarWidth();
        int height = getBarHeight();

        graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        graphics.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        drawSegmentedBar(graphics, 0, 0, width, height, data, getBarColor(), config.showProgressBarIcons(), getIconText());

        return new Dimension(width, height);
    }

    private void drawSegmentedBar(Graphics2D g, int x, int y, int width, int height, ProgressData data, Color fillColor, boolean showIcon, String iconText)
    {
        int filledSegments = getFilledSegments(data.current, data.max);

        g.setColor(new Color(0, 0, 0, 210));
        g.fillRoundRect(x, y, width, height, 8, 8);

        g.setColor(new Color(80, 80, 80));
        g.setStroke(new BasicStroke(1));
        g.drawRoundRect(x, y, width, height, 8, 8);

        int innerX = x + 4;
        int innerY = y + 4;
        int innerW = width - 8;
        int innerH = height - 8;

        int gap = 2;
        int segmentW = (innerW - ((SEGMENTS - 1) * gap)) / SEGMENTS;

        for (int i = 0; i < SEGMENTS; i++)
        {
            int segmentX = innerX + i * (segmentW + gap);

            if (i < filledSegments)
            {
                g.setColor(fillColor);
            }
            else
            {
                g.setColor(new Color(10, 10, 10, 230));
            }

            g.fillRect(segmentX, innerY, segmentW, innerH);

            g.setColor(new Color(0, 0, 0, 160));
            g.drawRect(segmentX, innerY, segmentW, innerH);
        }

        String text = data.text;

        if (showIcon)
        {
            text = getIconText() + "  " + text;
        }

        Font oldFont = g.getFont();
        g.setFont(oldFont.deriveFont(Font.BOLD, Math.max(9f, height * 0.48f)));

        FontMetrics metrics = g.getFontMetrics();
        int textX = x + (width - metrics.stringWidth(text)) / 2;
        int textY = y + ((height - metrics.getHeight()) / 2) + metrics.getAscent();

        g.setColor(Color.BLACK);
        g.drawString(text, textX + 1, textY + 1);

        g.setColor(Color.WHITE);
        g.drawString(text, textX, textY);

        g.setFont(oldFont);
    }

    private int getFilledSegments(int current, int max)
    {
        if (max <= 0)
        {
            return 0;
        }

        double ratio = Math.max(0.0, Math.min(1.0, current / (double) max));
        return (int) Math.round(ratio * SEGMENTS);
    }
}