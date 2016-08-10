package bn.blaszczyk.blstatistics.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import bn.blaszczyk.blstatistics.core.*;
import bn.blaszczyk.blstatistics.filters.BiFilter;
import bn.blaszczyk.blstatistics.gui.filters.BiFilterEvent;
import bn.blaszczyk.blstatistics.gui.filters.BiFilterListener;
import bn.blaszczyk.blstatistics.gui.filters.BiFilterPanel;
import bn.blaszczyk.blstatistics.tools.FilterIO;
import bn.blaszczyk.blstatistics.tools.FilterLog;
import bn.blaszczyk.blstatistics.tools.NewFilterMenu;

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
		filterPanel = NewFilterMenu.createNoFilterPanel();
		NewFilterMenu.populatePopupMenu(filterPanel);

		setFilterPanel(FilterIO.loadFilter(LAST_FILTER));
		filterLog.pushFilter(filterPanel, filterPanel);	
	}

	public void newFilter()
	{
		setFilterPanel(NewFilterMenu.createNoFilterPanel());
		filterLog.pushFilter(filterPanel, filterPanel);	
	}

	public void loadFilter()
	{
		setFilterPanel(FilterIO.loadFilter());
		filterLog.pushFilter(filterPanel, filterPanel);	
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
		System.out.println("Listening to " + panel);
		panel.addFilterListener(this);
		panel.getPanel().setPreferredSize(new Dimension(300,1000));
		paint();
		if(listener != null)
			listener.filter(new BiFilterEvent<Season,Game>(panel,panel,BiFilterEvent.RESET_FILTER));
	}
	
	@Override
	public void filter(BiFilterEvent<Season, Game> e)
	{
		if(e.getType() == BiFilterEvent.RESET_PANEL && e.getSource().equals(filterPanel))
			setFilterPanel(e.getPanel());
		else
			filterPanel.paint();
		if(e.getFilter() instanceof BiFilterPanel)
			filterLog.pushFilterName((BiFilterPanel<Season,Game>)e.getFilter(), filterPanel);
		else
			filterLog.pushFilter(e.getSource(), filterPanel);
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
	
	public void addFilterListener(BiFilterListener<Season, Game> listener)
	{
		this.listener = listener;
	}
	
	public void removeFilterListener(BiFilterListener<Season, Game> listener)
	{
		if(this.listener == listener)
			this.listener = null;
	}
	
	@Override
	public String toString()
	{
		return "FunctionalFilterPanel";
	}

}
