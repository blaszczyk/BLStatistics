package bn.blaszczyk.fussballstats.gui.filters;

public class FilterEvent<T>
{
	public static final int SET_FILTER = 1;
	public static final int SET_PANEL = 2;
	public static final int SET_ACTIVE = 3;
	
	private int type;
	private FilterPanel<T> source;
	private String oldSourceName;
	private FilterPanel<T> newPanel;

	public FilterEvent(FilterPanel<T> source, String oldSourceName)
	{
		type = SET_FILTER;
		this.oldSourceName = oldSourceName;
		this.source = source;
	}
	
	public FilterEvent(FilterPanel<T> source)
	{
		type = SET_ACTIVE;
		this.source = source;
	}
	
	public FilterEvent(FilterPanel<T> source, FilterPanel<T> newPanel)
	{
		type = SET_PANEL;
		this.source = source;
		this.newPanel = newPanel;
	}

	public int getType()
	{
		return type;
	}

	public String getOldSourceName()
	{
		if(type != SET_FILTER)
			throw new UnsupportedOperationException("OldSourceName only available for type SET_FILTER");
		return oldSourceName;
	}
	
	public FilterPanel<T> getSource()
	{
		return source;
	}

	public FilterPanel<T> getNewPanel()
	{
		if(type != SET_PANEL)
			throw new UnsupportedOperationException("NewPanel only available for type SET_PANEL");
		return newPanel;
	}
}
