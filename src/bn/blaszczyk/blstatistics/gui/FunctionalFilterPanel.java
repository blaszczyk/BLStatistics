package bn.blaszczyk.blstatistics.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.util.List;

import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import bn.blaszczyk.blstatistics.core.*;
import bn.blaszczyk.blstatistics.gui.corefilters.GameFilterPanelManager;
import bn.blaszczyk.blstatistics.gui.filters.BiFilterEvent;
import bn.blaszczyk.blstatistics.gui.filters.BiFilterListener;
import bn.blaszczyk.blstatistics.gui.filters.BiFilterPanel;
import bn.blaszczyk.blstatistics.gui.filters.NoFilterPanel;
import bn.blaszczyk.blstatistics.tools.FilterIO;
import bn.blaszczyk.blstatistics.tools.FilterLog;
import bn.blaszczyk.blstatistics.tools.FilterParser;

@SuppressWarnings("serial")
public class FunctionalFilterPanel extends JPanel implements BiFilterListener<Season,Game>, BiFilterPanel<Season,Game>
{
	
	public static final String LAST_FILTER = "last";
	
	private BiFilterPanel<Season,Game> filterPanel;
	private JLabel header = new JLabel("Filter", SwingConstants.CENTER);
	
	private BiFilterListener<Season,Game> listener = null;
	private GameFilterPanelManager filterManager;
	private FilterParser filterParser;
	private FilterIO filterIO = new FilterIO();
	
	private FilterLog filterLog;
	
	public FunctionalFilterPanel(List<String> teams, List<String> leagues)
	{
		super(new BorderLayout(5,5));
		setPreferredSize(new Dimension(300,700));		
		
		header.setPreferredSize(new Dimension(355, 50));
		header.setFont(new Font("Arial", Font.BOLD, 28));
		
		filterManager = new GameFilterPanelManager(teams,leagues,filterIO);
		filterParser = new FilterParser(filterManager);
		filterLog = new FilterLog( filterParser, e -> {
			setFilterPanel(filterLog.getFilterPanel());
		});
		filterIO.setParser(filterParser);
		filterPanel = new NoFilterPanel<>(filterManager);

		setFilterPanel(filterIO.loadFilter(LAST_FILTER));
		filterLog.pushFilter(filterPanel, filterPanel);	
	}


	public void newFilter()
	{
		setFilterPanel(new NoFilterPanel<>(filterManager));
		filterLog.pushFilter(filterPanel, filterPanel);	
	}

	public void loadFilter()
	{
		setFilterPanel(filterIO.loadFilter());
		filterLog.pushFilter(filterPanel, filterPanel);	
	}
	
	public void saveFilter()
	{
		filterIO.saveFilter(filterPanel);
	}
	

	public void saveLastFilter()
	{
		filterIO.saveFilter(filterPanel,LAST_FILTER);
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
			listener.filter(new BiFilterEvent<Season,Game>(this,panel,BiFilterEvent.RESET_FILTER));
	}
	
	@Override
	public void filter(BiFilterEvent<Season, Game> e)
	{
		if(e.getType() == BiFilterEvent.RESET_PANEL && e.getSource().equals(filterPanel))
			setFilterPanel(e.getPanel());
		else
			filterPanel.paint();
		if(e.getFilter() instanceof BiFilterPanel)
			filterLog.pushFilterIgnoreNext((BiFilterPanel<Season,Game>)e.getFilter(), filterPanel);
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
	
	@Override
	public void paint()
	{
		filterPanel.paint();
		removeAll();
		add(header, BorderLayout.NORTH);
		add(filterPanel.getPanel(),BorderLayout.CENTER);
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
