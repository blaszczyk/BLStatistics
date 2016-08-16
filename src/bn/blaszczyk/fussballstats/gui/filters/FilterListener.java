package bn.blaszczyk.fussballstats.gui.filters;

public interface FilterListener<T>
{
	public void filter(FilterEvent<T> e);
}
