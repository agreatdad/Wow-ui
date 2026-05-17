package com.example;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import javax.inject.Inject;
import net.runelite.api.Client;
import net.runelite.api.Skill;
import net.runelite.client.game.SkillIconManager;
import net.runelite.client.ui.overlay.Overlay;
import net.runelite.client.ui.overlay.OverlayLayer;
import net.runelite.client.ui.overlay.OverlayPosition;
import net.runelite.client.ui.overlay.OverlayPriority;

public class SkillsOverlay extends Overlay
{
    private static final int DESIGN_WIDTH = 306;
    private static final int DESIGN_HEIGHT = 430;
    private static final double DEFAULT_SCALE = 1.0;

    private static final int OUTER_PAD = 8;
    private static final int CELL_W = 94;
    private static final int CELL_H = 43;
    private static final int CELL_GAP = 3;

    private static final int START_X = 12;
    private static final int START_Y = 14;

    private final Client client;
    private final SkillIconManager skillIconManager;

    private boolean open = false;

    private static final Skill[] SKILLS =
            {
                    Skill.ATTACK, Skill.HITPOINTS, Skill.MINING,
                    Skill.STRENGTH, Skill.AGILITY, Skill.SMITHING,
                    Skill.DEFENCE, Skill.HERBLORE, Skill.FISHING,
                    Skill.RANGED, Skill.THIEVING, Skill.COOKING,
                    Skill.PRAYER, Skill.CRAFTING, Skill.FIREMAKING,
                    Skill.MAGIC, Skill.FLETCHING, Skill.WOODCUTTING,
                    Skill.RUNECRAFT, Skill.SLAYER, Skill.FARMING,
                    Skill.CONSTRUCTION, Skill.HUNTER, Skill.SAILING
            };

    @Inject
    public SkillsOverlay(Client client, SkillIconManager skillIconManager)
    {
        this.client = client;
        this.skillIconManager = skillIconManager;

        setPosition(OverlayPosition.TOP_LEFT);
        setLayer(OverlayLayer.ABOVE_WIDGETS);
        setPriority(OverlayPriority.MED);
        setMovable(true);
        setResizable(true);
    }

    public void toggle()
    {
        open = !open;
    }

    @Override
    public Dimension render(Graphics2D graphics)
    {
        if (!open)
        {
            return null;
        }

        Dimension size = getCurrentSize();

        double scaleX = size.getWidth() / DESIGN_WIDTH;
        double scaleY = size.getHeight() / DESIGN_HEIGHT;

        Graphics2D g = (Graphics2D) graphics.create();
        g.scale(scaleX, scaleY);

        drawFrame(g);
        drawSkillGrid(g);
        drawTotalLevel(g);

        g.dispose();

        return size;
    }

    private Dimension getCurrentSize()
    {
        Rectangle bounds = getBounds();

        if (bounds != null && bounds.width > 80 && bounds.height > 80)
        {
            return new Dimension(bounds.width, bounds.height);
        }

        return new Dimension(
                (int) Math.round(DESIGN_WIDTH * DEFAULT_SCALE),
                (int) Math.round(DESIGN_HEIGHT * DEFAULT_SCALE)
        );
    }

    private void drawFrame(Graphics2D g)
    {
        g.setColor(new Color(58, 53, 43, 245));
        g.fillRect(0, 0, DESIGN_WIDTH, DESIGN_HEIGHT);

        g.setColor(new Color(24, 22, 18));
        g.fillRect(5, 5, DESIGN_WIDTH - 10, DESIGN_HEIGHT - 10);

        g.setColor(new Color(86, 76, 58));
        g.fillRect(8, 8, DESIGN_WIDTH - 16, DESIGN_HEIGHT - 16);

        g.setColor(new Color(42, 37, 30));
        g.fillRect(11, 11, DESIGN_WIDTH - 22, DESIGN_HEIGHT - 22);

        g.setColor(new Color(125, 107, 74));
        g.setStroke(new BasicStroke(2));
        g.drawRect(1, 1, DESIGN_WIDTH - 2, DESIGN_HEIGHT - 2);

        g.setColor(new Color(23, 21, 17));
        g.drawRect(7, 7, DESIGN_WIDTH - 14, DESIGN_HEIGHT - 14);
    }

    private void drawSkillGrid(Graphics2D g)
    {
        for (int i = 0; i < SKILLS.length; i++)
        {
            int col = i % 3;
            int row = i / 3;

            int x = START_X + col * (CELL_W + CELL_GAP);
            int y = START_Y + row * (CELL_H + CELL_GAP);

            drawSkillCell(g, SKILLS[i], x, y);
        }
    }

    private void drawSkillCell(Graphics2D g, Skill skill, int x, int y)
    {
        int realLevel = client.getRealSkillLevel(skill);
        int boostedLevel = client.getBoostedSkillLevel(skill);

        g.setColor(new Color(72, 72, 66));
        g.fillRect(x, y, CELL_W, CELL_H);

        g.setColor(new Color(42, 42, 38));
        g.drawRect(x, y, CELL_W, CELL_H);

        g.setColor(new Color(115, 115, 105));
        g.drawLine(x + 1, y + 1, x + CELL_W - 2, y + 1);
        g.drawLine(x + 1, y + 1, x + 1, y + CELL_H - 2);

        g.setColor(new Color(18, 16, 13));
        g.drawLine(x + 1, y + CELL_H - 1, x + CELL_W - 1, y + CELL_H - 1);
        g.drawLine(x + CELL_W - 1, y + 1, x + CELL_W - 1, y + CELL_H - 1);

        Image icon = skillIconManager.getSkillImage(skill);

        if (icon != null)
        {
            drawSkillIcon(g, icon, skill, x, y);
        }

        g.setFont(new Font("Arial", Font.BOLD, 16));
        g.setColor(Color.BLACK);
        g.drawString(String.valueOf(boostedLevel), x + 62, y + 17);
        g.drawString(String.valueOf(realLevel), x + 72, y + 34);

        g.setColor(new Color(255, 255, 50));
        g.drawString(String.valueOf(boostedLevel), x + 61, y + 16);
        g.drawString(String.valueOf(realLevel), x + 71, y + 33);


    }
    private void drawSkillIcon(Graphics2D g, Image icon, Skill skill, int cellX, int cellY)
    {
        int iconX = cellX + 5;
        int iconY = cellY + 5;
        int iconW = 31;
        int iconH = 31;

        switch (skill)
        {
            case ATTACK:
                iconX = cellX + 4;
                iconY = cellY + 5;
                iconW = 32;
                iconH = 32;
                break;

            case HITPOINTS:
                iconX = cellX + 5;
                iconY = cellY + 4;
                iconW = 31;
                iconH = 31;
                break;

            case MINING:
                iconX = cellX + 4;
                iconY = cellY + 4;
                iconW = 34;
                iconH = 34;
                break;

            case STRENGTH:
                iconX = cellX + 5;
                iconY = cellY + 5;
                iconW = 31;
                iconH = 31;
                break;

            case AGILITY:
                iconX = cellX + 5;
                iconY = cellY + 5;
                iconW = 32;
                iconH = 32;
                break;

            case SMITHING:
                iconX = cellX + 6;
                iconY = cellY + 5;
                iconW = 31;
                iconH = 31;
                break;

            case DEFENCE:
                iconX = cellX + 5;
                iconY = cellY + 5;
                iconW = 31;
                iconH = 31;
                break;

            case HERBLORE:
                iconX = cellX + 4;
                iconY = cellY + 6;
                iconW = 34;
                iconH = 30;
                break;

            case FISHING:
                iconX = cellX + 5;
                iconY = cellY + 5;
                iconW = 33;
                iconH = 31;
                break;

            case RANGED:
                iconX = cellX + 5;
                iconY = cellY + 5;
                iconW = 32;
                iconH = 32;
                break;

            case THIEVING:
                iconX = cellX + 5;
                iconY = cellY + 9;
                iconW = 32;
                iconH = 23;
                break;

            case COOKING:
                iconX = cellX + 6;
                iconY = cellY + 5;
                iconW = 30;
                iconH = 31;
                break;

            case PRAYER:
                iconX = cellX + 5;
                iconY = cellY + 4;
                iconW = 33;
                iconH = 33;
                break;

            case CRAFTING:
                iconX = cellX + 5;
                iconY = cellY + 5;
                iconW = 32;
                iconH = 32;
                break;

            case FIREMAKING:
                iconX = cellX + 5;
                iconY = cellY + 5;
                iconW = 33;
                iconH = 31;
                break;

            case MAGIC:
                iconX = cellX + 5;
                iconY = cellY + 4;
                iconW = 32;
                iconH = 34;
                break;

            case FLETCHING:
                iconX = cellX + 5;
                iconY = cellY + 5;
                iconW = 32;
                iconH = 32;
                break;

            case WOODCUTTING:
                iconX = cellX + 5;
                iconY = cellY + 5;
                iconW = 33;
                iconH = 32;
                break;

            case RUNECRAFT:
                iconX = cellX + 5;
                iconY = cellY + 5;
                iconW = 31;
                iconH = 32;
                break;

            case SLAYER:
                iconX = cellX + 4;
                iconY = cellY + 5;
                iconW = 34;
                iconH = 32;
                break;

            case FARMING:
                iconX = cellX + 6;
                iconY = cellY + 5;
                iconW = 31;
                iconH = 32;
                break;

            case CONSTRUCTION:
                iconX = cellX + 5;
                iconY = cellY + 5;
                iconW = 32;
                iconH = 32;
                break;

            case HUNTER:
                iconX = cellX + 5;
                iconY = cellY + 5;
                iconW = 32;
                iconH = 32;
                break;

            case SAILING:
                iconX = cellX + 5;
                iconY = cellY + 5;
                iconW = 32;
                iconH = 32;
                break;

            default:
                break;
        }

        g.drawImage(icon, iconX, iconY, iconW, iconH, null);
    }
    private void drawTotalLevel(Graphics2D g)
    {
        int totalLevel = getTotalLevel();

        int x = 17;
        int y = 386;
        int width = DESIGN_WIDTH - 34;
        int height = 27;

        g.setColor(new Color(17, 15, 12));
        g.fillRect(x, y, width, height);

        g.setColor(new Color(102, 87, 61));
        g.drawRect(x, y, width, height);

        String text = "Total level: " + totalLevel;

        g.setFont(new Font("Arial", Font.BOLD, 14));

        int textWidth = g.getFontMetrics().stringWidth(text);
        int textX = x + (width - textWidth) / 2;
        int textY = y + 18;

        g.setColor(Color.BLACK);
        g.drawString(text, textX + 1, textY + 1);

        g.setColor(new Color(255, 255, 50));
        g.drawString(text, textX, textY);
    }

    private int getTotalLevel()
    {
        int total = 0;

        for (Skill skill : SKILLS)
        {
            int level = client.getRealSkillLevel(skill);

            if (level > 0)
            {
                total += level;
            }
        }

        return total;
    }
}