package bn.blaszczyk.blstatistics.gui.filters;


import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.border.Border;

import bn.blaszczyk.blstatistics.filters.BiFilter;
import bn.blaszczyk.blstatistics.filters.BiFilterListener;
import bn.blaszczyk.blstatistics.filters.LogicalBiFilter;

@SuppressWarnings("serial")
public abstract class AbstractBiFilterPanel<T,U> extends JPanel implements BiFilterPanel<T,U>
{
	private static final Border activeBorder = BorderFactory.createLoweredBevelBorder();
	private static final Border deactiveBorder = BorderFactory.createRaisedBevelBorder();
	
	
	private boolean isActive = true;
	private JMenuItem setActive;
	
	private JPopupMenu popup;
	
	private BiFilter<T,U> filter;
	private List<BiFilterListener<T,U>> listeners = new ArrayList<>();
	
	public AbstractBiFilterPanel()
	{
		this( LogicalBiFilter.getTRUEBiFilter());
	}
	
	public AbstractBiFilterPanel( BiFilter<T,U> filter)
	{
		createPopupMenu();
		setActive(true);
	}

	
	protected void setFilter(BiFilter<T,U> filter)
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
	
	
	@Override
	public boolean check(T t, U u)
	{
		return !isActive || filter.check(t, u);
	}
	
	/*
	 * FilterListener Methods
	 */

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
	public void notifyListeners()
	{
		for(BiFilterListener<T,U> listener : listeners)
			listener.filter(this);
	}
	
	protected void addPopupMenuItem(JMenuItem item)
	{
		popup.add(item);
	}

}
