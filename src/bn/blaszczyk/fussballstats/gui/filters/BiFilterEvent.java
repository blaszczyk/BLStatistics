package bn.blaszczyk.fussballstats.gui.filters;

public class BiFilterEvent<T,U>
{
	public static final int SET_FILTER = 1;
	public static final int SET_PANEL = 2;
	public static final int SET_ACTIVE = 3;
	
	private int type;
	private BiFilterPanel<T,U> source;
	private String oldSourceName;
	private BiFilterPanel<T,U> newPanel;

	public BiFilterEvent(BiFilterPanel<T,U> source, String oldSourceName)
	{
		type = SET_FILTER;
		this.oldSourceName = oldSourceName;
		this.source = source;
	}
	
	public BiFilterEvent(BiFilterPanel<T,U> source)
	{
		type = SET_ACTIVE;
		this.source = source;
	}
	
	public BiFilterEvent(BiFilterPanel<T,U> source, BiFilterPanel<T,U> newPanel)
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
	
	public BiFilterPanel<T,U> getSource()
	{
		return source;
	}

	public BiFilterPanel<T,U> getNewPanel()
	{
		if(type != SET_PANEL)
			throw new UnsupportedOperationException("NewPanel only available for type SET_PANEL");
		return newPanel;
	}
}
