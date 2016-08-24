package bn.blaszczyk.fussballstats.gui.filters;

import bn.blaszczyk.fussballstats.filters.Filter;

public class FilterEvent<T>
{
	public static final int SET_FILTER = 1;
	public static final int SET_PANEL = 2;
	public static final int SET_ACTIVE = 3;
	
	private int type;
	private FilterPanel<T> source;
	private Filter<T> filter;
	private boolean filterModified = true;

	public FilterEvent(FilterPanel<T> source, Filter<T> filter, int type)
	{
		if(type == SET_PANEL && !(filter instanceof FilterPanel ))
			throw new UnsupportedOperationException("Type SET_PANEL requires FilterPanel");
		this.type = type;
		this.source = source;
		this.filter = filter;
	}

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
