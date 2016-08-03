package bn.blaszczyk.blstatistics.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;

import bn.blaszczyk.blstatistics.core.*;
import bn.blaszczyk.blstatistics.gui.filters.*;

@SuppressWarnings("serial")
public class MainFrame extends JFrame implements BiFilterListener<Season,Game>
{
	private FunctionalFilterPanel functionalFilterPanel;
	private FunctionalGameTable functionalGameTable = new FunctionalGameTable();
	private FunctionalResultTable functionalResultTable = new FunctionalResultTable();
	
	private List<Game> gameList;
	private Iterable<League> leagues;
	
	public MainFrame(Iterable<League> leagues)
	{
		super("Fussball Statistiken");
		getContentPane().setLayout(new BorderLayout(5,5));
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		this.leagues = leagues;
		List<String> teams = new ArrayList<>();
		for(League league : leagues)
			for(String team : league.getTeams())
				if(!teams.contains(team))
					teams.add(team);
		Collections.sort(teams);

		functionalFilterPanel = new FunctionalFilterPanel(teams);
		functionalFilterPanel.setMinimumSize(new Dimension(400,700));
		functionalFilterPanel.addFilterListener(this);
		
		functionalResultTable.addListSelectionListener( e -> {
			if(!e.getValueIsAdjusting())
			{
				String team = functionalResultTable.getSelectedTeam();
				functionalGameTable.setSelectedTeam(team);
			}
		});
		
		
		
		JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, functionalResultTable, new JScrollPane(functionalGameTable));
		splitPane.setDividerLocation(1100);
		add(functionalFilterPanel, BorderLayout.WEST);
		add(splitPane, BorderLayout.CENTER);
		
		resetTable();
	}
	
	public void showFrame()
	{
		setExtendedState(JFrame.MAXIMIZED_BOTH);
		pack();
		setVisible(true);
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
