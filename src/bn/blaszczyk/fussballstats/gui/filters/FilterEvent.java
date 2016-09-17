package bn.blaszczyk.fussballstats.gui.filters;

import bn.blaszczyk.fussballstats.filters.Filter;

public class FilterEvent<T>
{
	/*
	 * Constants
	 */
	public static final int SET_FILTER = 1;
	public static final int SET_PANEL = 2;
	public static final int SET_ACTIVE = 3;

	/*
	 * Variables
	 */
	private final int type;
	private final FilterPanel<T> source;
	private final Filter<T> filter;
	private boolean filterModified = true;

	/*
	 * Constructor
	 */
	public FilterEvent(FilterPanel<T> source, Filter<T> filter, int type)
	{
		if(filter != null && type == SET_PANEL && !(filter instanceof FilterPanel ))
			throw new UnsupportedOperationException("Type SET_PANEL requires FilterPanel");
		this.type = type;
		this.source = source;
		this.filter = filter;
	}

	/*
	 * Getters, Setter
	 */
	public int getType()
	{
		return type;
	}

	
	public FilterPanel<T> getSource()
	{
		return source;
	}

	public FilterPanel<T> getNewPanel()
	{
		if(type != SET_PANEL)
			throw new UnsupportedOperationException("NewPanel only available for type SET_PANEL");
		return (FilterPanel<T>) filter;
	}
	
	public Filter<T> getFilter()
	{
		return filter;
	}
	
	public void setFilterModified(boolean filterModified)
	{
		this.filterModified = filterModified;
	}
	
	public boolean isFilterModified()
	{
		return filterModified;
	}
}
