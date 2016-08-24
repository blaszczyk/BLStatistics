package bn.blaszczyk.fussballstats.gui;

import java.util.ArrayList;
import java.util.List;

import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.table.TableModel;

import bn.blaszczyk.fussballstats.core.Game;
import bn.blaszczyk.fussballstats.gui.tools.MyTable;

@SuppressWarnings("serial")
public class GameTable extends MyTable<Game>
{

	/*
	 * Variables
	 */
	private List<String> selectedTeams;
	
	/*
	 * Constructor
	 */
	public GameTable()
	{
		setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		getSelectionModel().addListSelectionListener( e -> repaint());
	}

	/*
	 * Getters, Setters
	 */
	public List<String> getSelectedTeams()
	{
		int selectedIndex = getSelectionModel().getAnchorSelectionIndex();
		if(selectedIndex < 0)
			return selectedTeams;
		int modelSelectedIndex = sorter.convertRowIndexToModel(selectedIndex);
		selectedTeams = new ArrayList<>();
		selectedTeams.add( (String)getModel().getValueAt(modelSelectedIndex, 1) );
		selectedTeams.add( (String)getModel().getValueAt(modelSelectedIndex, 5) );
		return selectedTeams;
	}
	
	public void setSelectedTeams(List<String> teams)
	{
		this.selectedTeams = teams;
		getSelectionModel().setAnchorSelectionIndex(-1);
		repaint();
	}
	
	/*
	 * MyTable Methods
	 */
	@Override
	protected boolean isThisRowSelected(int rowIndex)
	{
		if(selectedTeams == null)
			return false;
		int modelRowIndex = sorter.convertRowIndexToModel(rowIndex);
		return selectedTeams.contains(getModel().getValueAt(modelRowIndex, 1)) 
				|| selectedTeams.contains(getModel().getValueAt(modelRowIndex, 5));
	}

	@Override
	protected TableModel createTableModel(List<Game> tList)
	{
		return new GameTableModel(tList);
	}

	@Override
	protected int columnWidth(int columnIndex)
	{
		switch(columnIndex)
		{
		case 0:
			return 100;
		case 1:
			return 200;
		case 2:
		case 4:
			return 30;
		case 3:
			return 20;
		default:
			return 230;	
		}
	}

	@Override
	protected int columnAlignment(int columnIndex)
	{
		switch(columnIndex)
		{
		case 0:
		case 3:
			return SwingConstants.CENTER;
		case 1:
		case 2:
			return SwingConstants.RIGHT;
		case 4:
		case 5:
			return SwingConstants.LEFT;
		}
		return 0;
	}

}
