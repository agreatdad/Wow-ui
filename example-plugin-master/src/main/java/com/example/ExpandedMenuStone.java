package com.example;

import java.util.function.Function;
import net.runelite.api.widgets.WidgetInfo;

public enum ExpandedMenuStone
{
    COMBAT(
            new WidgetInfo[]
                    {
                            WidgetInfo.FIXED_VIEWPORT_COMBAT_TAB,
                            WidgetInfo.RESIZABLE_VIEWPORT_COMBAT_TAB,
                            WidgetInfo.RESIZABLE_VIEWPORT_BOTTOM_LINE_COMBAT_ICON
                    },
            ExampleConfig::combatLabel
    ),

    STATS(
            new WidgetInfo[]
                    {
                            WidgetInfo.FIXED_VIEWPORT_STATS_TAB,
                            WidgetInfo.RESIZABLE_VIEWPORT_STATS_TAB,
                            WidgetInfo.RESIZABLE_VIEWPORT_BOTTOM_LINE_STATS_ICON
                    },
            ExampleConfig::statsLabel
    ),

    QUESTS(
            new WidgetInfo[]
                    {
                            WidgetInfo.FIXED_VIEWPORT_QUESTS_TAB,
                            WidgetInfo.RESIZABLE_VIEWPORT_QUESTS_TAB,
                            WidgetInfo.RESIZABLE_VIEWPORT_BOTTOM_LINE_QUESTS_ICON
                    },
            ExampleConfig::questsLabel
    ),

    INVENTORY(
            new WidgetInfo[]
                    {
                            WidgetInfo.FIXED_VIEWPORT_INVENTORY_TAB,
                            WidgetInfo.RESIZABLE_VIEWPORT_INVENTORY_TAB,
                            WidgetInfo.RESIZABLE_VIEWPORT_BOTTOM_LINE_INVENTORY_TAB
                    },
            ExampleConfig::inventoryLabel
    ),

    EQUIPMENT(
            new WidgetInfo[]
                    {
                            WidgetInfo.FIXED_VIEWPORT_EQUIPMENT_TAB,
                            WidgetInfo.RESIZABLE_VIEWPORT_EQUIPMENT_TAB,
                            WidgetInfo.RESIZABLE_VIEWPORT_BOTTOM_LINE_EQUIPMENT_ICON
                    },
            ExampleConfig::equipmentLabel
    ),

    PRAYER(
            new WidgetInfo[]
                    {
                            WidgetInfo.FIXED_VIEWPORT_PRAYER_TAB,
                            WidgetInfo.RESIZABLE_VIEWPORT_PRAYER_TAB,
                            WidgetInfo.RESIZABLE_VIEWPORT_BOTTOM_LINE_PRAYER_TAB
                    },
            ExampleConfig::prayerLabel
    ),

    SPELLBOOK(
            new WidgetInfo[]
                    {
                            WidgetInfo.FIXED_VIEWPORT_MAGIC_TAB,
                            WidgetInfo.RESIZABLE_VIEWPORT_MAGIC_TAB,
                            WidgetInfo.RESIZABLE_VIEWPORT_BOTTOM_LINE_MAGIC_ICON
                    },
            ExampleConfig::spellbookLabel
    ),

    FRIENDS_CHAT(
            new WidgetInfo[]
                    {
                            WidgetInfo.FIXED_VIEWPORT_FRIENDS_CHAT_TAB,
                            WidgetInfo.RESIZABLE_VIEWPORT_FRIENDS_CHAT_TAB,
                            WidgetInfo.RESIZABLE_VIEWPORT_BOTTOM_LINE_FRIEND_CHAT_ICON
                    },
            ExampleConfig::friendsChatLabel
    ),

    FRIENDS(
            new WidgetInfo[]
                    {
                            WidgetInfo.FIXED_VIEWPORT_FRIENDS_TAB,
                            WidgetInfo.RESIZABLE_VIEWPORT_FRIENDS_TAB,
                            WidgetInfo.RESIZABLE_VIEWPORT_BOTTOM_LINE_FRIEND_ICON
                    },
            ExampleConfig::friendsLabel
    ),

    IGNORE(
            new WidgetInfo[]
                    {
                            WidgetInfo.FIXED_VIEWPORT_IGNORES_TAB,
                            WidgetInfo.RESIZABLE_VIEWPORT_IGNORES_TAB
                    },
            ExampleConfig::ignoreLabel
    ),

    LOGOUT(
            new WidgetInfo[]
                    {
                            WidgetInfo.FIXED_VIEWPORT_LOGOUT_TAB,
                            WidgetInfo.RESIZABLE_VIEWPORT_LOGOUT_TAB
                    },
            ExampleConfig::logoutLabel
    ),

    OPTIONS(
            new WidgetInfo[]
                    {
                            WidgetInfo.FIXED_VIEWPORT_OPTIONS_TAB,
                            WidgetInfo.RESIZABLE_VIEWPORT_OPTIONS_TAB,
                            WidgetInfo.RESIZABLE_VIEWPORT_BOTTOM_LINE_OPTIONS_ICON
                    },
            ExampleConfig::settingsLabel
    ),

    EMOTES(
            new WidgetInfo[]
                    {
                            WidgetInfo.FIXED_VIEWPORT_EMOTES_TAB,
                            WidgetInfo.RESIZABLE_VIEWPORT_EMOTES_TAB,
                            WidgetInfo.RESIZABLE_VIEWPORT_BOTTOM_LINE_EMOTES_ICON
                    },
            ExampleConfig::emotesLabel
    ),

    MUSIC(
            new WidgetInfo[]
                    {
                            WidgetInfo.FIXED_VIEWPORT_MUSIC_TAB,
                            WidgetInfo.RESIZABLE_VIEWPORT_MUSIC_TAB,
                            WidgetInfo.RESIZABLE_VIEWPORT_BOTTOM_LINE_MUSIC_ICON
                    },
            ExampleConfig::musicLabel
    );

    private final WidgetInfo[] widgetInfos;
    private final Function<ExampleConfig, String> labelGetter;

    ExpandedMenuStone(WidgetInfo[] widgetInfos, Function<ExampleConfig, String> labelGetter)
    {
        this.widgetInfos = widgetInfos;
        this.labelGetter = labelGetter;
    }

    public WidgetInfo[] getWidgetInfos()
    {
        return widgetInfos;
    }

    public String getLabel(ExampleConfig config)
    {
        return labelGetter.apply(config);
    }
}