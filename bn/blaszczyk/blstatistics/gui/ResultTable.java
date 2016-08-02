package bn.blaszczyk.blstatistics.gui;

import java.awt.event.MouseEvent;
import java.util.*;

import javax.swing.table.TableModel;

import bn.blaszczyk.blstatistics.core.TeamResult;

@SuppressWarnings("serial")
public class ResultTable extends SwingTable<TeamResult>
{
	private boolean isRelativeTable = false;
	
	public ResultTable(Iterable<TeamResult> source)
	{
		super(source);
	}
	
	public ResultTable()
	{
		super();
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
	protected TableModel createTableModel(List<TeamResult> ts)
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
		if( columnIndex == 0)
			return 50;
		if( columnIndex < 2)
			return 200;
		if( columnIndex < 5)
			return 100;
		if( columnIndex < 8)
			return 75;
		return 75;
	}

}
