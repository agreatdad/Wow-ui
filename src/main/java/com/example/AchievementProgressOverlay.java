package com.example;

import java.awt.Color;
import javax.inject.Inject;

public class AchievementProgressOverlay extends ProgressBarOverlayBase
{
    @Inject
    public AchievementProgressOverlay(ExampleConfig config)
    {
        super(config);
    }

    @Override
    protected boolean isEnabled()
    {
        return config.showAchievementProgressBar();
    }

    @Override
    protected int getBarWidth()
    {
        return config.achievementProgressWidth();
    }

    @Override
    protected int getBarHeight()
    {
        return config.achievementProgressHeight();
    }

    @Override
    protected Color getBarColor()
    {
        return config.achievementProgressColor();
    }

    @Override
    protected String getIconText()
    {
        return "A";
    }

    @Override
    protected ProgressData getProgressData()
    {
        return new ProgressData("Achievements 0/0", 0, 1);
    }
}