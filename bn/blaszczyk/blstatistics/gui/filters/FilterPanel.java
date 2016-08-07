package bn.blaszczyk.blstatistics.gui.filters;

import javax.swing.JMenuItem;
import javax.swing.JPanel;

import bn.blaszczyk.blstatistics.filters.Filter;

public interface FilterPanel<T> extends Filter<T>
{
	public void paint();
	public JPanel getPanel();
	
	public void addPopupMenuItem(JMenuItem item);
	public void removePopupMenuItem(JMenuItem item);
	
	public void addFilterListener(FilterListener<T> listener);
	public void removeFilterListener(FilterListener<T> listener);
}
