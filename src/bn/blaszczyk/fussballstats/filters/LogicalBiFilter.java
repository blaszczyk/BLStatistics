package bn.blaszczyk.fussballstats.filters;

public abstract class LogicalBiFilter
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
	public static <T, U> BiFilter<T, U> getNOTBiFilter(BiFilter<T, U> filter)
	{
		BiFilter<T, U> f = (t, u) -> filter != null ? !filter.check(t, u) : false;
		return f;
	}

	/*
	 * AND
	 */
	public static <T, U, V extends BiFilter<T,U>> BiFilter<T, U> getANDBiFilter(Iterable<V> filters)
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
	public static <T, U, V extends BiFilter<T,U>> BiFilter<T, U> getORBiFilter(Iterable<V> filters)
	{
		BiFilter<T, U> f = (t, u) ->
		{
			for (BiFilter<T, U> filter : filters)
				if (filter != null && filter.check(t, u))
					return true;
			return false;
		};
		return f;
	}

	/*
	 * XOR
	 */
	public static <T, U, V extends BiFilter<T,U>> BiFilter<T, U> getXORBiFilter(Iterable<V> filters)
	{
		BiFilter<T, U> f = (t, u) ->
		{
			boolean retValue = false;
			for (BiFilter<T, U> filter : filters)
				if (filter != null)
					retValue ^= filter.check(t, u);
			return retValue;
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
				return thenFilter != null ? thenFilter.check(t,u) : false;
			else
				return elseFilter != null ? elseFilter.check(t,u) : false;
		};
		return f;
	}

}
