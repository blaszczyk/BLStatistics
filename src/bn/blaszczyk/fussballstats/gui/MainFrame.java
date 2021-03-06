package bn.blaszczyk.fussballstats.gui;

import java.awt.Color;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JSplitPane;
import javax.swing.KeyStroke;

import bn.blaszczyk.fussballstats.FussballStats;
import bn.blaszczyk.fussballstats.model.*;
import bn.blaszczyk.fussballstats.gui.filters.*;
import bn.blaszczyk.fussballstats.tools.FilterLog;
import bn.blaszczyk.rosecommon.controller.ModelController;

@SuppressWarnings("serial")
public class MainFrame extends JFrame implements BiFilterListener<Season,Game>, ActionListener
{
	/*
	 * Constatns
	 */
	private static final String ICON_FILE = "data/icon.png";
	private static final String NEW_ICON = "data/new.png";
	private static final String LOAD_ICON = "data/load.png";
	private static final String SAVE_ICON = "data/save.png";
	private static final String MANAGER_ICON = "data/manager.png";
	private static final String SETTINGS_ICON = "data/settings.png";
	private static final String EXIT_ICON = "data/exit.png";
	private static final String UNDO_ICON = "data/undo.png";
	private static final String REDO_ICON = "data/redo.png";
	private static final String HELP_ICON = "data/help.png";
	
	private static final Color BG_COLOR = new Color(227,255,227);
	
	/*
	 * Menu
	 */
	private final JMenuBar menuBar = new JMenuBar();
	private JMenuItem miNewFilter, miLoadFilter, miSaveFilter, miShowLeagueManager, miShowPreferences, miExit;
	private JMenuItem miUndo, miRedo;
	private JMenuItem miInfo;

	/*
	 * Variables
	 */
	private final FilterLog filterLog = new FilterLog();
	private final ModelController controller;
	
	/*
	 * Components
	 */
	private final FunctionalFilterPanel functionalFilterPanel= new FunctionalFilterPanel(filterLog);
	private final FunctionalGameTable functionalGameTable = new FunctionalGameTable();
	private final FunctionalResultTable functionalResultTable = new FunctionalResultTable();
	
	/*
	 * Constructors
	 */
	public MainFrame(final ModelController controller)
	{
		super("Fussball Statistiken");
		this.controller = controller;
		
		populateMenuBar();
		setJMenuBar(menuBar);
		
		initComponents();
		
		setIconImage( Toolkit.getDefaultToolkit().getImage(FussballStats.class.getResource(ICON_FILE) )  );
		
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e)
			{
				exit();
			}
		});
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
	}

	private void initComponents()
	{
		functionalFilterPanel.setFilterListener(this);

		functionalResultTable.addListSelectionListener( e -> {
			if(!e.getValueIsAdjusting())
				functionalGameTable.setSelectedTeams(functionalResultTable.getSelectedTeams());
		});

		functionalGameTable.addListSelectionListener( e -> {
			if(!e.getValueIsAdjusting())
				functionalResultTable.setSelectedTeams(functionalGameTable.getSelectedTeams());
		});
		
		JSplitPane spInner = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, functionalResultTable, functionalGameTable);
		spInner.setDividerLocation(940);
		spInner.setOpaque(false);
		JSplitPane spOuter = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, functionalFilterPanel, spInner );
		spOuter.setDividerLocation(355);
		spOuter.setBackground(BG_COLOR);
		add(spOuter);
	}
	
	
	/*
	 * Show On Screen
	 */
	public void showFrame()
	{
		resetGameList();
		setExtendedState(JFrame.MAXIMIZED_BOTH);
		pack();
		setVisible(true);
	}

	/*
	 * Menu Actions
	 */
	private void showLeagueManager()
	{
		LeagueManager lm = new LeagueManager(this, controller);
		lm.showDialog();
		resetGameList();
	}

	private void showPreferences()
	{
		PrefsDialog pd = new PrefsDialog(this);
		pd.showDialog();
		resetGameList();
	}
	
	private void showInfo()
	{
		InfoDialog hd = new InfoDialog(this);
		hd.showDialog();
	}

	private void exit()
	{
		functionalFilterPanel.saveLastFilter();
		System.exit(0);
	}
	/*
	 * Menu Methods
	 */
	private JMenuItem createMenuItem(JMenu menu, String name, char mnemonic, int acceleratorKeyCode, String iconFile )
	{
		JMenuItem mi = new JMenuItem(name);
		mi.setMnemonic(mnemonic);
		mi.addActionListener(this);
		mi.setAccelerator(KeyStroke.getKeyStroke(acceleratorKeyCode, KeyEvent.CTRL_MASK));
		mi.setIcon(new ImageIcon(Toolkit.getDefaultToolkit().getImage(FussballStats.class.getResource(iconFile))));
		menu.add(mi);
		return mi;
	}
	
	private void populateMenuBar()
	{
		JMenu mainMenu = new JMenu("Fussball Statistiken");
		mainMenu.setMnemonic('F');
		menuBar.add(mainMenu);
		
		miNewFilter = createMenuItem(mainMenu,"Neuer Filter",'N',KeyEvent.VK_N, NEW_ICON);
		miLoadFilter = createMenuItem(mainMenu, "Filter Laden", 'L',KeyEvent.VK_L,LOAD_ICON);
		miSaveFilter = createMenuItem(mainMenu, "Filter Speichern", 'S',KeyEvent.VK_S,SAVE_ICON);
		mainMenu.addSeparator();
		miShowLeagueManager = createMenuItem(mainMenu, "Liga Manager", 'M',KeyEvent.VK_M, MANAGER_ICON);
		miShowPreferences = createMenuItem(mainMenu, "Einstellungen", 'E',KeyEvent.VK_E, SETTINGS_ICON);
		mainMenu.addSeparator();
		miExit = createMenuItem(mainMenu, "Beenden", 'B',KeyEvent.VK_B,EXIT_ICON);
		
		
		JMenu editMenu = new JMenu("Bearbeiten");
		editMenu.setMnemonic('B');
		menuBar.add(editMenu);

		miUndo = createMenuItem(editMenu, "Rückgängig", 'R', KeyEvent.VK_Z,UNDO_ICON );
		miUndo.setEnabled(false);
		miRedo = createMenuItem(editMenu, "Wiederholen", 'W', KeyEvent.VK_Y,REDO_ICON );
		miRedo.setEnabled(false);
		

		JMenu infoMenu = new JMenu("Info");
		infoMenu.setMnemonic('I');
		menuBar.add(infoMenu);
		
		miInfo = createMenuItem(infoMenu, "Info", 'I', KeyEvent.VK_I, HELP_ICON);
	}

	/*
	 * Internal Methods
	 */
	private void resetGameList()
	{
		List<Game> gameList = new ArrayList<>();
		for(League league : controller.getEntities(League.class))
			for(Season season : league.getSeasons())
				for(Matchday matchday : season.getMatchdays())
					for(Game game : matchday.getGames())
						if(functionalFilterPanel.check(season, game))
							gameList.add(game);
		functionalGameTable.setGames(gameList);
		functionalResultTable.setGames(gameList);
	}

	/*
	 * BiFilterListener Methods
	 */
	@Override
	public void filter(BiFilterEvent<Season,Game> e)
	{
		miUndo.setEnabled(filterLog.hasUndo());
		miRedo.setEnabled(filterLog.hasRedo());
		if(e.isFilterModified() || e.getType() == BiFilterEvent.SET_ACTIVE)
			resetGameList();
	}


	/*
	 * ActionListener Methods
	 */
	@Override
	public void actionPerformed(ActionEvent e)
	{
		if(e.getSource() == miNewFilter)
			functionalFilterPanel.newFilter();
		else if(e.getSource() == miLoadFilter)
			functionalFilterPanel.loadFilter();
		else if(e.getSource() == miSaveFilter)
			functionalFilterPanel.saveFilter();
		else if(e.getSource() == miShowLeagueManager)
			showLeagueManager();
		else if(e.getSource() == miShowPreferences)
			showPreferences();
		else if(e.getSource() == miUndo)
			functionalFilterPanel.setFilterPanel(filterLog.undo());
		else if(e.getSource() == miRedo)
			functionalFilterPanel.setFilterPanel(filterLog.redo());
		else if(e.getSource() == miInfo)
			showInfo();
		else if(e.getSource() == miExit)
			exit();
	}
}
