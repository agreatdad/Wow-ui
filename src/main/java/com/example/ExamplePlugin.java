package com.example;

import com.google.inject.Provides;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import javax.inject.Inject;
import net.runelite.api.GameState;
import net.runelite.api.events.GameStateChanged;
import net.runelite.api.events.VarbitChanged;
import net.runelite.client.callback.ClientThread;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.events.ConfigChanged;
import net.runelite.client.input.KeyListener;
import net.runelite.client.input.KeyManager;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.ui.ClientToolbar;
import net.runelite.client.ui.NavigationButton;
import net.runelite.client.ui.overlay.OverlayManager;
import net.runelite.api.Skill;
import net.runelite.api.events.StatChanged;

@PluginDescriptor(
		name = "Wow Hud"
)
public class ExamplePlugin extends Plugin
{
	@Inject
	private XpProgressOverlay xpProgressOverlay;

	@Inject
	private LogoutRuneOverlay logoutRuneOverlay;

	@Inject
	private QuestProgressOverlay questProgressOverlay;

	@Inject
	private CombatTaskProgressOverlay combatTaskProgressOverlay;

	@Inject
	private AchievementProgressOverlay achievementProgressOverlay;

	@Inject
	private CollectionLogProgressOverlay collectionLogProgressOverlay;

	@Inject
	private OverlayManager overlayManager;

	@Inject
	private KeyManager keyManager;

	@Inject
	private ClientToolbar clientToolbar;

	@Inject
	private ConfigManager configManager;

	@Inject
	private ClientThread clientThread;

	@Inject
	private ExampleConfig config;

	@Inject
	private ExpandedMenuOverlay expandedMenuOverlay;

	@Inject
	private MmoHudOverlay mmoHudOverlay;

	@Inject
	private PaperdollOverlay paperdollOverlay;

	@Inject
	private SkillsOverlay skillsOverlay;

	@Inject
	private PortraitProvider portraitProvider;

	@Inject
	private CombatAchievementTracker combatAchievementTracker;

	private NavigationButton navButton;
	private MmoHudPanel mmoHudPanel;

	private final KeyListener keyListener = new KeyListener()
	{
		@Override
		public void keyTyped(KeyEvent event)
		{
		}

		@Override
		public void keyPressed(KeyEvent event)
		{
			if (config.paperdollEnabled() && matchesKey(config.paperdollKeybind(), event))
			{
				paperdollOverlay.toggle();
				event.consume();
				return;
			}

			if (config.skillsTabEnabled() && matchesKey(config.skillsKeybind(), event))
			{
				skillsOverlay.toggle();
				event.consume();
			}
		}

		@Override
		public void keyReleased(KeyEvent event)
		{
		}
	};

	@Override
	protected void startUp() throws Exception
	{
		overlayManager.add(expandedMenuOverlay);
		overlayManager.add(mmoHudOverlay);
		overlayManager.add(paperdollOverlay);
		overlayManager.add(skillsOverlay);
		overlayManager.add(xpProgressOverlay);
		overlayManager.add(questProgressOverlay);
		overlayManager.add(combatTaskProgressOverlay);
		overlayManager.add(achievementProgressOverlay);
		overlayManager.add(collectionLogProgressOverlay);
		overlayManager.add(logoutRuneOverlay);

		keyManager.registerKeyListener(keyListener);

		portraitProvider.reload();

		clientThread.invokeLater(combatAchievementTracker::refresh);

		mmoHudPanel = new MmoHudPanel(configManager, portraitProvider);

		navButton = NavigationButton.builder()
				.tooltip("MMO HUD")
				.icon(makePanelIcon())
				.priority(5)
				.panel(mmoHudPanel)
				.build();

		clientToolbar.addNavigation(navButton);
	}

	@Subscribe
	public void onStatChanged(StatChanged event)
	{
		Skill skill = event.getSkill();

		if (skill == null || skill == Skill.OVERALL)
		{
			return;
		}

		if (config.progressIgnoreHitpoints() && skill == Skill.HITPOINTS)
		{
			return;
		}

		xpProgressOverlay.setMostRecentSkill(skill);
	}

	@Override
	protected void shutDown() throws Exception
	{
		overlayManager.remove(expandedMenuOverlay);
		overlayManager.remove(mmoHudOverlay);
		overlayManager.remove(paperdollOverlay);
		overlayManager.remove(skillsOverlay);
		overlayManager.remove(xpProgressOverlay);
		overlayManager.remove(questProgressOverlay);
		overlayManager.remove(combatTaskProgressOverlay);
		overlayManager.remove(achievementProgressOverlay);
		overlayManager.remove(collectionLogProgressOverlay);
		overlayManager.remove(logoutRuneOverlay);

		keyManager.unregisterKeyListener(keyListener);

		paperdollOverlay.setOpen(false);

		if (navButton != null)
		{
			clientToolbar.removeNavigation(navButton);
			navButton = null;
		}

		mmoHudPanel = null;
	}

	@Subscribe
	public void onGameStateChanged(GameStateChanged event)
	{
		if (event.getGameState() == GameState.LOGGED_IN)
		{
			clientThread.invokeLater(combatAchievementTracker::refresh);
		}
	}

	@Subscribe
	public void onVarbitChanged(VarbitChanged event)
	{
		clientThread.invokeLater(combatAchievementTracker::refresh);
	}

	@Subscribe
	public void onConfigChanged(ConfigChanged event)
	{
		if (!ExampleConfig.GROUP.equals(event.getGroup()))
		{
			return;
		}

		if ("portraitChoice".equals(event.getKey()) || "useLocalProfileImage".equals(event.getKey()))
		{
			portraitProvider.reload();
		}
	}

	private boolean matchesKey(String keybindText, KeyEvent event)
	{
		if (keybindText == null)
		{
			return false;
		}

		String wanted = keybindText.trim().toLowerCase();

		if (wanted.isEmpty())
		{
			return false;
		}

		wanted = wanted.replace(" ", "");

		boolean wantsCtrl = wanted.contains("ctrl+") || wanted.contains("control+");
		boolean wantsShift = wanted.contains("shift+");
		boolean wantsAlt = wanted.contains("alt+");

		if (event.isControlDown() != wantsCtrl)
		{
			return false;
		}

		if (event.isShiftDown() != wantsShift)
		{
			return false;
		}

		if (event.isAltDown() != wantsAlt)
		{
			return false;
		}

		wanted = wanted
				.replace("control+", "")
				.replace("ctrl+", "")
				.replace("shift+", "")
				.replace("alt+", "");

		String keyText = KeyEvent.getKeyText(event.getKeyCode()).toLowerCase();

		String keyChar = "";
		if (event.getKeyChar() != KeyEvent.CHAR_UNDEFINED)
		{
			keyChar = String.valueOf(event.getKeyChar()).toLowerCase();
		}

		return wanted.equals(keyText) || wanted.equals(keyChar);
	}

	private BufferedImage makePanelIcon()
	{
		BufferedImage image = new BufferedImage(16, 16, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g = image.createGraphics();

		g.setColor(new Color(25, 20, 15));
		g.fillOval(1, 1, 14, 14);

		g.setColor(new Color(255, 210, 80));
		g.drawOval(1, 1, 14, 14);

		g.setColor(new Color(180, 60, 60));
		g.fillOval(5, 4, 6, 6);

		g.setColor(new Color(255, 210, 80));
		g.drawLine(4, 12, 12, 12);

		g.dispose();
		return image;
	}

	@Provides
	ExampleConfig provideConfig(ConfigManager configManager)
	{
		return configManager.getConfig(ExampleConfig.class);
	}
}