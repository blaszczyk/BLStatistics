package bn.blaszczyk.blstatistics.gui.filters;


import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
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
	protected JMenuItem popupSetActive;
	protected JMenuItem popupNegate;
	protected JMenuItem popupRemove;
	protected JMenu popupReplace;
	private JLabel title = new JLabel("Filter");
	
	private JPopupMenu popup;
	
	private BiFilter<T,U> filter = LogicalBiFilter.getTRUEBiFilter();
	private List<BiFilterListener<T,U>> listeners = new ArrayList<>();
	protected FilterPanelManager<T, U> filterManager;
	
	public AbstractBiFilterPanel(FilterPanelManager<T, U> filterManager)
	{
		this.filterManager = filterManager;
		
		popupSetActive = new JMenuItem("Deaktivieren");
		popupSetActive.addActionListener( e -> setActive(!isActive) );

		popupNegate = new JMenuItem("Invertieren");
		popupNegate.addActionListener( e -> negate() );
		
		popupRemove = new JMenuItem("Entfernen");
		popupRemove.addActionListener( e -> replaceMe( new NoFilterPanel<>(filterManager) ) );
		
		popupReplace = new JMenu("Ersetzten");
		filterManager.addMenuItems(popupReplace, e -> replaceMe( filterManager.getPanel() ));

		
		popup = new JPopupMenu();
		popup.add(title);
		popup.addSeparator();
		addPopupMenuItems();
		setComponentPopupMenu(popup);
		
		setActive(true);
		addFilterListener(e -> title.setText(this.toString()));
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

	protected void addPopupMenuItems()
	{
		popup.add(popupSetActive);
		popup.add(popupNegate);
		popup.add(popupRemove);
		popup.add(popupReplace);
	}
	
	private void negate()
	{
		if(this instanceof UnaryOperatorFilterPanel)
			replaceMe( ((UnaryOperatorFilterPanel<T, U>)this).getInnerPanel() );
		else
			replaceMe(new UnaryOperatorFilterPanel<T,U>(filterManager,this) );
	}
	
	private void setActive(boolean active)
	{
		if(active)
		{
			setBorder(activeBorder);
			this.isActive = true;
			popupSetActive.setText("Deaktivieren");
		}
		else
		{
			setBorder(deactiveBorder);
			this.isActive = false;
			popupSetActive.setText("Aktivieren");
		}
		notifyListeners(new BiFilterEvent<T, U>(this,this,BiFilterEvent.RESET_FILTER));
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
	public void removePopupMenuItem(JMenuItem item)
	{
		popup.remove(item);
	}

	@Override
	public void replaceMe(BiFilterPanel<T, U> newPanel)
	{
		notifyListeners(new BiFilterEvent<>(this, newPanel, BiFilterEvent.RESET_PANEL));
	}
}
