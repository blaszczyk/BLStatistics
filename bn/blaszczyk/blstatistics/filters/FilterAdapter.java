package bn.blaszczyk.blstatistics.filters;

public class FilterAdapter
{
	public static <T,U> BiFilter<T,U> toBiFilterArg1( Filter<T> filter )
	{
		BiFilter<T,U> f = (t,u) -> filter.check(t);
		return f;
	}
	
	public static <T,U> BiFilter<T,U> toBiFilterArg2( Filter<U> filter )
	{
		BiFilter<T,U> f = (t,u) -> filter.check(u);
		return f;
	}

	public static <T,U> FilterListener<T> getListenerAdapterArg1( BiFilterListener<T,U> bilistener )
	{
		FilterListener<T> listener = () -> bilistener.filter();
		return listener;
	}
	
	public static <T,U> FilterListener<U> getListenerAdapterArg2( BiFilterListener<T,U> bilistener )
	{
		FilterListener<U> listener = () -> bilistener.filter();
		return listener;
	}
}
