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
	
	private BiFilterPanel<Season,Game> filterPanel;
	private JLabel header = new JLabel("Filter", SwingConstants.CENTER);
	
	private BiFilterListener<Season,Game> listener = null;
	
	private FilterLog filterLog;
	
	public FunctionalFilterPanel()
	{
		super(new BorderLayout(5,5));
		setPreferredSize(new Dimension(300,700));		
		
		header.setPreferredSize(new Dimension(355, 50));
		header.setFont(new Font("Arial", Font.BOLD, 28));
		
		filterLog = new FilterLog( 10, e -> setFilterPanel(filterLog.getFilterPanel()) );
		filterPanel = FilterMenuFactory.createNoFilterPanel();
		FilterMenuFactory.createPopupMenu(filterPanel);

		setFilterPanel(FilterIO.loadFilter(LAST_FILTER));
		notifyLog(new BiFilterEvent<Season, Game>(null,filterPanel));
	}

	public void newFilter()
	{
		BiFilterPanel<Season, Game> newFilter = FilterMenuFactory.createNoFilterPanel();
		notifyLog(new BiFilterEvent<Season, Game>(filterPanel,newFilter));
		setFilterPanel(newFilter);
	}

	public void loadFilter()
	{
		BiFilterPanel<Season, Game> newFilter = FilterIO.loadFilter();
		notifyLog(new BiFilterEvent<Season, Game>(filterPanel,newFilter));
		setFilterPanel(newFilter);
	}
	
	public void saveFilter()
	{
		FilterIO.saveFilter(filterPanel);
	}
	

	public void saveLastFilter()
	{
		FilterIO.saveFilter(filterPanel,LAST_FILTER);
	}


	public FilterLog getFilterLog()
	{
		return filterLog;
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
			listener.filter(new BiFilterEvent<Season,Game>(panel,panel));
	}
	
	private void notifyLog(BiFilterEvent<Season, Game> e)
	{
		filterLog.setLastFilter(filterPanel);
		filterLog.filter(e);
	}
	
	@Override
	public void filter(BiFilterEvent<Season, Game> e)
	{
		notifyLog(e);
		if(e.getType() == BiFilterEvent.SET_PANEL && filterPanel.equals(e.getSource()))
			setFilterPanel(e.getNewPanel());
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
	
	public void paint()
	{
		filterPanel.paint();
		removeAll();
		add(header, BorderLayout.NORTH);
		add(filterPanel.getPanel(),BorderLayout.CENTER);
		revalidate();
	}
	
	public void setFilterListener(BiFilterListener<Season, Game> listener)
	{
		this.listener = listener;
	}
	
	@Override
	public String toString()
	{
		return "FunctionalFilterPanel";
	}

}
