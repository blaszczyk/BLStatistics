package bn.blaszczyk.fussballstats.gui;

import java.util.*;

import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.table.TableModel;

import bn.blaszczyk.fussballstats.core.TeamResult;
import bn.blaszczyk.fussballstats.gui.tools.MyTable;

@SuppressWarnings("serial")
public class ResultTable extends MyTable<TeamResult>
{
	/*
	 * Variables
	 */
	private boolean isRelativeTable = false;
	List<String> selectedTeams = new ArrayList<>();
	
	/*
	 * Constructor
	 */
	public ResultTable()
	{
		setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		getSelectionModel().addListSelectionListener( e -> repaint());
	}

	/*
	 * Getters, Setters
	 */
	public List<String> getSelectedTeams()
	{
		if( getSelectedRows().length != 0 )
		{
			selectedTeams.clear();
			for(int row : getSelectedRows())
				selectedTeams.add(getModel().getValueAt( sorter.convertRowIndexToModel(row)  , 1).toString());
		}
		return selectedTeams;
	}
	
	public void setSelectedTeams(List<String> teams)
	{
		this.selectedTeams = teams;
		getSelectionModel().removeSelectionInterval(0, getRowCount());
		repaint();
	}
	
	public void setRelativeTable(boolean isRelativeTable)
	{
		this.isRelativeTable = isRelativeTable;
	}

	/*
	 * MyTable Methods
	 */
	@Override
	protected TableModel createTableModel(List<TeamResult> ts)
	{
		if(isRelativeTable)
			return new RelativeResultTableModel(ts); 
		else
			return new ResultTableModel(ts);
	}

	@Override
	protected int columnWidth(int columnIndex)
	{
		switch(columnIndex)
		{
		case 0:
			return 50;
		case 1:
			return 230;
		case 2:
		case 3:
		case 4:
			return 90;
		case 5:
		case 6:
		case 7:
			return 70;
		case 8:
			return 70;
		case 9:
			return 20;
		default:
			return 100;	
		}
	}
	
	@Override
	protected int columnAlignment(int columnIndex)
	{
		if(columnIndex == 9 )	// " : "
			return SwingConstants.CENTER;
		if(columnIndex == 10)	// OpponentGoals
			return SwingConstants.LEFT;
		return SwingConstants.RIGHT;
	}

	@Override
	protected boolean isThisRowSelected(int rowIndex)
	{
		return getSelectedTeams().contains(getModel().getValueAt(sorter.convertRowIndexToModel(rowIndex), 1));
	}

}
