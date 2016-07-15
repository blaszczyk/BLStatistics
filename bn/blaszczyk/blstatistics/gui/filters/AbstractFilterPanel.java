package bn.blaszczyk.blstatistics.gui.filters;


import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.border.Border;

import bn.blaszczyk.blstatistics.filters.Filter;
import bn.blaszczyk.blstatistics.filters.FilterListener;
import bn.blaszczyk.blstatistics.filters.LogicalFilter;

@SuppressWarnings("serial")
public abstract class AbstractFilterPanel<T> extends JPanel implements Filter<T>
{
	private static final Border activeBorder = BorderFactory.createLoweredBevelBorder();
	private static final Border deactiveBorder = BorderFactory.createRaisedBevelBorder();
	
	
	private boolean isActive = true;
	private JMenuItem setActive;
	
	private JPopupMenu popup;
	
	private Filter<T> filter;
	private List<FilterListener<T>> listeners = new ArrayList<>();
	
	/*
	 * Constructors
	 */
	public AbstractFilterPanel()
	{
		this( LogicalFilter.getTRUEFilter());
	}
	
	public AbstractFilterPanel( Filter<T> filter)
	{
		onload();
		createPopupMenu();
		setActive(true);
		paint();
	}

	
	protected void setFilter(Filter<T> filter)
	{
		this.filter = filter;
	}
	
	private void setActive(boolean active)
	{
		if(active)
		{
			setBorder(activeBorder);
			this.isActive = true;
			setActive.setText("deaktivieren");
		}
		else
		{
			setBorder(deactiveBorder);
			this.isActive = false;
			setActive.setText("aktivieren");
		}
		notifyListeners();
	}
	
	private void createPopupMenu()
	{
		setActive = new JMenuItem("deaktivieren");
		setActive.addActionListener( e -> 
			setActive(!isActive)
		);
		
		popup = new JPopupMenu();
		popup.add(setActive);
		setComponentPopupMenu(popup);
	}
	
	protected void paint()
	{
		removeAll();
		addComponents();
		revalidate();
	}
	
	protected abstract void onload();
	protected abstract void addComponents();

	/*
	 * Filter Methods
	 */
	
	@Override
	public boolean check(T t)
	{
		return !isActive || filter.check(t);
	}
	
	/*
	 * FilterListener Methods
	 */

	public void addFilterListener(FilterListener<T> listener)
	{
		listeners.add(listener);
	}
	
	public void removeFilterListener(FilterListener<T> listener)
	{
		int i = listeners.indexOf(listener);
		if( i >= 0 )
			listeners.remove(i);
	}
	
	protected void notifyListeners()
	{
		for(FilterListener<T> listener : listeners)
			listener.filter(this);
	}

}
