package bn.blaszczyk.fussballstats.filters;

public interface Filter<T>
{
	public boolean check(T t);
}
