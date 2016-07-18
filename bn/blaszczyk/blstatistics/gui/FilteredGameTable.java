package bn.blaszczyk.blstatistics.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.swing.JPanel;
import javax.swing.JScrollPane;

import bn.blaszczyk.blstatistics.core.*;
import bn.blaszczyk.blstatistics.filters.BiFilterListener;
import bn.blaszczyk.blstatistics.gui.filters.*;

@SuppressWarnings("serial")
public class FilteredGameTable extends JPanel implements BiFilterListener<Season,Game>
{

	private BiFilterPanel<Season,Game> filterPanel;
	private GameTable gameTable = new GameTable();
	private List<Game> gameList;
	private Iterable<League> leagues;
	private PanelMenu<Season,Game> panelMenu;
	
	public FilteredGameTable(Iterable<League> leagues)
	{
		this.leagues = leagues;
		
		List<String> teams = new ArrayList<>();
		for(League league : leagues)
			for(String team : league.getTeams())
				if(!teams.contains(team))
					teams.add(team);
		Collections.sort(teams);
		panelMenu = new GameFilterPanelMenu(teams);
		
		filterPanel = new UnaryOperatorFilterPanel<>(panelMenu, false);
		filterPanel.addFilterListener(this);
		filterPanel.getPanel().setPreferredSize(new Dimension(300,1000));
		filterPanel.paint();
		resetTable();
		
		setLayout(new BorderLayout(5,5));
		add(new JScrollPane(gameTable), BorderLayout.CENTER);
		add(filterPanel.getPanel(), BorderLayout.WEST);
	}

	@Override
	public void filter()
	{
		resetTable();
		filterPanel.paint();
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
	}


}
