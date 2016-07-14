package bn.blaszczyk.blstatistics.gui;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.*;

import javax.swing.JTable;

import bn.blaszczyk.blstatistics.core.TeamResult;
import bn.blaszczyk.blstatistics.filters.Filter;
import bn.blaszczyk.blstatistics.filters.LogicalFilterFactory;

@SuppressWarnings("serial")
public class ResultTable extends JTable implements MouseListener{
	
	private List<TeamResult> results;
	private Filter<TeamResult> filter;
	private Iterable<TeamResult> source;
	private Comparator<TeamResult> comparator = TeamResult.COMPARE_LEAGUE;
//	private boolean compareBackwards = false;

	public ResultTable(Iterable<TeamResult> source)
	{
		this(source,LogicalFilterFactory.getTRUEFilter());
	}
	
	public ResultTable(Iterable<TeamResult> source, Filter<TeamResult> filter)
	{
		getTableHeader().addMouseListener(this);
		this.filter = filter;
		resetSource(source);
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
		Iterator<TeamResult> iterator = source.iterator();
		while(iterator.hasNext())
		{
			TeamResult result = iterator.next();
			if(filter.check(result))
				results.add(result);
		}		
		resetModel();
	}
	
	private void resetModel()
	{
		results.sort(comparator);
		setModel(new ResultTableModel(results));
	}
	
	
	
	private void setComparator(int columnIndex)
	{
		switch(columnIndex)
		{
		case 0:
			comparator = TeamResult.COMPARE_LEAGUE;
			break;
		case 1:
			comparator = TeamResult.COMPARE_TEAM;
			break;
		case 2:
			comparator = TeamResult.COMPARE_GAMES;
			break;
		case 3:
			comparator = TeamResult.COMPARE_POINTS;
			break;
		case 4:
			comparator = TeamResult.COMPARE_DIFF;
			break;
		case 5:
			comparator = TeamResult.COMPARE_WINS;
			break;
		case 6:
			comparator = TeamResult.COMPARE_DRAWS;
			break;
		case 7:
			comparator = TeamResult.COMPARE_LOSSOS;
			break;
		case 8:
			comparator = TeamResult.COMPARE_GOALS_TEAM;
			break;
		case 9:
			comparator = TeamResult.COMPARE_GOALS_OPPONENT;
			break;
		}
		resetModel();
	}
	
	/*
	 * Mouse Listener Methods
	 */
	@Override
	public void mouseClicked(MouseEvent e) {
		if( e.getComponent() == getTableHeader() )
		{
			int column = columnAtPoint(e.getPoint());
			setComparator(column);
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
