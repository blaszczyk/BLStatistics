package bn.blaszczyk.fussballstats.filters;

import bn.blaszczyk.fussballstats.core.Game;
import bn.blaszczyk.fussballstats.core.Season;

public abstract class SeasonFilter
{
	/*
	 * Pure Season Filters
	 */
	public static Filter<Season> getSeasonFilter(int year)
	{
		Filter<Season> f = s -> s.getYear() == year;
		return f;
	}
	public static Filter<Season> getSeasonMinFilter(int year)
	{
		Filter<Season> f = s -> s.getYear() >= year;
		return f;
	}
	public static Filter<Season> getSeasonMaxFilter(int year)
	{
		Filter<Season> f = s -> s.getYear() <= year;
		return f;
	}
	
	/*
	 * Pure League Filter
	 */
	public static Filter<Season> getLeagueFilter(String leagueName)
	{
		Filter<Season> f = s -> s.getLeague().getName().equals(leagueName);
		return f;
	}

	public static Filter<Season> getLeagueContainsFilter(String leagueName)
	{
		Filter<Season> f = s -> s.getLeague().getName().startsWith(leagueName);
		return f;
	}
	
	/*
	 * Hin- und Rückrunde
	 */

	public static BiFilter<Season,Game> getFirstRoundFilter()
	{
		BiFilter<Season,Game> f = (s,g) -> 2 * g.getMatchDay() <= s.getMatchDayCount();
		return f;
	}
	
	public static BiFilter<Season,Game> getSecondRoundFilter()
	{
		BiFilter<Season,Game> f = (s,g) -> 2 * g.getMatchDay() > s.getMatchDayCount();
		return f;
	}
}
