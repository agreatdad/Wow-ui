package com.example;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import javax.inject.Inject;
import net.runelite.api.Client;
import net.runelite.api.Player;
import net.runelite.api.Prayer;
import net.runelite.api.Skill;
import net.runelite.api.SpriteID;
import net.runelite.api.VarPlayer;
import net.runelite.client.game.SpriteManager;
import net.runelite.client.ui.overlay.Overlay;
import net.runelite.client.ui.overlay.OverlayLayer;
import net.runelite.client.ui.overlay.OverlayPosition;

public class MmoHudOverlay extends Overlay
{
    private static final int MIN_BAR_WIDTH = 50;
    private static final int MIN_TOTAL_HEIGHT = 60;

    private final Client client;
    private final ExampleConfig config;
    private final PortraitProvider portraitProvider;
    private final SpriteManager spriteManager;
    private final CombatAchievementTracker combatAchievementTracker;

    private final MmoHudBar hpBar;
    private final MmoHudBar prayerBar;
    private final MmoHudBar runBar;
    private final MmoHudBar specBar;

    private Image hpIcon;
    private Image prayerIcon;
    private Image runIcon;
    private Image specIcon;

    @Inject
    public MmoHudOverlay(
            Client client,
            ExampleConfig config,
            PortraitProvider portraitProvider,
            SpriteManager spriteManager,
            CombatAchievementTracker combatAchievementTracker
    )
    {
        this.client = client;
        this.config = config;
        this.portraitProvider = portraitProvider;
        this.spriteManager = spriteManager;
        this.combatAchievementTracker = combatAchievementTracker;

        setPosition(OverlayPosition.TOP_LEFT);
        setLayer(OverlayLayer.ABOVE_WIDGETS);
        setMovable(true);
        setSnappable(true);
        setResizable(true);

        hpBar = new MmoHudBar(
                () -> client.getRealSkillLevel(Skill.HITPOINTS),
                () -> client.getBoostedSkillLevel(Skill.HITPOINTS),
                this::getHpColor,
                () -> config.showMmoHudIcons() ? hpIcon : null
        );

        prayerBar = new MmoHudBar(
                () -> client.getRealSkillLevel(Skill.PRAYER),
                () -> client.getBoostedSkillLevel(Skill.PRAYER),
                this::getPrayerColor,
                () -> config.showMmoHudIcons() ? prayerIcon : null
        );

        runBar = new MmoHudBar(
                () -> 100,
                () -> client.getEnergy() / 100,
                this::getRunColor,
                () -> config.showMmoHudIcons() ? runIcon : null
        );

        specBar = new MmoHudBar(
                () -> 100,
                () -> client.getVarpValue(VarPlayer.SPECIAL_ATTACK_PERCENT) / 10,
                this::getSpecColor,
                () -> config.showMmoHudIcons() ? specIcon : null
        );
    }

    @Override
    public Dimension render(Graphics2D graphics)
    {
        if (!config.showMmoHud())
        {
            return null;
        }

        Player localPlayer = client.getLocalPlayer();

        if (localPlayer == null)
        {
            return null;
        }

        loadIcons();

        graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        graphics.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        Font oldFont = graphics.getFont();
        graphics.setFont(oldFont.deriveFont(Font.BOLD, 12f));

        int portraitDiameter = config.portraitDiameter();
        int configuredBarWidth = config.barWidth();
        int configuredBarHeight = config.barHeight();
        int barGap = config.barGap();

        int titleHeight = config.showCombatAchievementTitle() ? 22 : 0;
        int portraitX = 0;
        int portraitY = titleHeight;

        int barX = portraitDiameter - 8;
        int barY = titleHeight + 14;

        Dimension defaultSize = getDefaultSize();
        Dimension currentSize = getCurrentSize(defaultSize);

        int barWidth = Math.max(MIN_BAR_WIDTH, currentSize.width - barX);

        int visibleBars = getVisibleBarCount();
        int barHeight = configuredBarHeight;

        if (visibleBars > 0 && currentSize.height > defaultSize.height)
        {
            int availableBarHeight = currentSize.height - barY;
            barHeight = Math.max(
                    12,
                    (availableBarHeight - ((visibleBars - 1) * barGap)) / visibleBars
            );
        }

        if (config.showCombatAchievementTitle())
        {
            drawCombatAchievementTitle(graphics, portraitX, 0, portraitDiameter);
        }

        portraitProvider.drawPortrait(
                graphics,
                portraitX,
                portraitY,
                portraitDiameter
        );

        drawLevelAndName(graphics, localPlayer, portraitX, portraitY, portraitDiameter);

        if (config.showHpBar())
        {
            hpBar.render(graphics, barX, barY, barWidth, barHeight);
            barY += barHeight + barGap;
        }

        if (config.showPrayerBar())
        {
            prayerBar.render(graphics, barX, barY, barWidth, barHeight);
            barY += barHeight + barGap;
        }

        if (config.showRunBar())
        {
            runBar.render(graphics, barX, barY, barWidth, barHeight);
            barY += barHeight + barGap;
        }

        if (config.showSpecBar())
        {
            specBar.render(graphics, barX, barY, barWidth, barHeight);
        }

        graphics.setFont(oldFont);

        return currentSize;
    }

    private Dimension getDefaultSize()
    {
        int portraitDiameter = config.portraitDiameter();
        int barWidth = config.barWidth();
        int barHeight = config.barHeight();
        int barGap = config.barGap();
        int titleHeight = config.showCombatAchievementTitle() ? 22 : 0;

        int width = portraitDiameter + barWidth;

        int portraitSideHeight = titleHeight + portraitDiameter + 40;
        int barsHeight = titleHeight + 14 + getVisibleBarCount() * (barHeight + barGap);

        int height = Math.max(portraitSideHeight, barsHeight);

        return new Dimension(width, Math.max(height, MIN_TOTAL_HEIGHT));
    }

    private Dimension getCurrentSize(Dimension defaultSize)
    {
        Rectangle bounds = getBounds();

        if (bounds != null && bounds.width > 80 && bounds.height > 60)
        {
            return new Dimension(
                    Math.max(bounds.width, defaultSize.width),
                    Math.max(bounds.height, defaultSize.height)
            );
        }

        return defaultSize;
    }

    private void drawCombatAchievementTitle(Graphics2D graphics, int portraitX, int y, int portraitDiameter)
    {
        String title = getCombatAchievementTitle();

        Font oldFont = graphics.getFont();
        graphics.setFont(oldFont.deriveFont(Font.BOLD, 11f));

        FontMetrics metrics = graphics.getFontMetrics();

        int boxWidth = Math.max(portraitDiameter, metrics.stringWidth(title) + 16);
        int boxHeight = 18;

        int boxX = portraitX + (portraitDiameter - boxWidth) / 2;
        int boxY = y;

        graphics.setColor(new Color(15, 15, 15, 230));
        graphics.fillRoundRect(boxX, boxY, boxWidth, boxHeight, 10, 10);

        graphics.setColor(new Color(210, 178, 64));
        graphics.drawRoundRect(boxX, boxY, boxWidth, boxHeight, 10, 10);

        drawCenteredText(graphics, title, boxX + boxWidth / 2, boxY + 13);

        graphics.setFont(oldFont);
    }

    private String getCombatAchievementTitle()
    {
        CombatAchievementTracker.Progress progress = combatAchievementTracker.getProgress();

        if (progress == null)
        {
            return "No CA Tier";
        }

        return progress.getRewardTier().getTitle();
    }

    private int getVisibleBarCount()
    {
        int count = 0;

        if (config.showHpBar())
        {
            count++;
        }

        if (config.showPrayerBar())
        {
            count++;
        }

        if (config.showRunBar())
        {
            count++;
        }

        if (config.showSpecBar())
        {
            count++;
        }

        return count;
    }

    private void drawLevelAndName(Graphics2D graphics, Player localPlayer, int portraitX, int portraitY, int portraitDiameter)
    {
        String levelText;

        if (config.showTotalInsteadOfCombat())
        {
            levelText = "Total lvl " + getTotalLevel();
        }
        else
        {
            levelText = "Combat lvl " + localPlayer.getCombatLevel();
        }

        String nameText = localPlayer.getName() == null ? "" : localPlayer.getName();

        Font oldFont = graphics.getFont();
        graphics.setFont(oldFont.deriveFont(Font.BOLD, 12f));

        FontMetrics metrics = graphics.getFontMetrics();

        int levelWidth = metrics.stringWidth(levelText) + 12;
        int levelHeight = 18;

        int levelX = portraitX + (portraitDiameter - levelWidth) / 2;
        int levelY = portraitY + portraitDiameter - 18;

        graphics.setColor(new Color(15, 15, 15, 230));
        graphics.fillRoundRect(levelX, levelY, levelWidth, levelHeight, 10, 10);

        graphics.setColor(new Color(210, 178, 64));
        graphics.drawRoundRect(levelX, levelY, levelWidth, levelHeight, 10, 10);

        drawCenteredText(graphics, levelText, portraitX + portraitDiameter / 2, levelY + 13);

        int nameWidth = Math.max(50, metrics.stringWidth(nameText) + 12);
        int nameHeight = 18;

        int nameX = portraitX + (portraitDiameter - nameWidth) / 2;
        int nameY = portraitY + portraitDiameter + 2;

        graphics.setColor(new Color(15, 15, 15, 230));
        graphics.fillRoundRect(nameX, nameY, nameWidth, nameHeight, 10, 10);

        graphics.setColor(new Color(210, 178, 64));
        graphics.drawRoundRect(nameX, nameY, nameWidth, nameHeight, 10, 10);

        drawCenteredText(graphics, nameText, portraitX + portraitDiameter / 2, nameY + 13);

        graphics.setFont(oldFont);
    }

    private int getTotalLevel()
    {
        int total = 0;

        for (Skill skill : Skill.values())
        {
            if (skill == Skill.OVERALL)
            {
                continue;
            }

            int level = client.getRealSkillLevel(skill);

            if (level > 0)
            {
                total += level;
            }
        }

        return total;
    }

    private void drawCenteredText(Graphics2D graphics, String text, int centerX, int baselineY)
    {
        FontMetrics metrics = graphics.getFontMetrics();
        int textX = centerX - metrics.stringWidth(text) / 2;

        graphics.setColor(Color.BLACK);
        graphics.drawString(text, textX + 1, baselineY + 1);

        graphics.setColor(Color.WHITE);
        graphics.drawString(text, textX, baselineY);
    }

    private Color getHpColor()
    {
        int poison = client.getVarpValue(VarPlayer.POISON);

        if (poison >= 1_000_000)
        {
            return new Color(0, 70, 0);
        }

        if (poison > 0)
        {
            return new Color(0, 150, 0);
        }

        return config.hpColor();
    }

    private Color getPrayerColor()
    {
        for (Prayer prayer : Prayer.values())
        {
            if (client.isPrayerActive(prayer))
            {
                return new Color(70, 235, 190);
            }
        }

        return config.prayerColor();
    }

    private Color getRunColor()
    {
        if (client.getVarpValue(173) == 1)
        {
            return new Color(235, 220, 40);
        }

        return config.runColor();
    }

    private Color getSpecColor()
    {
        if (client.getVarpValue(301) == 1)
        {
            return new Color(20, 235, 40);
        }

        return config.specColor();
    }

    private void loadIcons()
    {
        if (hpIcon == null)
        {
            hpIcon = getSprite(SpriteID.MINIMAP_ORB_HITPOINTS_ICON);
        }

        if (prayerIcon == null)
        {
            prayerIcon = getSprite(SpriteID.MINIMAP_ORB_PRAYER_ICON);
        }

        if (runIcon == null)
        {
            runIcon = getSprite(SpriteID.MINIMAP_ORB_WALK_ICON);
        }

        if (specIcon == null)
        {
            specIcon = getSprite(SpriteID.MINIMAP_ORB_SPECIAL_ICON);
        }
    }

    private BufferedImage getSprite(int spriteId)
    {
        try
        {
            return spriteManager.getSprite(spriteId, 0);
        }
        catch (Exception ignored)
        {
            return null;
        }
    }
}