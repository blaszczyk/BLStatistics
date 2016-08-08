package bn.blaszczyk.blstatistics.filters;

import java.util.Arrays;

public class LogicalFilter
{
	/*
	 * TRUE
	 */
	public static <T> Filter<T> getTRUEFilter()
	{
		Filter<T> f = t -> true;
		return f;
	}

	/*
	 * FALSE
	 */
	public static <T> Filter<T> getFALSEFilter()
	{
		Filter<T> f = t -> false;
		return f;
	}

	/*
	 * NOT
	 */
	public static <T> Filter<T> getNOTFilter(Filter<T> filter)
	{
		Filter<T> f = t -> filter != null ? !filter.check(t) : false;
		return f;
	}

	/*
	 * AND
	 */
	@SafeVarargs
	public static <T> Filter<T> getANDFilter(Filter<T>... filters)
	{
		return getANDFilter(Arrays.asList(filters));

	}
	public static <T> Filter<T> getANDFilter(Iterable<Filter<T>> filters)
	{
		Filter<T> f = t ->
		{
			for (Filter<T> filter : filters)
				if (filter != null && !filter.check(t))
					return false;
			return true;
		};
		return f;
	}

	/*
	 * OR
	 */
	@SafeVarargs
	public static <T> Filter<T> getORFilter(Filter<T>... filters)
	{
		return getORFilter(Arrays.asList(filters));

	}
	public static <T> Filter<T> getORFilter(Iterable<Filter<T>> filters)
	{
		Filter<T> f = t ->
		{
			for (Filter<T> filter : filters)
				if (filter != null && filter.check(t))
					return true;
			return false;
		};
		return f;
	}

	/*
	 * IF THEN ELSE
	 */
	public static <T> Filter<T> getIF_THEN_ELSEFilter(Filter<T> ifFilter, Filter<T> thenFilter, Filter<T> elseFilter)
	{
		Filter<T> f = t ->
		{
			if (ifFilter != null && ifFilter.check(t))
				return thenFilter != null ? thenFilter.check(t) : true;
			else
				return elseFilter != null ? elseFilter.check(t) : true;
		};
		return f;
	}
	public static <T, U> BiFilter<T, U> getIF_THEN_ELSEBiFilter(Filter<T> ifFilter, Filter<U> thenFilter, Filter<U> elseFilter)
	{
		BiFilter<T, U> f = (t, u) ->
		{
			if (ifFilter != null && ifFilter.check(t))
				return thenFilter != null ? thenFilter.check(u) : true;
			else
				return elseFilter != null ? elseFilter.check(u) : true;
		};
		return f;
	}

}
