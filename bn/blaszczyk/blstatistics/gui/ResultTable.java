package bn.blaszczyk.blstatistics.gui;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.*;

import javax.swing.JTable;

import bn.blaszczyk.blstatistics.core.TeamResult;
import bn.blaszczyk.blstatistics.filters.Filter;
import bn.blaszczyk.blstatistics.filters.LogicalFilter;

@SuppressWarnings("serial")
public class ResultTable extends JTable implements MouseListener{
	
	private List<TeamResult> results;
	private Filter<TeamResult> filter;
	private Iterable<TeamResult> source;
	private Comparator<TeamResult> comparator = TeamResult.COMPARE_POSITION;
//	private boolean compareBackwards = false;

	public ResultTable(Iterable<TeamResult> source)
	{
		this(source,LogicalFilter.getTRUEFilter());
	}
	
	public ResultTable(Iterable<TeamResult> source, Filter<TeamResult> filter)
	{
		this.filter = filter;
		resetSource(source);
		getTableHeader().addMouseListener(this);
	}
	
	public void resetSource(Iterable<TeamResult> source)
	{
		this.source = source;
		resetList();
	}
	
	public void resetFilter(Filter<TeamResult> filter)
	{
		this.filter = filter;
		resetList();
	}
	
	private void resetList()
	{
		results = new ArrayList<>();
		for(TeamResult result : source)
			if(filter.check(result))
				results.add(result);	
		resetModel();
	}
	
	private void resetModel()
	{
		results.sort(comparator);
		setModel(new ResultTableModel(results));
	}
		
	private void setComparator(int columnIndex)
	{
		comparator = ResultTableModel.getComparator(columnIndex);
		resetModel();
	}
	
	/*
	 * Mouse Listener Methods
	 */
	@Override
	public void mouseClicked(MouseEvent e) {
		if( e.getComponent() == getTableHeader() )
		{
			int columnIndex = columnAtPoint(e.getPoint());
			setComparator(columnIndex);
		}
	}
	
	@Override
	public void mouseEntered(MouseEvent e)
	{
	}

	@Override
	public void mouseExited(MouseEvent e)
	{
	}

	@Override
	public void mousePressed(MouseEvent e)
	{
		if(e.isPopupTrigger())
			doPopup(e);
	}

	@Override
	public void mouseReleased(MouseEvent e)
	{
		if(e.isPopupTrigger())
			doPopup(e);
	}
	private void doPopup(MouseEvent e)
	{
	}
	
}
