package bn.blaszczyk.fussballstats.gui.filters;

import bn.blaszczyk.fussballstats.filters.BiFilter;

public class BiFilterEvent<T,U>
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
	private final BiFilterPanel<T,U> source;
	private final BiFilter<T,U> filter;
	private boolean filterModified = true;

	/*
	 * Constructor
	 */
	public BiFilterEvent(BiFilterPanel<T,U> source, BiFilter<T,U> filter, int type)
	{
		if(filter != null && type == SET_PANEL && !(filter instanceof BiFilterPanel ))
			throw new UnsupportedOperationException("Type SET_PANEL requires BiFilterPanel");
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
	
	public BiFilterPanel<T,U> getSource()
	{
		return source;
	}

	public BiFilterPanel<T,U> getNewPanel()
	{
		if(type != SET_PANEL)
			throw new UnsupportedOperationException("NewPanel only available for type SET_PANEL");
		return (BiFilterPanel<T, U>) filter;
	}
	
	public BiFilter<T,U> getFilter()
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
