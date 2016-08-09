package bn.blaszczyk.blstatistics.tools;

import java.awt.event.ActionListener;
import javax.swing.JMenu;
import javax.swing.JMenuItem;

import bn.blaszczyk.blstatistics.core.*;
import bn.blaszczyk.blstatistics.gui.corefilters.*;
import bn.blaszczyk.blstatistics.gui.filters.*;

public class NewFilterMenu  
{
	
	private static BiFilterPanel<Season,Game> panel;
	
	public static void addMenuItems(JMenu menu, ActionListener listener)
	{
		/*
		 * Goal Filters
		 */
		
		JMenu goalFilters = new JMenu("Tor Filter");

		addMenuItem(goalFilters,"Tore").addActionListener( e -> {
			setPanel( FilterPanelAdapter.getSecondArgAdapter( GoalFilterPanel.getGoalFilterPanel("=",0) ) );
			listener.actionPerformed(e);
		});
		addMenuItem(goalFilters,"Heimtore").addActionListener( e -> {
			setPanel( FilterPanelAdapter.getSecondArgAdapter( GoalFilterPanel.getHomeGoalFilterPanel("=",0) ) );
			listener.actionPerformed(e);
		});
		addMenuItem(goalFilters,"Auswärtstore").addActionListener( e -> {
			setPanel( FilterPanelAdapter.getSecondArgAdapter( GoalFilterPanel.getAwayGoalFilterPanel("=",0) ) );
			listener.actionPerformed(e);
		});
		addMenuItem(goalFilters,"Tordifferenz").addActionListener( e -> {
			setPanel( FilterPanelAdapter.getSecondArgAdapter( GoalFilterPanel.getGoalDiffFilterPanel("=",0) ) );
			listener.actionPerformed(e);
		});
		
		/*
		 * Team Filters
		 */
		JMenu teamFilters = new JMenu("Team Filter");
		
		addMenuItem(teamFilters,"Team").addActionListener( e -> {
			setPanel( FilterPanelAdapter.getSecondArgAdapter( new TeamFilterPanel() ) );
			listener.actionPerformed(e);
		});
		addMenuItem(teamFilters,"Direkter Vergleich").addActionListener( e -> {
			setPanel( FilterPanelAdapter.getSecondArgAdapter( new SubLeagueFilterPanel() ) );
			listener.actionPerformed(e);
		});
		
		/*
		 * Date Filters
		 */
		JMenu dateFilters = new JMenu("Spieltag Filter");

		addMenuItem(dateFilters, "Datum").addActionListener( e -> {
			setPanel(FilterPanelAdapter.getSecondArgAdapter(new DateFilterPanel()));
			listener.actionPerformed(e);
		});
		
		addMenuItem(dateFilters, "Wochentag").addActionListener( e -> {
			setPanel(FilterPanelAdapter.getSecondArgAdapter(new DayOfWeekFilterPanel()));
			listener.actionPerformed(e);
		});
		
		addMenuItem(dateFilters,"Spieltag").addActionListener( e -> {
			setPanel(FilterPanelAdapter.getSecondArgAdapter( new MatchDayFilterPanel()));
			listener.actionPerformed(e);
		});
		
		/*
		 * Season Filters
		 */
		
		JMenu seasonFilters = new JMenu("Saison Filter");

		addMenuItem(seasonFilters,"Liga").addActionListener( e -> {
			setPanel( FilterPanelAdapter.getFirstArgAdapter( new SingleLeagueFilterPanel()) );
			listener.actionPerformed(e);
		});
		addMenuItem(seasonFilters,"Saison").addActionListener( e -> {
			setPanel( FilterPanelAdapter.getFirstArgAdapter( new SeasonFilterPanel()) );
			listener.actionPerformed(e);
		});
		addMenuItem(seasonFilters,"Hin-/Rückrunde").addActionListener( e -> {
			setPanel( new RoundFilterPanel() );
			listener.actionPerformed(e);
		});
		
		/*
		 * Logical Filters
		 */

		JMenu logicalFilters = new JMenu("Logische Filter");
		
		addMenuItem(logicalFilters,"TRUE, FALSE").addActionListener( e -> {
			setPanel( new AbsoluteOperatorFilterPanel<>(true) );
			listener.actionPerformed(e);
		});
		addMenuItem(logicalFilters,"NOT").addActionListener( e -> {
			setPanel( new UnaryOperatorFilterPanel<>() );
			listener.actionPerformed(e);
		});
		addMenuItem(logicalFilters,"AND, OR, XOR").addActionListener( e -> {
			setPanel( new MultiOperatorFilterPanel<>());
			listener.actionPerformed(e);
		});
		addMenuItem(logicalFilters,"IF_THEN_ELSE").addActionListener( e -> {
			setPanel( new IfThenElseFilterPanel<>());
			listener.actionPerformed(e);
		});
		


		menu.add(goalFilters);
		menu.add(teamFilters);
		menu.add(dateFilters);
		menu.add(seasonFilters);
		menu.add(logicalFilters);
		
		
		addMenuItem(menu,"Lade Filter").addActionListener( e -> {
			setPanel( FilterIO.loadFilter() );
			listener.actionPerformed(e);
		});
	}

//	public static BiFilterPanel<Season,Game> getPanel()
//	{
//		return panel;
//	}
	
	@SuppressWarnings("unchecked")
	public static <T,U> BiFilterPanel<T,U> getPanel()
	{
		return (BiFilterPanel<T, U>) panel;
	}
	
	private static JMenuItem addMenuItem(JMenu menu, String label)
	{
		JMenuItem item = new JMenuItem(label);
		menu.add(item);
		return item;
	}
	
	private static void setPanel(BiFilterPanel<Season,Game> panel)
	{
		NewFilterMenu.panel = panel;
	}

}
