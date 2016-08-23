package bn.blaszczyk.fussballstats.gui.filters;


import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.*;
import javax.swing.border.BevelBorder;
import javax.swing.border.Border;

import bn.blaszczyk.fussballstats.filters.Filter;
import bn.blaszczyk.fussballstats.filters.LogicalFilter;

@SuppressWarnings("serial")
public abstract class AbstractFilterPanel<T> extends JPanel implements FilterPanel<T>
{
	public static final Border ACTIVE_BORDER = new BevelBorder(BevelBorder.LOWERED);
	public static final Border INACTIVE_BORDER = new BevelBorder(BevelBorder.RAISED);
	
	private Filter<T> filter = LogicalFilter.getTRUEFilter();
	private List<FilterListener<T>> listeners = new ArrayList<>();
	
	private boolean active = true;
	private boolean isPainted = false;
	private final boolean varComponents;

	protected ActionListener setFilterListener = e ->
	{
		setFilter();
		if(e.getSource() instanceof JComponent)
			((JComponent)e.getSource()).requestFocusInWindow();
	};
	
	/*
	 * Constructor
	 */
	public AbstractFilterPanel(boolean varComponents)
	{
		this.varComponents = varComponents;
		setActive(true);
	}

	protected abstract void addComponents();
	protected abstract void setFilter();
	
	protected void setFilter(Filter<T> filter)
	{
		this.filter = filter;
		if(isActive())
			notifyListeners(new FilterEvent<>(this,filter,FilterEvent.SET_FILTER));
	}
	
	@Override
	public void setActive(boolean active)
	{
		if(active)
		{
			setBorder(ACTIVE_BORDER);
			this.active = true;
		}
		else
		{
			setBorder(INACTIVE_BORDER);
			this.active = false;
		}
		notifyListeners(new FilterEvent<T>(this,filter,FilterEvent.SET_ACTIVE));
	}
	
	@Override
	public boolean isActive()
	{
		return active;
	}

	private void notifyListeners(FilterEvent<T> e)
	{
		for(FilterListener<T> listener : listeners)
			listener.filter(e);
	}	

	/*
	 * Filter Methods
	 */
	
	@Override
	public boolean check(T t)
	{
		return !active || filter.check(t);
	}
	
	/*
	 * FilterPanel Methods
	 */

	@Override 
	public JPanel getPanel()
	{
		return this;
	}
	
	@Override
	public void paint()
	{
		if(varComponents || !isPainted)
		{
			removeAll();
			addComponents();
			revalidate();
		}
		isPainted = true;
	}
	
	@Override
	public void addFilterListener(FilterListener<T> listener)
	{
		listeners.add(listener);
	}

	@Override
	public void removeFilterListener(FilterListener<T> listener)
	{
		int i = listeners.indexOf(listener);
		if( i >= 0 )
			listeners.remove(i);
	}
	
}
