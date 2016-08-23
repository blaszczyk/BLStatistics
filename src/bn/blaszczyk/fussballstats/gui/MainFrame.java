package bn.blaszczyk.fussballstats.gui;

/*
 * TODO
 * icon source: https://www.iconfinder.com/iconsets/32x32-free-design-icons
 * free licence
 * include link to origin
 */


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
import bn.blaszczyk.fussballstats.core.*;
import bn.blaszczyk.fussballstats.gui.filters.*;
import bn.blaszczyk.fussballstats.tools.FilterLog;

@SuppressWarnings("serial")
public class MainFrame extends JFrame implements BiFilterListener<Season,Game>, ActionListener
{
	private static final String ICON_FILE = "data/icon.png";
	private static final String NEW_ICON = "data/new.png";
	private static final String LOAD_ICON = "data/load.png";
	private static final String SAVE_ICON = "data/save.png";
	private static final String MANAGER_ICON = "data/manager.png";
	private static final String SETTINGS_ICON = "data/settings.png";
	private static final String EXIT_ICON = "data/exit.png";
	private static final String UNDO_ICON = "data/undo.png";
	private static final String REDO_ICON = "data/redo.png";
	
	private JMenuBar menuBar = new JMenuBar();
	private JMenuItem newFilter, loadFilter, saveFilter, showLeagueManager, showPreferences, exit;
	private JMenuItem menuUndo, menuRedo;
	
	private FunctionalFilterPanel functionalFilterPanel;
	private FunctionalGameTable functionalGameTable = new FunctionalGameTable();
	private FunctionalResultTable functionalResultTable = new FunctionalResultTable();
	
	private FilterLog filterLog;
	
	private List<League> leagues;
	
	public MainFrame(List<League> leagues)
	{
		super("Fussball Statistiken");
		this.leagues = leagues;
		
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
	
	public void showFrame()
	{
		resetTable();
		setExtendedState(JFrame.MAXIMIZED_BOTH);
		pack();
		setVisible(true);
	}

	private void showLeagueManager()
	{
		LeagueManager lm = new LeagueManager(this, leagues);
		lm.showDialog();
		resetTable();
	}

	private void showPreferences()
	{
		PrefsDialog pd = new PrefsDialog(this);
		pd.showDialog();
	}
	
	
	
	private void initComponents()
	{
		filterLog = new FilterLog(100);
		
		functionalFilterPanel= new FunctionalFilterPanel(filterLog);
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
		JSplitPane spOuter = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, functionalFilterPanel, spInner );
		spOuter.setDividerLocation(355);
		add(spOuter);
	}
	
	
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
		mainMenu.setMnemonic('S');
		
		newFilter = createMenuItem(mainMenu,"Neuer Filter",'N',KeyEvent.VK_N, NEW_ICON);
		loadFilter = createMenuItem(mainMenu, "Filter Laden", 'L',KeyEvent.VK_L,LOAD_ICON);
		saveFilter = createMenuItem(mainMenu, "Filter Speichern", 'S',KeyEvent.VK_S,SAVE_ICON);
		mainMenu.addSeparator();
		showLeagueManager = createMenuItem(mainMenu, "Liga Manager", 'M',KeyEvent.VK_M, MANAGER_ICON);
		showPreferences = createMenuItem(mainMenu, "Einstellungen", 'E',KeyEvent.VK_E, SETTINGS_ICON);
		mainMenu.addSeparator();
		exit = createMenuItem(mainMenu, "Beenden", 'B',KeyEvent.VK_B,EXIT_ICON);
		
		menuBar.add(mainMenu);
		
		JMenu editMenu = new JMenu("Bearbeiten");
		editMenu.setMnemonic('B');

		menuUndo = createMenuItem(editMenu, "Rückgängig", 'R', KeyEvent.VK_Z,UNDO_ICON );
		menuRedo = createMenuItem(editMenu, "Wiederholen", 'W', KeyEvent.VK_Y,REDO_ICON );
		
		menuUndo.setEnabled(false);
		menuRedo.setEnabled(false);
		
		menuBar.add(editMenu);
	}

	private void resetTable()
	{
		List<Game> gameList = new ArrayList<>();
		for(League league : leagues)
			for(Season season : league)
				for(Game game : season)
					if(functionalFilterPanel.check(season, game))
						gameList.add(game);
		functionalGameTable.setGames(gameList);
		functionalResultTable.setGames(gameList);
	}

	private void exit()
	{
		functionalFilterPanel.saveLastFilter();
		System.exit(0);
	}
	
	@Override
	public void filter(BiFilterEvent<Season,Game> e)
	{
		menuUndo.setEnabled(filterLog.hasUndo());
		menuRedo.setEnabled(filterLog.hasRedo());
		resetTable();
	}

	@Override
	public String toString()
	{
		return "MainFrame";
	}
	
	@Override
	public void actionPerformed(ActionEvent e)
	{
		if(e.getSource() == newFilter)
			functionalFilterPanel.newFilter();
		if(e.getSource() == loadFilter)
			functionalFilterPanel.loadFilter();
		else if(e.getSource() == saveFilter)
			functionalFilterPanel.saveFilter();
		else if(e.getSource() == showLeagueManager)
			showLeagueManager();
		else if(e.getSource() == showPreferences)
			showPreferences();
		else if(e.getSource() == menuUndo)
			functionalFilterPanel.setFilterPanel(filterLog.undo());
		else if(e.getSource() == menuRedo)
			functionalFilterPanel.setFilterPanel(filterLog.redo());
		else if(e.getSource() == exit)
			exit();
	}
}
