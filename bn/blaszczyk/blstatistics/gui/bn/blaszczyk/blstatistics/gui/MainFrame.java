package bn.blaszczyk.blstatistics.gui;

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
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		this.leagues = leagues;
		List<String> teams = new ArrayList<>();
		for(League league : leagues)
			for(String team : league.getTeams())
				if(!teams.contains(team))
					teams.add(team);
		Collections.sort(teams);

		functionalFilterPanel = new FunctionalFilterPanel(teams);
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
