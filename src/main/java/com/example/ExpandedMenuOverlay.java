package com.example;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import javax.inject.Inject;
import net.runelite.api.Client;
import net.runelite.api.GameState;
import net.runelite.api.widgets.Widget;
import net.runelite.client.ui.overlay.Overlay;
import net.runelite.client.ui.overlay.OverlayLayer;
import net.runelite.client.ui.overlay.OverlayPosition;

public class ExpandedMenuOverlay extends Overlay
{
    private final Client client;
    private final ExampleConfig config;

    @Inject
    ExpandedMenuOverlay(Client client, ExampleConfig config)
    {
        this.client = client;
        this.config = config;

        setPosition(OverlayPosition.DYNAMIC);
        setLayer(OverlayLayer.ABOVE_WIDGETS);
        setPriority(Overlay.PRIORITY_HIGH);
        setMovable(false);
    }

    @Override
    public Dimension render(Graphics2D graphics)
    {
        if (client.getGameState() != GameState.LOGGED_IN)
        {
            return null;
        }

        Font originalFont = graphics.getFont();
        Color originalColor = graphics.getColor();

        Font labelFont = originalFont.deriveFont(Font.BOLD, (float) config.fontSize());
        graphics.setFont(labelFont);

        for (ExpandedMenuStone stone : ExpandedMenuStone.values())
        {
            String label = cleanLabel(stone.getLabel(config));

            if (label.isEmpty())
            {
                continue;
            }

            Widget widget = findVisibleWidget(stone);

            if (widget == null)
            {
                continue;
            }

            drawLabel(graphics, widget.getBounds(), label);
        }

        graphics.setFont(originalFont);
        graphics.setColor(originalColor);

        return null;
    }

    private Widget findVisibleWidget(ExpandedMenuStone stone)
    {
        for (int componentId : stone.getComponentIds())
        {
            Widget widget = client.getWidget(componentId);

            if (isDrawable(widget))
            {
                return widget;
            }
        }

        return null;
    }

    private boolean isDrawable(Widget widget)
    {
        if (widget == null || widget.isHidden())
        {
            return false;
        }

        Rectangle bounds = widget.getBounds();

        return bounds != null && bounds.width > 0 && bounds.height > 0;
    }

    private String cleanLabel(String label)
    {
        if (label == null)
        {
            return "";
        }

        return label.trim();
    }

    private void drawLabel(Graphics2D graphics, Rectangle bounds, String label)
    {
        FontMetrics metrics = graphics.getFontMetrics();

        int textWidth = metrics.stringWidth(label);
        int x = bounds.x + bounds.width - textWidth - config.xOffset();
        int y = bounds.y + metrics.getAscent() + config.yOffset();

        if (config.showShadow())
        {
            graphics.setColor(safeColor(config.shadowColor(), Color.BLACK));
            graphics.drawString(label, x + 1, y + 1);
        }

        graphics.setColor(safeColor(config.labelColor(), Color.WHITE));
        graphics.drawString(label, x, y);
    }

    private Color safeColor(Color color, Color fallback)
    {
        return color == null ? fallback : color;
    }
}