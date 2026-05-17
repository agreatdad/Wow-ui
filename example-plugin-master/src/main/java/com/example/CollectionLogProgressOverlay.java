package com.example;

import java.awt.Color;
import javax.inject.Inject;

public class CollectionLogProgressOverlay extends ProgressBarOverlayBase
{
    @Inject
    public CollectionLogProgressOverlay(ExampleConfig config)
    {
        super(config);
    }

    @Override
    protected boolean isEnabled()
    {
        return config.showCollectionLogProgressBar();
    }

    @Override
    protected int getBarWidth()
    {
        return config.collectionLogProgressWidth();
    }

    @Override
    protected int getBarHeight()
    {
        return config.collectionLogProgressHeight();
    }

    @Override
    protected Color getBarColor()
    {
        return config.collectionLogProgressColor();
    }

    @Override
    protected String getIconText()
    {
        return "CL";
    }

    @Override
    protected ProgressData getProgressData()
    {
        return new ProgressData("Collection Log 0/0", 0, 1);
    }
}