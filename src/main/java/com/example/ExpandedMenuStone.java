package com.example;

import java.util.function.Function;
import net.runelite.api.gameval.InterfaceID;

public enum ExpandedMenuStone
{
    COMBAT(
            new int[]
                    {
                            InterfaceID.Toplevel.STONE0,
                            InterfaceID.ToplevelOsrsStretch.STONE0,
                            InterfaceID.ToplevelPreEoc.ICON0
                    },
            ExampleConfig::combatLabel
    ),

    STATS(
            new int[]
                    {
                            InterfaceID.Toplevel.STONE1,
                            InterfaceID.ToplevelOsrsStretch.STONE1,
                            InterfaceID.ToplevelPreEoc.ICON1
                    },
            ExampleConfig::statsLabel
    ),

    QUESTS(
            new int[]
                    {
                            InterfaceID.Toplevel.STONE2,
                            InterfaceID.ToplevelOsrsStretch.STONE2,
                            InterfaceID.ToplevelPreEoc.ICON2
                    },
            ExampleConfig::questsLabel
    ),

    INVENTORY(
            new int[]
                    {
                            InterfaceID.Toplevel.STONE3,
                            InterfaceID.ToplevelOsrsStretch.STONE3,
                            InterfaceID.ToplevelPreEoc.STONE3
                    },
            ExampleConfig::inventoryLabel
    ),

    EQUIPMENT(
            new int[]
                    {
                            InterfaceID.Toplevel.STONE4,
                            InterfaceID.ToplevelOsrsStretch.STONE4,
                            InterfaceID.ToplevelPreEoc.ICON4
                    },
            ExampleConfig::equipmentLabel
    ),

    PRAYER(
            new int[]
                    {
                            InterfaceID.Toplevel.STONE5,
                            InterfaceID.ToplevelOsrsStretch.STONE5,
                            InterfaceID.ToplevelPreEoc.STONE5
                    },
            ExampleConfig::prayerLabel
    ),

    SPELLBOOK(
            new int[]
                    {
                            InterfaceID.Toplevel.STONE6,
                            InterfaceID.ToplevelOsrsStretch.STONE6,
                            InterfaceID.ToplevelPreEoc.ICON6
                    },
            ExampleConfig::spellbookLabel
    ),

    FRIENDS_CHAT(
            new int[]
                    {
                            InterfaceID.Toplevel.STONE7,
                            InterfaceID.ToplevelOsrsStretch.STONE7,
                            InterfaceID.ToplevelPreEoc.ICON7
                    },
            ExampleConfig::friendsChatLabel
    ),

    FRIENDS(
            new int[]
                    {
                            InterfaceID.Toplevel.STONE9,
                            InterfaceID.ToplevelOsrsStretch.STONE9,
                            InterfaceID.ToplevelPreEoc.ICON9
                    },
            ExampleConfig::friendsLabel
    ),

    IGNORE(
            new int[]
                    {
                            InterfaceID.Toplevel.STONE8,
                            InterfaceID.ToplevelOsrsStretch.STONE8
                    },
            ExampleConfig::ignoreLabel
    ),

    LOGOUT(
            new int[]
                    {
                            InterfaceID.Toplevel.STONE10,
                            InterfaceID.ToplevelOsrsStretch.STONE10
                    },
            ExampleConfig::logoutLabel
    ),

    OPTIONS(
            new int[]
                    {
                            InterfaceID.Toplevel.STONE11,
                            InterfaceID.ToplevelOsrsStretch.STONE11,
                            InterfaceID.ToplevelPreEoc.ICON11
                    },
            ExampleConfig::settingsLabel
    ),

    EMOTES(
            new int[]
                    {
                            InterfaceID.Toplevel.STONE12,
                            InterfaceID.ToplevelOsrsStretch.STONE12,
                            InterfaceID.ToplevelPreEoc.ICON12
                    },
            ExampleConfig::emotesLabel
    ),

    MUSIC(
            new int[]
                    {
                            InterfaceID.Toplevel.STONE13,
                            InterfaceID.ToplevelOsrsStretch.STONE13,
                            InterfaceID.ToplevelPreEoc.ICON13
                    },
            ExampleConfig::musicLabel
    );

    private final int[] componentIds;
    private final Function<ExampleConfig, String> labelGetter;

    ExpandedMenuStone(int[] componentIds, Function<ExampleConfig, String> labelGetter)
    {
        this.componentIds = componentIds;
        this.labelGetter = labelGetter;
    }

    public int[] getComponentIds()
    {
        return componentIds;
    }

    public String getLabel(ExampleConfig config)
    {
        return labelGetter.apply(config);
    }
}