package bn.blaszczyk.blstatistics.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.swing.JPanel;
import javax.swing.JScrollPane;

import bn.blaszczyk.blstatistics.core.*;
import bn.blaszczyk.blstatistics.gui.filters.*;

@SuppressWarnings("serial")
public class FilteredGameTable extends JPanel implements BiFilterListener<Season,Game>
{

	private FunctionalFilterPanel filterPanel;
	private GameTable gameTable = new GameTable();
	private ResultTable resultTable = new ResultTable();
	private List<Game> gameList;
	private Iterable<League> leagues;
	
	public FilteredGameTable(Iterable<League> leagues)
	{
		this.leagues = leagues;
		
		List<String> teams = new ArrayList<>();
		for(League league : leagues)
			for(String team : league.getTeams())
				if(!teams.contains(team))
					teams.add(team);
		Collections.sort(teams);

		filterPanel = new FunctionalFilterPanel(teams);
		filterPanel.addFilterListener(this);
		filterPanel.setMinimumSize(new Dimension(300,700));

		resetTable();
		
		setLayout(new BorderLayout(5,5));
		add(filterPanel, BorderLayout.WEST);
		add(new JScrollPane(gameTable), BorderLayout.CENTER);
		add(new JScrollPane(resultTable),BorderLayout.EAST);
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
		Table table = new Table(gameList);
		table.sort();
		resultTable.setSource(table);
	}


	@Override
	public String toString()
	{
		return "FilteredGamePanel";
	}
}
