package bn.blaszczyk.blstatistics.gui;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import javax.swing.JTable;
import javax.swing.table.TableModel;

import bn.blaszczyk.blstatistics.filters.Filter;
import bn.blaszczyk.blstatistics.filters.FilterListener;
import bn.blaszczyk.blstatistics.filters.LogicalFilter;

@SuppressWarnings("serial")
public abstract class SwingTable<T> extends JTable implements FilterListener<T>, MouseListener
{
	private List<T> tList;
	private Filter<T> filter;
	private Iterable<T> source;
	private Comparator<T> comparator;
//	private boolean compareBackwards = false;

	public SwingTable(Iterable<T> source)
	{
		this(source,LogicalFilter.getTRUEFilter());
	}
	
	public SwingTable(Iterable<T> source, Filter<T> filter)
	{
		this.filter = filter;
		setSource(source);
		for(int i = 0 ; i < this.getColumnCount(); i++)
		{
			int width = columnWidth(i);
			if( width >= 0 )
				getColumnModel().getColumn(i).setPreferredWidth(width);
		}
		getTableHeader().addMouseListener(this);
	}
	
	public void setSource(Iterable<T> source)
	{
		this.source = source;
		resetList();
	}
	
	public void setFilter(Filter<T> filter)
	{
		this.filter = filter;
		resetList();
	}
	
	public void setComparator(Comparator<T> comparator)
	{
		this.comparator = comparator;
		resetModel();
	}
	
	
	private void resetList()
	{
		tList = new ArrayList<>();
		for(T t : source)
			if(filter.check(t))
				tList.add(t);	
		resetModel();
	}
	
	private void resetModel()
	{
		if(comparator != null)
			tList.sort(comparator);
		setModel(tableModel(tList));
	}
		
	
	protected abstract Comparator<T> comparator(int columnIndex);
	protected abstract TableModel tableModel(List<T> tList);
	protected abstract void doPopup(MouseEvent e);
	protected abstract int columnWidth(int columnIndex);
	
	/*
	 * Filter Listener Methods
	 */
	@Override
	public void filter(Filter<T> filter)
	{
		setFilter(filter);
	}
	
	
	/*
	 * Mouse Listener Methods
	 */
	@Override
	public void mouseClicked(MouseEvent e) {
		if( e.getComponent() == getTableHeader() )
		{
			int columnIndex = columnAtPoint(e.getPoint());
			comparator = comparator(columnIndex);
			resetModel();
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
	
}
