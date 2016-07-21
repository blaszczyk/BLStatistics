package bn.blaszczyk.blstatistics.gui.filters;

import bn.blaszczyk.blstatistics.filters.BiFilter;

public class BiFilterEvent<T,U>
{
	public static final int RESET_FILTER = 1;
	public static final int RESET_PANEL = 2;
	
	private int type;
	private BiFilterPanel<T,U> source;
	private BiFilter<T,U> filter;

	public BiFilterEvent(BiFilterPanel<T, U> source, BiFilter<T, U> filter, int type)
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

	public BiFilterPanel<T, U> getSource()
	{
		return source;
	}

	public BiFilter<T, U> getFilter()
	{
		return filter;
	}
	
	public BiFilterPanel<T, U> getPanel()
	{
		if(type != RESET_PANEL)
			throw new UnsupportedOperationException("Panel only available for type RESET_PANEL");
		return (BiFilterPanel<T, U>) filter;
	}
	
	@Override
	public String toString()
	{
		return "typ" + type + " ; src " + source + " ; flt " + filter;
	}

}
