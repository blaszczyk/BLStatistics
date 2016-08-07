package bn.blaszczyk.blstatistics.gui.filters;


import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.border.Border;

import bn.blaszczyk.blstatistics.filters.Filter;
import bn.blaszczyk.blstatistics.filters.LogicalFilter;

@SuppressWarnings("serial")
public abstract class AbstractFilterPanel<T> extends JPanel implements FilterPanel<T>
{
	private static final Border activeBorder = BorderFactory.createLoweredBevelBorder();
	private static final Border deactiveBorder = BorderFactory.createRaisedBevelBorder();
	
	
	private boolean isActive = true;
	private JMenuItem setActive;

	private JLabel title = new JLabel("Filter");
	private JPopupMenu popup;
	
	private Filter<T> filter = LogicalFilter.getTRUEFilter();
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
		setActive = new JMenuItem("Deaktivieren");
		setActive.addActionListener( e -> setActive(!isActive));
		popup = new JPopupMenu();
		popup.add(title);
		popup.addSeparator();
		popup.add(setActive);
		setComponentPopupMenu(popup);
		setActive(true);
		addFilterListener(e -> title.setText(this.toString()));
	}


	protected void setFilter(Filter<T> filter)
	{
		this.filter = filter;
	}
	
	protected Filter<T> getFilter()
	{
		return filter;
	}
	
	private void setActive(boolean active)
	{
		if(active)
		{
			setBorder(activeBorder);
			this.isActive = true;
			setActive.setText("Deaktivieren");
		}
		else
		{
			setBorder(deactiveBorder);
			this.isActive = false;
			setActive.setText("Aktivieren");
		}
		notifyListeners(new FilterEvent<T>(this, getFilter(), FilterEvent.RESET_FILTER));
	}
	
	@Override 
	public JPanel getPanel()
	{
		return this;
	}
	
	@Override
	public void paint()
	{
		removeAll();
		addComponents();
		revalidate();
	}
	
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

	public void notifyListeners(FilterEvent<T> e)
	{
		for(FilterListener<T> listener : listeners)
			listener.filter(e);
	}

	@Override
	public void addPopupMenuItem(JMenuItem item)
	{
		popup.add(item);
	}

	@Override
	public void removePopupMenuItem(JMenuItem item)
	{
		popup.remove(item);
	}
	
}
