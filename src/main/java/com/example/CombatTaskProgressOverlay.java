package com.example;

import java.awt.Color;
import javax.inject.Inject;

public class CombatTaskProgressOverlay extends ProgressBarOverlayBase
{
    private final CombatAchievementTracker combatAchievementTracker;

    @Inject
    public CombatTaskProgressOverlay(ExampleConfig config, CombatAchievementTracker combatAchievementTracker)
    {
        super(config);
        this.combatAchievementTracker = combatAchievementTracker;
    }

    @Override
    protected boolean isEnabled()
    {
        return config.showCombatTaskProgressBar();
    }

    @Override
    protected int getBarWidth()
    {
        return config.combatTaskProgressWidth();
    }

    @Override
    protected int getBarHeight()
    {
        return config.combatTaskProgressHeight();
    }

    @Override
    protected Color getBarColor()
    {
        return config.combatTaskProgressColor();
    }

    @Override
    protected String getIconText()
    {
        return "CA";
    }

    @Override
    protected ProgressData getProgressData()
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
}