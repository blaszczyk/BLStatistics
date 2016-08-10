package bn.blaszczyk.blstatistics.gui.filters;

import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.border.Border;

import bn.blaszczyk.blstatistics.filters.BiFilter;
import bn.blaszczyk.blstatistics.filters.LogicalBiFilter;

@SuppressWarnings("serial")
public abstract class AbstractBiFilterPanel<T,U> extends JPanel implements BiFilterPanel<T,U>
{
	private static final Border activeBorder = BorderFactory.createLoweredBevelBorder();
	private static final Border deactiveBorder = BorderFactory.createRaisedBevelBorder();
	
	
	private boolean isActive = true;
	private JLabel title = new JLabel("Filter");
	
	private JPopupMenu popup;
	
	private BiFilter<T,U> filter = LogicalBiFilter.getTRUEBiFilter();
	private List<BiFilterListener<T,U>> listeners = new ArrayList<>();
	
	public AbstractBiFilterPanel()
	{
		popup = new JPopupMenu();
		popup.add(title);
		popup.addSeparator();
		setComponentPopupMenu(popup);
		setBorder(activeBorder);
		setAlignmentX(LEFT_ALIGNMENT);
	}

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

	private void notifyListeners(BiFilterEvent<T,U> e)
	{
		List<BiFilterListener<T,U>> copy = new ArrayList<>(listeners.size());
		for(BiFilterListener<T, U> listener : listeners)
			copy.add(listener);
		for(BiFilterListener<T, U> listener : copy)
			listener.filter(e);		
		
		// In order to avoid ConcurrentModificationException we do not use		
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
		removeAll();
		addComponents();
		revalidate();
	}
	
	@Override
	public void addFilterListener(BiFilterListener<T,U> listener)
	{
//		System.out.println(this + " telling "+ listener);
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
//		System.out.println( "Replacing " + this + " by " + newPanel);
		notifyListeners(new BiFilterEvent<>(this, newPanel, BiFilterEvent.RESET_PANEL));
	}
}
