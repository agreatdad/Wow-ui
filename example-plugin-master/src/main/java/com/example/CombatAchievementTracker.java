package com.example;

import java.util.EnumMap;
import java.util.Map;
import javax.inject.Inject;
import javax.inject.Singleton;
import net.runelite.api.Client;
import net.runelite.api.EnumComposition;
import net.runelite.api.StructComposition;
import net.runelite.api.gameval.VarPlayerID;

@Singleton
public class CombatAchievementTracker
{
    public enum Tier
    {
        NONE("No CA Tier", 0, 0),
        EASY("Easy", 3981, 1),
        MEDIUM("Medium", 3982, 2),
        HARD("Hard", 3983, 3),
        ELITE("Elite", 3984, 4),
        MASTER("Master", 3985, 5),
        GRANDMASTER("Grandmaster", 3986, 6);

        private final String title;
        private final int enumId;
        private final int pointsPerTask;

        Tier(String title, int enumId, int pointsPerTask)
        {
            this.title = title;
            this.enumId = enumId;
            this.pointsPerTask = pointsPerTask;
        }

        public String getTitle()
        {
            return title;
        }

        public int getEnumId()
        {
            return enumId;
        }

        public int getPointsPerTask()
        {
            return pointsPerTask;
        }
    }

    public static class Progress
    {
        private final int totalPoints;
        private final int completedTasks;
        private final int totalTasks;
        private final Tier rewardTier;
        private final Map<Tier, int[]> perTier;

        public Progress(int totalPoints, int completedTasks, int totalTasks, Tier rewardTier, Map<Tier, int[]> perTier)
        {
            this.totalPoints = totalPoints;
            this.completedTasks = completedTasks;
            this.totalTasks = totalTasks;
            this.rewardTier = rewardTier;
            this.perTier = perTier;
        }

        public int getTotalPoints()
        {
            return totalPoints;
        }

        public int getCompletedTasks()
        {
            return completedTasks;
        }

        public int getTotalTasks()
        {
            return totalTasks;
        }

        public Tier getRewardTier()
        {
            return rewardTier;
        }

        public Map<Tier, int[]> getPerTier()
        {
            return perTier;
        }
    }

    private static final int PARAM_TASK_ID = 1306;

    private static final int[] VARP_IDS =
            {
                    VarPlayerID.CA_TASK_COMPLETED_0,
                    VarPlayerID.CA_TASK_COMPLETED_1,
                    VarPlayerID.CA_TASK_COMPLETED_2,
                    VarPlayerID.CA_TASK_COMPLETED_3,
                    VarPlayerID.CA_TASK_COMPLETED_4,
                    VarPlayerID.CA_TASK_COMPLETED_5,
                    VarPlayerID.CA_TASK_COMPLETED_6,
                    VarPlayerID.CA_TASK_COMPLETED_7,
                    VarPlayerID.CA_TASK_COMPLETED_8,
                    VarPlayerID.CA_TASK_COMPLETED_9,
                    VarPlayerID.CA_TASK_COMPLETED_10,
                    VarPlayerID.CA_TASK_COMPLETED_11,
                    VarPlayerID.CA_TASK_COMPLETED_12,
                    VarPlayerID.CA_TASK_COMPLETED_13,
                    VarPlayerID.CA_TASK_COMPLETED_14,
                    VarPlayerID.CA_TASK_COMPLETED_15,
                    VarPlayerID.CA_TASK_COMPLETED_16,
                    VarPlayerID.CA_TASK_COMPLETED_17,
                    VarPlayerID.CA_TASK_COMPLETED_18,
                    VarPlayerID.CA_TASK_COMPLETED_19
            };

    private final Client client;

    private Progress progress = new Progress(0, 0, 0, Tier.NONE, new EnumMap<>(Tier.class));

    @Inject
    public CombatAchievementTracker(Client client)
    {
        this.client = client;
    }

    public Progress getProgress()
    {
        return progress;
    }

    public void refresh()
    {
        progress = computeProgress();
    }

    private Progress computeProgress()
    {
        int totalPoints = 0;
        int completedTasks = 0;
        int totalTasks = 0;

        Map<Tier, int[]> perTier = new EnumMap<>(Tier.class);

        for (Tier tier : Tier.values())
        {
            if (tier == Tier.NONE)
            {
                continue;
            }

            EnumComposition enumComposition = client.getEnum(tier.getEnumId());

            if (enumComposition == null)
            {
                perTier.put(tier, new int[] {0, 0});
                continue;
            }

            int tierCompleted = 0;
            int tierTotal = 0;

            for (int structId : enumComposition.getIntVals())
            {
                StructComposition structComposition = client.getStructComposition(structId);

                if (structComposition == null)
                {
                    continue;
                }

                int taskId = structComposition.getIntValue(PARAM_TASK_ID);

                if (taskId < 0)
                {
                    continue;
                }

                tierTotal++;
                totalTasks++;

                if (isCompleted(taskId))
                {
                    tierCompleted++;
                    completedTasks++;
                    totalPoints += tier.getPointsPerTask();
                }
            }

            perTier.put(tier, new int[] {tierCompleted, tierTotal});
        }

        return new Progress(
                totalPoints,
                completedTasks,
                totalTasks,
                getRewardTierFromPoints(totalPoints),
                perTier
        );
    }

    private boolean isCompleted(int taskId)
    {
        int varpIndex = taskId / 32;
        int bitIndex = taskId % 32;

        if (varpIndex < 0 || varpIndex >= VARP_IDS.length)
        {
            return false;
        }

        return (client.getVarpValue(VARP_IDS[varpIndex]) & (1 << bitIndex)) != 0;
    }

    private Tier getRewardTierFromPoints(int points)
    {
        if (points >= 2630)
        {
            return Tier.GRANDMASTER;
        }

        if (points >= 1904)
        {
            return Tier.MASTER;
        }

        if (points >= 1064)
        {
            return Tier.ELITE;
        }

        if (points >= 416)
        {
            return Tier.HARD;
        }

        if (points >= 161)
        {
            return Tier.MEDIUM;
        }

        if (points >= 41)
        {
            return Tier.EASY;
        }

        return Tier.NONE;
    }
}