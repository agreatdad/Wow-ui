package com.example;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;
import javax.inject.Inject;
import net.runelite.client.game.ItemManager;

public class PortraitProvider
{
    private final ItemManager itemManager;
    private final ExampleConfig config;

    private PortraitChoice cachedChoice;
    private boolean cachedUseLocalProfileImage;
    private BufferedImage portrait;

    @Inject
    public PortraitProvider(ItemManager itemManager, ExampleConfig config)
    {
        this.itemManager = itemManager;
        this.config = config;
    }

    public void reload()
    {
        portrait = null;
        cachedChoice = null;
    }

    public BufferedImage getPortrait()
    {
        boolean useLocal = config.useLocalProfileImage();
        PortraitChoice choice = config.portraitChoice();

        if (choice == null)
        {
            choice = PortraitChoice.SANGUINE_TORVA_FULL_HELM;
        }

        if (portrait == null || cachedChoice != choice || cachedUseLocalProfileImage != useLocal)
        {
            cachedChoice = choice;
            cachedUseLocalProfileImage = useLocal;

            if (useLocal)
            {
                portrait = loadLocalProfileImage();
            }
            else
            {
                portrait = itemManager.getImage(choice.getItemId());
            }
        }

        return portrait;
    }

    private BufferedImage loadLocalProfileImage()
    {
        try
        {
            File runeliteFolder = new File(System.getProperty("user.home"), ".runelite");

            File[] possibleFiles =
                    {
                            new File(runeliteFolder, "profile.png"),
                            new File(runeliteFolder, "profile.jpg"),
                            new File(runeliteFolder, "profile.jpeg"),
                            new File(runeliteFolder, "profile.gif"),
                            new File(runeliteFolder, "profile")
                    };

            for (File file : possibleFiles)
            {
                if (file.exists() && file.isFile())
                {
                    return ImageIO.read(file);
                }
            }
        }
        catch (Exception ignored)
        {
        }

        PortraitChoice choice = config.portraitChoice();

        if (choice == null)
        {
            choice = PortraitChoice.SANGUINE_TORVA_FULL_HELM;
        }

        return itemManager.getImage(choice.getItemId());
    }

    public void drawPortrait(Graphics2D g, int x, int y, int diameter)
    {
        BufferedImage image = getPortrait();

        Graphics2D g2 = (Graphics2D) g.create();

        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        g2.setColor(new Color(12, 12, 12, 230));
        g2.fillOval(x, y, diameter, diameter);

        if (image != null)
        {
            drawImageContain(g2, image, x, y, diameter, diameter);
        }

        g2.setStroke(new BasicStroke(3));
        g2.setColor(config.portraitFrameColor());
        g2.drawOval(x + 1, y + 1, diameter - 2, diameter - 2);

        g2.setStroke(new BasicStroke(1));
        g2.setColor(new Color(0, 0, 0, 190));
        g2.drawOval(x + 4, y + 4, diameter - 8, diameter - 8);

        g2.dispose();
    }

    private void drawImageContain(Graphics2D g, Image image, int x, int y, int width, int height)
    {
        int imageWidth = image.getWidth(null);
        int imageHeight = image.getHeight(null);

        if (imageWidth <= 0 || imageHeight <= 0)
        {
            return;
        }

        double scale = Math.min(width / (double) imageWidth, height / (double) imageHeight) * 0.88;

        int drawWidth = (int) Math.round(imageWidth * scale);
        int drawHeight = (int) Math.round(imageHeight * scale);

        int drawX = x + (width - drawWidth) / 2;
        int drawY = y + (height - drawHeight) / 2;

        g.drawImage(image, drawX, drawY, drawWidth, drawHeight, null);
    }
}