package bn.blaszczyk.fussballstats.gui;

import java.util.*;

import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;

import bn.blaszczyk.fussballstats.core.TeamResult;
import bn.blaszczyk.fussballstats.gui.tools.MyTable;
import bn.blaszczyk.fussballstats.gui.tools.MyTableModel;

@SuppressWarnings("serial")
public class ResultTable extends MyTable<TeamResult>
{
	private boolean isRelativeTable = false;
	List<String> selectedTeams = new ArrayList<>();
	
	public ResultTable()
	{
		setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		getSelectionModel().addListSelectionListener( e -> repaint());
	}

	public List<String> getSelectedTeams()
	{
		if( getSelectedRows().length != 0 )
		{
			selectedTeams = new ArrayList<>();
			for(int row : getSelectedRows())
				selectedTeams.add(getModel().getValueAt( row  , 1).toString());
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

	
	@Override
	protected Comparator<TeamResult> comparator(int columnIndex)
	{	
		if(getModel() instanceof RelativeResultTableModel)
			return RelativeResultTableModel.comparator(columnIndex);
		return ResultTableModel.comparator(columnIndex);
	}

	@Override
	protected MyTableModel<TeamResult> createTableModel(List<TeamResult> ts)
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
		return getSelectedTeams().contains(getModel().getValueAt(rowIndex, 1));
	}

}
