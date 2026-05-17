package com.example;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import javax.inject.Inject;
import net.runelite.api.Client;
import net.runelite.api.SpriteID;
import net.runelite.api.gameval.InterfaceID;
import net.runelite.api.widgets.Widget;
import net.runelite.client.game.SpriteManager;
import net.runelite.client.ui.overlay.Overlay;
import net.runelite.client.ui.overlay.OverlayLayer;
import net.runelite.client.ui.overlay.OverlayPosition;
import net.runelite.client.ui.overlay.OverlayPriority;

public class LogoutRuneOverlay extends Overlay
{
    private final Client client;
    private final SpriteManager spriteManager;
    private final ExampleConfig config;

    private BufferedImage logoutSprite;

    @Inject
    public LogoutRuneOverlay(Client client, SpriteManager spriteManager, ExampleConfig config)
    {
        this.client = client;
        this.spriteManager = spriteManager;
        this.config = config;

        setPosition(OverlayPosition.DYNAMIC);
        setLayer(OverlayLayer.ABOVE_WIDGETS);
        setPriority(OverlayPriority.HIGHEST);
    }

    @Override
    public Dimension render(Graphics2D graphics)
    {
        Widget friendsStone = client.getWidget(InterfaceID.ToplevelPreEoc.ICON9);

        if (friendsStone == null || friendsStone.isHidden())
        {
            friendsStone = client.getWidget(InterfaceID.ToplevelOsrsStretch.ICON9);
        }

        if (friendsStone == null || friendsStone.isHidden())
        {
            return null;
        }

        Rectangle friendBounds = friendsStone.getBounds();

        if (friendBounds == null || friendBounds.width <= 0 || friendBounds.height <= 0)
        {
            return null;
        }

        int stoneWidth = 38;
        int stoneHeight = 38;

        int targetX = friendBounds.x - stoneWidth + 2;
        int targetY = friendBounds.y - 2;

        drawLogoutSprite(graphics, targetX, targetY, stoneWidth, stoneHeight);
        drawLogoutKeybind(graphics, targetX, targetY, stoneWidth, stoneHeight);

        return null;
    }

    private void drawLogoutSprite(Graphics2D graphics, int targetX, int targetY, int stoneWidth, int stoneHeight)
    {
        BufferedImage sprite = getLogoutSprite();

        if (sprite != null)
        {
            int size = 20;
            int x = targetX + (stoneWidth - size) / 2;
            int y = targetY + (stoneHeight - size) / 2 + 2;

            graphics.drawImage(sprite, x, y, size, size, null);
        }
        else
        {
            graphics.setColor(new Color(255, 210, 80));
            graphics.setFont(new Font("Arial", Font.BOLD, 18));
            graphics.drawString("X", targetX + stoneWidth / 2 - 6, targetY + stoneHeight / 2 + 7);
        }
    }

    private void drawLogoutKeybind(Graphics2D graphics, int targetX, int targetY, int stoneWidth, int stoneHeight)
    {
        String label = config.logoutLabel();

        if (label == null || label.trim().isEmpty())
        {
            return;
        }

        graphics.setFont(new Font("Arial", Font.BOLD, 12));

        int textX = targetX + stoneWidth - 12 + config.xOffset();
        int textY = targetY + 11 + config.yOffset();

        if (config.showShadow())
        {
            graphics.setColor(config.shadowColor());
            graphics.drawString(label, textX + 1, textY + 1);
        }

        graphics.setColor(config.labelColor());
        graphics.drawString(label, textX, textY);
    }

    private BufferedImage getLogoutSprite()
    {
        if (logoutSprite != null)
        {
            return logoutSprite;
        }

        int[] possibleSprites =
                {
                        SpriteID.TAB_LOGOUT,
                        SpriteID.RS2_TAB_LOGOUT,
                        SpriteID.UNUSED_TAB_LOGOUT
                };

        for (int spriteId : possibleSprites)
        {
            try
            {
                BufferedImage sprite = spriteManager.getSprite(spriteId, 0);

                if (sprite != null)
                {
                    logoutSprite = sprite;
                    return logoutSprite;
                }
            }
            catch (Exception ignored)
            {
            }
        }

        return null;
    }
}