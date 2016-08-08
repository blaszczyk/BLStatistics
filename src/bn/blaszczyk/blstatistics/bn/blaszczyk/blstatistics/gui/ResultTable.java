package bn.blaszczyk.blstatistics.gui;

import java.awt.event.MouseEvent;
import java.util.*;

import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;

import bn.blaszczyk.blstatistics.core.TeamResult;

@SuppressWarnings("serial")
public class ResultTable extends MyTable<TeamResult>
{
	private boolean isRelativeTable = false;
	List<String> selectedTeams = new ArrayList<>();
	
	public ResultTable()
	{
		super();
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
	protected void doPopup(MouseEvent e)
	{
	}

	@Override
	protected int columnWidth(int columnIndex)
	{
		if( columnIndex == 0)	//Position
			return 50;
		if( columnIndex == 1)	//Team
			return 250;
		if( columnIndex < 5)	//Games,Points,Diff
			return 100;
		if( columnIndex < 8)	// S U N
			return 80;
		if( columnIndex == 9)	// " : "
			return 20;
		return 70;				//Goals
	}
	
	@Override
	protected int columnAlignment(int columnIndex)
	{
		if(columnIndex == 9 || (columnIndex > 4 && columnIndex< 8 ) )	// " : "
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
