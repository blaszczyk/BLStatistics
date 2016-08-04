package bn.blaszczyk.blstatistics.gui.corefilters;

import java.awt.event.ActionListener;
//import java.util.ArrayList;
//import java.util.Collections;
import java.util.List;
//import java.util.Map;

import javax.swing.JMenu;
import javax.swing.JMenuItem;

import bn.blaszczyk.blstatistics.core.*;
import bn.blaszczyk.blstatistics.gui.filters.AbsoluteOperatorFilterPanel;
import bn.blaszczyk.blstatistics.gui.filters.BiFilterPanel;
import bn.blaszczyk.blstatistics.gui.filters.FilterPanelAdapter;
import bn.blaszczyk.blstatistics.gui.filters.IfThenElseFilterPanel;
import bn.blaszczyk.blstatistics.gui.filters.MultiOperatorFilterPanel;
import bn.blaszczyk.blstatistics.gui.filters.FilterPanelManager;
import bn.blaszczyk.blstatistics.gui.filters.UnaryOperatorFilterPanel;
import bn.blaszczyk.blstatistics.tools.FilterIO;
//import bn.blaszczyk.blstatistics.tools.TeamAlias;

public class GameFilterPanelManager implements FilterPanelManager<Season,Game> {
	
	private BiFilterPanel<Season,Game> panel;
	private List<String> teams;
	private FilterIO filterIO;
	
	
	public GameFilterPanelManager(List<String> teams, FilterIO filterIO)
	{
		this.teams = teams;
//		this.teams = new ArrayList<>();
//		for(String team : teams)
//			this.teams.add(TeamAlias.getAlias(team));
//		Collections.sort(this.teams);
		this.filterIO = filterIO;
	}
	
	@Override
	public void addMenuItems(JMenu menu, ActionListener listener)
	{
		/*
		 * Goal Filters
		 */
		
		JMenu goalFilters = new JMenu("Tor Filter");

		addMenuItem(goalFilters,"Tore").addActionListener( e -> {
			setPanel( FilterPanelAdapter.getSecondArgAdapter( GoalFilterPanel.getGoalFilterPanel("=",0),this ) );
			listener.actionPerformed(e);
		});
		addMenuItem(goalFilters,"Heimtore").addActionListener( e -> {
			setPanel( FilterPanelAdapter.getSecondArgAdapter( GoalFilterPanel.getHomeGoalFilterPanel("=",0),this ) );
			listener.actionPerformed(e);
		});
		addMenuItem(goalFilters,"Auswärtstore").addActionListener( e -> {
			setPanel( FilterPanelAdapter.getSecondArgAdapter( GoalFilterPanel.getAwayGoalFilterPanel("=",0),this ) );
			listener.actionPerformed(e);
		});
		addMenuItem(goalFilters,"Tordifferenz").addActionListener( e -> {
			setPanel( FilterPanelAdapter.getSecondArgAdapter( GoalFilterPanel.getGoalDiffFilterPanel("=",0),this ) );
			listener.actionPerformed(e);
		});
		
		/*
		 * Game Filters
		 */
		JMenu gameFilters = new JMenu("Team Filter");
		
		addMenuItem(gameFilters,"Team").addActionListener( e -> {
			setPanel( FilterPanelAdapter.getSecondArgAdapter( new TeamFilterPanel(teams),this ) );
			listener.actionPerformed(e);
		});
		addMenuItem(gameFilters,"Direkter Vergleich").addActionListener( e -> {
			setPanel( FilterPanelAdapter.getSecondArgAdapter( new SubLeagueFilterPanel(teams),this ) );
			listener.actionPerformed(e);
		});
		
		/*
		 * Season Filters
		 */
		JMenu seasonFilters = new JMenu("Spieltag Filter");
		
		addMenuItem(seasonFilters,"Saison").addActionListener( e -> {
			setPanel( FilterPanelAdapter.getFirstArgAdapter( new SeasonFilterPanel(),this) );
			listener.actionPerformed(e);
		});
		addMenuItem(seasonFilters,"Hin-/Rückrunde").addActionListener( e -> {
			setPanel( new RoundFilterPanel(this) );
			listener.actionPerformed(e);
		});
		addMenuItem(seasonFilters,"Spieltag").addActionListener( e -> {
			setPanel(FilterPanelAdapter.getSecondArgAdapter( new MatchDayFilterPanel(),this));
			listener.actionPerformed(e);
		});
		
		/*
		 * Logical Filters
		 */

		JMenu logicalFilters = new JMenu("Logische Filter");
		
		addMenuItem(logicalFilters,"TRUE, FALSE").addActionListener( e -> {
			setPanel( new AbsoluteOperatorFilterPanel<>(true,this) );
			listener.actionPerformed(e);
		});
		addMenuItem(logicalFilters,"NOT").addActionListener( e -> {
			setPanel( new UnaryOperatorFilterPanel<>(this) );
			listener.actionPerformed(e);
		});
		addMenuItem(logicalFilters,"AND, OR, XOR").addActionListener( e -> {
			setPanel( new MultiOperatorFilterPanel<>(this));
			listener.actionPerformed(e);
		});
		addMenuItem(logicalFilters,"IF_THEN_ELSE").addActionListener( e -> {
			setPanel( new IfThenElseFilterPanel<>(this));
			listener.actionPerformed(e);
		});
		


		menu.add(goalFilters);
		menu.add(gameFilters);
		menu.add(seasonFilters);
		menu.add(logicalFilters);
		
		
		addMenuItem(menu,"Lade Filter").addActionListener( e -> {
			setPanel( filterIO.loadFilter() );
			listener.actionPerformed(e);
		});
	}
	
	@Override
	public BiFilterPanel<Season,Game> getPanel()
	{
		return panel;
	}
	
	private JMenuItem addMenuItem(JMenu menu, String label)
	{
		JMenuItem item = new JMenuItem(label);
		menu.add(item);
		return item;
	}
	
	private void setPanel(BiFilterPanel<Season,Game> panel)
	{
		this.panel = panel;
	}

	public List<String> getTeams()
	{
		return teams;
	}

//	@Override
//	public void savePanel(BiFilterPanel<Season, Game> panel)
//	{
//		String name = JOptionPane.showInputDialog("Bitte Namen eingeben.", "Filter");
//		filters.put(name,panel);
//	}


}
