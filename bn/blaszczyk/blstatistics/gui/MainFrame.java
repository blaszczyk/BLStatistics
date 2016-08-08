package bn.blaszczyk.blstatistics.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.KeyStroke;

import bn.blaszczyk.blstatistics.BLStatistics;
import bn.blaszczyk.blstatistics.core.*;
import bn.blaszczyk.blstatistics.gui.filters.*;

@SuppressWarnings("serial")
public class MainFrame extends JFrame implements BiFilterListener<Season,Game>, ActionListener
{
	private static final String ICON_FILE = "data/icon.png";
	
	private JMenuBar menuBar = new JMenuBar();
	private JMenuItem newFilter, loadFilter, saveFilter, showLeagueManager, exit;
	private JMenu menuBack = new JMenu("Rückgängig");
	private JMenu menuFore = new JMenu("Wiederholen");
	
	
	private FunctionalFilterPanel functionalFilterPanel;
	private FunctionalGameTable functionalGameTable = new FunctionalGameTable();
	private FunctionalResultTable functionalResultTable = new FunctionalResultTable();
	
	private List<Game> gameList;
	private List<League> leagues;
	private List<String> leagueNames = new ArrayList<>();
	private List<String> teams = new ArrayList<>();
	
	public MainFrame(List<League> leagues)
	{
		super("Fussball Statistiken");
		this.leagues = leagues;

		initLists();
		if(teams.size() == 0)
			showLeagueManager();
		
		populateMenuBar();
		setJMenuBar(menuBar);
		initComponents();
		initIcon();
		
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e)
			{
				exit();
			}
		});
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		resetTable();
	}
	
	
	
	public void showFrame()
	{
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
	
	private void initComponents()
	{
		functionalFilterPanel = new FunctionalFilterPanel(teams,leagueNames);
		functionalFilterPanel.addFilterListener(this);

		functionalResultTable.addListSelectionListener( e -> {
			if(!e.getValueIsAdjusting())
				functionalGameTable.setSelectedTeams(functionalResultTable.getSelectedTeams());
		});

		functionalGameTable.addListSelectionListener( e -> {
			if(!e.getValueIsAdjusting())
				functionalResultTable.setSelectedTeams(functionalGameTable.getSelectedTeams());
		});
		
		JSplitPane spInner = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, functionalResultTable, new JScrollPane(functionalGameTable));
		spInner.setDividerLocation(940);
		JSplitPane spOuter = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, functionalFilterPanel, spInner );
		spOuter.setDividerLocation(355);
		add(spOuter);
	}

	private void initLists()
	{
		for(League league : leagues)
		{
			for(String team : league.getTeams())
				if(!teams.contains(team))
					teams.add(team);
			if(!leagueNames.contains(league.getName()))
				leagueNames.add(league.getName());
		}
		Collections.sort(teams);
	}
	
	private void initIcon()
	{
		try
		{
			setIconImage( ImageIO.read( BLStatistics.class.getResourceAsStream(ICON_FILE) )  );
		}
		catch (IOException e1)
		{
		}
	}
	
	private JMenuItem createMenuItem(JMenu menu, String name, char mnemonic, int acceleratorKeyCode )
	{
		JMenuItem mi = new JMenuItem(name);
		mi.setMnemonic(mnemonic);
		mi.addActionListener(this);
		mi.setAccelerator(KeyStroke.getKeyStroke(acceleratorKeyCode, KeyEvent.CTRL_MASK));
		menu.add(mi);
		return mi;
	}
	
	private void populateMenuBar()
	{
		JMenu mainMenu = new JMenu("Fussball Statistiken");
		mainMenu.setMnemonic('S');
		
		newFilter = createMenuItem(mainMenu,"Neuer Filter",'N',KeyEvent.VK_N);
		loadFilter = createMenuItem(mainMenu, "Filter Laden", 'L',KeyEvent.VK_L);
		saveFilter = createMenuItem(mainMenu, "Filter Speichern", 'S',KeyEvent.VK_S);
		mainMenu.addSeparator();
		showLeagueManager = createMenuItem(mainMenu, "Liga Manager", 'M',KeyEvent.VK_M);
		mainMenu.addSeparator();
		exit = createMenuItem(mainMenu, "Beenden", 'B',KeyEvent.VK_B);
		
		menuBar.add(mainMenu);
		
		JMenu editMenu = new JMenu("Bearbeiten");
		editMenu.setMnemonic('B');
		menuBack.setEnabled(false);
		editMenu.add(menuBack);
		menuFore.setEnabled(false);
		editMenu.add(menuFore);
		
		menuBar.add(editMenu);
		
		
	}

	private void resetTable()
	{
		gameList = new ArrayList<>();
		for(League league : leagues)
			for(Season season : league)
				for(Game game : season.getAllGames())
					if(functionalFilterPanel.check(season, game))
						gameList.add(game);
		functionalGameTable.setGameList(gameList);
		functionalResultTable.setSource(gameList);
	}

	private void exit()
	{
		functionalFilterPanel.saveLastFilter();
		System.exit(0);
	}
	
	@Override
	public void filter(BiFilterEvent<Season,Game> e)
	{
		functionalFilterPanel.getFilterLog().populateBackwardsMenu(menuBack);
		functionalFilterPanel.getFilterLog().populateForwardsMenu(menuFore);
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
		else if(e.getSource() == exit)
			exit();
	}
}
