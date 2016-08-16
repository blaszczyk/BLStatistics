package bn.blaszczyk.fussballstats.filters;

public abstract class FilterAdapter
{
	public static <T,U> BiFilter<T,U> getFirstArgAdapter( Filter<T> filter )
	{
		BiFilter<T,U> f = (t,u) -> filter.check(t);
		return f;
	}
	
	public static <T,U> BiFilter<T,U> getSecondArgAdapter( Filter<U> filter )
	{
		BiFilter<T,U> f = (t,u) -> filter.check(u);
		return f;
	}
	
}
