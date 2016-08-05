package bn.blaszczyk.blstatistics.gui;

import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;

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
	private List<String> teams = new ArrayList<>();
	
	public MainFrame(List<League> leagues)
	{
		super("Fussball Statistiken");
		this.leagues = leagues;
		
		populateMenuBar();
		initTeams();
		
		setJMenuBar(menuBar);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		

		functionalFilterPanel = new FunctionalFilterPanel(teams,leagues);
		functionalFilterPanel.addFilterListener(this);
		
		functionalResultTable.addListSelectionListener( e -> {
			if(!e.getValueIsAdjusting())
				functionalGameTable.setSelectedTeams(functionalResultTable.getSelectedTeams());
		});
		
		JSplitPane spInner = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, functionalResultTable, new JScrollPane(functionalGameTable));
		JSplitPane spOuter = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, functionalFilterPanel, spInner );
		add(spOuter);
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



	
	private void initTeams()
	{
		for(League league : leagues)
			for(String team : league.getTeams())
				if(!teams.contains(team))
					teams.add(team);
		Collections.sort(teams);
	}
	
	private void populateMenuBar()
	{
		JMenuItem loadFilter = new JMenuItem("Filter Laden");
		JMenuItem saveFilter = new JMenuItem("Filter Speichern");
		JMenuItem showLeagueManager = new JMenuItem("Liga Manager");
		JMenuItem exit = new JMenuItem("Beenden");
		
		ActionListener listener = e -> {
			if(e.getSource() == loadFilter)
				functionalFilterPanel.loadFilter();
			else if(e.getSource() == saveFilter)
				functionalFilterPanel.saveFilter();
			else if(e.getSource() == showLeagueManager)
				showLeagueManager();
			else if(e.getSource() == exit)
				System.exit(0);
		};
		loadFilter.addActionListener(listener);
		
		saveFilter.addActionListener(listener);
		
		showLeagueManager.addActionListener(listener);
		
		exit.addActionListener(listener);
		
		JMenu mainMenu = new JMenu("Fussball Statistiken");
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
