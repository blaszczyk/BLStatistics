package bn.blaszczyk.fussballstats.gui.filters;

public interface BiFilterListener<T,U>
{
	public void filter(BiFilterEvent<T,U> e);
}
