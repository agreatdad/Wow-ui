# Wow HUD

Wow HUD is a RuneLite plugin that adds an MMO-inspired interface overlay to Old School RuneScape. It is designed to make RuneLite feel more like a traditional MMORPG UI while keeping the plugin cosmetic and overlay-focused.

## Features

### MMO HUD Overlay

Wow HUD adds a movable MMO-style portrait frame with status bars beside it.

The HUD can show:

- Character portrait
- Username
- Combat level or total level
- Combat Achievement tier title
- Hitpoints bar
- Prayer bar
- Run Energy bar
- Special Attack bar

The HUD can be moved by holding `Alt` and dragging it. The bars can be resized through the overlay resize handle, while portrait size is controlled separately through the plugin config.

### Portrait Options

The portrait supports multiple display options:

- In-game item sprite portrait selection
- Local profile image from the `.runelite` folder
- Configurable portrait frame color
- Configurable portrait size

If local profile image mode is enabled, the plugin attempts to load a local profile image from the RuneLite directory.

### Status Bars

The MMO HUD status bars support:

- HP, Prayer, Run, and Special Attack tracking
- Custom colors for each bar
- Optional minimap-orb style icons
- Configurable default width, height, and spacing
- Alt-resize support for adjusting the bar area in-game

### Paperdoll Overlay

Wow HUD includes a paperdoll equipment overlay that can be toggled with a configurable keybind.

The paperdoll overlay displays:

- Character name
- Combat level
- Total level
- Equipped item icons
- Attack and defence bonuses
- Strength bonuses
- Prayer bonus
- Weapon speed
- Max hit
- Max DPS
- Average DPS

The paperdoll is intended to provide a compact equipment summary without opening or interacting with the normal equipment interface.

### Skills Overlay

The plugin includes a movable skills overlay inspired by the in-game skills tab.

It displays:

- Skill icons
- Current levels
- Real levels
- Total level
- Sailing support

The skills overlay can be toggled with a configurable keybind.

### Progress Bars

Wow HUD includes optional segmented progression bars inspired by MMO UI addons.

Available progress bars include:

- Most recent XP-to-level progress
- Quest completion progress
- Combat Achievement progress
- Achievement Diary progress placeholder
- Collection Log progress placeholder

Progress bar options include:

- Independent movement for each bar
- Custom width and height for each bar
- Custom colors
- Optional icon/label text inside the bar
- Ignore Hitpoints option for most-recent XP tracking

### Combat Achievement Tracking

The plugin can calculate Combat Achievement progress from the client and display the current reward tier title above the portrait.

Supported tiers include:

- Easy
- Medium
- Hard
- Elite
- Master
- Grandmaster

### Menu Stone Keybind Labels

Wow HUD can draw custom keybind labels over the side-panel menu stones.

Supported menu stones include:

- Combat
- Stats
- Quests
- Inventory
- Equipment
- Prayer
- Magic
- Friends Chat
- Friends
- Ignore
- Logout
- Settings
- Emotes
- Music

Each label can be customized in the plugin config.

### Logout Rune Overlay

For resizable modern layouts, Wow HUD can overlay a logout rune icon and label onto the empty bottom-row menu stone.

## Configuration

The plugin config is organized into sections:

### Portrait

Controls portrait sprite, local profile image mode, portrait size, and portrait frame color.

### Bars

Controls default MMO HUD bar width, height, spacing, and bar colors.

### Display Toggles

Controls whether the MMO HUD, status bars, icons, total level, and Combat Achievement title are shown.

### Progress Bars

Controls segmented XP, quest, combat task, achievement, and collection log bars.

### Paperdoll

Controls the paperdoll overlay toggle and keybind.

### Skills Tab

Controls the skills overlay toggle and keybind.

### Menu Stone Labels

Controls the text, position, color, shadow, and visibility of menu-stone keybind labels.

## Default Keybinds

| Overlay | Default Keybind |
|---|---|
| Paperdoll | `P` |
| Skills Overlay | `K` |

These can be changed in the plugin config.

## Development

To run the plugin locally:

```bash
./gradlew run
```

On Windows:

```powershell
.\gradlew.bat run
```

To build the plugin:

```bash
./gradlew build
```

On Windows:

```powershell
.\gradlew.bat build
```

## Notes

This plugin is cosmetic and overlay-focused. It does not automate gameplay, perform actions for the player, or interact with the game world beyond reading client state for display purposes.

Some progress features, such as Achievement Diary and Collection Log completion, may require additional future work depending on RuneLite API support and available client state.

## Disclaimer

Wow HUD is an unofficial RuneLite plugin for Old School RuneScape. It is not affiliated with Jagex, RuneLite, or Old School RuneScape.

## License

This project is licensed under the BSD 2-Clause License.
