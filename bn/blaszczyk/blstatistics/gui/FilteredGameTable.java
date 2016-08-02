package bn.blaszczyk.blstatistics.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;

import bn.blaszczyk.blstatistics.core.*;
import bn.blaszczyk.blstatistics.gui.filters.*;

@SuppressWarnings("serial")
public class FilteredGameTable extends JPanel implements BiFilterListener<Season,Game>
{
	private FunctionalFilterPanel filterPanel;
	private GameTable gameTable = new GameTable();
	private FunctionalResultTable functionalResultTable = new FunctionalResultTable();
	
	private List<Game> gameList;
	private Iterable<League> leagues;
	
	public FilteredGameTable(Iterable<League> leagues)
	{
		super(new BorderLayout(5,5));
		this.leagues = leagues;
		
		List<String> teams = new ArrayList<>();
		for(League league : leagues)
			for(String team : league.getTeams())
				if(!teams.contains(team))
					teams.add(team);
		Collections.sort(teams);

		filterPanel = new FunctionalFilterPanel(teams);
		filterPanel.addFilterListener(this);
		filterPanel.setMinimumSize(new Dimension(400,700));
		
		JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, functionalResultTable, new JScrollPane(gameTable));
		splitPane.setDividerLocation(1100);
		add(filterPanel, BorderLayout.WEST);
		add(splitPane, BorderLayout.CENTER);
		
		resetTable();
	}
	
	
	@Override
	public void filter(BiFilterEvent<Season,Game> e)
	{
		resetTable();
	}
	
	private void resetTable()
	{
		gameList = new ArrayList<>();
		for(League league : leagues)
			for(Season season : league)
				for(Game game : season.getAllGames())
					if(filterPanel.check(season, game))
						gameList.add(game);
		gameTable.setSource(gameList);
		functionalResultTable.setSource(gameList);
	}


	@Override
	public String toString()
	{
		return "FilteredGamePanel";
	}
}
