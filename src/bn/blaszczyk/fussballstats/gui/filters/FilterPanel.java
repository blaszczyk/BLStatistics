package bn.blaszczyk.fussballstats.gui.filters;

import javax.swing.JPanel;

import bn.blaszczyk.fussballstats.filters.Filter;

public interface FilterPanel<T> extends Filter<T>
{
	public void paint();
	public JPanel getPanel();

	public void addFilterListener(FilterListener<T> listener);
	public void removeFilterListener(FilterListener<T> listener);
	
	public void setActive(boolean active);
	public boolean isActive();
}
