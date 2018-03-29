package bn.blaszczyk.fussballstats.filters;

import bn.blaszczyk.fussballstats.model.*;

public class SeasonFilterFactory
{
	/*
	 * Season Filters
	 */
	public static Filter<Season> createSeasonFilter(int year)
	{
		Filter<Season> f = s -> s.getYear() == year;
		return f;
	}
	public static Filter<Season> createSeasonMinFilter(int year)
	{
		Filter<Season> f = s -> s.getYear() >= year;
		return f;
	}
	public static Filter<Season> createSeasonMaxFilter(int year)
	{
		Filter<Season> f = s -> s.getYear() <= year;
		return f;
	}
	
	/*
	 * League Filters
	 */
	public static Filter<Season> createLeagueFilter(League league)
	{
		Filter<Season> f = s -> s.getLeague().equals(league);
		return f;
	}

	public static Filter<Season> createLeagueContainsFilter(final League league)
	{
		Filter<Season> f = s -> {
			League l = s.getLeague();
			while(l != null)
			{
				if(l.equals(league))
					return true;
				l = l.getParent();
			}
			return false;
		};
		return f;
	}
	
	/*
	 * Round Filters
	 */

	public static BiFilter<Season,Game> createFirstRoundFilter()
	{
		BiFilter<Season,Game> f = (s,g) -> 2 * g.getMatchday().getCount().intValue() <= s.getMatchdays().size();
		return f;
	}
	
	public static BiFilter<Season,Game> createSecondRoundFilter()
	{
		BiFilter<Season,Game> f = (s,g) -> 2 * g.getMatchday().getCount().intValue() > s.getMatchdays().size();
		return f;
	}
}
