package com.example;

import java.awt.Color;
import javax.inject.Inject;
import net.runelite.api.Client;
import net.runelite.api.Quest;
import net.runelite.api.QuestState;

public class QuestProgressOverlay extends ProgressBarOverlayBase
{
    private final Client client;

    @Inject
    public QuestProgressOverlay(Client client, ExampleConfig config)
    {
        super(config);
        this.client = client;
    }

    @Override
    protected boolean isEnabled()
    {
        return config.showQuestProgressBar();
    }

    @Override
    protected int getBarWidth()
    {
        return config.questProgressWidth();
    }

    @Override
    protected int getBarHeight()
    {
        return config.questProgressHeight();
    }

    @Override
    protected Color getBarColor()
    {
        return config.questProgressColor();
    }

    @Override
    protected String getIconText()
    {
        return "Q";
    }

    @Override
    protected ProgressData getProgressData()
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
}