package com.example;

import java.awt.Color;
import javax.inject.Inject;
import net.runelite.api.Client;
import net.runelite.api.Experience;
import net.runelite.api.Skill;

public class XpProgressOverlay extends ProgressBarOverlayBase
{
    private final Client client;
    private Skill mostRecentSkill = Skill.ATTACK;

    @Inject
    public XpProgressOverlay(Client client, ExampleConfig config)
    {
        super(config);
        this.client = client;
    }

    public void setMostRecentSkill(Skill skill)
    {
        if (skill == null || skill == Skill.OVERALL)
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
    protected boolean isEnabled()
    {
        return config.showXpProgressBar();
    }

    @Override
    protected int getBarWidth()
    {
        return config.xpProgressWidth();
    }

    @Override
    protected int getBarHeight()
    {
        return config.xpProgressHeight();
    }

    @Override
    protected Color getBarColor()
    {
        return config.xpProgressColor();
    }

    @Override
    protected String getIconText()
    {
        return "XP";
    }

    @Override
    protected ProgressData getProgressData()
    {
        Skill skill = mostRecentSkill;

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
}