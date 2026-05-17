package com.example;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import javax.inject.Inject;
import net.runelite.api.Client;
import net.runelite.api.EquipmentInventorySlot;
import net.runelite.api.InventoryID;
import net.runelite.api.Item;
import net.runelite.api.ItemContainer;
import net.runelite.api.Skill;
import net.runelite.client.game.ItemManager;
import net.runelite.client.game.ItemStats;
import net.runelite.client.ui.overlay.Overlay;
import net.runelite.client.ui.overlay.OverlayLayer;
import net.runelite.client.ui.overlay.OverlayPosition;
import net.runelite.client.ui.overlay.OverlayPriority;
import net.runelite.api.Prayer;

public class PaperdollOverlay extends Overlay
{
    private static final int DESIGN_WIDTH = 300;
    private static final int DESIGN_HEIGHT = 315;
    private static final double DEFAULT_SCALE = 1;

    private static final int SLOT = 34;

    private final Client client;
    private final ItemManager itemManager;

    private boolean open = false;

    @Inject
    public PaperdollOverlay(Client client, ItemManager itemManager)
    {
        this.client = client;
        this.itemManager = itemManager;

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

    public void setOpen(boolean open)
    {
        this.open = open;
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

        drawBackground(g);
        drawHeader(g);
        drawEquipmentPanel(g);
        drawStatsPanel(g);

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

    private void drawBackground(Graphics2D g)
    {
        g.setColor(new Color(34, 26, 18, 235));
        g.fillRoundRect(0, 0, DESIGN_WIDTH, DESIGN_HEIGHT, 10, 10);

        g.setColor(new Color(132, 96, 48));
        g.setStroke(new BasicStroke(2));
        g.drawRoundRect(1, 1, DESIGN_WIDTH - 2, DESIGN_HEIGHT - 2, 10, 10);


    }

    private void drawHeader(Graphics2D g)
    {
        String name = "Unknown";
        int combat = 0;
        int total = getTotalLevel();

        if (client.getLocalPlayer() != null)
        {
            name = client.getLocalPlayer().getName();
            combat = client.getLocalPlayer().getCombatLevel();
        }

        g.setFont(new Font("Arial", Font.BOLD, 16));
        g.setColor(new Color(255, 210, 80));
        drawCentered(g, name, 0, 9, DESIGN_WIDTH, 18);

        g.setFont(new Font("Arial", Font.BOLD, 12));
        g.setColor(new Color(230, 220, 190));
        drawCentered(g, "Combat: " + combat + "     Total: " + total, 0, 29, DESIGN_WIDTH, 14);


    }

    private void drawEquipmentPanel(Graphics2D g)
    {
        ItemContainer equipment = client.getItemContainer(InventoryID.EQUIPMENT);

        drawSlot(g, equipment, EquipmentInventorySlot.HEAD.getSlotIdx(), 43, 60);
        drawSlot(g, equipment, EquipmentInventorySlot.CAPE.getSlotIdx(), 4, 100);
        drawSlot(g, equipment, EquipmentInventorySlot.AMULET.getSlotIdx(), 43, 100);
        drawSlot(g, equipment, EquipmentInventorySlot.AMMO.getSlotIdx(), 82, 100);

        drawSlot(g, equipment, EquipmentInventorySlot.WEAPON.getSlotIdx(), 4, 140);
        drawSlot(g, equipment, EquipmentInventorySlot.BODY.getSlotIdx(), 43, 140);
        drawSlot(g, equipment, EquipmentInventorySlot.SHIELD.getSlotIdx(), 82, 140);

        drawSlot(g, equipment, EquipmentInventorySlot.LEGS.getSlotIdx(), 43, 180);

        drawSlot(g, equipment, EquipmentInventorySlot.GLOVES.getSlotIdx(), 04, 220);
        drawSlot(g, equipment, EquipmentInventorySlot.BOOTS.getSlotIdx(), 43, 220);
        drawSlot(g, equipment, EquipmentInventorySlot.RING.getSlotIdx(), 82, 220);
    }

    private void drawSlot(Graphics2D g, ItemContainer equipment, int slotIndex, int x, int y)
    {
        g.setColor(new Color(25, 20, 15));
        g.fillRect(x, y, SLOT, SLOT);

        g.setColor(new Color(145, 106, 54));
        g.drawRect(x, y, SLOT, SLOT);

        g.setColor(new Color(65, 47, 28));
        g.drawRect(x + 2, y + 2, SLOT - 4, SLOT - 4);

        if (equipment == null)
        {
            return;
        }

        Item item = equipment.getItem(slotIndex);

        if (item == null || item.getId() <= 0)
        {
            return;
        }

        Image image = itemManager.getImage(item.getId());

        if (image != null)
        {
            g.drawImage(image, x + 4, y + 4, SLOT - 8, SLOT - 8, null);
        }
    }

    private void drawStatsPanel(Graphics2D g)
    {
        BonusTotals totals = getBonusTotals();

        g.setFont(new Font("Arial", Font.BOLD, 13));
        g.setColor(new Color(255, 210, 80));
        drawCentered(g, "Equipment Stats", 124, 60, 180, 16);

        g.setFont(new Font("Arial", Font.PLAIN, 11));

        int x = 134;
        int y = 93;
        int line = 17;

        drawCombinedLine(g, "Stab Att/Def", totals.astab, totals.dstab, x, y); y += line;
        drawCombinedLine(g, "Slash Att/Def", totals.aslash, totals.dslash, x, y); y += line;
        drawCombinedLine(g, "Crush Att/Def", totals.acrush, totals.dcrush, x, y); y += line;
        drawCombinedLine(g, "Magic Att/Def", totals.amagic, totals.dmagic, x, y); y += line;
        drawCombinedLine(g, "Range Att/Def", totals.arange, totals.drange, x, y); y += line + 8;

        drawSingleLine(g, "Melee Str", totals.str, x, y); y += line;
        drawSingleLine(g, "Ranged Str", totals.rstr, x, y); y += line;
        drawSingleLine(g, "Magic Dmg", totals.mdmg, x, y); y += line;
        drawSingleLine(g, "Prayer", totals.prayer, x, y); y += line + 8;

        int maxHit = calculateMaxHit(totals);

        g.setColor(new Color(235, 225, 195));

        if (totals.weaponSpeed > 0)
        {
            double seconds = totals.weaponSpeed * 0.60;
            double maxDps = maxHit / seconds;
            double averageDps = maxDps / 2.0;

            g.drawString("Speed: " + totals.weaponSpeed + " ticks", x, y); y += line;
            g.drawString("Max Hit: " + maxHit, x, y); y += line;
            g.drawString("Max DPS: " + formatOneDecimal(maxDps), x, y); y += line;
            g.drawString("Avg DPS: " + formatOneDecimal(averageDps), x, y);
        }
        else
        {
            g.drawString("Speed: -", x, y); y += line;
            g.drawString("Max Hit: " + maxHit, x, y);
        }
    }
    private int calculateMaxHit(BonusTotals totals)
    {
        CombatType combatType = getLikelyCombatType(totals);

        if (combatType == CombatType.RANGED)
        {
            return calculateRangedMaxHit(totals);
        }

        if (combatType == CombatType.MAGIC)
        {
            return calculateMagicMaxHit(totals);
        }

        return calculateMeleeMaxHit(totals);
    }

    private CombatType getLikelyCombatType(BonusTotals totals)
    {
        if (totals.rstr > totals.str && totals.rstr > 0)
        {
            return CombatType.RANGED;
        }

        if (totals.mdmg > 0 && totals.str <= 0 && totals.rstr <= 0)
        {
            return CombatType.MAGIC;
        }

        return CombatType.MELEE;
    }

    private int calculateMeleeMaxHit(BonusTotals totals)
    {
        int boostedStrength = client.getBoostedSkillLevel(Skill.STRENGTH);

        double prayerMultiplier = getStrengthPrayerMultiplier();
        int styleBonus = getMeleeStyleBonus();

        int effectiveStrength = (int) Math.floor(
                Math.floor(boostedStrength * prayerMultiplier) + styleBonus + 8
        );

        int strengthBonus = totals.str;

        int maxHit = (int) Math.floor(0.5 + effectiveStrength * (strengthBonus + 64) / 640.0);

        return Math.max(0, maxHit);
    }

    private int calculateRangedMaxHit(BonusTotals totals)
    {
        int boostedRanged = client.getBoostedSkillLevel(Skill.RANGED);

        double prayerMultiplier = getRangedPrayerMultiplier();
        int styleBonus = getRangedStyleBonus();

        int effectiveRangedStrength = (int) Math.floor(
                Math.floor(boostedRanged * prayerMultiplier) + styleBonus + 8
        );

        int rangedStrengthBonus = totals.rstr;

        int maxHit = (int) Math.floor(0.5 + effectiveRangedStrength * (rangedStrengthBonus + 64) / 640.0);

        return Math.max(0, maxHit);
    }

    private int calculateMagicMaxHit(BonusTotals totals)
    {
        int boostedMagic = client.getBoostedSkillLevel(Skill.MAGIC);

        /*
         * This is a safe paperdoll approximation.
         * The full Max-Hit plugin handles powered staves, autocast spell IDs,
         * spellbook, elemental weaknesses, Tumeken's shadow, Virtus, Smoke staff,
         * and opponent-specific effects.
         *
         * For this paperdoll display, use a simple base magic damage estimate:
         * base = magic level / 3, then apply magic damage percentage.
         */
        int baseMagicHit = Math.max(1, boostedMagic / 3);

        double magicDamageBonus = totals.mdmg / 100.0;
        double prayerBonus = isPrayerActiveSafe(Prayer.AUGURY) ? 0.04 : 0.0;

        int maxHit = (int) Math.floor(baseMagicHit * (1.0 + magicDamageBonus + prayerBonus));

        return Math.max(0, maxHit);
    }

    private double getStrengthPrayerMultiplier()
    {
        if (isPrayerActiveSafe(Prayer.PIETY))
        {
            return 1.23;
        }

        if (isPrayerActiveSafe(Prayer.CHIVALRY))
        {
            return 1.18;
        }

        if (isPrayerActiveSafe(Prayer.ULTIMATE_STRENGTH))
        {
            return 1.15;
        }

        if (isPrayerActiveSafe(Prayer.SUPERHUMAN_STRENGTH))
        {
            return 1.10;
        }

        if (isPrayerActiveSafe(Prayer.BURST_OF_STRENGTH))
        {
            return 1.05;
        }

        return 1.0;
    }

    private double getRangedPrayerMultiplier()
    {
        if (isPrayerActiveSafe(Prayer.RIGOUR))
        {
            return 1.23;
        }

        if (isPrayerActiveSafe(Prayer.EAGLE_EYE))
        {
            return 1.15;
        }

        if (isPrayerActiveSafe(Prayer.HAWK_EYE))
        {
            return 1.10;
        }

        if (isPrayerActiveSafe(Prayer.SHARP_EYE))
        {
            return 1.05;
        }

        return 1.0;
    }

    private int getMeleeStyleBonus()
    {
        /*
         * Full Max-Hit reads current attack style from weapon style varbits.
         * This simple paperdoll version assumes aggressive/strength style.
         */
        return 3;
    }

    private int getRangedStyleBonus()
    {
        /*
         * Full Max-Hit only gives +3 for accurate ranged style.
         * This simple paperdoll version uses +3 as a reasonable default.
         */
        return 3;
    }

    private boolean isPrayerActiveSafe(Prayer prayer)
    {
        try
        {
            return client.isPrayerActive(prayer);
        }
        catch (Exception ignored)
        {
            return false;
        }
    }

    private String formatOneDecimal(double value)
    {
        return String.format("%.1f", value);
    }

    private enum CombatType
    {
        MELEE,
        RANGED,
        MAGIC
    }

    private void drawCombinedLine(Graphics2D g, String label, int attack, int defence, int x, int y)
    {
        g.setColor(new Color(235, 225, 195));
        g.drawString(label + ":", x, y);

        g.setColor(getValueColor(attack));
        g.drawString(formatSigned(attack), x + 92, y);

        g.setColor(new Color(235, 225, 195));
        g.drawString("/", x + 116, y);

        g.setColor(getValueColor(defence));
        g.drawString(formatSigned(defence), x + 124, y);
    }

    private void drawSingleLine(Graphics2D g, String label, int value, int x, int y)
    {
        g.setColor(new Color(235, 225, 195));
        g.drawString(label + ":", x, y);

        g.setColor(getValueColor(value));
        g.drawString(formatSigned(value), x + 92, y);
    }

    private Color getValueColor(int value)
    {
        if (value > 0)
        {
            return new Color(120, 255, 120);
        }

        if (value < 0)
        {
            return new Color(255, 120, 120);
        }

        return new Color(235, 225, 195);
    }

    private String formatSigned(int value)
    {
        if (value > 0)
        {
            return "+" + value;
        }

        return String.valueOf(value);
    }

    private BonusTotals getBonusTotals()
    {
        BonusTotals totals = new BonusTotals();

        ItemContainer equipment = client.getItemContainer(InventoryID.EQUIPMENT);

        if (equipment == null)
        {
            return totals;
        }

        Item[] items = equipment.getItems();

        for (Item item : items)
        {
            if (item == null || item.getId() <= 0)
            {
                continue;
            }

            ItemStats itemStats = itemManager.getItemStats(item.getId());

            if (itemStats == null || itemStats.getEquipment() == null)
            {
                continue;
            }

            var stats = itemStats.getEquipment();

            totals.astab += stats.getAstab();
            totals.aslash += stats.getAslash();
            totals.acrush += stats.getAcrush();
            totals.amagic += stats.getAmagic();
            totals.arange += stats.getArange();

            totals.dstab += stats.getDstab();
            totals.dslash += stats.getDslash();
            totals.dcrush += stats.getDcrush();
            totals.dmagic += stats.getDmagic();
            totals.drange += stats.getDrange();

            totals.str += stats.getStr();
            totals.rstr += stats.getRstr();
            totals.mdmg += stats.getMdmg();
            totals.prayer += stats.getPrayer();

            if (stats.getAspeed() > 0)
            {
                totals.weaponSpeed = stats.getAspeed();
            }
        }

        return totals;
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

    private void drawCentered(Graphics2D g, String text, int x, int y, int width, int height)
    {
        FontMetrics metrics = g.getFontMetrics();
        int textX = x + ((width - metrics.stringWidth(text)) / 2);
        int textY = y + ((height - metrics.getHeight()) / 2) + metrics.getAscent();

        g.drawString(text, textX, textY);
    }

    private static class BonusTotals
    {
        int astab;
        int aslash;
        int acrush;
        int amagic;
        int arange;

        int dstab;
        int dslash;
        int dcrush;
        int dmagic;
        int drange;

        int str;
        int rstr;
        int mdmg;
        int prayer;

        int weaponSpeed;
    }
}