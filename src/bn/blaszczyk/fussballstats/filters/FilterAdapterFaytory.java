package bn.blaszczyk.fussballstats.filters;

public class FilterAdapterFaytory
{
	/*
	 * Adapters from Filter to BiFilter
	 */
	public static <T,U> BiFilter<T,U> createFirstArgAdapter( Filter<T> filter )
	{
		BiFilter<T,U> f = (t,u) -> filter.check(t);
		return f;
	}
	
	public static <T,U> BiFilter<T,U> createSecondArgAdapter( Filter<U> filter )
	{
		BiFilter<T,U> f = (t,u) -> filter.check(u);
		return f;
	}
	
}
