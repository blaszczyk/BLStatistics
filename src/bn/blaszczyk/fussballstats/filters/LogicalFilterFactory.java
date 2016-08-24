package bn.blaszczyk.fussballstats.filters;

public class LogicalFilterFactory
{
	/*
	 * TRUE
	 */
	public static <T> Filter<T> createTRUEFilter()
	{
		Filter<T> f = t -> true;
		return f;
	}

	/*
	 * FALSE
	 */
	public static <T> Filter<T> createFALSEFilter()
	{
		Filter<T> f = t -> false;
		return f;
	}

	/*
	 * NOT
	 */
	public static <T> Filter<T> createNOTFilter(Filter<T> filter)
	{
		Filter<T> f = t -> filter != null ? !filter.check(t) : false;
		return f;
	}

	/*
	 * AND
	 */
	public static <T> Filter<T> createANDFilter(Iterable<Filter<T>> filters)
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
	public static <T> Filter<T> createORFilter(Iterable<Filter<T>> filters)
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
	public static <T> Filter<T> createIF_THEN_ELSEFilter(Filter<T> ifFilter, Filter<T> thenFilter, Filter<T> elseFilter)
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

}
