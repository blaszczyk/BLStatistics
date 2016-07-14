package bn.blaszczyk.blstatistics.filters;

import java.util.Arrays;

public class LogicalBiFilterFactory
{
	/*
	 * TRUE
	 */
	public static <T, U> BiFilter<T, U> getTRUEBiFilter()
	{
		BiFilter<T, U> f = (t, u) -> true;
		return f;
	}

	/*
	 * FALSE
	 */
	public static <T, U> BiFilter<T, U> getFALSEBiFilter()
	{
		BiFilter<T, U> f = (t, u) -> false;
		return f;
	}

	/*
	 * NOT
	 */
	public static <T, U> BiFilter<T, U> getFALSEBiFilter(BiFilter<T, U> filter)
	{
		BiFilter<T, U> f = (t, u) -> filter != null ? !filter.check(t, u) : false;
		return f;
	}

	/*
	 * AND
	 */
	@SafeVarargs
	public static <T, U> BiFilter<T, U> getANDBiFilter(BiFilter<T, U>... filters)
	{
		return getANDBiFilter(Arrays.asList(filters));

	}
	public static <T, U> BiFilter<T, U> getANDBiFilter(Iterable<BiFilter<T, U>> filters)
	{
		BiFilter<T, U> f = (t, u) ->
		{
			for (BiFilter<T, U> filter : filters)
				if (filter != null && !filter.check(t, u))
					return false;
			return true;
		};
		return f;
	}

	/*
	 * OR
	 */
	@SafeVarargs
	public static <T, U> BiFilter<T, U> getORBiFilter(BiFilter<T, U>... filters)
	{
		return getANDBiFilter(Arrays.asList(filters));

	}
	public static <T, U> BiFilter<T, U> getORBiFilter(Iterable<BiFilter<T, U>> filters)
	{
		BiFilter<T, U> f = (t, u) ->
		{
			for (BiFilter<T, U> filter : filters)
				if (filter != null && !filter.check(t, u))
					return true;
			return false;
		};
		return f;
	}
	/*
	 * IF THEN ELSE
	 */
	public static <T, U> BiFilter<T, U> getIF_THEN_ELSEBiFilter(BiFilter<T,U> ifFilter, BiFilter<T,U> thenFilter, BiFilter<T,U> elseFilter)
	{
		BiFilter<T, U> f = (t, u) ->
		{
			if (ifFilter != null && ifFilter.check(t,u))
				return thenFilter != null ? thenFilter.check(t,u) : true;
			else
				return elseFilter != null ? elseFilter.check(t,u) : true;
		};
		return f;
	}

}
