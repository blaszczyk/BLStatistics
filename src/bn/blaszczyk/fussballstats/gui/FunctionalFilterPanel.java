package bn.blaszczyk.fussballstats.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import bn.blaszczyk.fussballstats.core.*;
import bn.blaszczyk.fussballstats.filters.BiFilter;
import bn.blaszczyk.fussballstats.gui.filters.BiFilterEvent;
import bn.blaszczyk.fussballstats.gui.filters.BiFilterListener;
import bn.blaszczyk.fussballstats.gui.filters.BiFilterPanel;
import bn.blaszczyk.fussballstats.tools.FilterIO;
import bn.blaszczyk.fussballstats.tools.FilterLog;
import bn.blaszczyk.fussballstats.tools.FilterMenuFactory;

@SuppressWarnings("serial")
public class FunctionalFilterPanel extends JPanel implements BiFilterListener<Season,Game>, BiFilter<Season,Game>
{
	
	public static final String LAST_FILTER = "last";

	private JLabel header = new JLabel("Filter", SwingConstants.CENTER);
	private BiFilterPanel<Season,Game> filterPanel;
	
	private BiFilterListener<Season,Game> listener = null;
	private FilterLog filterLog;
	
	private static boolean loadLastFilter;
	
	public FunctionalFilterPanel(FilterLog filterLog)
	{
		super(new BorderLayout(5,5));
		setPreferredSize(new Dimension(300,700));		
		
		header.setPreferredSize(new Dimension(355, 50));
		header.setFont(new Font("Arial", Font.BOLD, 28));
		
		this.filterLog = filterLog;
		
		if(loadLastFilter)
			setFilterPanel(FilterIO.loadFilter(LAST_FILTER));
		else
			setFilterPanel(FilterMenuFactory.createNoFilterPanel());
	}

	public void newFilter()
	{
		BiFilterPanel<Season, Game> newFilter = FilterMenuFactory.createNoFilterPanel();
		setFilterPanel(newFilter);
	}

	public void loadFilter()
	{
		BiFilterPanel<Season, Game> newFilter = FilterIO.loadFilter();
		setFilterPanel(newFilter);
	}
	
	public void saveFilter()
	{
		FilterIO.saveFilter(filterPanel);
	}
	

	public void saveLastFilter()
	{
		if(loadLastFilter)
			FilterIO.saveFilter(filterPanel,LAST_FILTER);
	}

	public void setFilterListener(BiFilterListener<Season, Game> listener)
	{
		this.listener = listener;
	}
	
	public static boolean isLoadLastFilter()
	{
		return loadLastFilter;
	}

	public static void setLoadLastFilter(boolean loadLastFilter)
	{
		FunctionalFilterPanel.loadLastFilter = loadLastFilter;
	}
	
	private void paint()
	{
		filterPanel.paint();
		removeAll();
		add(header, BorderLayout.NORTH);
		add(filterPanel.getPanel(),BorderLayout.CENTER);
		revalidate();
	}
	
	public void setFilterPanel(BiFilterPanel<Season,Game> panel)
	{
		if(panel == null)
			return;
		if(this.filterPanel != null)
			this.filterPanel.removeFilterListener(this);
		if(listener != null)
			listener.filter(new BiFilterEvent<Season,Game>(filterPanel,panel, BiFilterEvent.SET_PANEL));
		this.filterPanel = panel;
		panel.addFilterListener(this);
		panel.getPanel().setPreferredSize(new Dimension(300,1000));
		paint();
		notifyLog();
	}
	
	private void notifyLog()
	{
		if(filterLog != null)
			filterLog.logFilter(filterPanel);
	}
	
	@Override
	public void filter(BiFilterEvent<Season, Game> e)
	{
		if(e.getType() == BiFilterEvent.SET_PANEL && filterPanel.equals(e.getSource()))
			setFilterPanel(e.getNewPanel());
		else
		{
			filterPanel.paint();
			notifyLog();
		}
		if(listener != null)
			listener.filter(e);
	}
	
	@Override
	public boolean check(Season s, Game g)
	{
		return filterPanel.check(s, g);
	}
	
	@Override
	public String toString()
	{
		return "FunctionalFilterPanel";
	}

}
