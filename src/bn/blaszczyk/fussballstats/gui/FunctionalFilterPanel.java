package bn.blaszczyk.fussballstats.gui;

import java.awt.Dimension;
import java.awt.Font;

import javax.swing.Box;
import javax.swing.BoxLayout;
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
	/*
	 * Constatns
	 */
	public static final String LAST_FILTER = "last";

	/*
	 * Global Variables
	 */
	private static boolean loadLastFilter;
	/*
	 * Components
	 */
	private JLabel header = new JLabel("Filter", SwingConstants.CENTER);
	private BiFilterPanel<Season,Game> filterPanel;
	
	/*
	 * Variables
	 */
	private BiFilterListener<Season,Game> listener = null;
	private FilterLog filterLog;
	
	/*
	 * Constructor
	 */
	public FunctionalFilterPanel(FilterLog filterLog)
	{
		setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
		setPreferredSize(new Dimension(300,700));		

		header.setMinimumSize(new Dimension(355, 50));
		header.setMaximumSize(new Dimension(355, 50));
		header.setFont(new Font("Arial", Font.BOLD, 28));
		
		this.filterLog = filterLog;
		
		if(loadLastFilter)
			setFilterPanel(FilterIO.loadFilter(LAST_FILTER));
		else
			setFilterPanel(FilterMenuFactory.createNoFilterPanel());
	}


	/*
	 * Global Getter, Setter
	 */
	public static boolean isLoadLastFilter()
	{
		return loadLastFilter;
	}

	public static void setLoadLastFilter(boolean loadLastFilter)
	{
		FunctionalFilterPanel.loadLastFilter = loadLastFilter;
	}
	
	/*
	 * Filter Control Methods
	 */
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

	/*
	 * Setters
	 */
	public void setFilterListener(BiFilterListener<Season, Game> listener)
	{
		this.listener = listener;
	}
	
	public void setFilterPanel(BiFilterPanel<Season,Game> filterPanel)
	{
		if(filterPanel == null)
			return;
		BiFilterPanel<Season, Game> oldFilter = this.filterPanel;
		this.filterPanel = filterPanel;
		if(oldFilter != null)
			oldFilter.removeFilterListener(this);
		filterPanel.addFilterListener(this);
		filterPanel.getPanel().setAlignmentX(LEFT_ALIGNMENT);
		paint();
		notifyLog();
		if(listener != null)
			listener.filter(new BiFilterEvent<Season,Game>(this.filterPanel,filterPanel, BiFilterEvent.SET_PANEL));
	}

	/*
	 * Internal Methods
	 */
	private void paint()
	{
		filterPanel.paint();
		removeAll();
		add(header);
		add(Box.createVerticalStrut(30));
		add(filterPanel.getPanel());
		revalidate();
	}
	
	private void notifyLog()
	{
		if(filterLog != null)
			filterLog.logFilter(filterPanel);
	}
	
	/*
	 * BiFilterListener Methods
	 */
	@Override
	public void filter(BiFilterEvent<Season, Game> e)
	{
		if(e.getType() == BiFilterEvent.SET_PANEL && filterPanel.equals(e.getSource()))
		{
			setFilterPanel(e.getNewPanel());
			return;
		}
		filterPanel.paint();
		notifyLog();
		if(listener != null)
			listener.filter(e);
	}
	
	/*
	 * BiFilter Methods
	 */
	@Override
	public boolean check(Season s, Game g)
	{
		return filterPanel.check(s, g);
	}

}
