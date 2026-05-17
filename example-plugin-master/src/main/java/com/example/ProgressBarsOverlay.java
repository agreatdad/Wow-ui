package com.example;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import javax.inject.Inject;
import net.runelite.api.Client;
import net.runelite.api.Experience;
import net.runelite.api.Quest;
import net.runelite.api.QuestState;
import net.runelite.api.Skill;
import net.runelite.client.ui.overlay.Overlay;
import net.runelite.client.ui.overlay.OverlayLayer;
import net.runelite.client.ui.overlay.OverlayPosition;

public class ProgressBarsOverlay extends Overlay
{
    private static final int BAR_WIDTH = 190;
    private static final int BAR_HEIGHT = 20;
    private static final int BAR_GAP = 5;
    private static final int SEGMENTS = 10;

    private final Client client;
    private final ExampleConfig config;
    private final CombatAchievementTracker combatAchievementTracker;

    private Skill mostRecentSkill = Skill.ATTACK;

    @Inject
    public ProgressBarsOverlay(
            Client client,
            ExampleConfig config,
            CombatAchievementTracker combatAchievementTracker
    )
    {
        this.client = client;
        this.config = config;
        this.combatAchievementTracker = combatAchievementTracker;

        setPosition(OverlayPosition.TOP_LEFT);
        setLayer(OverlayLayer.ABOVE_WIDGETS);
        setMovable(true);
        setSnappable(true);
        setResizable(true);
    }

    public void setMostRecentSkill(Skill skill)
    {
        if (skill == null)
        {
            return;
        }

        if (config.progressIgnoreHitpoints() && skill == Skill.HITPOINTS)
        {
            return;
        }

        mostRecentSkill = skill;
    }

    @Override
    public Dimension render(Graphics2D graphics)
    {
        if (!config.showProgressBars())
        {
            return null;
        }

        graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        graphics.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        int y = 0;
        int visibleBars = 0;

        if (config.showXpProgressBar())
        {
            ProgressData xp = getXpProgress();
            drawSegmentedBar(graphics, 0, y, xp, config.xpProgressColor(), config.showProgressBarIcons(), "XP");
            y += BAR_HEIGHT + BAR_GAP;
            visibleBars++;
        }

        if (config.showQuestProgressBar())
        {
            ProgressData quests = getQuestProgress();
            drawSegmentedBar(graphics, 0, y, quests, config.questProgressColor(), config.showProgressBarIcons(), "Q");
            y += BAR_HEIGHT + BAR_GAP;
            visibleBars++;
        }

        if (config.showCombatTaskProgressBar())
        {
            ProgressData cas = getCombatAchievementProgress();
            drawSegmentedBar(graphics, 0, y, cas, config.combatTaskProgressColor(), config.showProgressBarIcons(), "CA");
            y += BAR_HEIGHT + BAR_GAP;
            visibleBars++;
        }

        if (config.showAchievementProgressBar())
        {
            ProgressData achievements = getAchievementDiaryProgress();
            drawSegmentedBar(graphics, 0, y, achievements, config.achievementProgressColor(), config.showProgressBarIcons(), "A");
            y += BAR_HEIGHT + BAR_GAP;
            visibleBars++;
        }

        if (config.showCollectionLogProgressBar())
        {
            ProgressData collection = getCollectionLogProgress();
            drawSegmentedBar(graphics, 0, y, collection, config.collectionLogProgressColor(), config.showProgressBarIcons(), "CL");
            y += BAR_HEIGHT + BAR_GAP;
            visibleBars++;
        }

        if (visibleBars == 0)
        {
            return null;
        }

        return new Dimension(BAR_WIDTH, y - BAR_GAP);
    }

    private ProgressData getXpProgress()
    {
        Skill skill = mostRecentSkill;

        if (skill == null || skill == Skill.OVERALL)
        {
            skill = Skill.ATTACK;
        }

        int level = client.getRealSkillLevel(skill);
        int xp = client.getSkillExperience(skill);

        if (level >= 99)
        {
            return new ProgressData(skill.getName() + " 99", 1, 1);
        }

        int currentLevelXp = Experience.getXpForLevel(level);
        int nextLevelXp = Experience.getXpForLevel(level + 1);

        int current = xp - currentLevelXp;
        int max = nextLevelXp - currentLevelXp;

        return new ProgressData(skill.getName() + " " + current + "/" + max, current, max);
    }

    private ProgressData getQuestProgress()
    {
        int complete = 0;
        int total = 0;

        for (Quest quest : Quest.values())
        {
            QuestState state = quest.getState(client);

            if (state == null)
            {
                continue;
            }

            total++;

            if (state == QuestState.FINISHED)
            {
                complete++;
            }
        }

        return new ProgressData("Quests " + complete + "/" + total, complete, total);
    }

    private ProgressData getCombatAchievementProgress()
    {
        CombatAchievementTracker.Progress progress = combatAchievementTracker.getProgress();

        if (progress == null || progress.getTotalTasks() <= 0)
        {
            return new ProgressData("Combat Tasks 0/0", 0, 1);
        }

        return new ProgressData(
                "Combat Tasks " + progress.getCompletedTasks() + "/" + progress.getTotalTasks(),
                progress.getCompletedTasks(),
                progress.getTotalTasks()
        );
    }

    private ProgressData getAchievementDiaryProgress()
    {
        return new ProgressData("Achievements 0/0", 0, 1);
    }

    private ProgressData getCollectionLogProgress()
    {
        return new ProgressData("Collection Log 0/0", 0, 1);
    }

    private void drawSegmentedBar(Graphics2D g, int x, int y, ProgressData data, Color fillColor, boolean showIcon, String iconText)
    {
        int filledSegments = getFilledSegments(data.current, data.max);

        g.setColor(new Color(0, 0, 0, 210));
        g.fillRoundRect(x, y, BAR_WIDTH, BAR_HEIGHT, 8, 8);

        g.setColor(new Color(80, 80, 80));
        g.setStroke(new BasicStroke(1));
        g.drawRoundRect(x, y, BAR_WIDTH, BAR_HEIGHT, 8, 8);

        int innerX = x + 4;
        int innerY = y + 4;
        int innerW = BAR_WIDTH - 8;
        int innerH = BAR_HEIGHT - 8;

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
            text = iconText + "  " + text;
        }

        Font oldFont = g.getFont();
        g.setFont(oldFont.deriveFont(Font.BOLD, 11f));

        FontMetrics metrics = g.getFontMetrics();
        int textX = x + (BAR_WIDTH - metrics.stringWidth(text)) / 2;
        int textY = y + ((BAR_HEIGHT - metrics.getHeight()) / 2) + metrics.getAscent();

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

    private static class ProgressData
    {
        private final String text;
        private final int current;
        private final int max;

        private ProgressData(String text, int current, int max)
        {
            this.text = text;
            this.current = current;
            this.max = max;
        }
    }
}