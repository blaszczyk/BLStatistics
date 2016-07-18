package bn.blaszczyk.blstatistics.gui.filters;

import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.JMenu;
import javax.swing.JMenuItem;

import bn.blaszczyk.blstatistics.core.*;

public class GameFilterPanelMenu implements PanelMenu<Season,Game> {
	
	private BiFilterPanel<Season,Game> panel;
	private List<String> teams;
	
	public GameFilterPanelMenu(List<String> teams)
	{
		this.teams = teams;
	}
	
	@Override
	public void addMenuItems(JMenu menu, ActionListener listener)
	{
		
		/*
		 * Team Filters
		 */
		JMenu gameFilters = new JMenu("Spiel Filter");
		
		addMenuItem(gameFilters,"Team").addActionListener( e -> {
			setPanel( FilterPanelAdapter.getSecondArgAdapter( new TeamFilterPanel(teams) ) );
			listener.actionPerformed(e);
		});
		addMenuItem(gameFilters,"Duell").addActionListener( e -> {
			setPanel( FilterPanelAdapter.getSecondArgAdapter( new DuelFilterPanel(teams) ) );
			listener.actionPerformed(e);
		});
		addMenuItem(gameFilters,"Direkter Vergleich").addActionListener( e -> {
			setPanel( FilterPanelAdapter.getSecondArgAdapter( new SubLeagueFilterPanel(teams) ) );
			listener.actionPerformed(e);
		});
		addMenuItem(gameFilters,"Spieltag").addActionListener( e -> {
			setPanel(FilterPanelAdapter.getSecondArgAdapter( new MatchDayFilterPanel()));
			listener.actionPerformed(e);
		});
		
		/*
		 * Season Filters
		 */
		JMenu seasonFilters = new JMenu("Saison Filter");
		
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
		
		addMenuItem(logicalFilters,"TRUE/FALSE").addActionListener( e -> {
			setPanel( new AbsoluteOperatorFilterPanel<>(true) );
			listener.actionPerformed(e);
		});
		addMenuItem(logicalFilters,"NOT/ID").addActionListener( e -> {
			setPanel( new UnaryOperatorFilterPanel<>(this) );
			listener.actionPerformed(e);
		});
		addMenuItem(logicalFilters,"AND/OR/XOR").addActionListener( e -> {
			setPanel( new MultiOperatorFilterPanel<>(this));
			listener.actionPerformed(e);
		});
		addMenuItem(logicalFilters,"IF_THEN_ELSE").addActionListener( e -> {
			setPanel( new IfThenElseFilterPanel<>(this));
			listener.actionPerformed(e);
		});

		menu.add(gameFilters);
		menu.add(seasonFilters);
		menu.add(logicalFilters);
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
}
