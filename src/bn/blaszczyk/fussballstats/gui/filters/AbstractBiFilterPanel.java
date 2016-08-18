package bn.blaszczyk.fussballstats.gui.filters;

import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.*;

import bn.blaszczyk.fussballstats.filters.BiFilter;
import bn.blaszczyk.fussballstats.filters.LogicalBiFilter;

@SuppressWarnings("serial")
public abstract class AbstractBiFilterPanel<T,U> extends JPanel implements BiFilterPanel<T,U>
{
	private final boolean varComponents;
	private boolean isActive = true;
	private boolean isPainted = false;
	
	private BiFilter<T,U> filter = LogicalBiFilter.getTRUEBiFilter();
	private List<BiFilterListener<T,U>> listeners = new ArrayList<>();

	protected ActionListener setFilterListener = e ->
	{
		setFilter();
		if(e.getSource() instanceof JComponent)
			((JComponent)e.getSource()).requestFocusInWindow();
	};
	
	
	/*
	 * Constructor
	 */
	public AbstractBiFilterPanel(boolean varComponents)
	{
		this.varComponents = varComponents;
		setBorder(AbstractFilterPanel.ACTIVE_BORDER);
		setAlignmentX(LEFT_ALIGNMENT);
	}

	protected abstract void setFilter();
	protected abstract void addComponents();
	
	protected void setFilter(BiFilter<T,U> filter)
	{
		String oldName = toString();
		this.filter = filter;
		notifyListeners(new BiFilterEvent<>(this,oldName));
	}
	

	protected void notifyListeners(BiFilterEvent<T,U> e)
	{
		List<BiFilterListener<T,U>> copy = new ArrayList<>(listeners.size());
		for(BiFilterListener<T, U> listener : listeners)
			copy.add(listener);
		for(BiFilterListener<T, U> listener : copy)
			listener.filter(e);		
		
// 		//In order to avoid ConcurrentModificationException we do not use		
//		for(BiFilterListener<T,U> listener : listeners)
//			listener.filter(e);
	}	
	
	/*
	 * BiFilter Methods
	 */
	@Override
	public boolean check(T t, U u)
	{
		return !isActive || filter.check(t, u);
	}
	
	/*
	 * BiFilterPanel Methods
	 */

	@Override
	public void setActive(boolean active)
	{
		if(active)
		{
			setBorder(AbstractFilterPanel.ACTIVE_BORDER);
			this.isActive = true;
		}
		else
		{
			setBorder(AbstractFilterPanel.INACTIVE_BORDER);
			this.isActive = false;
		}
		notifyListeners(new BiFilterEvent<T, U>(this));
	}

	@Override
	public boolean isActive()
	{
		return isActive;
	}
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
	public void addFilterListener(BiFilterListener<T,U> listener)
	{
		listeners.add(listener);
	}

	@Override
	public void removeFilterListener(BiFilterListener<T,U> listener)
	{
		int i = listeners.indexOf(listener);
		if( i >= 0 )
			listeners.remove(i);
	}

	@Override
	public void replaceMe(BiFilterPanel<T, U> newPanel)
	{
		notifyListeners(new BiFilterEvent<>(this, newPanel));
	}
}
