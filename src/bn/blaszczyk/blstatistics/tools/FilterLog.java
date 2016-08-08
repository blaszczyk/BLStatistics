package bn.blaszczyk.blstatistics.tools;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JMenu;
import javax.swing.JMenuItem;

import bn.blaszczyk.blstatistics.core.Game;
import bn.blaszczyk.blstatistics.core.Season;
import bn.blaszczyk.blstatistics.gui.filters.BiFilterPanel;

public class FilterLog
{
	private class MyListener implements ActionListener
	{
		int panelIndex;
		private MyListener(int panelIndex)
		{
			this.panelIndex = panelIndex;
		}

		@Override
		public void actionPerformed(ActionEvent e)
		{
			panel = parser.parseFilter( filterLog.get(panelIndex) );
			selectedFilterIndex = panelIndex;
			listener.actionPerformed(e);
		}
	}

	private List<String> nameLog = new ArrayList<>();
	private List<String> filterLog = new ArrayList<>();
	
	private BiFilterPanel<Season,Game> lastSource;
	
	private BiFilterPanel<Season, Game> panel;
	
	private FilterParser parser;
	private ActionListener listener;
	
	private int selectedFilterIndex;

	public FilterLog(FilterParser parser, ActionListener listener)
	{
		this.parser = parser;
		this.listener = listener;
	}


	public void populateBackwardsMenu(JMenu menuBackwards)
	{
		clearMenu(menuBackwards);
		if(selectedFilterIndex > 0)
			menuBackwards.addActionListener(new MyListener(selectedFilterIndex-1));
		else
			menuBackwards.setEnabled(false);
		for(int i = selectedFilterIndex - 1; i >= 0; i--)
		{
			ActionListener listener = new MyListener(i);
			JMenuItem mi = new JMenuItem( nameLog.get(i+1) );
			mi.addActionListener( listener );
			menuBackwards.add(mi);
		}
	}
	

	public void populateForwardsMenu(JMenu menuForewards)
	{
		clearMenu(menuForewards);
		if(selectedFilterIndex < filterLog.size() - 1 )
			menuForewards.addActionListener(new MyListener(selectedFilterIndex+1));
		else
			menuForewards.setEnabled(false);
		for(int i = selectedFilterIndex + 1; i < filterLog.size(); i++)
		{
			ActionListener listener = new MyListener(i);
			JMenuItem mi = new JMenuItem( nameLog.get(i).toString() );
			mi.addActionListener( listener );
			menuForewards.add(mi);
		}
	}
	
	public BiFilterPanel<Season,Game> getFilterPanel()
	{
		return panel;
	}
	
	public void pushFilter(BiFilterPanel<Season, Game> source, BiFilterPanel<Season, Game> fullFilter )
	{
//		chopLog(selectedFilterIndex);
//		if(source == lastSource)
//			chopLog(selectedFilterIndex-1);
		lastSource = source;
		nameLog.add(source.toString());
		filterLog.add(parser.writeFilter(fullFilter));
		selectedFilterIndex = filterLog.size() - 1;
	}

	private void chopLog(int chopIndex)
	{
		if(chopIndex < 0)
			return;
		for(int i = chopIndex + 1; i < filterLog.size(); i++)
		{
			filterLog.remove(chopIndex);
			nameLog.remove(chopIndex);
		}
	}
	
	private void clearMenu(JMenu menu)
	{
		menu.removeAll();
		ActionListener[] as = menu.getActionListeners();
		for(ActionListener a : as )
			menu.removeActionListener(a);
		menu.setEnabled(true);
	}

}
