package bn.blaszczyk.blstatistics.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.swing.JPanel;
import javax.swing.JScrollPane;

import bn.blaszczyk.blstatistics.core.*;
import bn.blaszczyk.blstatistics.gui.corefilters.GameFilterPanelMenu;
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
		
		setFilterPanel(new BlankFilterPanel<Season, Game>(panelMenu));
		
		setLayout(new BorderLayout(5,5));
		add(new JScrollPane(gameTable), BorderLayout.CENTER);
		add(filterPanel.getPanel(), BorderLayout.WEST);
		resetTable();
	}

	private void setFilterPanel(BiFilterPanel<Season,Game> panel)
	{
		this.filterPanel = panel;
		panel.addFilterListener(this);
		filterPanel.getPanel().setPreferredSize(new Dimension(300,1000));
		paint();
	}
	
	private void paint()
	{
		filterPanel.paint();
		removeAll();
		add(new JScrollPane(gameTable), BorderLayout.CENTER);
		add(filterPanel.getPanel(), BorderLayout.WEST);
		revalidate();
	}
	
	@Override
	public void filter(BiFilterEvent<Season,Game> e)
	{
		if(e.getType() == BiFilterEvent.RESET_PANEL)
			setFilterPanel(e.getPanel());
		else
			filterPanel.paint();
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
	}


}
