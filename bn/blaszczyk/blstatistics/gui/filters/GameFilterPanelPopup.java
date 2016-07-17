package bn.blaszczyk.blstatistics.gui.filters;

import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.JMenu;
import javax.swing.JMenuItem;

import bn.blaszczyk.blstatistics.core.*;

public class GameFilterPanelPopup implements PanelMenu<Season,Game> {
	
	private BiFilterPanel<Season,Game> panel;
	private List<String> teams;
	
	public GameFilterPanelPopup(List<String> teams)
	{
		this.teams = teams;
	}
	
	@Override
	public void addMenuItems(JMenu menu, ActionListener listener)
	{
		/*
		 * Team Filters
		 */
		addMenuItem(menu,"Team").addActionListener( e -> {
			setPanel( FilterPanelAdapter.getSecondArgAdapter( new TeamFilterPanel(teams) ) );
			listener.actionPerformed(e);
		});
		addMenuItem(menu,"Duell").addActionListener( e -> {
			setPanel( FilterPanelAdapter.getSecondArgAdapter( new DuelFilterPanel(teams) ) );
			listener.actionPerformed(e);
		});
		addMenuItem(menu,"Direkter Vergleich").addActionListener( e -> {
			setPanel( FilterPanelAdapter.getSecondArgAdapter( new SubLeagueFilterPanel(teams) ) );
			listener.actionPerformed(e);
		});
		/*
		 * Logical Filters
		 */

		addMenuItem(menu,"TRUE/FALSE").addActionListener( e -> {
			setPanel( new AbsoluteOperatorFilterPanel<>(true) );
			listener.actionPerformed(e);
		});
		addMenuItem(menu,"NOT/ID").addActionListener( e -> {
			setPanel( new UnaryOperatorFilterPanel<>(this) );
			listener.actionPerformed(e);
		});
		addMenuItem(menu,"AND/OR/XOR").addActionListener( e -> {
			setPanel( new MultiOperatorFilterPanel<>(this));
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
}
