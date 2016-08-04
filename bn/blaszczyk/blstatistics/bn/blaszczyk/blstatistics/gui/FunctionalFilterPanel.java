package bn.blaszczyk.blstatistics.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.util.List;

import javax.swing.JMenuItem;
import javax.swing.JPanel;

import bn.blaszczyk.blstatistics.core.*;
import bn.blaszczyk.blstatistics.gui.corefilters.GameFilterPanelManager;
import bn.blaszczyk.blstatistics.gui.filters.BiFilterEvent;
import bn.blaszczyk.blstatistics.gui.filters.BiFilterListener;
import bn.blaszczyk.blstatistics.gui.filters.BiFilterPanel;
import bn.blaszczyk.blstatistics.gui.filters.BlankFilterPanel;
import bn.blaszczyk.blstatistics.tools.FilterIO;

@SuppressWarnings("serial")
public class FunctionalFilterPanel extends JPanel implements BiFilterListener<Season,Game>, BiFilterPanel<Season,Game>
{
	
	private BiFilterListener<Season,Game> listener = null;
	private BiFilterPanel<Season,Game> filterPanel;
	private FilterIO filterIO = new FilterIO();
	
	
	public FunctionalFilterPanel(List<String> teams)
	{
		super(new BorderLayout(5,5));
		setPreferredSize(new Dimension(300,700));
		
		GameFilterPanelManager manager = new GameFilterPanelManager(teams,filterIO);
		
		filterPanel = new BlankFilterPanel<>(manager);
		filterPanel.addFilterListener(this);		
		filterIO.setManager(manager);
		paint();
	}


	public void loadFilter()
	{
		setFilterPanel(filterIO.loadFilter());
	}
	
	public void saveFilter()
	{
		filterIO.saveFilter(filterPanel);
	}

	private void setFilterPanel(BiFilterPanel<Season,Game> panel)
	{
		if(panel == null)
			return;
		if(this.filterPanel != null)
			this.filterPanel.removeFilterListener(this);
		this.filterPanel = panel;
		panel.addFilterListener(this);
		panel.getPanel().setPreferredSize(new Dimension(300,1000));
		paint();
		if(listener != null)
			listener.filter(new BiFilterEvent<Season,Game>(this,panel,BiFilterEvent.RESET_FILTER));
	}
	
	@Override
	public void filter(BiFilterEvent<Season, Game> e)
	{
		if(e.getType() == BiFilterEvent.RESET_PANEL && e.getSource().equals(filterPanel))
			setFilterPanel(e.getPanel());
		else
			filterPanel.paint();
		if(listener != null)
			listener.filter(e);
	}
	
	@Override
	public boolean check(Season s, Game g)
	{
		return filterPanel.check(s, g);
	}
	
	@Override
	public void paint()
	{
		filterPanel.paint();
		removeAll();
		add(filterPanel.getPanel());
		revalidate();
	}
	
	@Override
	public JPanel getPanel()
	{
		return this;
	}
	
	@Override
	public void addFilterListener(BiFilterListener<Season, Game> listener)
	{
		this.listener = listener;
	}
	
	@Override
	public void removeFilterListener(BiFilterListener<Season, Game> listener)
	{
		if(this.listener == listener)
			this.listener = null;
	}
	
	@Override
	public void addPopupMenuItem(JMenuItem item)
	{
	}
	
	@Override
	public void removePopupMenuItem(JMenuItem item)
	{
	}


}
