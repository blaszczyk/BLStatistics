package bn.blaszczyk.blstatistics.gui.filters;


import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JMenu;
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
	protected JMenuItem setActive;
	protected JMenu replace;
	
	private JPopupMenu popup;
	
	private BiFilter<T,U> filter;
	private List<BiFilterListener<T,U>> listeners = new ArrayList<>();
	
	public AbstractBiFilterPanel(PanelMenu<T, U> panelMenu)
	{
		this( LogicalBiFilter.getTRUEBiFilter(),panelMenu);
	}
	
	public AbstractBiFilterPanel( BiFilter<T,U> filter, PanelMenu<T, U> panelMenu)
	{
		setFilter(filter);
		
		setActive = new JMenuItem("Deaktivieren");
		setActive.addActionListener( e -> 
			setActive(!isActive)
		);
		
		replace = new JMenu("Ersetzten durch");
		panelMenu.addMenuItems(replace, e -> notifyListeners(new BiFilterEvent<>(this, panelMenu.getPanel(), BiFilterEvent.RESET_PANEL)));
		
		popup = new JPopupMenu();
		popup.add(setActive);
		popup.add(replace);
		setComponentPopupMenu(popup);
		
		setActive(true);
	}

	
	protected void setFilter(BiFilter<T,U> filter)
	{
		this.filter = filter;
	}
	
	protected BiFilter<T,U> getFilter()
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
		notifyListeners(new BiFilterEvent<T, U>(this,this,BiFilterEvent.RESET_FILTER));
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
//		int i = listeners.indexOf(listener);
//		if( i >= 0 )
//			listeners.remove(i);
	}
	
	protected void notifyListeners(BiFilterEvent<T,U> e)
	{
		for(BiFilterListener<T,U> listener : listeners)
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
