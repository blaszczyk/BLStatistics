package bn.blaszczyk.fussballstats.tools;

import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JMenu;
import javax.swing.JMenuItem;

import bn.blaszczyk.fussballstats.core.Game;
import bn.blaszczyk.fussballstats.core.Season;
import bn.blaszczyk.fussballstats.gui.corefilters.TeamFilterPanel;
import bn.blaszczyk.fussballstats.gui.corefilters.TeamSearchFilterPanel;
import bn.blaszczyk.fussballstats.gui.filters.*;

public class FilterLog implements BiFilterListener<Season, Game>
{

	private List<String> logUndoLabel = new ArrayList<>();
	private List<String> logRedoLabel = new ArrayList<>();
	private List<String> logFilter = new ArrayList<>();
	
	private BiFilterPanel<Season,Game> lastSource;
	private BiFilterPanel<Season, Game> currentFilter;
	
	private ActionListener listener;
	
	private int selectedFilterIndex;
	private int maxLog;
	private boolean skipNext = false;

	public FilterLog( int maxLog, ActionListener listener)
	{
		this.maxLog = maxLog;
		this.listener = listener;
	}


	public void populateUndoMenu(JMenu menuUndo)
	{
		menuUndo.removeAll();
		menuUndo.setEnabled( selectedFilterIndex > 0  );
		for(int i = selectedFilterIndex - 1  ; i >= 0; i--)
		{
			JMenuItem mi = new JMenuItem( logUndoLabel.get(i + 1) );
			mi.addActionListener(createIndexedListener(i));
			menuUndo.add(mi);
		}
	}

	public void populateRedoMenu(JMenu menuRedo)
	{
		menuRedo.removeAll();
		menuRedo.setEnabled( selectedFilterIndex + 1< logFilter.size() );
		for(int i = selectedFilterIndex + 1; i < logFilter.size(); i++)
		{
			JMenuItem mi = new JMenuItem( logRedoLabel.get(i).toString() );
			mi.addActionListener( createIndexedListener(i) );
			menuRedo.add(mi);
		}
	}
	
	public BiFilterPanel<Season,Game> getFilterPanel()
	{
		return currentFilter;
	}
	
	private void addLogItem(String text)
	{
		addLogItem(text, text);
	}
	
	
	
	
	private void addLogItem(String undoLabel, String redoLabel)
	{
		chopLog(selectedFilterIndex+1);
		if(logFilter.size() > maxLog)
		{
			logFilter.remove(0);
			logUndoLabel.remove(0);
			logRedoLabel.remove(0);
		}
		logUndoLabel.add(undoLabel);
		logRedoLabel.add(redoLabel);
		logFilter.add(FilterParser.writeFilter(currentFilter));
		selectedFilterIndex = logFilter.size() - 1;

		System.out.println(undoLabel);
		System.out.println(redoLabel);
		System.out.println(logFilter.size());
		System.out.println(logUndoLabel);
		System.out.println(logRedoLabel);
		System.out.println();
	}

	
	private void setLastUndoLabel(String undoLabel)
	{
		logUndoLabel.set(selectedFilterIndex,undoLabel);		
	}
		
	private ActionListener createIndexedListener(int index)
	{
		final int panelIndex = index;
		return e -> {
			currentFilter = FilterParser.parseFilter( logFilter.get(panelIndex) );
			TeamFilterPanel.setTeamList( TeamSearchFilterPanel.getTeamList());
			selectedFilterIndex = panelIndex;
			skipNext = true;
			listener.actionPerformed(e);
		};
	}
	
	private void chopLog(int chopIndex)
	{
		while(logFilter.size() > chopIndex)
		{
			System.out.println("CHOP: " + logFilter.size() + " to " + chopIndex);
			logFilter.remove(chopIndex);
			logUndoLabel.remove(chopIndex);
			logRedoLabel.remove(chopIndex);
		}
	}


	public void setLastFilter(BiFilterPanel<Season, Game> currentFilter)
	{
		this.currentFilter = currentFilter;
	}


	@Override
	public void filter(BiFilterEvent<Season, Game> e)
	{
		if( "K".toLowerCase().equals("k"))
			return;
		BiFilterPanel<Season, Game> source = e.getSource();
		if(lastSource != null && lastSource.equals(source))
		{
//			chopLog(selectedFilterIndex);
		}
		
		/*
		 * 	 F0
		 * |U0 |R0
		 * 	 F1
		 * |U1 |R1
		 * 	 F2
		 * 
		 * 
		 */
		
		if(skipNext)
		{
			System.out.println("SKIP");
			skipNext = false;
			return;
		}
		if(e.getType() == BiFilterEvent.SET_PANEL)
		{
			skipNext = true;
			BiFilterPanel<Season, Game> newPanel = e.getNewPanel();
			if(source == null || source instanceof NoFilterPanel)
			{
				addLogItem("Neuer Filter: " + newPanel);
				lastSource = newPanel;
			}
			else if(newPanel == null || newPanel instanceof NoFilterPanel)
			{
				addLogItem("Entferne Filter: " + source);
				lastSource = source;
			}
			else
			{
				addLogItem("Ersetzte Filter: " + source);
				lastSource = newPanel;
			}
		}
		else if (e.getType() == BiFilterEvent.SET_FILTER)
		{
			if( lastSource == null || !lastSource.equals(source))
			{
				setLastUndoLabel("Ändere Filter: " + e.getOldSourceName());
				lastSource = source;
			}
			else
			{
				System.out.println();
				System.out.println("Same Source");
				System.out.println(logFilter.size());
				chopLog(selectedFilterIndex);
				System.out.println(logFilter.size());
				System.out.println();
			}
			addLogItem("Ändere Filter: " + e.getOldSourceName());
		}
	}

}
