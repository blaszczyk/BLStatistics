package bn.blaszczyk.blstatistics.gui.filters;

import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.*;
import javax.swing.border.Border;

import bn.blaszczyk.blstatistics.filters.BiFilter;
import bn.blaszczyk.blstatistics.filters.LogicalBiFilter;

@SuppressWarnings("serial")
public abstract class AbstractBiFilterPanel<T,U> extends JPanel implements BiFilterPanel<T,U>
{
	private static final Border activeBorder = BorderFactory.createLoweredBevelBorder();
	private static final Border deactiveBorder = BorderFactory.createRaisedBevelBorder();
	
	protected ActionListener setFilterListener = e ->
	{
		setFilter();
		if(e.getSource() instanceof JComponent)
			((JComponent)e.getSource()).requestFocusInWindow();
	};
	
	private boolean isActive = true;
	
	private JPopupMenu popup;
	
	private BiFilter<T,U> filter = LogicalBiFilter.getTRUEBiFilter();
	private List<BiFilterListener<T,U>> listeners = new ArrayList<>();
	
	public AbstractBiFilterPanel()
	{
		popup = new JPopupMenu();
		setComponentPopupMenu(popup);
		setBorder(activeBorder);
		setAlignmentX(LEFT_ALIGNMENT);
	}

	protected abstract void setFilter();
	protected abstract void addComponents();
	
	protected void setFilter(BiFilter<T,U> filter)
	{
		this.filter = filter;
		notifyListeners(new BiFilterEvent<>(this, filter, BiFilterEvent.RESET_FILTER));
	}
	
	protected void passFilterEvent(BiFilterEvent<T, U> e)
	{
		notifyListeners(e);
	}	
	

	private void notifyListeners(BiFilterEvent<T,U> e)
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
			setBorder(activeBorder);
			this.isActive = true;
		}
		else
		{
			setBorder(deactiveBorder);
			this.isActive = false;
		}
		notifyListeners(new BiFilterEvent<T, U>(this,this,BiFilterEvent.RESET_FILTER));
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
		removeAll();
		addComponents();
		revalidate();
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
	public void addPopupMenuItem(JMenuItem item)
	{
		popup.add(item);
	}
	
	@Override
	public void addPopupMenuLabel(JLabel item)
	{
		popup.add(item);
	}

	@Override
	public void addPopupMenuSeparator()
	{
		popup.addSeparator();
	}

	
	@Override
	public void replaceMe(BiFilterPanel<T, U> newPanel)
	{
		notifyListeners(new BiFilterEvent<>(this, newPanel, BiFilterEvent.RESET_PANEL));
	}
}
