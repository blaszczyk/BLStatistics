package bn.blaszczyk.blstatistics.gui.filters;

import bn.blaszczyk.blstatistics.filters.Filter;

public class FilterEvent<T>
{
	public static final int RESET_FILTER = 1;
	public static final int RESET_PANEL = 2;
	
	private int type;
	private FilterPanel<T> source;
	private Filter<T> filter;

	public FilterEvent(FilterPanel<T> source, Filter<T> filter, int type)
	{
		this.type = type;
		this.source = source;
		this.filter = filter;
		if(type == RESET_PANEL && ! BiFilterPanel.class.isAssignableFrom(filter.getClass()))
			throw new UnsupportedOperationException("Second Argument must be a BiFilterPanel for type RESET_PANEL");
	}

	public int getType()
	{
		return type;
	}

	public FilterPanel<T> getSource()
	{
		return source;
	}

	public Filter<T> getFilter()
	{
		return filter;
	}
	
	public FilterPanel<T> getPanel()
	{
		if(type != RESET_PANEL)
			throw new UnsupportedOperationException("Panel only available for type RESET_PANEL");
		return (FilterPanel<T>) filter;
	}
}
