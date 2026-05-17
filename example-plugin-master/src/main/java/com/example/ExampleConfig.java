package com.example;

import java.awt.Color;
import net.runelite.client.config.Alpha;
import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;
import net.runelite.client.config.ConfigSection;
import net.runelite.client.config.Range;

@ConfigGroup(ExampleConfig.GROUP)
public interface ExampleConfig extends Config
{
	String GROUP = "expandedmenu";

	@ConfigSection(
			name = "Portrait",
			description = "MMO HUD portrait image, frame, title, and name settings.",
			position = 0,
			closedByDefault = false
	)
	String portraitSection = "portraitSection";

	@ConfigSection(
			name = "Bars",
			description = "MMO HUD HP, Prayer, Run, and Special bar settings.",
			position = 1,
			closedByDefault = false
	)
	String barsSection = "barsSection";

	@ConfigSection(
			name = "Display Toggles",
			description = "Show or hide MMO HUD elements.",
			position = 2,
			closedByDefault = false
	)
	String displayTogglesSection = "displayTogglesSection";

	@ConfigSection(
			name = "Progress Bars",
			description = "Extra movable segmented progression bars.",
			position = 3,
			closedByDefault = true
	)
	String progressBarsSection = "progressBarsSection";

	@ConfigSection(
			name = "Paperdoll",
			description = "Equipment paperdoll overlay.",
			position = 4,
			closedByDefault = true
	)
	String paperdollSection = "paperdollSection";

	@ConfigSection(
			name = "Skills Tab",
			description = "Skills stats overlay.",
			position = 5,
			closedByDefault = true
	)
	String skillsSection = "skillsSection";

	@ConfigSection(
			name = "Menu Stone Labels",
			description = "Editable labels shown on side menu stones.",
			position = 6,
			closedByDefault = true
	)
	String menuStoneSection = "menuStoneSection";

	// Display Toggles

	@ConfigItem(
			keyName = "showMmoHud",
			name = "Show MMO HUD",
			description = "Shows the MMO-style portrait and status bars.",
			section = displayTogglesSection,
			position = 0
	)
	default boolean showMmoHud()
	{
		return true;
	}

	@ConfigItem(
			keyName = "showTotalInsteadOfCombat",
			name = "Show Total Level",
			description = "Show total level instead of combat level under the portrait.",
			section = displayTogglesSection,
			position = 1
	)
	default boolean showTotalInsteadOfCombat()
	{
		return false;
	}

	@ConfigItem(
			keyName = "showCombatAchievementTitle",
			name = "Add Combat Achievement Title",
			description = "Shows a Combat Achievement tier box above the portrait.",
			section = displayTogglesSection,
			position = 2
	)
	default boolean showCombatAchievementTitle()
	{
		return false;
	}

	@ConfigItem(
			keyName = "showMmoHudIcons",
			name = "Show Bar Icons",
			description = "Show the HP, Prayer, Run, and Special icons at the end of the MMO HUD bars.",
			section = displayTogglesSection,
			position = 3
	)
	default boolean showMmoHudIcons()
	{
		return true;
	}

	@ConfigItem(
			keyName = "showHpBar",
			name = "Show HP Bar",
			description = "Show or hide the HP bar.",
			section = displayTogglesSection,
			position = 4
	)
	default boolean showHpBar()
	{
		return true;
	}

	@ConfigItem(
			keyName = "showPrayerBar",
			name = "Show Prayer Bar",
			description = "Show or hide the Prayer bar.",
			section = displayTogglesSection,
			position = 5
	)
	default boolean showPrayerBar()
	{
		return true;
	}

	@ConfigItem(
			keyName = "showRunBar",
			name = "Show Run Bar",
			description = "Show or hide the Run Energy bar.",
			section = displayTogglesSection,
			position = 6
	)
	default boolean showRunBar()
	{
		return true;
	}

	@ConfigItem(
			keyName = "showSpecBar",
			name = "Show Special Attack Bar",
			description = "Show or hide the Special Attack bar.",
			section = displayTogglesSection,
			position = 7
	)
	default boolean showSpecBar()
	{
		return true;
	}

	// Portrait

	@ConfigItem(
			keyName = "portraitChoice",
			name = "Portrait Sprite",
			description = "Choose which in-game item sprite to use as your portrait.",
			section = portraitSection,
			position = 0
	)
	default PortraitChoice portraitChoice()
	{
		return PortraitChoice.SANGUINE_TORVA_FULL_HELM;
	}

	@ConfigItem(
			keyName = "useLocalProfileImage",
			name = "Use Local Profile Image",
			description = "Use .runelite/profile.png as the portrait image instead of the selected sprite.",
			section = portraitSection,
			position = 1
	)
	default boolean useLocalProfileImage()
	{
		return false;
	}

	@Range(
			min = 48,
			max = 180
	)
	@ConfigItem(
			keyName = "portraitDiameter",
			name = "Portrait Size",
			description = "Size of the circular portrait. Alt-resizing the MMO HUD does not change this.",
			section = portraitSection,
			position = 2
	)
	default int portraitDiameter()
	{
		return 88;
	}

	@Alpha
	@ConfigItem(
			keyName = "portraitFrameColor",
			name = "Portrait Frame Color",
			description = "Color of the circular portrait border.",
			section = portraitSection,
			position = 3
	)
	default Color portraitFrameColor()
	{
		return new Color(210, 178, 64);
	}

	// Bars

	@Range(
			min = 80,
			max = 300
	)
	@ConfigItem(
			keyName = "barWidth",
			name = "Default Bar Width",
			description = "Default width of the HP, Prayer, Run, and Special bars before Alt-resizing. Alt-resizing can stretch the bars wider.",
			section = barsSection,
			position = 0
	)
	default int barWidth()
	{
		return 145;
	}

	@Range(
			min = 10,
			max = 30
	)
	@ConfigItem(
			keyName = "barHeight",
			name = "Default Bar Height",
			description = "Default height of each MMO HUD bar before Alt-resizing. Alt-resizing taller can increase the bar height.",
			section = barsSection,
			position = 1
	)
	default int barHeight()
	{
		return 18;
	}

	@Range(
			min = 0,
			max = 12
	)
	@ConfigItem(
			keyName = "barGap",
			name = "Bar Gap",
			description = "Space between each MMO HUD status bar.",
			section = barsSection,
			position = 2
	)
	default int barGap()
	{
		return 3;
	}

	@Alpha
	@ConfigItem(
			keyName = "hpColor",
			name = "HP Color",
			description = "Normal HP bar color.",
			section = barsSection,
			position = 3
	)
	default Color hpColor()
	{
		return new Color(190, 32, 22);
	}

	@Alpha
	@ConfigItem(
			keyName = "prayerColor",
			name = "Prayer Color",
			description = "Normal Prayer bar color.",
			section = barsSection,
			position = 4
	)
	default Color prayerColor()
	{
		return new Color(35, 150, 185);
	}

	@Alpha
	@ConfigItem(
			keyName = "runColor",
			name = "Run Color",
			description = "Normal Run Energy bar color.",
			section = barsSection,
			position = 5
	)
	default Color runColor()
	{
		return new Color(210, 180, 45);
	}

	@Alpha
	@ConfigItem(
			keyName = "specColor",
			name = "Special Attack Color",
			description = "Normal Special Attack bar color.",
			section = barsSection,
			position = 6
	)
	default Color specColor()
	{
		return new Color(40, 190, 65);
	}

	// Progress Bars

	@ConfigItem(
			keyName = "showProgressBars",
			name = "Show Progress Bars",
			description = "Show extra segmented progression bars.",
			section = progressBarsSection,
			position = 0
	)
	default boolean showProgressBars()
	{
		return false;
	}

	@ConfigItem(
			keyName = "showXpProgressBar",
			name = "Show XP Progress Bar",
			description = "Shows the most recent skill's XP progress to next level.",
			section = progressBarsSection,
			position = 1
	)
	default boolean showXpProgressBar()
	{
		return true;
	}

	@ConfigItem(
			keyName = "progressIgnoreHitpoints",
			name = "Ignore HP",
			description = "Ignore Hitpoints when choosing the most recent XP skill.",
			section = progressBarsSection,
			position = 2
	)
	default boolean progressIgnoreHitpoints()
	{
		return true;
	}

	@ConfigItem(
			keyName = "showQuestProgressBar",
			name = "Show Quest Bar",
			description = "Shows quest completion progress.",
			section = progressBarsSection,
			position = 3
	)
	default boolean showQuestProgressBar()
	{
		return true;
	}

	@ConfigItem(
			keyName = "showCombatTaskProgressBar",
			name = "Show Combat Task Bar",
			description = "Shows combat achievement task completion progress.",
			section = progressBarsSection,
			position = 4
	)
	default boolean showCombatTaskProgressBar()
	{
		return true;
	}

	@ConfigItem(
			keyName = "showAchievementProgressBar",
			name = "Show Achievement Bar",
			description = "Shows achievement diary progress. Placeholder for now.",
			section = progressBarsSection,
			position = 5
	)
	default boolean showAchievementProgressBar()
	{
		return false;
	}

	@ConfigItem(
			keyName = "showCollectionLogProgressBar",
			name = "Show Collection Log Bar",
			description = "Shows collection log progress. Placeholder for now.",
			section = progressBarsSection,
			position = 6
	)
	default boolean showCollectionLogProgressBar()
	{
		return false;
	}

	@ConfigItem(
			keyName = "showProgressBarIcons",
			name = "Show Progress Bar Icons",
			description = "Shows a small icon label inside each progress bar.",
			section = progressBarsSection,
			position = 7
	)
	default boolean showProgressBarIcons()
	{
		return true;
	}

	@Range(
			min = 50,
			max = 500
	)
	@ConfigItem(
			keyName = "xpProgressWidth",
			name = "XP Bar Width",
			description = "Width of the XP progress bar.",
			section = progressBarsSection,
			position = 8
	)
	default int xpProgressWidth()
	{
		return 190;
	}

	@Range(
			min = 8,
			max = 80
	)
	@ConfigItem(
			keyName = "xpProgressHeight",
			name = "XP Bar Height",
			description = "Height of the XP progress bar.",
			section = progressBarsSection,
			position = 9
	)
	default int xpProgressHeight()
	{
		return 20;
	}

	@Range(
			min = 50,
			max = 500
	)
	@ConfigItem(
			keyName = "questProgressWidth",
			name = "Quest Bar Width",
			description = "Width of the quest progress bar.",
			section = progressBarsSection,
			position = 10
	)
	default int questProgressWidth()
	{
		return 190;
	}

	@Range(
			min = 8,
			max = 80
	)
	@ConfigItem(
			keyName = "questProgressHeight",
			name = "Quest Bar Height",
			description = "Height of the quest progress bar.",
			section = progressBarsSection,
			position = 11
	)
	default int questProgressHeight()
	{
		return 20;
	}

	@Range(
			min = 50,
			max = 500
	)
	@ConfigItem(
			keyName = "combatTaskProgressWidth",
			name = "Combat Task Bar Width",
			description = "Width of the combat task progress bar.",
			section = progressBarsSection,
			position = 12
	)
	default int combatTaskProgressWidth()
	{
		return 190;
	}

	@Range(
			min = 8,
			max = 80
	)
	@ConfigItem(
			keyName = "combatTaskProgressHeight",
			name = "Combat Task Bar Height",
			description = "Height of the combat task progress bar.",
			section = progressBarsSection,
			position = 13
	)
	default int combatTaskProgressHeight()
	{
		return 20;
	}

	@Range(
			min = 50,
			max = 500
	)
	@ConfigItem(
			keyName = "achievementProgressWidth",
			name = "Achievement Bar Width",
			description = "Width of the achievement progress bar.",
			section = progressBarsSection,
			position = 14
	)
	default int achievementProgressWidth()
	{
		return 190;
	}

	@Range(
			min = 8,
			max = 80
	)
	@ConfigItem(
			keyName = "achievementProgressHeight",
			name = "Achievement Bar Height",
			description = "Height of the achievement progress bar.",
			section = progressBarsSection,
			position = 15
	)
	default int achievementProgressHeight()
	{
		return 20;
	}

	@Range(
			min = 50,
			max = 500
	)
	@ConfigItem(
			keyName = "collectionLogProgressWidth",
			name = "Collection Log Bar Width",
			description = "Width of the collection log progress bar.",
			section = progressBarsSection,
			position = 16
	)
	default int collectionLogProgressWidth()
	{
		return 190;
	}

	@Range(
			min = 8,
			max = 80
	)
	@ConfigItem(
			keyName = "collectionLogProgressHeight",
			name = "Collection Log Bar Height",
			description = "Height of the collection log progress bar.",
			section = progressBarsSection,
			position = 17
	)
	default int collectionLogProgressHeight()
	{
		return 20;
	}

	@Alpha
	@ConfigItem(
			keyName = "xpProgressColor",
			name = "XP Bar Color",
			description = "Color of the XP progression bar.",
			section = progressBarsSection,
			position = 18
	)
	default Color xpProgressColor()
	{
		return new Color(139, 63, 174);
	}

	@Alpha
	@ConfigItem(
			keyName = "questProgressColor",
			name = "Quest Bar Color",
			description = "Color of the quest progression bar.",
			section = progressBarsSection,
			position = 19
	)
	default Color questProgressColor()
	{
		return new Color(60, 150, 220);
	}

	@Alpha
	@ConfigItem(
			keyName = "combatTaskProgressColor",
			name = "Combat Task Bar Color",
			description = "Color of the combat task progression bar.",
			section = progressBarsSection,
			position = 20
	)
	default Color combatTaskProgressColor()
	{
		return new Color(220, 80, 80);
	}

	@Alpha
	@ConfigItem(
			keyName = "achievementProgressColor",
			name = "Achievement Bar Color",
			description = "Color of the achievement progression bar.",
			section = progressBarsSection,
			position = 21
	)
	default Color achievementProgressColor()
	{
		return new Color(230, 180, 60);
	}

	@Alpha
	@ConfigItem(
			keyName = "collectionLogProgressColor",
			name = "Collection Log Bar Color",
			description = "Color of the collection log progression bar.",
			section = progressBarsSection,
			position = 22
	)
	default Color collectionLogProgressColor()
	{
		return new Color(80, 200, 120);
	}

	// Paperdoll

	@ConfigItem(
			keyName = "paperdollEnabled",
			name = "Paperdoll",
			description = "Enable the paperdoll overlay keybind.",
			section = paperdollSection,
			position = 0
	)
	default boolean paperdollEnabled()
	{
		return true;
	}

	@ConfigItem(
			keyName = "paperdollKeybind",
			name = "Paperdoll Keybind",
			description = "Press this key to toggle the paperdoll overlay. Examples: P, F6, Ctrl+P.",
			section = paperdollSection,
			position = 1
	)
	default String paperdollKeybind()
	{
		return "P";
	}

	// Skills Tab

	@ConfigItem(
			keyName = "skillsTabEnabled",
			name = "Skills Tab",
			description = "Enable the skills overlay keybind.",
			section = skillsSection,
			position = 0
	)
	default boolean skillsTabEnabled()
	{
		return true;
	}

	@ConfigItem(
			keyName = "skillsKeybind",
			name = "Skills Keybind",
			description = "Press this key to toggle the skills overlay. Examples: K, F7, Ctrl+K.",
			section = skillsSection,
			position = 1
	)
	default String skillsKeybind()
	{
		return "K";
	}

	// Menu Stone Labels

	@Range(
			min = 8,
			max = 24
	)
	@ConfigItem(
			keyName = "fontSize",
			name = "Menu Label Font Size",
			description = "The size of the keybind label text.",
			section = menuStoneSection,
			position = 0
	)
	default int fontSize()
	{
		return 11;
	}

	@Range(
			min = -30,
			max = 30
	)
	@ConfigItem(
			keyName = "xOffset",
			name = "Menu Label X Offset",
			description = "Moves the label left or right from the top-right corner of the stone.",
			section = menuStoneSection,
			position = 1
	)
	default int xOffset()
	{
		return 3;
	}

	@Range(
			min = -30,
			max = 30
	)
	@ConfigItem(
			keyName = "yOffset",
			name = "Menu Label Y Offset",
			description = "Moves the label up or down from the top-right corner of the stone.",
			section = menuStoneSection,
			position = 2
	)
	default int yOffset()
	{
		return 2;
	}

	@Alpha
	@ConfigItem(
			keyName = "labelColor",
			name = "Menu Label Color",
			description = "The main menu-stone label text color.",
			section = menuStoneSection,
			position = 3
	)
	default Color labelColor()
	{
		return Color.WHITE;
	}

	@Alpha
	@ConfigItem(
			keyName = "shadowColor",
			name = "Menu Label Shadow Color",
			description = "The one-pixel shadow color behind the menu-stone text.",
			section = menuStoneSection,
			position = 4
	)
	default Color shadowColor()
	{
		return Color.BLACK;
	}

	@ConfigItem(
			keyName = "showShadow",
			name = "Show Menu Label Shadow",
			description = "Draws a small shadow behind each menu-stone label.",
			section = menuStoneSection,
			position = 5
	)
	default boolean showShadow()
	{
		return true;
	}

	@ConfigItem(
			keyName = "combatLabel",
			name = "Combat Label",
			description = "Text shown on the Combat Options stone.",
			section = menuStoneSection,
			position = 10
	)
	default String combatLabel()
	{
		return "F1";
	}

	@ConfigItem(
			keyName = "inventoryLabel",
			name = "Inventory Label",
			description = "Text shown on the Inventory stone.",
			section = menuStoneSection,
			position = 11
	)
	default String inventoryLabel()
	{
		return "F2";
	}

	@ConfigItem(
			keyName = "equipmentLabel",
			name = "Equipment Label",
			description = "Text shown on the Equipment stone.",
			section = menuStoneSection,
			position = 12
	)
	default String equipmentLabel()
	{
		return "F3";
	}

	@ConfigItem(
			keyName = "prayerLabel",
			name = "Prayer Label",
			description = "Text shown on the Prayer stone.",
			section = menuStoneSection,
			position = 13
	)
	default String prayerLabel()
	{
		return "F4";
	}

	@ConfigItem(
			keyName = "spellbookLabel",
			name = "Spellbook Label",
			description = "Text shown on the Spellbook/Magic stone.",
			section = menuStoneSection,
			position = 14
	)
	default String spellbookLabel()
	{
		return "F5";
	}

	@ConfigItem(
			keyName = "friendsLabel",
			name = "Friends Label",
			description = "Text shown on the Friends stone.",
			section = menuStoneSection,
			position = 15
	)
	default String friendsLabel()
	{
		return "";
	}

	@ConfigItem(
			keyName = "ignoreLabel",
			name = "Ignore Label",
			description = "Text shown on the Ignore stone.",
			section = menuStoneSection,
			position = 16
	)
	default String ignoreLabel()
	{
		return "";
	}

	@ConfigItem(
			keyName = "logoutLabel",
			name = "Logout Label",
			description = "Text shown on the Logout stone.",
			section = menuStoneSection,
			position = 17
	)
	default String logoutLabel()
	{
		return "";
	}

	@ConfigItem(
			keyName = "settingsLabel",
			name = "Settings Label",
			description = "Text shown on the Settings/Options stone.",
			section = menuStoneSection,
			position = 18
	)
	default String settingsLabel()
	{
		return "";
	}

	@ConfigItem(
			keyName = "statsLabel",
			name = "Stats Label",
			description = "Text shown on the Stats stone.",
			section = menuStoneSection,
			position = 21
	)
	default String statsLabel()
	{
		return "";
	}

	@ConfigItem(
			keyName = "questsLabel",
			name = "Quests Label",
			description = "Text shown on the Quests stone.",
			section = menuStoneSection,
			position = 22
	)
	default String questsLabel()
	{
		return "";
	}

	@ConfigItem(
			keyName = "friendsChatLabel",
			name = "Friends Chat Label",
			description = "Text shown on the Friends Chat stone.",
			section = menuStoneSection,
			position = 23
	)
	default String friendsChatLabel()
	{
		return "";
	}

	@ConfigItem(
			keyName = "emotesLabel",
			name = "Emotes Label",
			description = "Text shown on the Emotes stone.",
			section = menuStoneSection,
			position = 19
	)
	default String emotesLabel()
	{
		return "";
	}

	@ConfigItem(
			keyName = "musicLabel",
			name = "Music Label",
			description = "Text shown on the Music stone.",
			section = menuStoneSection,
			position = 20
	)
	default String musicLabel()
	{
		return "";
	}
}