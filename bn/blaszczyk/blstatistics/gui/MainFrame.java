package bn.blaszczyk.blstatistics.gui;

import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.KeyStroke;

import bn.blaszczyk.blstatistics.core.*;
import bn.blaszczyk.blstatistics.gui.filters.*;

@SuppressWarnings("serial")
public class MainFrame extends JFrame implements BiFilterListener<Season,Game>
{
	private JMenuBar menuBar = new JMenuBar();
	
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
		
		populateMenuBar();
		initLists();
		
		setJMenuBar(menuBar);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		

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
		
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e)
			{
				exit();
			}
		});
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
	
	private void populateMenuBar()
	{
		JMenuItem loadFilter = new JMenuItem("Filter Laden");
		loadFilter.setMnemonic('l');
		loadFilter.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_L, KeyEvent.CTRL_MASK));
		JMenuItem saveFilter = new JMenuItem("Filter Speichern");
		saveFilter.setMnemonic('s');
		saveFilter.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, KeyEvent.CTRL_MASK));
		JMenuItem showLeagueManager = new JMenuItem("Liga Manager");
		showLeagueManager.setMnemonic('m');
		showLeagueManager.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_M, KeyEvent.CTRL_MASK));
		JMenuItem exit = new JMenuItem("Beenden");
		exit.setMnemonic('b');
		exit.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_B, KeyEvent.CTRL_MASK));
		
		ActionListener listener = e -> {
			if(e.getSource() == loadFilter)
				functionalFilterPanel.loadFilter();
			else if(e.getSource() == saveFilter)
				functionalFilterPanel.saveFilter();
			else if(e.getSource() == showLeagueManager)
				showLeagueManager();
			else if(e.getSource() == exit)
				exit();
		};
		loadFilter.addActionListener(listener);
		
		saveFilter.addActionListener(listener);
		
		showLeagueManager.addActionListener(listener);
		
		exit.addActionListener(listener);
		
		JMenu mainMenu = new JMenu("Fussball Statistiken");
		mainMenu.setMnemonic('f');
		mainMenu.add(loadFilter);
		mainMenu.add(saveFilter);
		mainMenu.add(showLeagueManager);
		mainMenu.add(exit);
		
		menuBar.add(mainMenu);
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
		functionalFilterPanel.saveFilter("last");
		System.exit(0);
	}
	
	@Override
	public void filter(BiFilterEvent<Season,Game> e)
	{
		resetTable();
	}

	@Override
	public String toString()
	{
		return "MainFrame";
	}
}
